class AppliancesController < ApplicationController
  
  # GET /appliances/new
  # GET /appliances/new.xml
  def new
    @library_appliances = LibraryAppliance.all
    
    @appliance = Appliance.new
    #if !session[:get].nil? and session[:get][:username]
    #  user_params = { "first_name" => "(blank)", "last_name" => "(blank)", "email_address" => "(blank)" }
    #else
    #  user_params = {}
    #end
    user_params = session[:get].select { |k,v| [:first_name, :last_name, :email_address, :username].include? k.to_sym } unless session[:get].nil?
    @appliance.user = User.new(user_params)

    respond_to do |format|
      format.html # new.html.erb
      format.xml  { render :xml => @appliance }
    end
  end
  
  def create
    @library_appliance = LibraryAppliance.find(params[:appliance][:library_appliance_id])
    @appliance = Appliance.new(params[:appliance])
    @appliance.name = @library_appliance.name
    @appliance.price = @library_appliance.price
    
    respond_to do |format|
      if @appliance.save
        if @appliance.provision
          format.html { redirect_to provisioned_appliance_path(@appliance) }
        else
          if @appliance.provisioned?
            flash[:notice] = "Your appliance has already been successfully provisioned."
          else
            flash[:alert] = "There was a problem provisioning your appliance. Please contact us."
          end
          if @appliance.status[:status] == :unapproved
            format.html { redirect_to approval_appliance_path(@appliance) }
          else
            format.html { redirect_to error_appliance_path(@appliance) }
          end
        end
      else
        format.html { render :action => "edit" }
        format.xml  { render :xml => @appliance.errors, :status => :unprocessable_entity }
      end
    end    
  end

  def approval
    @appliance = Appliance.find(params[:id])
  end
  
  def provisioned
    @appliance = Appliance.find(params[:id])
  end

end
