class AddStatus < ActiveRecord::Migration
  def self.up
    add_column :enterprises, :status, :string
    add_column :parameters, :include_first, :boolean
  end

  def self.down
    remove_column :enterprises, :status
    remove_column :parameters, :include_first
  end
end
