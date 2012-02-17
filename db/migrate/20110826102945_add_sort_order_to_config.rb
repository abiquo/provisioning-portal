class AddSortOrderToConfig < ActiveRecord::Migration
  def self.up
    add_column :configuration_groups, :sort_order, :integer
    add_column :configuration_values, :sort_order, :integer
  end

  def self.down
    remove_column :configuration_groups, :sort_order, :integer
    remove_column :configuration_values, :sort_order, :integer
  end
end
