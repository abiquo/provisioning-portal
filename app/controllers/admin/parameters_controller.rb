class Admin::ParametersController < Admin::AdminController
  def index
    @enterprise = Enterprise.find_by_name("default")
  end

  def update
    @enterprise = Enterprise.find_by_name("default")
    
    respond_to do |format|
      if @enterprise.update_attributes(params[:enterprise])
        format.html { redirect_to admin_parameters_path, :notice => 'Parameters were successfully updated.' }
        format.xml  { head :ok }
      else
        format.html { render :action => "index" }
        format.xml  { render :xml => @parameter.errors, :status => :unprocessable_entity }
      end
    end
  end

end
