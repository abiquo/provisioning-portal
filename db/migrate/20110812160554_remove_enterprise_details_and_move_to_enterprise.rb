class RemoveEnterpriseDetailsAndMoveToEnterprise < ActiveRecord::Migration
  def self.up
    drop_table :enterprise_details
    add_column :enterprises, :first_name, :string
    add_column :enterprises, :last_name, :string
    add_column :enterprises, :email_address, :string
  end

  def self.down
    create_table :enterprise_details do |t|
      t.integer :enterprise_id
      t.string :email_address
      t.string :company
      t.string :first_name
      t.string :last_name
      t.string :tel
      t.string :address
      t.string :city
      t.string :state
      t.string :zip
      t.string :country

      t.timestamps
    end
    remove_column :enterprises, :first_name, :string
    remove_column :enterprises, :last_name, :string
    remove_column :enterprises, :email_address, :string
  end
end
