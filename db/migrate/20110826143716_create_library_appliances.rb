class CreateLibraryAppliances < ActiveRecord::Migration
  def self.up
    create_table :library_appliances do |t|
      t.string :name
      t.text :description
      t.decimal :price
      t.integer :abiquo_app_id
      t.string :icon_file_name
      t.string :icon_content_type
      t.integer :icon_file_size
      t.datetime :icon_updated_at

      t.timestamps
    end
  end

  def self.down
    drop_table :library_appliances
  end
end
