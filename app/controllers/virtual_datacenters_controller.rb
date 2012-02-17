class VirtualDatacentersController < ApplicationController
  before_filter :get_datacenters, :only => [:new, :create]

  # GET /virtual_datacenters/new
  # GET /virtual_datacenters/new.xml
  def new
    @virtual_datacenter = VirtualDatacenter.new
    user_params = session[:get].select { |k,v| [:first_name, :last_name, :email_address, :username].include? k.to_sym } unless session[:get].nil?
    @virtual_datacenter.user = User.new(user_params)
    if not session[:get].nil? and session[:get][:datacenter]
      @virtual_datacenter.datacenter_id = @datacenters[session[:get][:datacenter]]
      @datacenter = WeBee::Datacenter.find(@virtual_datacenter.datacenter_id)
    elsif $app_config['vdc_datacenter_id']
      @virtual_datacenter.datacenter_id = $app_config['vdc_datacenter_id']
      logger.debug "VDC_ID: " + @virtual_datacenter.datacenter_id.to_s
      session[:get] ||= {}
      session[:get][:datacenter] = @datacenters.key($app_config['vdc_datacenter_id'])
      @datacenter = WeBee::Datacenter.find(@virtual_datacenter.datacenter_id)
    end

    if not session[:get].nil? and session[:get][:hypervisor]
      @virtual_datacenter.hypervisor = session[:get][:hypervisor]
    elsif $app_config['vdc_hypervisor']
      @virtual_datacenter.hypervisor = $app_config['vdc_hypervisor']
      session[:get] ||= {}
      session[:get][:hypervisor] = $app_config['vdc_hypervisor'] unless $app_config['vdc_hypervisor'].empty?
    end

    respond_to do |format|
      format.html # new.html.erb
      format.xml  { render :xml => @virtual_datacenter }
    end
  end

  # POST /virtual_datacenters
  # POST /virtual_datacenters.xml
  def create
    @virtual_datacenter = VirtualDatacenter.new(params[:virtual_datacenter])
    
    if not session[:get].nil? and session[:get][:enterprise_name]
      @virtual_datacenter.enterprise = Enterprise.find_or_create_by_name(:name => session[:get][:enterprise_name])
    else
      @virtual_datacenter.enterprise = Enterprise.find_or_create_by_resource_id(:resource_id => $app_config['dc_enterprise_id'])
    end
    
    respond_to do |format|
      if @virtual_datacenter.save
        if @virtual_datacenter.provision
          format.html { redirect_to provisioned_virtual_datacenter_path(@virtual_datacenter) }
        else
          if @virtual_datacenter.provisioned?
            flash[:notice] = "Your virtual datacenter has already been successfully provisioned."
          else
            flash[:alert] = "There was a problem provisioning your virtual datacenter. Please contact us."
          end
          if @virtual_datacenter.status[:status] == :unapproved
            format.html { redirect_to approval_virtual_datacenter_path(@virtual_datacenter) }
          else
            format.html { redirect_to error_virtual_datacenter_path(@virtual_datacenter) }
          end
        end
      else
        logger.debug @virtual_datacenter.errors
        format.html { render :action => "new" }
        format.xml  { render :xml => @virtual_datacenter.errors, :status => :unprocessable_entity }
      end
    end
  end
  
  def approval
    @virtual_datacenter = VirtualDatacenter.find(params[:id])
  end
  
  def provisioned
    @virtual_datacenter = VirtualDatacenter.find(params[:id])
  end
  
  def get_datacenters
    if not session[:get].nil? and session[:get][:enterprise_name]
      @limits = WeBee::Enterprise.find_by_name(session[:get][:enterprise_name]).first.limits.map { |l| l.raw[/<link href=\"(.*)\" rel=\"datacenter\"\/>/, 1].split('/').last }
    else
      @limits = WeBee::Enterprise.find($app_config['dc_enterprise_id']).limits.map { |l| l.raw[/<link href=\"(.*)\" rel=\"datacenter\"\/>/, 1].split('/').last }
    end
    
    @datacenters = datacenter_list.select { |k,v| @limits.include? v }
  end

end
