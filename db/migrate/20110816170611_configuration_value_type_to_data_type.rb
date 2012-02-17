class ConfigurationValueTypeToDataType < ActiveRecord::Migration
  def self.up
    rename_column :configuration_values, :type, :field_type
  end

  def self.down
    rename_column :configuration_values, :field_type, :type
  end
end
