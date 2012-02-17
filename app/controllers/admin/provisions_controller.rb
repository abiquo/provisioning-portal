class Admin::ProvisionsController < Admin::AdminController
  def index
    @enterprises = Enterprise.where(:name.ne => "default")
  end
end
