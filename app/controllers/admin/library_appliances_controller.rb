class Admin::LibraryAppliancesController < Admin::AdminController

  def index
    @library_appliances = LibraryAppliance.all
  end
  
  def new
    @library_appliance = LibraryAppliance.new
  end
  
  def edit
    @library_appliance = LibraryAppliance.find(params[:id])
  end
  
  def update
    @library_appliance = LibraryAppliance.find(params[:id])

    respond_to do |format|
      if @library_appliance.update_attributes(params[:library_appliance])
        format.html { redirect_to(admin_library_appliances_path, :notice => 'Appliance was successfully updated.') }
        format.xml  { head :ok }
      else
        format.html { render :action => "edit" }
        format.xml  { render :xml => @library_appliance.errors, :status => :unprocessable_entity }
      end
    end
  end

  def create
    @library_appliance = LibraryAppliance.new(params[:library_appliance])

    respond_to do |format|
      if @library_appliance.save
        format.html { redirect_to(admin_library_appliances_path, :notice => 'Appliance was successfully added.') }
        format.xml  { head :ok }
      else
        format.html { render :action => "edit" }
        format.xml  { render :xml => @library_appliance.errors, :status => :unprocessable_entity }
      end
    end
  end

  def destroy
    @library_appliance = LibraryAppliance.find(params[:id])
    @library_appliance.destroy

    respond_to do |format|
      format.html { redirect_to(admin_library_appliances_path, :notice => 'Appliance was deleted.') }
      format.xml  { head :ok }
    end
  end

end
