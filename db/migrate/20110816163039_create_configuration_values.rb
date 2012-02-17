class CreateConfigurationValues < ActiveRecord::Migration
  def self.up
    create_table :configuration_values do |t|
      t.integer :configuration_group_id
      t.string :name
      t.string :label
      t.string :type
      t.text :value

      t.timestamps
    end
  end

  def self.down
    drop_table :configuration_values
  end
end
