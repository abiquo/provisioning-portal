class CreateVirtualDatacenters < ActiveRecord::Migration
  def self.up
    create_table :virtual_datacenters do |t|
      t.string :name
      t.integer :enterprise_id

      t.timestamps
    end
  end

  def self.down
    drop_table :virtual_datacenters
  end
end
