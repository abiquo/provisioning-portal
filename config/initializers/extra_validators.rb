# config/initializers/extra_validators.rb
class EmailValidator < ActiveModel::EachValidator
  def validate_each(record, attr_name, value)
    unless value =~ /^([^@\s]+)@((?:[-a-z0-9]+\.)+[a-z]{2,})$/i
      record.errors.add(attr_name, :email, options.merge(:value => value))
    end
  end
end

class PhoneValidator < ActiveModel::EachValidator
  def validate_each(record, attr_name, value)
    unless value =~ /^[ 0-9+\(\)\-(ext|ex|x|e)]*$/i
      record.errors.add(attr_name, :phone, options.merge(:value => value))
    end
  end
end

# This allows us to assign the validator in the model
module ActiveModel::Validations::HelperMethods
  def validates_email(*attr_names)
    validates_with EmailValidator, _merge_attributes(attr_names)
  end

  def validates_phone(*attr_names)
    validates_with PhoneValidator, _merge_attributes(attr_names)
  end
end
