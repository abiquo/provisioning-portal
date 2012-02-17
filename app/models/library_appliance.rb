class LibraryAppliance < ActiveRecord::Base
  validates_presence_of :name, :description, :price, :abiquo_app_id
  has_attached_file :icon, :styles => { :tiny => '16x16', :small => '32x32', :normal => '100x100' }, 
      :storage => :s3,
      :s3_credentials => "#{RAILS_ROOT}/config/s3.yml",
      :path => ":attachment/:id/:style.:extension",
      :bucket => 'abiquoprov'
      
  has_many :appliances
      
end
