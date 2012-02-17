class Configuration < ActiveRecord::Base
  has_many :configuration_groups

  accepts_nested_attributes_for :configuration_groups
end
