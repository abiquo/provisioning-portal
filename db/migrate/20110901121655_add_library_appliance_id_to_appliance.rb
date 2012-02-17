class AddLibraryApplianceIdToAppliance < ActiveRecord::Migration
  def self.up
    add_column :appliances, :library_appliance_id, :integer
  end

  def self.down
    remove_column :appliances, :library_appliance_id
  end
end
