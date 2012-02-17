class EnterprisesController < ApplicationController
  
  before_filter :get_default_enterprise
  
  # GET /enterprises/new
  # GET /enterprises/new.xml
  def new
    enterprise_params = { :name => session[:get][:enterprise_name] } unless session[:get].nil? or session[:get][:enterprise_name].nil?
    @enterprise = Enterprise.new(enterprise_params)
    user_params = session[:get].select { |k,v| [:first_name, :last_name, :email_address, :username].include? k.to_sym } unless session[:get].nil?
    @enterprise.user = User.new(user_params)

    respond_to do |format|
      format.html # new.html.erb
      format.xml  { render :xml => @enterprise }
    end
  end

  # GET /enterprise/1
  # GET /enterprise/1.xml
  def show
    @enterprise = Enterprise.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.xml  { render :xml => @enterprise }
    end
  end

  # POST /enterprises
  # POST /enterprises.xml
  def create
    @enterprise = Enterprise.new(params[:enterprise])
    @enterprise.add_parameters(params[:parameter])
    
    @enterprise.name = $app_config['enterprise_prefix'] + '_' + @enterprise.name unless $app_config['enterprise_prefix'].nil? or $app_config['enterprise_prefix'].empty?
    
    respond_to do |format|
      if @enterprise.save
        if @enterprise.provision
          format.html { redirect_to provisioned_enterprise_path(@enterprise) }
        else
          if @enterprise.provisioned?
            flash[:notice] = "Your enterprise has already been successfully provisioned."
          else
            flash[:alert] = "There was a problem provisioning your enterprise. Please contact us."
          end
          if @enterprise.status[:status] == :unapproved
            format.html { redirect_to approval_enterprise_path(@enterprise) }
          else
            format.html { redirect_to error_enterprise_path(@enterprise) }
          end
        end
      else
        logger.debug @enterprise.resource_id.to_s
        logger.debug @enterprise.errors
        format.html { render :action => "new" }
        format.xml  { render :xml => @enterprise.errors, :status => :unprocessable_entity }
      end
    end
  end
  
  def provision(enterprise)
    @enterprise = enterprise
    respond_to do |format|
      if @enterprise.provision
        format.html { redirect_to provisioned_enterprise_path(@enterprise) }
      else
        if @enterprise.provisioned?
          flash[:notice] = "Your enterprise has already been successfully provisioned."
        else
          flash[:alert] = "There was a problem provisioning your enterprise. Please contact us."
        end
        if @enterprise.status[:status] == :unapproved
          format.html { redirect_to approval_enterprise_path(@enterprise) }
        else
          format.html { redirect_to error_enterprise_path(@enterprise) }
        end
      end
    end
  end
  
  def approval
    @enterprise = Enterprise.find(params[:id])
  end
  
  def provisioned
    @enterprise = Enterprise.find(params[:id])
  end

  def get_default_enterprise
    # get the default enterprise and parameters
    @default_enterprise = Enterprise.find_by_name(:default)
    @parameters = @default_enterprise.parameters.visible unless @default_enterprise.nil?
  end
end
