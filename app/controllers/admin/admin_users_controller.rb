class Admin::AdminUsersController < Admin::AdminController
  
  def index
    @admin_users = AdminUser.all
  end
  
  def new
    @admin_user = AdminUser.new
  end
  
  def edit
    @admin_user = AdminUser.find(params[:id])
  end
  
  def update
    @admin_user = AdminUser.find(params[:id])

    respond_to do |format|
      if @admin_user.update_attributes(params[:admin_user])
        format.html { redirect_to(admin_admin_users_path, :notice => 'Admin User was successfully updated.') }
        format.xml  { head :ok }
      else
        format.html { render :action => "edit" }
        format.xml  { render :xml => @admin_user.errors, :status => :unprocessable_entity }
      end
    end
  end

  def create
    @admin_user = AdminUser.new(params[:admin_user])

    respond_to do |format|
      if @admin_user.save
        format.html { redirect_to(admin_admin_users_path, :notice => 'Admin User was successfully added.') }
        format.xml  { head :ok }
      else
        format.html { render :action => "edit" }
        format.xml  { render :xml => @admin_user.errors, :status => :unprocessable_entity }
      end
    end
  end

  def destroy
    @admin_user = AdminUser.find(params[:id])
    @admin_user.destroy

    respond_to do |format|
      format.html { redirect_to(admin_admin_users_path, :notice => 'Admin User was deleted.') }
      format.xml  { head :ok }
    end
  end

  # Allow the current logged in user to change their password
  def edit_password
    @admin_user = current_admin_user
  end
  
  def update_password
    @admin_user = current_admin_user

    if @admin_user.update_with_password(params[:admin_user])
      sign_in(@admin_user, :bypass => true)
      redirect_to admin_enterprises_path, :notice => "Password updated!"
    else
      render :edit_password
    end
  end

end
