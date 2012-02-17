class AddHypervisorToVdc < ActiveRecord::Migration
  def self.up
    add_column :virtual_datacenters, :hypervisor, :string
  end

  def self.down
    add_column :virtual_datacenters, :hypervisor
  end
end
