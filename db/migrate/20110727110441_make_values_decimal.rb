class MakeValuesDecimal < ActiveRecord::Migration
  def self.up
    change_column :parameters, :min, :decimal
    change_column :parameters, :max, :decimal
    change_column :parameters, :interval, :decimal
    change_column :parameters, :value, :decimal
  end

  def self.down
    change_column :parameters, :min, :integer
    change_column :parameters, :max, :integer
    change_column :parameters, :interval, :integer
    change_column :parameters, :value, :integer
  end
end
