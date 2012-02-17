class CreateParameters < ActiveRecord::Migration
  def self.up
    create_table :parameters do |t|
      t.integer :enterprise_id
      t.string :name
      t.string :api_id
      t.boolean :visible
      t.string :unit
      t.integer :min
      t.integer :max
      t.integer :interval
      t.integer :default_value
      t.decimal :unit_price

      t.timestamps
    end
  end

  def self.down
    drop_table :parameters
  end
end
