function addMenu() {
	if (jQuery.trim(document.getElementById('newMenu').value) != '') {
		var newMenuName = jQuery.trim(document.getElementById('newMenu').value);
		
		if (jQuery('#menuListItems').find('#' + newMenuName + 'Option').size() > 0) {
			alert ('Menu exists already. Please provide a different name');
		} else {
			var menuEntry = '<li id="' + newMenuName + '">' + newMenuName + '&nbsp;&nbsp;&nbsp; ' + 
					'<img style="cursor:pointer;" src="/glo-cms/javax.faces.resource/remove.jpg.faces?ln=images" align="center" onclick="javascript:removeMenu(\'' + newMenuName + '\');" /> &nbsp;&nbsp;&nbsp; ' + 
					'<div id="' + newMenuName + 'Div" style="padding-left:25px;padding-top:0px;padding-bottom:5px;"> ' +
						'<table id="' + newMenuName + 'Table"> ' +
						'</table> ' + 
					'</div> ' + 
					'</li>';
			jQuery('#menuList').append(menuEntry);
			var menuOption = '<option id="' + newMenuName + 'Option" value="' + newMenuName + '" + > ' + newMenuName + ' </option>';
			jQuery('#menuListItems').append(menuOption);
	
			document.getElementById('newMenu').value = '';
		}
		
	}
};

function addItem() {
	if (jQuery.trim(document.getElementById('newMenuItem').value) != '') {
		var newMenuItemName = jQuery.trim(document.getElementById('newMenuItem').value);
		
		if (jQuery('#itemListItems').find('#' + newMenuItemName + 'Option').size() > 0) {
			alert ('Menu item exists already. Please provide a different name');
		} else {
			var menuItemOption = '<option id="' + newMenuItemName + 'Option" value="' + newMenuItemName + '" + > ' + newMenuItemName + ' </option>';
			jQuery('#itemListItems').append(menuItemOption);
	
			document.getElementById('newMenuItem').value = '';
		}
		
	}
};

function addMenuItem() {
	var item = jQuery.trim(document.getElementById('itemListItems').value);
	var menu = jQuery.trim(document.getElementById('menuListItems').value);
	var price = jQuery.trim(document.getElementById('price').value);
	var isPriceValid = price.search("^\[0-9]+(\.\?[0-9]+)?$");
				
	if (menu != '' && item != '' && price != '' && isPriceValid != -1) {

		if (jQuery('#' + menu + 'Table').find('#' + menu + item + 'Row').size() == 0) {
			var menuItemEntry = '<tr id="' + menu + item + 'Row"> ' +
								'<td style="padding-right:15px;">' + item + '</td>' + 
								'<td style="padding-right:15px;">' + price + '</td>' + 
								'</td>' + 
								'<td style="padding-right:15px;">' + 
								'<img style="cursor:pointer;" src="/glo-cms/javax.faces.resource/remove.jpg.faces?ln=images" align="center" onclick="javascript:removeMenuItem(\'' + menu + '\', \'' + item + '\');" /> &nbsp;&nbsp;&nbsp;' + 
								'</td>' + 
								'</tr>';
			
			jQuery('#' + menu + 'Table').append(menuItemEntry);
		} else {
			alert('Item "' + item + '" already exists in menu "' + menu + '"');
		}
		
	} else {
		var error = '';
		if (item == '') {
			error = error + 'Please select an Item\n';
		}
		if (menu == '') {
			error = error + 'Please select a Menu\n';
		}
		if (price == '' || isPriceValid == -1) {
			error = error + 'Please enter a valid Price\n';
		}
		alert(error);
	}
};

function removeMenu(menuId) {
	jQuery('#deleteConfirm').dialog( {
        autoOpen : true,
        width: 350,
        buttons: {
                "OK": function() {
					jQuery('#' + menuId).remove();
					jQuery('#' + menuId + 'Option').remove();
                	jQuery(this).dialog("close");
                },
                "Cancel": function() {
                	jQuery(this).dialog("close");
                }
        }
	}); 											
};


function removeMenuItem(menuId, menuItemId) {
												
	jQuery('#deleteItemPrompt').dialog( {
        autoOpen : true,
        width: 350,
        buttons: {
                "OK": function() {
                	if (document.getElementById('deleteItemLocal').checked) {
						jQuery('#deleteConfirm').dialog( {
					        autoOpen : true,
					        width: 350,
					        buttons: {
					                "OK": function() {
				                		removeItemLocal(menuId, menuItemId);
					                	jQuery(this).dialog("close");
					                },
					                "Cancel": function() {
					                	jQuery(this).dialog("close");
					                }
					        }
						}); 											
                	} else {
						jQuery('#deleteConfirm').dialog( {
					        autoOpen : true,
					        width: 350,
					        buttons: {
					                "OK": function() {
				                		removeItemGlobal(menuId, menuItemId);                		
					                	jQuery(this).dialog("close");
					                },
					                "Cancel": function() {
					                	jQuery(this).dialog("close");
					                }
					        }
						}); 											
                	}
                	jQuery(this).dialog("close");
                },
                "Cancel": function() {
                	jQuery(this).dialog("close");
                }
        }
	}); 											
};

function removeItemLocal(menuId, menuItemId) {
	jQuery('#' + menuId + menuItemId + 'Row').remove();
};

function removeItemGlobal(menuId, menuItemId) {
	jQuery('*[id$=' + menuItemId + 'Row]').remove();
	jQuery('#' + menuItemId + 'Option').remove();
	
};
