class AddConfirmableToAdminUser < ActiveRecord::Migration
  def self.up
    change_table :admin_users do |t|
      t.confirmable
    end
    
    AdminUser.update_all({:confirmed_at => DateTime.now, :confirmation_token => "Grandfathered Account", :confirmation_sent_at => DateTime.now})
  end

  def self.down
    remove_column :admin_users, [:confirmed_at, :confirmation_token, :confirmation_sent_at]
  end
end
