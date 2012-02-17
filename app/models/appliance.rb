class Appliance < ActiveRecord::Base
  belongs_to :user
  
  belongs_to :library_appliance

  accepts_nested_attributes_for :user
  
  before_create :default_status
  
  serialize :status
  
  def provision
    # this is a stub function that will eventually deploy the appliance
    if $app_config['require_approval'] == '0' or self.status[:status] == :approved or self.status[:status] == :error
      self.status = { :status => :provisioned, :message => "This appliance has been successfully provisioned." }
      self.save
      Notifier.notify_user(self).deliver
      true
    else
      self.status = { :status => :unapproved, :message => "This appliance provision requires approval." }
      self.save
      Notifier.notify_approver(self).deliver if $app_config['send_request_to_approver']
      Notifier.notify_user_of_pending_approval(self).deliver if $app_config['send_approval_to_user']
      false
    end
  end
  
  def label
    "Virtual Appliance"
  end
  
  def provisioned?
    self.status[:status] == :provisioned unless self.status.nil?
  end
  
  def approve
    self.status = { :status => :approved, :message => "This appliance has been approved." }
    self.save
    Notifier.notify_user_of_approval(self).deliver
  end
  
  def deny
    self.status = { :status => :denied, :message => "This appliance has been denied." }
    self.save
    Notifier.notify_user_of_denial(self).deliver
  end
  
  private
  
  def default_status
    self.status = { :status => nil, :message => nil }
  end
  
end
