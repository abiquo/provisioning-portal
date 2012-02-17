def datacenter_list
  begin
    datacenters = WeBee::Datacenter.all
    list = {}
    datacenters.each { |d| list[d.name] = d.datacenter_id }
    list
  rescue Exception
    {}
  end
end

def enterprise_list
  begin
    enterprises = WeBee::Enterprise.all
    list = {}
    enterprises.each { |e| list[e.name] = e.resource_id }
    list
  rescue Exception
    {}
  end
end

def hypervisor_list(datacenter = nil)
  hypervisors = {}
  begin
    if not datacenter.nil? and datacenter.class.to_s == "WeBee::Datacenter"
      datacenter.racks.each do |rack|
        rack.machines.each do |m|
          hypervisors[m.hypervisortype] = m.hypervisortype
        end
      end
    else
      WeBee::Datacenter.all.each do |dc|
        dc.racks.each do |rack|
          rack.machines.each do |m|
            hypervisors[m.hypervisortype] = m.hypervisortype
          end
        end
      end
    end
    hypervisors
  rescue Exception
    {}
  end
end