class AddFieldsToConfigurationValues < ActiveRecord::Migration
  def self.up
    add_column :configuration_values, :options, :text
    add_column :configuration_values, :help_text, :text
  end

  def self.down
    remove_column :configuration_values, :options
    remove_column :configuration_values, :help_text
  end
end
