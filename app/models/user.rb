class User < ActiveRecord::Base
  validates_presence_of :first_name, :last_name, :email_address, :unless => :has_username
  has_many :enterprises, :dependent => :destroy
  has_many :appliances, :dependent => :destroy
  has_many :virtual_datacenters, :dependent => :destroy
  attr_encrypted :password, :key => 'mm2cQe1ORlbbFQAzTFhLbCiG1tSjHs6ea4VihJaUOCzSoTaR7HWL4NmpJHPdILN'
  validate :unique_username, :on => :create

  before_validation :create_username_password

  def create_username_password
    # --- 
    # FirstName.LastName: "#{first_name}.#{last_name}"
    # Initial and Lastname: "#{initial}#{last_name}"
    # EmailAddress: "#{email_address}"
    
    case $app_config['username_template']
    when '#{first_name}.#{last_name}'
      self.username = (self.first_name + '.' + self.last_name).downcase
    when '#{initial}#{last_name}'
      self.username = (self.initial + self.last_name).downcase
    when '#{email_address}'
      self.username = self.email_address.downcase
    else
      self.username = self.name.downcase.gsub(' ', '')[0...8] + "_admin"
    end if self.username.nil? or self.username.empty?
    
  self.password = (0...8).map{ ('a'..'z').to_a[rand(26)] }.join if self.password.nil? or self.password.empty?
  end
  
  def initial
    self.first_name[0]
  end
  
  def create_user_via_api(enterprise, attributes = {})
    default_attributes = { :name => self.first_name,
                           :role => WeBee::UserRole.user,
                           :password => Digest::MD5.hexdigest(self.password),
                           :email => self.email_address,
                           :surname => self.last_name,
                           :locale => 'en_US',
                           :nick => self.username,
                           :active => 'true' }
    attributes = default_attributes.merge(attributes)
    begin
      enterprise.create_user(attributes)
    rescue Exception
      false
    end
  end
  
  private
  
  def has_username
    !self.username.nil? and !self.username.empty?
  end

  def unique_username
    # check with the api to make sure the name hasn't been used
    self.create_username_password
    unless self.username.nil? or self.username.empty?
      errors.add(:first_name, 'The username "'+self.username+'" has already been taken.') unless WeBee::User.all.index { |u| u.nick == self.username }.nil?
    end
  end
  
end
