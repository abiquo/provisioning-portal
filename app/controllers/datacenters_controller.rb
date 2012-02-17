class DatacentersController < ApplicationController
  def index
    @datacenters = datacenter_list
    respond_to do |format|
      format.xml  { render :xml => @datacenters.invert }
      format.json  { render :json => @datacenters.invert }
    end
  end
  
  def show
    @datacenter = WeBee::Datacenter.find(params[:id])
    respond_to do |format|
      format.xml { render :xml => @datacenter.raw }
      format.json { render :json => @datacenter.to_json }
    end
  end
  
  def hypervisors
    @datacenter = WeBee::Datacenter.find(params[:id])
    @hypervisors = hypervisor_list @datacenter
    respond_to do |format|
      format.xml  { render :xml => @hypervisors.invert }
      format.json  { render :json => @hypervisors.invert }
    end
  end
end
