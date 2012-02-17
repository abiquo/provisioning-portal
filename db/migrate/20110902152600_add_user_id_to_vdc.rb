class AddUserIdToVdc < ActiveRecord::Migration
  def self.up
    add_column :virtual_datacenters, :user_id, :integer
  end

  def self.down
    remove_column :virtual_datacenters, :user_id
  end
end
