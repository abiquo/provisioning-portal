class CreateEnterpriseDetails < ActiveRecord::Migration
  def self.up
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
  end

  def self.down
    drop_table :enterprise_details
  end
end
