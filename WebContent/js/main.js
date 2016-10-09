jQuery(document).ready(function(){
	//cache DOM elements
	var sidebar = $('.cd-side-nav'),
		accountInfo = $('.account');
	
	//click on item and show submenu
	$('.has-children > a').on('click', function(event){
		var selectedItem = $(this);
		
		event.preventDefault();
		if( selectedItem.parent('li').hasClass('selected')) {
			selectedItem.parent('li').removeClass('selected');
		} else {
			$('.submenu').find('.has-children.selected').removeClass('selected');
			selectedItem.parent('li').addClass('selected');
		}
		
	});

	$(document).on('click', function(event){
		if( !$(event.target).is('.has-children a') ) {
			sidebar.find('.has-children.selected').removeClass('selected');
			accountInfo.removeClass('selected');
		}
	});
	
});