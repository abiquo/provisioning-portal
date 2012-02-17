require 'configure_webee'
require 'webee_functions'

class ApplicationController < ActionController::Base
  protect_from_forgery
  before_filter :load_config, :handle_passed_parameters
  
  def load_config
    $app_config = {}
    ConfigurationValue.all.each do |config_value|
      $app_config[config_value.name] = config_value.value
    end
    @app_config = $app_config
    configure_webee($app_config['api_url'], $app_config['api_user'], $app_config['api_pass'])
  end

  def after_sign_in_path_for(resource_or_scope)
    admin_enterprises_path
  end

  def after_sign_out_path_for(resource_or_scope)
    new_admin_user_session_path
  end
  
  def not_found
    raise ActionController::RoutingError.new('Not Found')
  end
  
  def handle_passed_parameters
    @referrer_host = request.referrer[/https?:\/\/([a-z.]+)/, 1]
    if @referrer_host.nil? or @referrer_host.empty?
      reset_session
    end
    session[:get] = request.GET unless request.GET.empty?
  end
  
end
