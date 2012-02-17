class Admin::AdminController < ApplicationController
  layout "admin/layouts/admin"
  before_filter :authenticate_admin_user!
end
