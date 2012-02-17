class Admin::VirtualDatacentersController < Admin::AdminController
  def index
    @virtual_datacenters = VirtualDatacenter.all
  end

  def show
    begin
      @virtual_datacenter = VirtualDatacenter.find(params[:id])
    rescue ActiveRecord::RecordNotFound
      render :not_found, :status => :not_found
    end
  end

  def edit
    @virtual_datacenter = VirtualDatacenter.find(params[:id])
  end

  def update
    @virtual_datacenter = VirtualDatacenter.find(params[:id])

    respond_to do |format|
      if @virtual_datacenter.update_attributes(params[:virtual_datacenter])
        format.html { redirect_to(admin_virtual_datacenters_path, :notice => 'VirtualDatacenter was successfully updated.') }
        format.xml  { head :ok }
      else
        format.html { render :action => "edit" }
        format.xml  { render :xml => @virtual_datacenter.errors, :status => :unprocessable_entity }
      end
    end
  end

  def provision
    @virtual_datacenter = VirtualDatacenter.find(params[:id])
    respond_to do |format|
      if @virtual_datacenter.provision
        format.html { redirect_to admin_virtual_datacenters_path, :notice => "VirtualDatacenter provisioned successfully." }
      else
        format.html { redirect_to admin_virtual_datacenters_path, :alert => "There was a problem provisioning the virtual_datacenter. Please see the status field." }
      end
    end
  end

  def destroy
    @virtual_datacenter = VirtualDatacenter.find(params[:id])
    @virtual_datacenter.destroy

    respond_to do |format|
      format.html { redirect_to(admin_virtual_datacenters_path, :notice => 'VirtualDatacenter was deleted.') }
      format.xml  { head :ok }
    end
  end

  def approve
    @virtual_datacenter = VirtualDatacenter.find(params[:id])
    @virtual_datacenter.approve
    respond_to do |format|
      if @virtual_datacenter.provision
        format.html { redirect_to admin_virtual_datacenters_path, :notice => "VirtualDatacenter provisioned successfully." }
      else
        format.html { redirect_to admin_virtual_datacenters_path, :alert => "There was a problem provisioning the virtual_datacenter. Please see the status field." }
      end
    end
  end

  def deny
    @virtual_datacenter = VirtualDatacenter.find(params[:id])
    @virtual_datacenter.deny
    redirect_to admin_virtual_datacenters_path, :notice => "VirtualDatacenter has been denied."
  end

end
