class Enterprise < ActiveRecord::Base
  has_many :parameters, :dependent => :destroy
  belongs_to :user
  has_many :virtual_datacenters, :dependent => :destroy
  validates_presence_of :name
  validates_uniqueness_of :name
  validate :unique_name, :if => :should_validate_unique_name?
  
  before_create :default_status
  
  serialize :status
  
  accepts_nested_attributes_for :parameters, :user
  
  def add_parameters(parameters)
    Enterprise.find_by_name(:default).parameters.each do |p|
      self.parameters << Parameter.new(:name => p.name,
                                       :api_id => p.api_id,
                                       :unit => p.unit,
                                       :api_mult => p.api_mult,
                                       :unit_price => p.unit_price,
                                       :include_first => p.include_first,
                                       :value => parameters[p.api_id])
    end
  end
  
  def provision
    unless self.provisioned?
      if $app_config['require_approval'] == '0' or self.status[:status] == :approved or self.status[:status] == :error
        if enterprise = create_enterprise_via_api
          if self.user.create_user_via_api(enterprise, :role => WeBee::UserRole.enterprise_admin)
            self.status = { :status => :provisioned, :message => "This enterprise has been successfully provisioned." }
            self.resource_id = enterprise.resource_id
            self.save
            Notifier.notify_user(self).deliver
          else
            # If there's a problem creating the user we don't want to leave the enterprise.            
            enterprise.delete

            self.status = { :status => :error, :message => 'Problem creating the user: "' + $!.to_s + '"' }
            self.save :validate => false
          end
        end
      else
        self.status = { :status => :unapproved, :message => "This enterprise provision requires approval." }
        self.save
        Notifier.notify_approver(self).deliver if $app_config['send_request_to_approver']
        Notifier.notify_user_of_pending_approval(self).deliver if $app_config['send_approval_to_user']
        false
      end
    end
  end
  
  def provisioned?
    self.status[:status] == :provisioned unless self.status.nil?
  end
  
  def default
    self.name == "default"
  end
  
  def approve
    self.status = { :status => :approved, :message => "This enterprise has been approved." }
    self.save
    Notifier.notify_user_of_approval(self).deliver
  end
  
  def deny
    self.status = { :status => :denied, :message => "This enterprise has been denied." }
    self.save
    Notifier.notify_user_of_denial(self).deliver
  end
  
  def label
    "Virtual Enterprise"
  end
  
  def get_via_api
    if self.resource_id
      if enterprise = WeBee::Enterprise.find(self.resource_id)
        self.name = enterprise.name
        self.status = { :status => :provisioned, :message => "This enterprise has been successfully provisioned." }
      end
    else
      unless self.name.nil? or self.name.empty?
        if enterprise = WeBee::Enterprise.find_by_name(self.name).first
          self.status = { :status => :provisioned, :message => "This enterprise has been successfully provisioned." }
          self.resource_id = enterprise.resource_id
        end
      end
    end
    enterprise
  end
  
  def has_vdc?
    self.virtual_datacenters.present?
  end
  
  def should_validate_unique_name?
    not provisioned?
  end
  
  private
  
  def api_parameters
    api_params = { :name => self.name }
    self.parameters.each do |p|
      value = (p.api_mult ? p.value * p.api_mult : p.value)
      api_params[(p.api_id + "Hard").to_sym] = value.to_i
      api_params[(p.api_id + "Soft").to_sym] = value.to_i
    end
    api_params
  end
  
  def create_enterprise_via_api
    begin
      WeBee::Enterprise.create(api_parameters)
    rescue Exception
      self.status = { :status => :error, :message => 'Problem creating the enterprise: "' + $!.to_s + '"' }
      self.save :validate => false
      
      nil        
    end
  end
  
  def default_status
    self.status = { :status => nil, :message => nil }
  end

  def unique_name
    # check with the api to make sure the name hasn't been used
    errors.add(:name, 'has already been taken.') if get_via_api
  end
  
end
