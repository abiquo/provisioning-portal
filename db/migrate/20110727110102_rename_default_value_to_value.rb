class RenameDefaultValueToValue < ActiveRecord::Migration
  def self.up
    rename_column :parameters, :default_value, :value
  end

  def self.down
    rename_column :parameters, :value, :default_value
  end
end
