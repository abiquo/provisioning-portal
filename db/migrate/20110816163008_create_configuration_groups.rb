class CreateConfigurationGroups < ActiveRecord::Migration
  def self.up
    create_table :configuration_groups do |t|
      t.integer :configuration_id
      t.string :name
      t.string :label

      t.timestamps
    end
  end

  def self.down
    drop_table :configuration_groups
  end
end
