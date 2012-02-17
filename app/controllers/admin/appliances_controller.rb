class Admin::AppliancesController < Admin::AdminController
  def index
    @appliances = Appliance.all
  end
  
  def show
    begin
      @appliance = Appliance.find(params[:id])
    rescue ActiveRecord::RecordNotFound
      render :not_found, :status => :not_found
    end
  end
  
  def edit
    @appliance = Appliance.find(params[:id])
  end
  
  def update
    @appliance = Appliance.find(params[:id])

    respond_to do |format|
      if @appliance.update_attributes(params[:appliance])
        format.html { redirect_to(admin_appliances_path, :notice => 'Appliance was successfully updated.') }
        format.xml  { head :ok }
      else
        format.html { render :action => "edit" }
        format.xml  { render :xml => @appliance.errors, :status => :unprocessable_entity }
      end
    end
  end

  def provision
    @appliance = Appliance.find(params[:id])
    respond_to do |format|
      if @appliance.provision
        format.html { redirect_to admin_appliances_path, :notice => "Appliance provisioned successfully." }
      else
        format.html { redirect_to admin_appliances_path, :alert => "There was a problem provisioning the appliance. Please see the status field." }
      end
    end
  end

  def destroy
    @appliance = Appliance.find(params[:id])
    @appliance.destroy

    respond_to do |format|
      format.html { redirect_to(admin_appliances_path, :notice => 'Appliance was deleted.') }
      format.xml  { head :ok }
    end
  end

  def approve
    @appliance = Appliance.find(params[:id])
    @appliance.approve
    respond_to do |format|
      if @appliance.provision
        format.html { redirect_to admin_appliances_path, :notice => "Appliance provisioned successfully." }
      else
        format.html { redirect_to admin_appliances_path, :alert => "There was a problem provisioning the appliance. Please see the status field." }
      end
    end
  end
  
  def deny
    @appliance = Appliance.find(params[:id])
    @appliance.deny
    redirect_to admin_appliances_path, :notice => "Appliance has been denied."
  end

end
