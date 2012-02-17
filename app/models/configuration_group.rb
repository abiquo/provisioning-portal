class ConfigurationGroup < ActiveRecord::Base
  belongs_to :configuration
  has_many :configuration_values

  default_scope order('sort_order ASC')

  accepts_nested_attributes_for :configuration_values
end
