class Parameter < ActiveRecord::Base
  belongs_to :enterprise
  
  default_scope :order => 'sort_order ASC'
  
  scope :default, lambda {
    joins(:enterprise).where(:enterprise => { :name => "default" })
  }

  scope :visible, lambda {
    where(:visible => true)
  }
end
