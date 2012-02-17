class Admin::EnterprisesController < Admin::AdminController
  def index
    @enterprises = Enterprise.where(:name.ne => "default")
    # @enterprises = Enterprise.where(:name.ne => "default").includes(:virtual_datacenters).where(:virtual_datacenters => {:name => nil})
  end
  
  def show
    begin
      @enterprise = Enterprise.find(params[:id])
    rescue ActiveRecord::RecordNotFound
      render :not_found, :status => :not_found
    end
  end
  
  def edit
    @enterprise = Enterprise.find(params[:id])
  end
  
  def update
    @enterprise = Enterprise.find(params[:id])

    respond_to do |format|
      if @enterprise.update_attributes(params[:enterprise])
        format.html { redirect_to(admin_enterprises_path, :notice => 'Enterprise was successfully updated.') }
        format.xml  { head :ok }
      else
        format.html { render :action => "edit" }
        format.xml  { render :xml => @enterprise.errors, :status => :unprocessable_entity }
      end
    end
  end

  def provision
    @enterprise = Enterprise.find(params[:id])
    respond_to do |format|
      if @enterprise.provision
        format.html { redirect_to admin_enterprises_path, :notice => "Enterprise provisioned successfully." }
      else
        format.html { redirect_to admin_enterprises_path, :alert => "There was a problem provisioning the enterprise. Please see the status field." }
      end
    end
  end

  def destroy
    @enterprise = Enterprise.find(params[:id])
    @enterprise.destroy

    respond_to do |format|
      format.html { redirect_to(admin_enterprises_path, :notice => 'Enterprise was deleted.') }
      format.xml  { head :ok }
    end
  end

  def approve
    @enterprise = Enterprise.find(params[:id])
    @enterprise.approve
    respond_to do |format|
      if @enterprise.provision
        format.html { redirect_to admin_enterprises_path, :notice => "Enterprise provisioned successfully." }
      else
        format.html { redirect_to admin_enterprises_path, :alert => "There was a problem provisioning the enterprise. Please see the status field." }
      end
    end
  end
  
  def deny
    @enterprise = Enterprise.find(params[:id])
    @enterprise.deny
    redirect_to admin_enterprises_path, :notice => "Enterprise has been denied."
  end

end
