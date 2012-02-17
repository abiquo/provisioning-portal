class AddDatacenterIdToVdc < ActiveRecord::Migration
  def self.up
    add_column :virtual_datacenters, :datacenter_id, :integer
  end

  def self.down
    remove_column :virtual_datacenters, :datacenter_id
  end
end
