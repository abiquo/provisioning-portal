class AddEnabledToConfigurationValue < ActiveRecord::Migration
  def self.up
    add_column :configuration_values, :enabled, :boolean, :default => true
  end

  def self.down
    remove_column :configuration_values, :enabled
  end
end
