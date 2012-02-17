class AddTotalPrice < ActiveRecord::Migration
  def self.up
    add_column :enterprises, :total_price, :decimal
  end

  def self.down
    remove_column :enterprises, :total_price
  end
end
