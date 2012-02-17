class ConfigurationValue < ActiveRecord::Base
  belongs_to :configuration_group  
  serialize :options
  validates_uniqueness_of :name
  
  default_scope order('sort_order ASC').where(:enabled => true)
end
