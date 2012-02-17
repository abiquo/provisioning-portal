class AddUsernamePasswordToEnterprise < ActiveRecord::Migration
  def self.up
    add_column :enterprises, :username, :string
    add_column :enterprises, :password, :string
  end

  def self.down
    remove_column :enterprises, :username
    remove_column :enterprises, :password
  end
end
