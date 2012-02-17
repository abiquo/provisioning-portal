class Notifier < ActionMailer::Base
  default :from => "provisioning@abiquo.com"
  
  def notify_user(object)
    @abiquo_url = $app_config['abiquo_url']
    @object = object
    mail(:to => "#{@object.user.first_name} #{@object.user.last_name} <#{@object.user.email_address}>",
         :from => "#{$app_config['sender_name']} <#{$app_config['sender_email']}>",
         :subject => $app_config['user_provisioned_email_subject'].gsub('{object}', @object.label))
  end
  
  def notify_approver(object)
    @object = object
    mail(:to => $app_config['approver_email_address'],
         :from => "#{$app_config['sender_name']} <#{$app_config['sender_email']}>",
         :subject => $app_config['approval_request_subject'].gsub('{object}', @object.label))
  end
  
  def notify_user_of_pending_approval(object)
    @object = object
    mail(:to => "#{@object.user.first_name} #{@object.user.last_name} <#{@object.user.email_address}>",
         :from => "#{$app_config['sender_name']} <#{$app_config['sender_email']}>",
         :subject => $app_config['user_approval_email_subject'].gsub('{object}', @object.label))
  end
  
  def notify_user_of_approval(object)
    @object = object
    mail(:to => "#{@object.user.first_name} #{@object.user.last_name} <#{@object.user.email_address}>",
         :from => "#{$app_config['sender_name']} <#{$app_config['sender_email']}>",
         :subject => $app_config['user_approved_email_subject'].gsub('{object}', @object.label))
  end
  
  def notify_user_of_denial(object)
    @object = object
    mail(:to => "#{@object.user.first_name} #{@object.user.last_name} <#{@object.user.email_address}>",
         :from => "#{$app_config['sender_name']} <#{$app_config['sender_email']}>",
         :subject => $app_config['user_denied_email_subject'].gsub('{object}', @object.label))
  end
  
end
