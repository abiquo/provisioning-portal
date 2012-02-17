class AddSortOrderToParams < ActiveRecord::Migration
  def self.up
    add_column :parameters, :sort_order, :integer
  end

  def self.down
    remove_column :parameters, :sort_order
  end
end
