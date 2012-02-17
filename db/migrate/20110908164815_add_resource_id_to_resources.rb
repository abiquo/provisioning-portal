class AddResourceIdToResources < ActiveRecord::Migration
  def self.up
    add_column :enterprises, :resource_id, :integer
    add_column :virtual_datacenters, :resource_id, :integer
    add_column :appliances, :resource_id, :integer
  end

  def self.down
    remove_column :enterprises, :resource_id
    remove_column :virtual_datacenters, :resource_id
    remove_column :appliances, :resource_id
  end
end
