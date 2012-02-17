class AddApiMult < ActiveRecord::Migration
  def self.up
    add_column :parameters, :api_mult, :decimal
  end

  def self.down
    remove_column :parameters, :api_mult
  end
end
