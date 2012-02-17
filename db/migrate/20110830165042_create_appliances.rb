class CreateAppliances < ActiveRecord::Migration
  def self.up
    create_table :appliances do |t|
      t.string :name
      t.decimal :price
      t.integer :user_id

      t.timestamps
    end
  end

  def self.down
    drop_table :appliances
  end
end
