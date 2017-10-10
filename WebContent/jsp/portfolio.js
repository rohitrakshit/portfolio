$(document).ready(function(){
	var $scrollingDiv = $("#fixedPosition");
    $(window).scroll(function(){            
        $scrollingDiv.stop().animate({"marginTop": ($(window).scrollTop() )}, "fast" );         
    });
	
	$(".more_info").click(function () {
	    var $title = $(this).find(".title");
	    if (!$title.length) {
	        $(this).append('<span class="title">' + $(this).attr("title") + '</span>');
	    } else {
	        $title.remove();
	    }
	});
	$(".notes").click(function () {
		for (var i = 0; i < this.childNodes.length; i++) {
		    if (this.childNodes[i].className == "modal") {
		      window.modal = this.childNodes[i];
		      break;
		    }        
		}
		window.modal.style.display = "block";
	});
	var originalnumberOfStock = $("#totalComapanyCount").html();
	var checkbox;
	var checked = JSON.parse(localStorage.getItem('hideChecked'));
	if (checked == true) {
	    checkbox = document.getElementById("onlyNonZeroes")
	    checkbox.checked = true;
	    var numberOfStock = 0;
        $('.curr_qty_spn').each(function (k, element) {
        	numberOfStock = numberOfStock+1;
            if ($(element).html() == "0") {
            	numberOfStock = numberOfStock-1;
                $(element).parent().parent().hide();
            }
        });
        $("#totalComapanyCount").html(numberOfStock);
	    $('#searchbox').keyup();
	}
	$("#onlyNonZeroes").change(function() {
		var numberOfStock = 0;
		checkbox = this;
	    if (this.checked) {
	    	localStorage.setItem('hideChecked', true);
	        $('.curr_qty_spn').each(function (k, element) {
	        	numberOfStock = numberOfStock+1;
	            if ($(element).html() == "0") {
	            	numberOfStock = numberOfStock-1;
	                $(element).parent().parent().hide();
	            }
	        });
	        $("#totalComapanyCount").html(numberOfStock);
	    } else {
	    	localStorage.removeItem('hideChecked');
	    	$("#totalComapanyCount").html(originalnumberOfStock);
	        $('.curr_qty_spn').parent().parent().show();
	    }
	    $('#searchbox').keyup();
	});
	$('#searchbox').keyup(function() {
	    var val = '^(?=.*\\b' + $.trim($(this).val()).split(/\s+/).join('\\b)(?=.*\\b') + ').*$',
	        reg = RegExp(val, 'i'),
	        text;
	    
	    $('#maintable tbody tr').show().filter(function(k, element) {
	    	var elems = element.children;
    		if(elems[1].innerText=='0' && checkbox!=null && checkbox.checked){
    		    return true;
    		}else{	
		        text = $(this).text().replace(/\s+/g, ' ');
		        return !reg.test(text);
    		}
	    }).hide();
	});
	var table = $('#maincontent'); 
	$('#symbol_col, #lastprice_col, #qty_col, #avgprice_col, #cost_col, #value_col, #loss_col, #percent_col, #nbuy_col, #ncost_col, #nsell_col').wrapInner('<span title="Sort"/>').each(function(){          
        var th = $(this),
            thIndex = th.index(),
            inverse = false;
        th.click(function(){
            table.find('td').filter(function(){
                return $(this).index() === thIndex;           
            }).sortElements(function(a, b){
            	if(isNaN(parseFloat($.text([a])))){
            		return returnVal = $.text([a]) > $.text([b]) ? inverse ? -1 : 1 : inverse ? 1 : -1;
            	}else{
            		return parseFloat($.text([a])) > parseFloat($.text([b])) ? inverse ? -1 : 1 : inverse ? 1 : -1;
            	}
            }, function(){         
                // parentNode is the element we want to move
                return this.parentNode;         
            });          
            inverse = !inverse;         
        });           
    });
	$("quantitycell").filter(function() {         
	     return +$(this).text().trim() === 0;         
	}).parent().hide();
	$("#top").click(function(){
		$("html, body").animate({ scrollTop: 0 }, "slow");
	});
	$("#bottom").click(function(){
		$("html, body").animate({ scrollTop: $(document).height() }, 1000);
	});
	
	(function(){var special=jQuery.event.special,uid1='D'+(+new Date()),uid2='D'+(+new Date()+1);special.scrollstart={setup:function(){var timer,handler=function(evt){var _self=this,_args=arguments;if(timer){clearTimeout(timer)}else{evt.type='scrollstart';jQuery.event.handle.apply(_self,_args)}timer=setTimeout(function(){timer=null},special.scrollstop.latency)};jQuery(this).bind('scroll',handler).data(uid1,handler)},teardown:function(){jQuery(this).unbind('scroll',jQuery(this).data(uid1))}};special.scrollstop={latency:300,setup:function(){var timer,handler=function(evt){var _self=this,_args=arguments;if(timer){clearTimeout(timer)}timer=setTimeout(function(){timer=null;evt.type='scrollstop';jQuery.event.handle.apply(_self,_args)},special.scrollstop.latency)};jQuery(this).bind('scroll',handler).data(uid2,handler)},teardown:function(){jQuery(this).unbind('scroll',jQuery(this).data(uid2))}}})();
	$(function() {
	var $elem = $('body');
	$('#button_up').fadeIn('slow');
	$('#button_down').fadeIn('slow'); 
	$(window).bind('scrollstart', function(){
	$('#button_up,#button_down').stop().animate({'opacity':'0.2'});
	});
	$(window).bind('scrollstop', function(){
	$('#button_up,#button_down').stop().animate({'opacity':'1'});
	});
	$('#button_down').click(
	function (e) {
	$('html, body').animate({scrollTop: $elem.height()}, 800);
	} );
	$('#button_up').click(
	function (e) {
	$('html, body').animate({scrollTop: '0px'}, 800);
	} );});
});
window.onclick = function(event) {
	if (event.target == window.modal) {
	    window.modal.style.display = "none";
	}
}