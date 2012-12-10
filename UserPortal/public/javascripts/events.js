$(document).ready(function () {
	//console.log("hola");
    $('#tabnav a[href^="' + location.pathname + location.search + '"]').addClass('active');
    $( "#tabs" ).tabs({
        ajaxOptions: {
            error: function( xhr, status, index, anchor ) {
                $( anchor.hash ).html(
                    "Couldn't load this tab. We'll try to fix this as soon as possible. " );
            }
        }
    });
    
    $('.accordion .head').live('click', function() {
        $(this).next().toggle();
        return false;
    }).next().hide();
    
    $('#showRegister').live('click', function() {
    	$('#loginForm').fadeOut( function() {
    		$('#registerForm').fadeIn();
		});    	        
        return false;
    });
    
    $('#showLogin').live('click', function() {
    	$('#registerForm').fadeOut( function() {
    		$('#loginForm').fadeIn();
		});    	        
        return false;
    });

    $('.publishCheck').live('click', function() {	

    	var $state = $(this).find('#state');	
    	var $check = $(this).find('#check');
    
    	var val = $state.attr('value');
    	var checked = $check.is('.active');
    	//console.log(checked);
    	
    	if (checked) {
    		$check.val('Publish')
    		$check.removeClass('btn-danger active').addClass('btn-warning');    	
    	}
    	else  {
    		$check.val('Actived')
    		$check.addClass('btn-danger active').removeClass('btn-warning');    	
    	}
    	if (val == 'true') val = 'false';
    	else val = 'true';    	
    	
    	$state.attr('value', val);	
    	console.log($state.val());
    	//console.log($(this).find('#state').attr('value'));
    	return false;
    });
    /*$('.checkPublish').live('click', function() {    	
		var $checkbox = $(this);
		if ($checkbox.is(':checked')) $checkbox.attr('checked','unchecked');
		else $checkbox.attr('checked','checked');		
	    $(this).attr('name','scOffer');
    	//console.log("publish");		
		return false;
	});*/

    $('.ui-accordion').bind('accordionchange', function(event, ui) {
      
	  //ui.newHeader // jQuery object, activated header
	  //ui.oldHeader // jQuery object, previous header
	  //ui.newContent // jQuery object, activated content
	  //ui.oldContent // jQuery object, previous content
	});
    
    
    //******************Input type***********************//
    
	/*var wrapper = $('<div/>').css({height:0,width:0,'overflow':'hidden'});
	var fileInput = $(':file').wrap(wrapper);
	
	fileInput.live('change', function(){
	    $this = $(this);
	    $('#file').text($this.val());
	})
	
	$('#file').live('click', function(){
	    fileInput.click();
	}).show();  */
    
    //***************************************************//
    $(".offerDetails").live('click', function(){
        //alert($(this).attr("id").toString());
        var url = $(this).attr("url").toString();
        var dialog = $('<div id="progressbar" class="loading dialog"></div>').appendTo('body');
        
        $('#progressbar').progressbar();
        var pGress = setInterval(function() {
            //$('#progressbar').css("background-image","http://jqueryui.com/resources/demos/progressbar/images/pbar-ani.gif");
            var pVal = $('#progressbar').progressbar('option', 'value');
            var pCnt = !isNaN(pVal) ? (pVal + 1) : 1;
            if (pCnt > 100) {
                clearInterval(pGress);
            } else {
                $('#progressbar').progressbar({value: pCnt});
            }
        },10);
        var posy = $(document).height() / 8;
        var posx = $(document).width() / 4;
           
        dialog.dialog({
        	autoOpen: false,
        	class: 'flora',
            // add a close listener to prevent adding multiple divs to the document
            close: function(event, ui) {
                // remove div with all data and events
                dialog.remove();
            },           
            width: '800px',   
            draggable: true,
            //heigth: 'auto',
            resizable: false,
            modal: true,
            buttons: [{             
                text: "Cancel",
                class: "btn btn-danger",
                click: function() {
                    $( this ).dialog( "close" );
                }
            }]
        });
                
        // load remote content
        dialog.load(
            url, function (responseText, textStatus, XMLHttpRequest) {
                // remove the loading class
                dialog.removeClass('loading');
                //$(this).setContent("Success");
                $(this).dialog('open');
            }
        );
        
       /* var url = $( "#accordionOffers" ).find('.ui-state-active a').attr('href');
		var activeTab = $( "#accordionOffers" ).find('.ui-accordion-content-active');		
		activeTab.load(
            url, function (responseText, textStatus, XMLHttpRequest) {               
            	//$("#dialog").dialog("close");
            }
        ); */
        //prevent the browser to follow the link
        return false;    
    });
    /*$(".vnc").live('click', function(){
        //alert($(this).attr("id").toString());
        var url = $(this).attr("url").toString();
        var dialog = $('<div style="display:none" class="loading dialog"></div>');
        var posy = $(document).height() / 8;
        var posx = $(document).width() / 4;
           
        dialog.dialog({
        	autoOpen: false,        	
            // add a close listener to prevent adding multiple divs to the document
            close: function(event, ui) {
                // remove div with all data and events
                dialog.remove();
            },           
            width: '800px',   
            //heigth: 'auto',
            resizable: false,
            modal: true,
            buttons: [{             
                text: "Cancel",
                class: "btn btn-danger",
                click: function() {
                    $( this ).dialog( "close" );
                }
            }]
        });
                
        // load remote content
        dialog.load(
            url, function (responseText, textStatus, XMLHttpRequest) {
                // remove the loading class
                dialog.removeClass('loading');
                //$(this).setContent("Success");
                $(this).dialog('open');                
            }
        );
        //prevent the browser to follow the link
        return false;    
    });*/
    
    //$('#noVNC_connect_button').on('ready', click());
    
    $(".configureExistingOffer").live('click', function(){
        //alert($(this).attr("id").toString());
        var url = $(this).attr("id").toString();
        var dialog = $('<div id="dialogConfigureExisting" class="loading dialog" style="display: none"></div>').appendTo('body');
        
        /*$('#progressbar').progressbar();
        var pGress = setInterval(function() {
            //$('#progressbar').css("background-image","http://jqueryui.com/resources/demos/progressbar/images/pbar-ani.gif");
            var pVal = $('#progressbar').progressbar('option', 'value');
            var pCnt = !isNaN(pVal) ? (pVal + 1) : 1;
            if (pCnt > 100) {
                clearInterval(pGress);
            } else {
                $('#progressbar').progressbar({value: pCnt});
            }
        },10);*/
        //var posy = $(document).height() / 8;
        //var posx = $(document).width() / 4;
        
        dialog.dialog({
        	autoOpen: false,
        	//position: [posx,posy],
            // add a close listener to prevent adding multiple divs to the document
            close: function(event, ui) {
                // remove div with all data and events
                dialog.remove();
            },           
            draggable: true,
            width: 'auto',          
            resizable: false,
            modal: true,
            buttons: [{             
                text: "Cancel",
                class: "btn btn-danger",
                click: function() {
                    $( this ).dialog( "close" );
                }
            }]
        });
        // load remote content
        dialog.load(
            url, function (responseText, textStatus, XMLHttpRequest) {
                // remove the loading class
                dialog.removeClass('loading');
                $(this).dialog('open');
            }
        ).fadeOut().fadeIn();
        //prevent the browser to follow the link
        return false;    
    }); 
    $(".configureVDC").live('click', function(){
        //alert($(this).attr("id").toString());
        var url = $(this).attr("id").toString();
        var dialog = $('<div id="dialogconfigure" class="loading dialog" style="display:none"></div>').appendTo('body');        
     
        /*$('#progressbar').progressbar();
        var pGress = setInterval(function() {
            //$('#progressbar').css("background-image","http://jqueryui.com/resources/demos/progressbar/images/pbar-ani.gif");
            var pVal = $('#progressbar').progressbar('option', 'value');
            var pCnt = !isNaN(pVal) ? (pVal + 1) : 1;
            if (pCnt > 100) {
                clearInterval(pGress);
            } else {
                $('#progressbar').progressbar({value: pCnt});
            }
        },10);
        var posy = $(document).height() / 8;
        var posx = $(document).width() / 4;*/
        
        dialog.dialog({
        	autoOpen: false,
        	//position: [posx,posy],
            // add a close listener to prevent adding multiple divs to the document
            close: function(event, ui) {
                // remove div with all data and events
                dialog.remove();
            },
            width: 'auto',          
            resizable: false,
            draggable: true,
            modal: true,
            buttons: [{             
                text: "Cancel",
                class: "btn btn-danger",
                click: function() {
                    $( this ).dialog( "close" );
                }
            }]
        });
        // load remote content
        dialog.load(
            url, function (responseText, textStatus, XMLHttpRequest) {
                // remove the loading class
                dialog.removeClass('loading');
                $(this).dialog('open');                
            }
        ).fadeOut().fadeIn();
        //prevent the browser to follow the link
        return false;    
    }); 
    $(".disableOffer").live('click', function(){
        //alert($(this).attr("id").toString());
        var url = $(this).attr("id").toString();
        var dialog = $('<div id="progressbar" class="loading dialog" style="display:none"></div>').appendTo('body');
        
        $('#progressbar').progressbar();
        var pGress = setInterval(function() {
            //$('#progressbar').css("background-image","http://jqueryui.com/resources/demos/progressbar/images/pbar-ani.gif");
            var pVal = $('#progressbar').progressbar('option', 'value');
            var pCnt = !isNaN(pVal) ? (pVal + 1) : 1;
            if (pCnt > 100) {
                clearInterval(pGress);
            } else {
                $('#progressbar').progressbar({value: pCnt});
            }
        },10);
               
        // load remote content
        dialog.load(
            url, function (responseText, textStatus, XMLHttpRequest) {
                // remove the loading class
                dialog.removeClass('loading');
                //$(this).setContent("Success");
                //$(this).dialog('open');
                var url = $( "#accordionOffers" ).find('.ui-state-active').find('.offerUrl').attr('href');
                //console.log(url);
        		var activeTab = $( "#accordionOffers" ).find('.ui-accordion-content-active');		
        		 //console.log(activeTab);
        		activeTab.load(
                    url, function (responseText, textStatus, XMLHttpRequest) {               
                    	//$("#dialog").dialog("close");
                    }
                ).fadeOut().fadeIn();
                //prevent the browser to follow the link
            }
        );        
       
        return false;    
    }); 
    $(".listVM").live('click', function(){
        //alert($(this).attr("id").toString());
        var url = $(this).attr("id").toString();
        var dialog = $('<div style="margin-bottom: 9px;">' +
                        '<div><div><h3><i class="icon-tasks"></i>Loading</h3></div><br />Please Wait...<br /></div>' +
                        '<div class="progressbar" style="width: 100%"></div></div>').appendTo('body');
        
        $('.progressbar').progressbar({value: 100});
        

        var posy = $(document).height() / 8;
        var posx = $(document).width() / 4;
        
        dialog.dialog({
        	autoOpen: true,           
        	//show: 'slide', 
            // add a close listener to prevent adding multiple divs to the document
        	position: [posx,posy],
            close: function(event, ui) {
                // remove div with all data and events
                dialog.remove();
            },
            modal: true,
            resizable: false,
            draggable: true,
            width: '800px',          
            buttons: [{             
                text: "Close",
                class: "btn btn-warning",
                click: function() {
                    $( this ).dialog( "close" );
                }
            }]
        });
        
        // load remote content
        dialog.css({opacity: 0}, 1000).load(
            url, function (responseText, textStatus, XMLHttpRequest) {
                // remove the loading class
        	$(this).find('.vmDetails').each(function() {
    		var vmDetails = $(this).attr("id");
				$(this).css({opacity: 0}, 1000).load(vmDetails, function (responseText, textStatus, XMLHttpRequest) {
					$(this).animate({opacity: 1}, 1000)
				});					
    		});            	     
        }).animate({opacity: 1}, 1000);
        
        //dialog.dialog('open');
             
        //console.log(dialog.getElementsByTagName('a'));
       // console.log(dialog.find('a')[0].value)
		
        //prevent the browser to follow the link
        return false;    
    }); 
    $(".publishMarketplace").live('click', function(){
        //alert($(this).attr("id").toString());
        var url = $(this).attr("url").toString();
       var dialog = $('<div style="margin-bottom: 9px;">' +
                '<div><div><h3><i class="icon-tasks"></i>Loading</h3></div><br />Please Wait...<br /></div>' +
                '<div class="progressbar" style="width: 100%"></div></div>').appendTo('body');
        
        $('.progressbar').progressbar({value: 100});

        var posy = $(document).height() / 8;
        var posx = $(document).width() / 4;
        
        dialog.dialog({        	
        	autoOpen: false,
        	position: [posx,posy],
            // add a close listener to prevent adding multiple divs to the document
            close: function(event, ui) {
                // remove div with all data and events
                dialog.remove();
            },         	
            width: 'auto',          
            resizable: false,
            draggable: true,
            modal: true,
            buttons: [{             
                text: "Cancel",
                class: "btn btn-danger",
                click: function() {
                    $( this ).dialog( "close" );
                }
            }]
        });
        // load remote content
        dialog.load(
            url, function (responseText, textStatus, XMLHttpRequest) {
                // remove the loading class
                dialog.removeClass('loading');
                $(this).dialog('open');
            }
        ).fadeOut().fadeIn();
        //prevent the browser to follow the link
        return false;    
    }); 
    $(".enableMarketplace").live('click', function(){
        //alert($(this).attr("id").toString());
        var url = $(this).attr("url").toString();
        var dialog = $('<div style="margin-bottom: 9px;">' +
                '<div><div><h3><i class="icon-tasks"></i>Loading</h3></div><br />Please Wait...<br /></div>' +
                '<div class="progressbar" style="width: 100%"></div></div>').appendTo('body');
        
        $('.progressbar').progressbar({value: 100});
        
        dialog.dialog({
        	autoOpen: false,
        	//position: [posx,posy],
            // add a close listener to prevent adding multiple divs to the document                	
            close: function(event, ui) {
                // remove div with all data and events
                dialog.remove();
            },
            //title: "Configure",
            //height: 'auto',
            //show: { effect: 'show'},
            width: 'auto',
            resizable: false,
            draggable: true,
            modal: true,
            buttons: [{             
                text: "Cancel",
                class: "btn btn-danger",
                click: function() {
                    $( this ).dialog( "close" );
                }
            }]
        });
        // load remote content
        dialog.load(
            url, function (responseText, textStatus, XMLHttpRequest) {
                // remove the loading class
                dialog.removeClass('loading');
                $(this).dialog('open');
            }
        ).fadeOut().fadeIn();       
        return false;    
    }); 
    $(".purchaseConfirm").live('click', function(){
        //alert($(this).attr("id").toString());
        var url = $(this).attr("url").toString();
        var dialog = $('<div style="margin-bottom: 9px;">' +
                '<div><div><h3><i class="icon-tasks"></i>Loading</h3></div><br />Please Wait...<br /></div>' +
                '<div class="progressbar" style="width: 100%"></div></div>').appendTo('body');
        
        $('.progressbar').progressbar({value: 100});

        var posy = $(document).height() / 8;
        var posx = $(document).width() / 4;
        dialog.dialog({
        	autoOpen: false,
        	position: [posx,posy],
            // add a close listener to prevent adding multiple divs to the document                	
            close: function(event, ui) {
                // remove div with all data and events
                dialog.remove();
            },
            //title: "Configure",
            //height: 'auto',
            //show: { effect: 'show'},
            width: '800px',   
            resizable: false,
            draggable: true,
            modal: true,
            buttons: [{             
                text: "Cancel",
                class: "btn btn-danger",
                click: function() {
                	$( ".ui-dialog-content" ).dialog( "close" );
                }
            }]
        });
        // load remote content
        dialog.load(
            url, function (responseText, textStatus, XMLHttpRequest) {
                // remove the loading class
                dialog.removeClass('loading');
                $(this).dialog('open');
            }
        );       
        return false;    
    }); 

    $(".finalPurchaseConfirm").live('click', function(){
        //alert($(this).attr("id").toString());
        var url = $(this).attr("url").toString();
        url = url + "&new_name=" + $('.input-deploy-name').attr('value').toString();
        url = url + "&new_lease_period=" + $('.input-deploy-date').attr('value').toString();
        
        if ($(this).is('.extend')) {
        	var dialog = $('<div style="margin-bottom: 9px;">' +
    				'<div><div><h3><i class="icon-tasks"></i>Extending Lease Period</h3></div><br />Please Wait...<br /></div>' +
                    '<div class="progressbar" style="width: 100%"></div></div>').appendTo('body');

        } else {
        	var dialog = $('<div style="margin-bottom: 9px;">' +
    				'<div><div><h3><i class="icon-tasks"></i>Deploying Offer</h3></div><br />Please Wait...<br /></div>' +
    				'<div class="progressbar" style="width: 100%"></div></div>').appendTo('body');
        }
        
        $('.progressbar').progressbar();
        var pGress = setInterval(function() {
            //$('#progressbar').css("background-image","http://jqueryui.com/resources/demos/progressbar/images/pbar-ani.gif");
            var pVal = $('.progressbar').progressbar('option', 'value');
            var pCnt = !isNaN(pVal) ? (pVal + 1) : 1;
            if (pCnt > 100) {
                clearInterval(pGress);
            } else {
                $('.progressbar').progressbar({value: pCnt});
            }
        },10);
        var posy = $(document).height() / 8;
        var posx = $(document).width() / 4;
        
        dialog.dialog({
            autoOpen: true,
            position: [posx,posy],
            // add a close listener to prevent adding multiple divs to the document                 
            close: function(event, ui) {
                // remove div with all data and events
                dialog.remove();
            },
            //title: "Configure",
            //height: 'auto',
            //show: { effect: 'show'},
            width: '800px',     
            resizable: false,
            draggable: true,
            modal: true,
            buttons: [{             
                text: "Close",
                class: "btn btn-danger",
                click: function() {
                    $( ".ui-dialog-content" ).dialog( "close" );
                }
            }]
        });
        // load remote content
        dialog.load(
            url, function (responseText, textStatus, XMLHttpRequest) {
                // remove the loading class
                dialog.removeClass('progress progress-warning progress-striped active');
                
            }
        );       
        return false;   
    }); 
    
    $(".deleteConfirm").live('click', function(){
        //alert($(this).attr("id").toString());        
        var url = $(this).attr("url").toString();
        var dialog = $('<div style="display:none" class="loading dialog"><h3><i class="icon-warning-sign"></i> Are you sure you want to delete this Offer?</h3></div>').appendTo('body');
        var posy = $(document).height() / 8;
        var posx = $(document).width() / 4;
        
        dialog.dialog({
        	autoOpen: true,
        	position: [posx,posy],
            // add a close listener to prevent adding multiple divs to the document                	
            close: function(event, ui) {
                // remove div with all data and events
                dialog.remove();
            },
            //title: "Configure",
            //height: 'auto',
            //show: { effect: 'show'},
            width: '800px',   
            resizable: false,
            draggable: true,
            modal: true,
            buttons: [
                {             
                	text: "Delete",
                	class: "btn btn-danger finalDeleteConfirm",
                	url: url
                },
                {             
                	text: "Cancel",
                	class: "btn btn-warning",
                	click: function() {
                		$( this ).dialog( "close" );
                	}
                }
            ]
        });
        // load remote content
        /*dialog.load(
            url, function (responseText, textStatus, XMLHttpRequest) {
                // remove the loading class
                dialog.removeClass('loading');
                $(this).dialog('open');
            }
        );       */
        return false;    
    }); 
    
    $(".finalDeleteConfirm").live('click', function(){
        //alert($(this).attr("id").toString());
        var url = $(this).attr("url").toString();
        
        var dialog = $('<div style="margin-bottom: 9px;">' +
	    				'<div><div><h3><i class="icon-tasks"></i>Deleting Offer</h3></div><br />Please Wait...<br /></div>' +
	    				'<div class="progressbar" style="width: 100%"></div></div>').appendTo('body');   
        
        $('.progressbar').progressbar();
        var pGress = setInterval(function() {
            //$('#progressbar').css("background-image","http://jqueryui.com/resources/demos/progressbar/images/pbar-ani.gif");
            var pVal = $('.progressbar').progressbar('option', 'value');
            var pCnt = !isNaN(pVal) ? (pVal + 1) : 1;
            if (pCnt > 100) {
                clearInterval(pGress);
            } else {
                $('.progressbar').progressbar({value: pCnt});
            }
        },10);
        var posy = $(document).height() / 8;
        var posx = $(document).width() / 4;
        
        dialog.dialog({
        	autoOpen: true,
        	position: [posx,posy],
            // add a close listener to prevent adding multiple divs to the document                	
            close: function(event, ui) {
                // remove div with all data and events
                dialog.remove();
            },
            //title: "Configure",
            //height: 'auto',
            //show: { effect: 'show'},
            width: '800px',     
            resizable: false,
            draggable: true,
            modal: true,
            buttons: [{             
                text: "Close",
                class: "btn btn-danger",
                click: function() {
                    $( ".ui-dialog-content" ).dialog( "close" );
                }
            }]
        });
        // load remote content
        dialog.load(
            url, function (responseText, textStatus, XMLHttpRequest) {
                // remove the loading class
                dialog.removeClass('progress progress-warning progress-striped active');
                
            }
        );       
        return false;    
    }); 
    
    $(".undeployConfirm").live('click', function(){
        //alert($(this).attr("id").toString());
        var url = $(this).attr("url").toString();
        
        var dialog = $('<div style="margin-bottom: 9px;">' +
	    				'<div><div><h3><i class="icon-tasks"></i>Pausing Offer</h3></div><br />Please Wait...<br /></div>' +
	    				'<div class="progressbar" style="width: 100%"></div></div>').appendTo('body');

        $('.progressbar').progressbar();
        var pGress = setInterval(function() {
            //$('#progressbar').css("background-image","http://jqueryui.com/resources/demos/progressbar/images/pbar-ani.gif");
            var pVal = $('.progressbar').progressbar('option', 'value');
            var pCnt = !isNaN(pVal) ? (pVal + 1) : 1;
            if (pCnt > 100) {
                clearInterval(pGress);
            } else {
                $('.progressbar').progressbar({value: pCnt});
            }
        },10);
        var posy = $(document).height() / 8;
        var posx = $(document).width() / 4;
         
        dialog.dialog({
        	autoOpen: true,
        	position: [posx,posy],
            // add a close listener to prevent adding multiple divs to the document                	
            close: function(event, ui) {
                // remove div with all data and events
                dialog.remove();
            },
            //title: "Configure",
            //height: 'auto',
            //show: { effect: 'show'},
            width: '800px',     
            resizable: false,
            draggable: true,
            modal: true,
            buttons: [{             
                text: "Close",
                class: "btn btn-danger",
                click: function() {
                    $( this ).dialog( "close" );
                }
            }]
        });
        // load remote content
        dialog.load(
            url, function (responseText, textStatus, XMLHttpRequest) {
                // remove the loading class
                dialog.removeClass('progress progress-warning progress-striped active');
                
            }
        );       
        return false;    
    }); 
    $(".resetConfirm").live('click', function(){
        //alert($(this).attr("id").toString());
        var url = $(this).attr("url").toString();
        
        var dialog = $('<div style="margin-bottom: 9px;">' +
	    				'<div><div><h3><i class="icon-tasks"></i>Resetting Offer</h3></div><br />Please Wait...<br /></div>' +
	    				'<div class="progressbar" style="width: 100%"></div></div>').appendTo('body');
        $('.progressbar').progressbar();
        var pGress = setInterval(function() {
            //$('#progressbar').css("background-image","http://jqueryui.com/resources/demos/progressbar/images/pbar-ani.gif");
            var pVal = $('.progressbar').progressbar('option', 'value');
            var pCnt = !isNaN(pVal) ? (pVal + 1) : 1;
            if (pCnt > 100) {
                clearInterval(pGress);
            } else {
                $('.progressbar').progressbar({value: pCnt});
            }
        },10);
        var posy = $(document).height() / 8;
        var posx = $(document).width() / 4;
         
        dialog.dialog({
        	autoOpen: true,
        	position: [posx,posy],
            // add a close listener to prevent adding multiple divs to the document                	
            close: function(event, ui) {
                // remove div with all data and events
                dialog.remove();
            },
            //title: "Configure",
            //height: 'auto',
            //show: { effect: 'show'},
            width: '800px',     
            resizable: false,
            draggable: true,
            modal: true,
            buttons: [{             
                text: "Close",
                class: "btn btn-danger",
                click: function() {
                    $( this ).dialog( "close" );
                }
            }]
        });
        // load remote content
        dialog.load(
            url, function (responseText, textStatus, XMLHttpRequest) {
                // remove the loading class
                dialog.removeClass('progress progress-warning progress-striped active');
                
            }
        );       
        return false;    
    }); 
    
    $(".resumeConfirm").live('click', function(){
        //alert($(this).attr("id").toString());
        var url = $(this).attr("url").toString();
        
        var dialog = $('<div style="margin-bottom: 9px;">' +
	    				'<div><div><h3><i class="icon-tasks"></i>Resuming Offer</h3></div><br />Please Wait...<br /></div>' +
	    				'<div class="progressbar" style="width: 100%"></div></div>').appendTo('body');

        $('.progressbar').progressbar();
        var pGress = setInterval(function() {
            //$('#progressbar').css("background-image","http://jqueryui.com/resources/demos/progressbar/images/pbar-ani.gif");
            var pVal = $('.progressbar').progressbar('option', 'value');
            var pCnt = !isNaN(pVal) ? (pVal + 1) : 1;
            if (pCnt > 100) {
                clearInterval(pGress);
            } else {
                $('.progressbar').progressbar({value: pCnt});
            }
        },10);
        var posy = $(document).height() / 8;
        var posx = $(document).width() / 4;
        
        dialog.dialog({
        	autoOpen: true,
        	position: [posx,posy],
            // add a close listener to prevent adding multiple divs to the document                	
            close: function(event, ui) {
                // remove div with all data and events
                dialog.remove();
            },
            //title: "Configure",
            //height: 'auto',
            //show: { effect: 'show'},
            width: '800px',     
            resizable: false,
            draggable: true,
            modal: true,
            buttons: [{             
                text: "Close",
                class: "btn btn-danger",
                click: function() {
                    $( this ).dialog( "close" );
                }
            }]
        });
        // load remote content
        dialog.load(
            url, function (responseText, textStatus, XMLHttpRequest) {
                // remove the loading class
                dialog.removeClass('progress progress-warning progress-striped active');
                
            }
        );       
        return false;    
    }); 
    
    
    
    function checkall()
    {
	    void(d=document);
	    void(el=d.getElementsByName('scOffer'));
	    for(i=0;i<el.length;i++)
	    void(el[i].checked=1)
    }
    function uncheckall()
    {
	    void(d2=document);
	    void(e2=d2.getElementsByName('scOffer'));
	    for(i=0;i<el.length;i++)
	    void(el[i].checked=0)
    }
    
    var cfg = ($.hoverintent = {
		sensitivity: 7,
		interval: 100
	});

	$.event.special.hoverintent = {
		setup: function() {
			$( this ).bind( "mouseover", jQuery.event.special.hoverintent.handler );			
		},
		teardown: function() {
			$( this ).unbind( "mouseover", jQuery.event.special.hoverintent.handler );
		},
		handler: function( event ) {
			var self = this,
				args = arguments,
				target = $( event.target ),
				cX, cY, pX, pY;
			
			function track( event ) {
				cX = event.pageX;
				cY = event.pageY;
			};
			pX = event.pageX;
			pY = event.pageY;
			function clear() {
				target
					.unbind( "mousemove", track )
					.unbind( "mouseout", arguments.callee );
				clearTimeout( timeout );
			}
			function handler() {
				if ( ( Math.abs( pX - cX ) + Math.abs( pY - cY ) ) < cfg.sensitivity ) {
					clear();
					event.type = "hoverintent";
					// prevent accessing the original event since the new event
					// is fired asynchronously and the old event is no longer
					// usable (#6028)
					event.originalEvent = {};
					jQuery.event.handle.apply( self, args );
				} else {
					pX = cX;
					pY = cY;
					timeout = setTimeout( handler, cfg.interval );
				}
			}
			var timeout = setTimeout( handler, cfg.interval );
			target.mousemove( track ).mouseout( clear );
			return true;
		}
	};
	
	
	
	/******************FORMS*********************/
	$('form[name=saveOffer]').live('submit',function(){

		$(this).ajaxSubmit(); 	  
		var url = $( "#accordionOffers" ).find('.ui-state-active').find('.offerUrl').attr('href');
		var activeTab = $( "#accordionOffers" ).find('.ui-accordion-content-active');		
		$(".dialog").dialog("close");
		activeTab.animate({opacity: 0}, 600).load(url, function (responseText, textStatus, XMLHttpRequest) {
        	//$("#dialog").dialog("close");
			$(this).animate({opacity: 1}, 600);
		});
	    return false; // prevent default action

	});
	$('form[name=configure]').live('submit',function(){

		$(this).ajaxSubmit(); 	    
		 var url = $( "#accordionOffers" ).find('.ui-state-active').find('.offerUrl').attr('href');
		var activeTab = $( "#accordionOffers" ).find('.ui-accordion-content-active');		
		$(".dialog").dialog("close");
		activeTab.animate({opacity: 0}, 600).load(url, function (responseText, textStatus, XMLHttpRequest) {
        	//$("#dialog").dialog("close");
			$(this).animate({opacity: 1}, 600);
		});
	    return false; // prevent default action

	});
	 
	$('form[name=configureOffer]').live('submit',function(){

	     // stop form from submitting normally 
		   // event.preventDefault(); 
		var code = $(this).ajaxSubmit(function (responseText, textStatus, XMLHttpRequest) {
			console.log(responseText);			
			
			var dialog = $('<div style="display:none" class="loading dialog">' + responseText + '</div>').appendTo('body');
			
			var posy = $(document).height() / 8;
		    var posx = $(document).width() / 4;
		    
            dialog.dialog({
		    	autoOpen: true,
		    	//position: [posx,posy],
		        // add a close listener to prevent adding multiple divs to the document                	
		        close: function(event, ui) {
		            // remove div with all data and events
		            dialog.remove();
		        },
		        //title: "Configure",
		        //height: 'auto',
		        //show: { effect: 'show'},
		        width: 'auto',     
		        resizable: false,
                draggable: true,
		        modal: true,
		        buttons: [{             
		            text: "Accept",
		            class: "btn btn-warning",
		            click: function() {
		                $( this ).dialog( "close" );
		                //if (!$(this).find('#response').is('.error')) {
		                	$("#dialogconfigure").dialog( "close" );
		               // }
		            }
		        }]
		    });
		});
		
		//// refresh table
		
		var url = $( "#accordionOffers" ).find('.ui-state-active').find('.offerUrl').attr('href');
		console.log(url);
		var activeTab = $( "#accordionOffers" ).find('.ui-accordion-content-active');		
		//console.dir(activeTab);
		//$(".dialog").dialog( "close" );
		activeTab.animate({opacity: 0}, 600).load(url, function (responseText, textStatus, XMLHttpRequest) {
            
//			// force to refresh images
//			$('#accordionOffers').find('img').each( function() {				
//				console.log($(this).attr("src"));					
//				var url = $(this).attr("src");
//				$(this).load(url);			
//			})			
			$(this).animate({opacity: 1}, 600);        	
        });
		
		
	    return false; // prevent default action

	});
	
	$('form[name=configureExistingOffer]').live('submit',function(){

		var code = $(this).ajaxSubmit(function (responseText, textStatus, XMLHttpRequest) {
			console.log(responseText);			
			
			var dialog = $('<div style="display:none" class="loading dialog">' + responseText + '</div>').appendTo('body');
			
			var posy = $(document).height() / 8;
		    var posx = $(document).width() / 4;
		    
		    dialog.dialog({
		    	autoOpen: true,
		    	//position: [posx,posy],
		        // add a close listener to prevent adding multiple divs to the document                	
		        close: function(event, ui) {
		            // remove div with all data and events
		            dialog.remove();
		        },
		        //title: "Configure",
		        //height: 'auto',
		        //show: { effect: 'show'},
		        width: 'auto',     
		        resizable: false,
                draggable: true,
		        modal: true,
		        buttons: [{             
		            text: "Accept",
		            class: "btn btn-warning",
		            click: function() {
		                $( this ).dialog( "close" );
		                $("#dialogConfigureExisting").dialog( "close" );
		            }
		        }]
		    });
		});  	  
		//// refresh table
		
		var url = $( "#accordionOffers" ).find('.ui-state-active').find('.offerUrl').attr('href');
		console.log(url);
		var activeTab = $( "#accordionOffers" ).find('.ui-accordion-content-active');		
		//console.dir(activeTab);
		//$(".dialog").dialog( "close" );
		activeTab.animate({opacity: 0}, 600).load(url, function (responseText, textStatus, XMLHttpRequest) {
            //$("#dialog").dialog("close");
//			$('#accordionOffers').find('img').each( function() {
//				var url = $(this).attr("src");
//				$(this).load(url);
//				//var img = $('<div class="img"><img src="'+ $(this).attr("src") +' /></div>').appendTo($(this));
//				//$(this).load($(this).attr("src"));		
//				//$(this).attr("src", "@{Helper.displayIcon(subscoffers.sc_offer.sc_offer_id)}");
//				//$(this).attr("src", $(this).attr("src"));
//				/*$(this).attr("src", $(this).attr("src"));
//				$(this).attr("ref", $(this).attr("src"));*/
//			});		
        	$(this).animate({opacity: 1}, 600);
        	
        });
	    return false; // prevent default action

	});
	
	$('form[name=configureExisting]').live('submit',function(){

		var code = $(this).ajaxSubmit(function (responseText, textStatus, XMLHttpRequest) {
			console.log(responseText);			
			
			var dialog = $('<div style="display:none" class="loading dialog">' + responseText + '</div>').appendTo('body');
			
			var posy = $(document).height() / 8;
		    var posx = $(document).width() / 4;
		    
		    dialog.dialog({
		    	autoOpen: true,
		    	//position: [posx,posy],
		        // add a close listener to prevent adding multiple divs to the document                	
		        close: function(event, ui) {
		            // remove div with all data and events
		            dialog.remove();
		        },
		        //title: "Configure",
		        //height: 'auto',
		        //show: { effect: 'show'},
		        width: 'auto',     
		        resizable: false,
                draggable: true,
		        modal: true,
		        buttons: [{             
		            text: "Accept",
		            class: "btn btn-warning",
		            click: function() {
		                $( this ).dialog( "close" );
		                $(".dialog").dialog( "close" );
		            }
		        }]
		    });
		});  
		//// refresh table
		
		var url = $( "#accordionOffers" ).find('.ui-state-active').find('.offerUrl').attr('href');
		console.log(url);
		var activeTab = $( "#accordionOffers" ).find('.ui-accordion-content-active');		
		//console.dir(activeTab);
		//$(".dialog").dialog( "close" );
		activeTab.animate({opacity: 0}, 600).load(url, function (responseText, textStatus, XMLHttpRequest) {
            //$("#dialog").dialog("close");
//			$('#accordionOffers').find('img').each( function() {	
//				console.log($(this).attr("src"));
//				var url = $(this).attr("src");
//				$(this).load(url);
//				//$(this).attr("src", $(this).attr("src"));
//				/*$(this).attr("src", $(this).attr("src"));
//				$(this).attr("ref", $(this).attr("src"));*/
//			})			
        	$(this).animate({opacity: 1}, 600);
        	
        });
	    return false; // prevent default action

	});
	
	$('form[name=enableMarket]').live('submit',function(){

		$(this).ajaxSubmit(); 	  
		
		//console.log(document.enableMarket.enterprise_id.value);
		//console.log($(this).enterprise_name);		
		var url = $(this).find("#url").attr('value');
		console.log(url);
		$(".dialog").dialog("close");
		$('#ui-tabs-2').animate({opacity: 0}, 200).load(url, function (responseText, textStatus, XMLHttpRequest) {
        	//$("#dialog").dialog("close");
			$(this).animate({opacity: 1}, 200);
		});   
		/*var entId = document.enableMarket.enterprise_id.value;
    	var entName = document.enableMarket.enterprise_name.value;
    	var disableEnableId = "#enableMarket_" + entId + "_" + entName;
    	var enablePublishId = "#publishMarket_" + entId + "_" + entName;*/
    	
    	//console.dir($(enablePublishId));
    	//$(disableEnableId).css("display","none");
    	//$(enablePublishId).css("display","block");
		//$(this).find(".enableMarketplace").css("display","none");
		//$(this).find(".publishMarketplace").css("display","block");
    	//die();
		
	    return false; // prevent default action

	});
	
	//*********************Comments***************************************/
    
    
    /*$("#enableMarketplace").live('click', function () {
        $( "#enableMarketplaceForm" ).dialog( "open" );
    	//alert("entra");
    });*/
    /*$("p").live("myCustomEvent", function(e, myName, myValue) {
    	  $(this).text("Hi there!");
    	  $("span").stop().css("opacity", 1)
    	           .text("myName = " + myName)
    	           .fadeIn(30).fadeOut(1000);
    });
    	$("button").click(function () {
    	  $("p").trigger("myCustomEvent");
    });*/
    /*function enableDialogListVDC() {   
    	//alert("hola");
    	//$( "#enableMarketplaceForm" ).css("display","block");
	    $( "#listVMForm" ).dialog({    	    	
	        autoOpen: false,
	        open: function(event, ui) {
	        },
	        //height: auto,
	        //width: auto,	       
	        modal: true,
	        resizable: false,
	        buttons: [{	        	
	            text: "Cancel",
	            class: "btn",
	            click: function() {
	                $( this ).dialog( "close" );
	            }
	        }]
	    });
    }*/
    /*$(".submitAddToServiceCatalog").live('click', function(){
    	//alert($(this).attr("id").toString());
    	var url = $(this).attr("id").toString();
    	//$( "#configureVDCForm" ).dialog( "destroy" );
    	//enableDialogConfigureVDC();
    	$('#configureVDCForm').load(url, function() {
    		//$(this).dialog('open');
    	});
    	return false;
    });*/   
  
	/*$(".configureExistingOffer").live('click', function(){
    	alert($(this).attr("id").toString());
    	var url = $(this).attr("id").toString();
    	$( "#listVMForm" ).dialog( "destroy" );
    	enableDialogListVDC();
    	$('#listVMForm').load(url, function() {
    		$(this).dialog('open');
    	});
    	return false;
    });     */
    /*$(".disableOffer").live('click', function(){
    	//alert($(this).attr("id").toString());
    	var url = $(this).attr("id").toString();
    	$( "#configureVDCForm" ).dialog( "destroy" );
    	enableDialogListVDC();
    	$('#configureVDCForm').load(url, function() {
    		$(this).dialog('open');
    	});
    	return false;
    });     */
    /*$(".listVM").live('click', function(){
    	//alert($(this).attr("id").toString());
    	var url = $(this).attr("id").toString();
    	$( "#listVMForm" ).dialog( "destroy" );
    	enableDialogListVDC();
    	$('#listVMForm').load(url, function() {
    		$(this).dialog('open');
    	});
    	return false;
    });      */
    /*$(".configureVDC").live('click', function(){
    	//alert($(this).attr("id").toString());
    	var url = $(this).attr("id").toString();
    	$( "#configureVDCForm" ).dialog( "destroy" );
    	enableDialogConfigureVDC();
    	$('#configureVDCForm').load(url, function() {
    		$(this).dialog('open');
    	});
    	return false;
    });*/
    /*$(".publishMarketplace").live('click', function(){
    	//alert($(this).attr("id").toString());
    	var url = $(this).attr("id").toString();
    	$( "#publishMarketplaceForm" ).dialog( "destroy" );
    	enableDialogPublishMarketplace();
    	$('#publishMarketplaceForm').load(url, function() {
    		$(this).dialog('open');
    	});
    	return false;
    });*/
    /*$(".enableMarketplace").live('click', function(){
    	//alert($(this).attr("id").toString());
    	var url = $(this).attr("id").toString();
    	$( "#enableMarketplaceForm" ).dialog( "destroy" );
    	enableDialogMarketplace();
    	$('#enableMarketplaceForm').load(url, function() {
    		$(this).dialog('open');
    	});
    	return false;
    });*/
    /*function enableDialogConfigureVDC() {   
    	//alert("hola");
    	//$( "#enableMarketplaceForm" ).css("display","block");
	    $( "#configureVDCForm" ).dialog({    	    	
	        autoOpen: false,
	        open: function(event, ui) {
	        },
	        //height: auto,
	        //width: auto,	       
	        modal: true,
	        resizable: false,
	        buttons: [{	        	
	            text: "Cancel",
	            class: "btn",
	            click: function() {
	                $( this ).dialog( "close" );
	            }
	        }]
	    });
    }*/
    
    /*
    function enableDialogPublishMarketplace() {   
    	//alert("hola");
    	//$( "#enableMarketplaceForm" ).css("display","block");
	    $( "#publishMarketplaceForm" ).dialog({    	    	
	        autoOpen: false,
	        open: function(event, ui) {
	        	//load(url);
	        },
	        //height: auto,
	        //width: auto,	   
	        //position: "center",
	        width: "500",
	        modal: true,
	        resizable: false,
	        buttons: [{	   
	        	text: "Save",
	            class: "btn",
	            click: function() {
	            	
	                $( this ).dialog( "close" );
	            },
	            text: "Cancel",
	            class: "btn",
	            click: function() {
	                $( this ).dialog( "close" );
	            }
	        }]
	    });
    }
    
    function enableDialogMarketplace(url) {   
    	//alert("hola");
    	//$( "#enableMarketplaceForm" ).css("display","block");
	    $( "#enableMarketplaceForm" ).dialog({    	    	
	        autoOpen: false,
	        open: function(event, ui) {
	        	
	        },
	        //height: auto,
	        //width: auto,	       
	        modal: true,
	        resizable: false,
	        buttons: [{
	        	
	            "Enable": function() {
	                var bValid = true;
	                allFields.removeClass( "ui-state-error" );
	
	                //bValid = bValid && checkLength( name, "username", 3, 16 );
	                //bValid = bValid && checkLength( email, "email", 6, 80 );
	                //bValid = bValid && checkLength( password, "password", 5, 16 );
	
	                //bValid = bValid && checkRegexp( name, /^[a-z]([0-9a-z_])+$/i, "Username may consist of a-z, 0-9, underscores, begin with a letter." );
	                // From jquery.validate.js (by joern), contributed by Scott Gonzalez: http://projects.scottsplayground.com/email_address_validation/
	                //bValid = bValid && checkRegexp( email, /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i, "eg. ui@jquery.com" );
	                //bValid = bValid && checkRegexp( password, /^([0-9a-zA-Z])+$/, "Password field only allow : a-z 0-9" );
	
	                //if ( bValid ) {
	                    $( "#users tbody" ).append( "<tr>" +
	                        "<td>" + name.val() + "</td>" + 
	                        "<td>" + email.val() + "</td>" + 
	                        "<td>" + password.val() + "</td>" +
	                    "</tr>" ); 
	                    //$( this ).dialog( "close" );
	               // }
	            },
	            text: "Cancel",
	            class: "btn",
	            click: function() {
	                $( this ).dialog( "close" );
	            }
	        }]
	    });
    }*/
	//******************************************************************************************************//
});