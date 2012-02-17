class Admin::ConfigurationController < Admin::AdminController
  def index
    @configuration = Configuration.find(1)
  end
  
  def update
    @configuration = Configuration.find(1)
    
    respond_to do |format|
      if @configuration.update_attributes(params[:configuration])
        format.html { redirect_to admin_configuration_index_path, :notice => 'Configuration was successfully updated.' }
        format.xml  { head :ok }
      else
        format.html { render :action => "index" }
        format.xml  { render :xml => @configuration.errors, :status => :unprocessable_entity }
      end
    end
  end

end
