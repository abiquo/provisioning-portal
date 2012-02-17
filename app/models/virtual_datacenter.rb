class VirtualDatacenter < ActiveRecord::Base
  validates_presence_of :name, :datacenter_id, :hypervisor
  belongs_to :enterprise
  belongs_to :user, :dependent => :destroy
  #validate :enterprise_exists
  validates_uniqueness_of :resource_id
  
  accepts_nested_attributes_for :user
  
  before_create :default_status
  
  serialize :status
  
  def provision
    # this is a stub function that will eventually deploy the virtual datacenter
    if $app_config['require_approval'] == '0' or self.status[:status] == :approved or self.status[:status] == :error
      enterprise = self.enterprise.get_via_api
      
      if vdc = create_vdc_via_api(enterprise)
        if self.user.create_user_via_api(enterprise, :availableVirtualDatacenters => vdc.vdc_id)
          self.status = { :status => :provisioned, :message => "This virtual datacenter has been successfully provisioned." }
          self.resource_id = vdc.vdc_id
          self.save
          Notifier.notify_user(self).deliver
        else
          # If there's a problem creating the user we don't want to leave the enterprise.            
          vdc.delete

          self.status = { :status => :error, :message => 'Problem creating the user: "' + $!.to_s + '"' }
          self.save :validate => false
        end
      end
     
    else
      self.status = { :status => :unapproved, :message => "This virtual datacenter provision requires approval." }
      self.save
      Notifier.notify_approver(self).deliver if $app_config['send_request_to_approver']
      Notifier.notify_user_of_pending_approval(self).deliver if $app_config['send_approval_to_user']
      false
    end
  end
  
  def label
    "Virtual Datacenter"
  end
  
  def provisioned?
    self.status[:status] == :provisioned unless self.status.nil?
  end
  
  def approve
    self.status = { :status => :approved, :message => "This virtual datacenter has been approved." }
    self.save
    Notifier.notify_user_of_approval(self).deliver
  end
  
  def deny
    self.status = { :status => :denied, :message => "This virtual datacenter has been denied." }
    self.save
    Notifier.notify_user_of_denial(self).deliver
  end
  
  def get_via_api
    if self.resource_id
      WeBee::VDC.find(self.resource_id)
    else
      unless self.name.nil? or self.name.empty?
        if vdc = WeBee::VDC.find_by_name(self.name).first
          self.resource_id = vdc.vdc_id
          self.save
        end
      end
    end
    self.save
    vdc
  end
  
  private
  
  def create_vdc_via_api(enterprise)
    begin
      datacenter = WeBee::Datacenter.find(self.datacenter_id)
      
      enterprise.create_vdc :name => self.name,
                            :datacenter => datacenter,
                            :hypervisortype => 'KVM'
    rescue Exception
      self.status = { :status => :error, :message => 'Problem creating the VDC: "' + $!.to_s + '"' }
      self.save :validate => false
      
      false
    end
  end

  def default_status
    self.status = { :status => nil, :message => nil }
  end
  
  def enterprise_exists
    errors.add(:name, 'The specified Enterprise does not exist on the Abiquo system.') if self.enterprise.nil? or not self.enterprise.get_via_api
  end
  
  def unique_name
    # check with the api to make sure the name hasn't been used
    errors.add(:name, 'has already been taken.') if get_via_api
  end
  
end
