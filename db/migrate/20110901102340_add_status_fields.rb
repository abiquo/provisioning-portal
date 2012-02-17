class AddStatusFields < ActiveRecord::Migration
  def self.up
    add_column :virtual_datacenters, :status, :string
    add_column :appliances, :status, :string
  end

  def self.down
    remove_column :virtual_datacenters, :status
    remove_column :appliances, :status
  end
end
