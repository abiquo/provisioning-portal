class CreateUsers < ActiveRecord::Migration
  def self.up
    create_table :users do |t|
      t.string :first_name
      t.string :last_name
      t.string :email_address
      t.string :username
      t.string :password
      t.string :encrypted_password

      t.timestamps
    end
    add_column :enterprises, :user_id, :integer
    remove_column :enterprises, :first_name
    remove_column :enterprises, :last_name
    remove_column :enterprises, :email_address
    remove_column :enterprises, :username
    remove_column :enterprises, :password
  end

  def self.down
    drop_table :users
    remove_column :enterprises, :user_id
    add_column :enterprises, :username, :string
    add_column :enterprises, :password, :string
    add_column :enterprises, :first_name, :string
    add_column :enterprises, :last_name, :string
    add_column :enterprises, :email_address, :string
  end
end