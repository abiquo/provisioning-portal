// Place your application-specific JavaScript functions and classes here
// This file is automatically included by javascript_include_tag :defaults

$(function() {
  
  $('tr:even').addClass('even');
  $('tr:odd').addClass('odd');

  $("input[type=submit]").button();
  
  update_totals();
  
  $('#configuration_configuration_groups_attributes_1_configuration_values_attributes_2_value').click(function() {
    set_visibility($('#provision_enterprises'), this.checked)
  });
  
  $('#configuration_configuration_groups_attributes_1_configuration_values_attributes_4_value').click(function() {
    set_visibility($('#provision_datacenters'), this.checked)
  });
  
  set_visibility($('#provision_enterprises'), $('#configuration_configuration_groups_attributes_1_configuration_values_attributes_2_value').is(':checked'));
  set_visibility($('#provision_datacenters'), $('#configuration_configuration_groups_attributes_1_configuration_values_attributes_4_value').is(':checked'));
  
  $(".slider").each(function() {
    input = $("#"+$(this).attr("data-for"));
    min = input.attr("min");
    max = input.attr("max");
    step = input.attr("step");
    value = input.val();
    $(this).slider({
      min: parseFloat(min),
      max: parseFloat(max),
      step: parseFloat(step),
      value: parseFloat(value),
			slide: function(event, ui) {
				$("#"+$(this).attr("data-for")).val(ui.value);
				update_totals();
			}
    });
  });
  
  $("#provision_tabs").tabs();
  $("#edit_configuration_1").tabs();
  
  $("#library_appliances li a").click(function () {
    appliance_id = $(this).attr("data-library-appliance-id");
    $("#library_appliances li a").removeClass("selected");
    $(this).addClass("selected");
    $("#appliance_submit").button({ disabled: false });
    $("#appliance_library_appliance_id").val(appliance_id);
    $("#selected_appliance").text("You have selected: " + $(this).children("h3").text())
    return false;
  });
  
  $("#appliance_submit").button({ disabled: true });
  
  function update_totals() {
    total_price = 0;
    $(".price_per_unit").each(function () {
      id = $(this).attr("data-for");
      value = $("#"+id).val();
      $("#"+id).val(parseFloat(value));
      step = $("#"+id).attr("step");
      units = value / step;
      if ($(this).attr("data-include-first") == "true") {
        units = units - 1;
      }
      price = roundNumber($(this).attr("data-unit-price") * units, 2);
      total_price = total_price + price;
      $(this).val(price.toFixed(2));
    })
    $("#total_price").val(total_price.toFixed(2));
  }
  
  $(".virtual_datacenter select#virtual_datacenter_hypervisor").after('<img src="/images/ajax-loader.gif" id="hypervisor_loader" align="absmiddle" style="margin-left: 1em; display: none;" />');
  
  $(".virtual_datacenter select#virtual_datacenter_datacenter_id").change(function () {
    set_datacenter();
  });
  
  set_datacenter();
  
});

function set_datacenter() {
  if ($("select#virtual_datacenter_hypervisor").length) {
    datacenter_id = $("#virtual_datacenter_datacenter_id").val();
    if (datacenter_id) {
      $("#hypervisor_loader").show();
      $("#virtual_datacenter_datacenter_id").attr("disabled", "disabled");
      $.get("/datacenters/" + datacenter_id + "/hypervisors.json", function(data) {
        $("#hypervisor_loader").hide();
        $("select#virtual_datacenter_hypervisor").html('<option value=""></option>');
        $.each(data, function(k, v) {
          $("select#virtual_datacenter_hypervisor").append('<option value="' + k + '">' + v + '</option>')
        });
        $("#virtual_datacenter_datacenter_id").removeAttr("disabled");
        $("select#virtual_datacenter_hypervisor").removeAttr("disabled");
      });
    }
    else {
      $("select#virtual_datacenter_hypervisor").html('<option value=""></option>');
      $("select#virtual_datacenter_hypervisor").attr("disabled", "disabled");
    }
  }
}

function set_visibility(object, visible) {
  if (visible) {
    object.show();
  }
  else {
    object.hide();
  }
}

function roundNumber(num, dec) {
	var result = Math.round(num*Math.pow(10,dec))/Math.pow(10,dec);
	return result;
}
