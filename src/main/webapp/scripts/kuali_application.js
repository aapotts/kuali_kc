

function selectAllGGForms(document) {
    var j = 0;
	for (var i = 0; i < document.KualiForm.elements.length; i++) {
	  var e = document.KualiForm.elements[i];
	  if(e.type == 'checkbox') {
	  	if (e.name == 'document.s2sOpportunity.s2sOppForms[' + j + '].selectToPrint') {
 		    if(e.disabled == false){
 		    	e.checked = true;
 		    }
	  		j++; 
	  	}
	  }
	}
}

function selectAllSponsorForms(document) {
    var j = 0;
	for (var i = 0; i < document.KualiForm.elements.length; i++) {
	  var e = document.KualiForm.elements[i];
	  if(e.type == 'checkbox') {
	  	if (e.name == 'sponsorFormTemplates[' + j + '].selectToPrint') {
 		    if(e.disabled == false){
 		    	e.checked = true;
 		    }
	  		j++; 
	  	}
	  }
	}
}

function unselectAllSponsorForms(document) {
    var j = 0;
	for (var i = 0; i < document.KualiForm.elements.length; i++) {
	  var e = document.KualiForm.elements[i];
	  if(e.type == 'checkbox') {
	  	if (e.name == 'sponsorFormTemplates[' + j + '].selectToPrint') {
 		    if(e.disabled == false){
 		    	e.checked = false;
 		    }
	  		j++; 
	  	}
	  }
	}
}

function selectAllIncludedGGForms(document) {
    var j = 0;
	for (var i = 0; i < document.KualiForm.elements.length; i++) {
	  var e = document.KualiForm.elements[i];
	  if (e.name == 'document.s2sOpportunity.s2sOppForms[' + j + '].mandatory') {
	  		var e1 = e;
	  }
	  	if (e.name == 'document.s2sOpportunity.s2sOppForms[' + j + '].include') {
	  		var e2 = e;
	  }
	  if(e.type == 'checkbox') {	  	
	  	if (e.name == 'document.s2sOpportunity.s2sOppForms[' + j + '].selectToPrint') {
 		    if(e.disabled == false){
 		    	if(e1.value == 'Yes'){
 		    		e.checked = true;
 		    	}
 		    	if(e2.checked == true){
 		    		e.checked = true;
 		    	}
 		    }
	  		j++; 
	  	}
	  }
	}
}

function unselectAllGGForms(document) {
    var j = 0;
	for (var i = 0; i < document.KualiForm.elements.length; i++) {
	  var e = document.KualiForm.elements[i];
	  if(e.type == 'checkbox') {
	  	if (e.name == 'document.s2sOpportunity.s2sOppForms[' + j + '].selectToPrint') {
 		    if(e.disabled == false){
 		    	e.checked = false;
 		    }
	  		j++; 
	  	}
	  }
	}
}
function selectAllKeywords(document) {
    var j = 0;
	for (var i = 0; i < document.KualiForm.elements.length; i++) {
	  var e = document.KualiForm.elements[i];
	  if(e.type == 'checkbox') {
	  	var name = 'document.propScienceKeyword[' + j + '].selectKeyword';
	  	if (e.name == name) {
 		    e.checked = true;
	  		j++; 
	  	}
	  }
	}
}

function kraTextAreaPop(textAreaName,htmlFormAction,textAreaLabel,docFormKey, sessionDocument){
  var documentWebScope
  if (sessionDocument == "true") {
      documentWebScope="session"
  }
  url=window.location.href
  pathname=window.location.pathname
  idx1=url.indexOf(pathname);
  idx2=url.indexOf("/",idx1+1);
  extractUrl=url.substr(0,idx2)
  //text=text.replace(/\n/g,'<br>');
  //window.open(extractUrl+"/updateTextArea.do?" + text+"&textAreaFieldName="+textAreaName+"&htmlFormAction="+htmlFormAction+"&textAreaFieldLabel="+textAreaLabel+"&docFormKey="+docFormKey+"&documentWebScope="+documentWebScope, "_blank", "width=640, height=600, scrollbars=yes");
  window.open(extractUrl+"/kraUpdateTextArea.do?&textAreaFieldName="+textAreaName+"&htmlFormAction="+htmlFormAction+"&textAreaFieldLabel="+textAreaLabel+"&docFormKey="+docFormKey+"&documentWebScope="+documentWebScope, "_blank", "width=640, height=600, scrollbars=yes");
}

var kraTextAreaFieldName
function kraSetTextArea() {
  passData=document.location.search.substring(1);
  var idx=passData.indexOf("&textAreaFieldName=")
  var idx2=passData.indexOf("&htmlFormAction=")
  kraTextAreaFieldName=passData.substring(idx+19,idx2)
  text = window.opener.document.getElementById(kraTextAreaFieldName).value;
  //text=passData.substr(0,idx)
  //text=unescape(text).replace(/<br>/g,"\n")
  document.getElementById(kraTextAreaFieldName).value = text;
  
//  alert (escape(text))
//  alert (unescape(text))

}

function kraPostValueToParentWindow() {
  opener.document.getElementById(kraTextAreaFieldName).value = document.getElementById(kraTextAreaFieldName).value;
  self.close();
}

// dwr functions
// this is a sample function for sponsor code
function loadSponsorCode( sponsorCodeFieldName) {
	var sponsorCode = DWRUtil.getValue( sponsorCodeFieldName );

	//if (sponsorCode == "") {
	//	clearRecipients( sponsorCodeFieldName, "" );
	//} else {
		var dwrReply = {
			callback:function(data) {
			if ( data != null ) {
				if ( sponsorCodeFieldName != null && sponsorCodeFieldName != "" ) {
					setRecipientValue( sponsorCodeFieldName, data );
				}
			} else {
				if ( sponsorCodeFieldName != null && sponsorCodeFieldName != "" ) {
					setRecipientValue( sponsorCodeFieldName, "" );
				}
			} },
			errorHandler:function( errorMessage ) {
				window.status = errorMessage;
				if ( sponsorCodeFieldName != null && sponsorCodeFieldName != "" ) {
					setRecipientValue( sponsorCodeFieldName, "" );
				}
			}
		};

		SponsorService.getSponsorCode(sponsorCode,dwrReply);

	//}
}

/*
 * Load the Sponsor Name field based on the Sponsor Code passed in.
 */
function loadSponsorName(sponsorCodeFieldName, sponsorNameFieldName ) {
	var sponsorCode = DWRUtil.getValue( sponsorCodeFieldName );

	if (sponsorCode=='') {
		clearRecipients( sponsorNameFieldName, "" );
	} else {
		var dwrReply = {
			callback:function(data) {
				if ( data != null ) {
					if ( sponsorNameFieldName != null && sponsorNameFieldName != "" ) {
						setRecipientValue( sponsorNameFieldName, data );
					}
				} else {
					if ( sponsorNameFieldName != null && sponsorNameFieldName != "" ) {
						setRecipientValue(  sponsorNameFieldName, wrapError( "not found" ), true );
					}
				}
			},
			errorHandler:function( errorMessage ) {
				window.status = errorMessage;
				setRecipientValue( sponsorNameFieldName, wrapError( "not found" ), true );
			}
		};
		SponsorService.getSponsorName(sponsorCode,dwrReply);
	}
}

/*
 * Load the Budget Category Code based on Object Code(Cost Element)
 */ 
function loadBudgetCategoryCode(objectCode,budgetCategoryCode){
	var objectCodeValue = DWRUtil.getValue( objectCode );

	if (objectCodeValue=='') {
		clearRecipients( budgetCategoryCode, "" );
	} else {
		var dwrReply = {
			callback:function(data) {
				if ( data != null ) {
					if ( objectCode != null && objectCode != "" ) {
						setRecipientValue( budgetCategoryCode, data );
					}
				} else {
					if ( objectCode != null && objectCode != "" ) {
						setRecipientValue(  budgetCategoryCode, wrapError( "not found" ), true );
					}
				}
			},
			errorHandler:function( errorMessage ) {
				window.status = errorMessage;
				setRecipientValue( budgetCategoryCode, wrapError( "not found" ), true );
			}
		};
		ObjectCodeToBudgetCategoryCodeService.getBudgetCategoryCodeForCostElment(objectCodeValue,dwrReply);
	}
}

/*
 * Load Start and End Dates based on the Fiscal Year
 */ 
function loadStartAndEndDates(fiscalYear,startDate,endDate){
	var fiscalYearValue = DWRUtil.getValue( fiscalYear );

	if (fiscalYearValue=='') {
		clearRecipients( startDate, "" );
		clearRecipients( endDate, "" );
	} else {
		var dwrReply = {
			callback:function(data) {
				if ( data != null ) {
					if ( fiscalYear != null && fiscalYear != "" ) {						
						var option_array=data.split(",");						
						setRecipientValue( startDate, option_array[0] );
						setRecipientValue( endDate, option_array[1] );
					}
				} else {
					if ( fiscalYear != null && fiscalYear != "" ) {
						setRecipientValue(  startDate, wrapError( "not found" ), true );
						setRecipientValue(  endDate, wrapError( "not found" ), true );
					}
				}
			},
			errorHandler:function( errorMessage ) {
				window.status = errorMessage;
				setRecipientValue( startDate, wrapError( "not found" ), true );
				setRecipientValue( endDate, wrapError( "not found" ), true );
			}
		};
		AwardFandaRateService.getStartAndEndDatesBasedOnFiscalYear(fiscalYearValue,dwrReply);
	}
}
 
/*
 * Load the Unit Name field based on the Unit Number passed in.
 */
function loadUnitName(unitNumberFieldName) {
	var unitNumber = DWRUtil.getValue( unitNumberFieldName );
    var elPrefix = findElPrefix( unitNumberFieldName );
	var unitNameFieldName = elPrefix + ".unitName";
	if (unitNumber=='') {
		clearRecipients( unitNameFieldName, "(select)" );
	} else {
		var dwrReply = {
			callback:function(data) {
				if ( data != null ) {
					if ( unitNameFieldName != null && unitNameFieldName != "" ) {
						setRecipientValue( unitNameFieldName, data );
					}
				} else {
					if ( unitNameFieldName != null && unitNameFieldName != "" ) {
						setRecipientValue(  unitNameFieldName, wrapError( "not found" ), true );
					}
				}
			},
			errorHandler:function( errorMessage ) {
				window.status = errorMessage;
				setRecipientValue( unitNameFieldName, wrapError( "not found" ), true );
			}
		};
		UnitService.getUnitName(unitNumber,dwrReply);
	}
}

/*
 * Load the JobCode Title field based on the Job Code passed in.
 */
function loadJobCodeTitle(jobCodeFieldName, jobCodeTitleFieldName ) {
	var jobCode = DWRUtil.getValue( jobCodeFieldName );

	if (jobCode=='') {
		clearRecipients( jobCodeTitleFieldName, "" );
	} else {
		var dwrReply = {
			callback:function(data) {
				if ( data != null ) {
					if ( jobCodeTitleFieldName != null && jobCodeTitleFieldName != "" ) {
						setRecipientValue( jobCodeTitleFieldName, data );
					}
				} else {
					if ( jobCodeTitleFieldName != null && jobCodeTitleFieldName != "" ) {
						setRecipientValue(  jobCodeTitleFieldName, wrapError( "not found" ), true );
					}
				}
			},
			errorHandler:function( errorMessage ) {
				window.status = errorMessage;
				setRecipientValue( jobCodeTitleFieldName, wrapError( "not found" ), true );
			}
		};
		JobCodeService.findJobCodeTitle(jobCode,dwrReply);
	}
}

function loadSponsorCode_1( sponsorCodeFieldName) {
    // alternative, delete later
	var sponsorCode = DWRUtil.getValue( sponsorCodeFieldName );
	//alert(sponsorCodeFieldName+" "+sponsorCode)
	//SponsorService.getSponsorCode(sponsorCode,function(data) {
    //DWRUtil.setValue(sponsorCodeFieldName, data);});
	SponsorService.getSponsorCode(sponsorCode,loadinfo);

}

function loadinfo(data) {
  //alert("loadinfo "+data)
  DWRUtil.setValue("document.sponsorCode", data);
}
var propAttRightWindow;
function proposalAttachmentRightsPop(lineNumber,docFormKey, sessionDocument){
  var documentWebScope
  if (sessionDocument == "true") {
      documentWebScope="session"
  }

  if (propAttRightWindow && propAttRightWindow.open && !propAttRightWindow.closed){
  	propAttRightWindow.focus();
  }else{
    propAttRightWindow = window.open(extractUrlBase()+"/proposalDevelopmentAbstractsAttachments.do?methodToCall=getProposalAttachmentRights&line="+lineNumber+"&docFormKey="+docFormKey+"&documentWebScope="+documentWebScope, "mywindow", "width=800, height=300, scrollbars=yes");
  }
}  

var propInstAttRightWindow;
function proposalInstituteAttachmentRightsPop(lineNumber,docFormKey, sessionDocument){
  var documentWebScope
  if (sessionDocument == "true") {
      documentWebScope="session"
  }
  if (propInstAttRightWindow && propInstAttRightWindow.open && !propInstAttRightWindow.closed){
  	propInstAttRightWindow.focus();
  }else{
    propInstAttRightWindow = window.open(extractUrlBase()+"/proposalDevelopmentAbstractsAttachments.do?methodToCall=getInstituteAttachmentRights&line="+lineNumber+"&docFormKey="+docFormKey+"&documentWebScope="+documentWebScope, "instAttWindow", "width=800, height=300, scrollbars=yes");
    if (window.focus) {
         propInstAttRightWindow.focus()
    }
  }
}
var fileBrowseWindow;
function openNewFileBrowseWindow(filePropertyName,fileFieldLabel,htmlFormAction,methodToCall,methodToSave,lineNumber){
  if (fileBrowseWindow && fileBrowseWindow.open && !fileBrowseWindow.closed){
  	fileBrowseWindow.focus();
  }else{
    fileBrowseWindow = window.open(extractUrlBase()+"/proposalDevelopmentAbstractsAttachments.do?methodToCall="+methodToCall+"&methodToSave="+methodToSave+"&line="+lineNumber+"&filePropertyName="+filePropertyName+"&fileFieldLabel="+fileFieldLabel, "mywindow", "width=800, height=300, scrollbars=yes");
  }
}
function extractUrlBase(){
  url=window.location.href;
  pathname=window.location.pathname;
  idx1=url.indexOf(pathname);
  idx2=url.indexOf("/",idx1+1);
  extractUrl=url.substr(0,idx2);
  return extractUrl; 
}
function openNewWindow(action,methodToCall,lineNumber,docFormKey, sessionDocument){
  var documentWebScope
  if (sessionDocument == "true") {
      documentWebScope="session"
  }
//function openNewWindow(action,methodToCall,lineNumber){
//  window.open(extractUrlBase()+"/"+action+".do?methodToCall="+methodToCall+"&line="+lineNumber);
  window.open(extractUrlBase()+"/"+action+".do?methodToCall="+methodToCall+"&line="+lineNumber+"&docFormKey="+docFormKey+"&documentWebScope="+documentWebScope);
}


function showHide(showId,hideId){
  var style_sheet = getStyleObject(showId);
  if (style_sheet)
  {
	changeObjectVisibility(showId, "block");
	changeObjectVisibility(hideId, "none");
  }
  else 
  {
    alert("sorry, this only works in browsers that do Dynamic HTML");
  }
}
function getStyleObject(objectId) {
  // checkW3C DOM, then MSIE 4, then NN 4.
  //
  if(document.getElementById && document.getElementById(objectId)) {
	return document.getElementById(objectId).style;
   }
   else if (document.all && document.all(objectId)) {  
	return document.all(objectId).style;
   } 
   else if (document.layers && document.layers[objectId]) { 
	return document.layers[objectId];
   } else {
	return false;
   }
}


function changeObjectVisibility(objectId, newVisibility) {
    // first get the object's stylesheet
    var styleObject = getStyleObject(objectId);

    // then if we find a stylesheet, set its visibility
    // as requested
    //
    if (styleObject) {
		styleObject.display = newVisibility;
	return true;
    } else {
	return false;
    }
}

/**
 * Display the Proposal's set of Roles and their Rights.
 * The roles are Aggregator, Budget Creator, etc.
 */
var propRoleRightsWindow = null;

function proposalRoleRightsPop(docFormKey, sessionDocument) {

	var documentWebScope = "";
	if (sessionDocument == true) {
		documentWebScope = "session";
	}

	if (propRoleRightsWindow != null) {
	    propRoleRightsWindow.close();
	} 

    propRoleRightsWindow = window.open(extractUrlBase() +
    	                               "/proposalDevelopmentPermissions.do?methodToCall=getPermissionsRoleRights" +
    	                               "&docFormKey=" + docFormKey + 
    	                               "&documentWebScope=" + documentWebScope, 
    	                               "permissionsRoleRights", 
    	                               "width=800, height=750, scrollbars=yes, resizable=yes");
    
}

/**
 * Display the Edit Roles popup window.  This window allows users
 * to change the roles for a user within a proposal.
 */
var propEditRolesWindow;

function editRolesPop(lineNumber, docFormKey, sessionDocument) {

    var documentWebScope = "";
    if (sessionDocument == "true") {
        documentWebScope="session"
    }
    
	if (propEditRolesWindow != null) {
	    propEditRolesWindow.close();
	} 

    propEditRolesWindow = window.open(extractUrlBase() +
    	                               "/proposalDevelopmentPermissions.do?methodToCall=editRoles" +
    	                               "&line=" + lineNumber +
    	                               "&docFormKey=" + docFormKey + 
    	                               "&documentWebScope=" + documentWebScope, 
    	                               "permissionsEditRoles", 
    	                               "width=800, height=350, scrollbars=yes, resizable=yes");
}

/**
 * Utility function for trimming a string.
 */
String.prototype.trim = function() {
  return this.replace(/^\s+|\s+$/g, "");
}

/**
 * The User Class.  A user has a name, its line number
 * within the user table, and a set of roles.
 */
function User(name, lineNumber) {
    this._name = name;
    this._lineNumber = lineNumber;
    this._roles = new Array();
}

User.prototype._name;
User.prototype._lineNumber;
User.prototype._roles;

User.prototype.getName = function() {
    return this._name;
}

User.prototype.getLineNumber = function() {
    return this._lineNumber;
}

User.prototype.getRoles = function() {
	return this._roles;
}

User.prototype.addRole = function(role) {
    this._roles[this._roles.length] = role;
}

User.prototype.clearRoles = function() {
    this._roles.length = 0;
}

User.prototype.hasRole = function(role) {
    for (var i = 0; i < this._roles.length; i++) {
        if (role == this._roles[i]) {
            return true;
        }
    }
    return false;
}

/**
 * The PropRoleState Class.  Stores the states of the roles as 
 * selected by the Edit Roles web page.  We store the name of the
 * role and it's state (true or false).  A value of true indicates
 * that the role was selected by the user; otherwise false is
 * unselected.
 */
function PropRoleState(name, state) {
    this._name = name;
    this._state = state;
}

PropRoleState.prototype._name;
PropRoleState.prototype._state;

PropRoleState.prototype.getName = function() {
    return this._name;
}

PropRoleState.prototype.getState = function() {
    return this._state;
}


/**
 * When the Edit Roles popup window is closed, this function is invoked in
 * order to update the parent window and to close the popup window.  We need
 * to change the roles for the user that was modified.  We also need to 
 * update the listing of assigned roles.
 */
function updateEditRoles(lineNumber, roleStates) {

	var users = getUsers();
	updateUserRoles(users[lineNumber], roleStates);
    displayUserRoles(users[lineNumber]);
    
    for (var i = 0; i < roleStates.length; i++) {
        displayAssignedRoles(users, roleStates[i].getName(), roleStates[i].getName());
    }
    
    self.close();
}

/**
 * Display the roles for a user.  This visibly changes the roles
 * for a user in the User Permissions panel.
 */
function displayUserRoles(user) {
	var html = "";
    var roles = user.getRoles();
    for (var i = 0; i < roles.length; i++) {
        if (i != 0) html += "<BR>";
        html += "<NOBR>" + roles[i] + "</NOBR>";
    }
     
    var roleElement = opener.document.getElementById("role" + user.getLineNumber());
    roleElement.innerHTML = html;
}

/**
 * Displays the names of users for a specific role.
 */
function displayAssignedRoles(users, elementId, role) {
    if (role != "unassigned") {
    var usernames = new Array();
	for (var i = 0; i < users.length; i++) {
	    if (users[i].hasRole(role)) {
	        usernames[usernames.length] = users[i].getName();
	    }
	}
	var node = opener.document.getElementById(elementId);
	node.innerHTML = usernames.join("; ");
}
}

/**
 * Changes the roles for a user.  The current set of roles is cleared and
 * a new set of roles is added.
 */
function updateUserRoles(user, roleStates) {
    user.clearRoles();
    for (var i = 0; i < roleStates.length; i++) {
        var state = roleStates[i].getState();
        if (state.toLowerCase() == 'true') {
            user.addRole(roleStates[i].getName());
    }
    }
 }
    
/**
 * Get the users in the Permission's Users panel.  Extract the information
 * from the HTML table.  We will store each user's name, line number in
 * the table, and the user's set of roles.
 */
function getUsers() {
    var users = new Array();
    var tableElement = opener.document.getElementById("user-roles");
    var numRows = tableElement.tBodies[0].rows.length;
    for (var i = 2; i < numRows; i++) {
    	var rowElement = tableElement.tBodies[0].rows[i];
    	var nameCell = rowElement.cells[2];
    	var name = nameCell.childNodes[0].innerHTML;
    	var user = new User(name, i - 2);
    	
    	var roleCell = rowElement.cells[5];
    	var numRoles = roleCell.childNodes.length;
    	for (var j = 0; j < numRoles; j++) {
    		var node = roleCell.childNodes[j];
    		if (node.nodeName.toUpperCase() == "NOBR") {
    		    var roleName = node.innerHTML.trim();
    		    user.addRole(roleName);
    		}
    	}
    	users[users.length] = user;
    }
    return users;
}

/*
 * Load the person's full name based on the person's username.
 */
function loadPersonName(usernameFieldName, fullnameElementId) {
    if (document.getElementById(fullnameElementId) != null) {
		var username = DWRUtil.getValue( usernameFieldName );
		var fullNameElement = document.getElementById(fullnameElementId);
		if (username == '') {
			fullNameElement.innerHTML = "&nbsp;";
		} else {
			var dwrReply = {
				callback:function(data) {
					if ( data != null ) {
					    fullNameElement.innerHTML = data;
					} else {
						fullNameElement.innerHTML = wrapError( "not found" );
					}
				},
				errorHandler:function( errorMessage ) {
					window.status = errorMessage;
					fullNameElement.innerHTML = wrapError( "not found" );
				}
			};
			PersonService.getPersonFullname(username, dwrReply);
		}
	}
}


/*
 * functions for custom attribute maintenance 
 */	
  
  var lookupReturnName ;
 function updateLookupReturn( lookupClassField, callbackFunction ) {
    //alert ("enter update"+lookupClassField.name);
	
	if (lookupClassField.name == "lookupClass" ) {
	    lookupReturnName = "lookupReturn" ;
	} else {
	    lookupReturnName =  findElPrefix( lookupClassField.name ) + ".lookupReturn" ;
	}
    //alert ("in update" +lookupClassField+"-"+lookupClassField.name+"-"+lookupReturn+lookupClassField.value);
	//var lookupClass = getElementValue( lookupClassField.name ); // ie7 get nothing out of this
    var lookupClass = lookupClassField.value;
    //alert ('update ' +lookupClass);
	if ( lookupClass != "") {
		var dwrReply = {
			callback:callbackFunction,
			errorHandler:function( errorMessage ) { 
				window.status = errorMessage;
			}
		};
		CustomAttributeService.getLookupReturnsForAjaxCall( lookupClass, dwrReply );
	} else {
	    kualiElements[lookupReturnName].options.length=1;
	}
}

function updateLookupReturn_Callback( data ) {
            //alert ("enter callback" +lookupReturnName);
			
			kualiElements[lookupReturnName].options.length=0; //reset 
			var option_array=data.split(",");
			var optionNum=0;
			var nameLabelPair;
			while (optionNum < option_array.length)
			 {
				  if (optionNum == 0) {
				        //alert(optionNum+option_array[0])
				        kualiElements[lookupReturnName].options[0]=new Option("select:","", true, true);
				  } else {
				        //alert("else"+optionNum+option_array[optionNum])
				        nameLabelPair = option_array[optionNum].split(";");
				        kualiElements[lookupReturnName].options[optionNum]=new Option(nameLabelPair[1], nameLabelPair[0]);
				  }
				  optionNum+=1;
			 }

}
 
var oldDisplayValue;
var displayValue;
var dataType;
var hasLookup;
var lookupArgument;
var lookupPkReturn;
var fieldPrefix;
var changedValue;
var comments;
var lookupReturn;

function updateOtherFields(editableColumnNameField, callbackFunction ) {
	var proposalNumber = DWRUtil.getValue( 'document.proposalNumber' );

	fieldPrefix = findElPrefix( editableColumnNameField.name );
	oldDisplayValue =  fieldPrefix + ".oldDisplayValue" ;
	displayValue =  fieldPrefix + ".displayValue" ;
	dataType =  fieldPrefix + ".editableColumn.dataType" ;
	hasLookup =  fieldPrefix + ".editableColumn.hasLookup" ;
	lookupArgument =  fieldPrefix + ".editableColumn.lookupClass" ;
	lookupReturn = fieldPrefix + ".editableColumn.lookupReturn" ;
	lookupPkReturn = fieldPrefix + ".editableColumn.lookupPkReturn" ;
	changedValue = fieldPrefix + ".changedValue" ;
	comments = fieldPrefix + ".comments" ;

	var editableColumnNameRef = fieldPrefix + ".editableColumn.columnName" ;
	document.getElementById(editableColumnNameRef).value = editableColumnNameField.value; 
	
	var editableColumnName = editableColumnNameField.value;
	if (editableColumnName != "") {
		var dwrReply = {
			callback:callbackFunction,
			errorHandler:function( errorMessage ) { 
				window.status = errorMessage;
			}
		};
		ProposalDevelopmentService.populateProposalEditableFieldMetaDataForAjaxCall(proposalNumber, editableColumnName, dwrReply );
	} else {
			document.getElementById(oldDisplayValue).value = ""; 
			document.getElementById(displayValue).value = ""; 
			document.getElementById(dataType).value = "";
			document.getElementById(hasLookup).value = "";
			document.getElementById(lookupArgument).value = "";
			document.getElementById(lookupReturn).value = "";
			document.getElementById(lookupPkReturn).value = "";
			document.getElementById(changedValue).value = "";
			document.getElementById(changedValue).style.borderColor = "";
			document.getElementById(comments).value = "";
	}
}

function updateOtherFields_Callback( data ) {
	var value_array = data.split(",");
	var counter=0;
	
	//reset
	document.getElementById(oldDisplayValue).value = ""; 
	document.getElementById(displayValue).value = ""; 
	document.getElementById(dataType).value = "";
	document.getElementById(hasLookup).value = "";
	document.getElementById(lookupArgument).value = "";
	document.getElementById(lookupReturn).value = "";
	document.getElementById(lookupPkReturn).value = "";
	document.getElementById(changedValue).value = "";
	document.getElementById(changedValue).style.borderColor = "";
	document.getElementById(comments).value = "";
	
	while (counter < value_array.length)
	{
		if(counter == 0) {
			document.getElementById(lookupPkReturn).value = value_array[counter];
		}

		if(counter == 1) {
			document.getElementById(lookupReturn).value = value_array[counter];
		}
		
		if(counter == 2) {
			document.getElementById(oldDisplayValue).value = value_array[counter];
			document.getElementById(displayValue).value = value_array[counter];
		}
		
		if(counter == 3) {
			document.getElementById(dataType).value = value_array[counter];
			
		}
		
		if(counter == 4) {
			document.getElementById(hasLookup).value = value_array[counter];
		}
		
		if(counter == 5) {
			document.getElementById(lookupArgument).value = value_array[counter];
		}
		
		counter+=1;
	}
	
	var displayValueField = document.getElementById(displayValue).name;
	var prefix = findElPrefix( document.getElementById(displayValue).name );
	var imageUrl = document.getElementById("imageUrl").value;
	var tabIndex = document.getElementById("tabIndex").value;
	var lookupClass = document.getElementById(lookupArgument).value;
	var lookupPkReturnValue = document.getElementById(lookupPkReturn).value;
	var changedValueFieldName = document.getElementById(changedValue).name;
	var myDiv = document.getElementById('changedValueExtraBody');
	var dataTypeValue = document.getElementById(dataType).value;
	var lookupReturnValue = document.getElementById(lookupReturn).value;
	dynamicDivUpdate(lookupClass, lookupPkReturnValue, lookupReturnValue, changedValueFieldName, displayValueField, dataTypeValue);
}

function dynamicDivUpdate(lookupClass, lookupPkReturnValue, lookupReturnValue, changedValueFieldName, displayValueField, dataTypeValue) {
   	var imageUrl = document.getElementById("imageUrl").value;
	var tabIndex = document.getElementById("tabIndex").value;
    var myDiv = document.getElementById('changedValueExtraBody');
	var innerDivContent = "";
	
	if(lookupClass != "" && lookupPkReturnValue != "" && changedValueFieldName != "") {
		innerDivContent = "<input type='image' tabindex='' ";
		innerDivContent = innerDivContent + " name='methodToCall.performLookup.(!!" + lookupClass + "!!).(((" + lookupPkReturnValue + ":" + changedValueFieldName + "," + lookupReturnValue + ":" + displayValueField + "))).((##)).((<>)).(([])).((**)).((^^)).((&&)).((//)).((~~)).anchorProposalDataOverride' ";
		innerDivContent = innerDivContent + " src='" + imageUrl + "searchicon.gif' border='0' class='tinybutton' valign='middle' alt='Search' title='Search' /> ";
	} 
	
	if(dataTypeValue != "" && (dataTypeValue == 'DATE' || dataTypeValue == 'date')) {
		innerDivContent = innerDivContent + "<img src=\"" + imageUrl + "cal.gif\" id=\"newProposalChangedData.changedValue_datepicker\" style=\"cursor: pointer;\"";
		innerDivContent = innerDivContent + " title=\"Date selector\" alt=\"Date selector\" onmouseover=\"this.style.backgroundColor='red';\" onmouseout=\"this.style.backgroundColor='transparent';\" />";
	}
	
	myDiv.innerHTML = innerDivContent;
	
	if(dataTypeValue != "" && (dataTypeValue == 'DATE' || dataTypeValue == 'date')) {
		Calendar.setup(
			{
			  inputField : "newProposalChangedData.changedValue", // ID of the input field
			  ifFormat : "%m/%d/%Y", // the date format
			  button : "newProposalChangedData.changedValue_datepicker" // ID of the button
		    }
		);
	}
}

function enableBudgetStatus(document, index) {
	var newFinalIndicator;
	var newFinalStatus;
	var newFinalStatusHidden;
	var j = 0;
	var cancelled = false;
	for (var i = 0; i < document.KualiForm.elements.length; i++) {
	  var e = document.KualiForm.elements[i];
	  if(e.type == 'checkbox') {
	  	var status = document.KualiForm.elements[i - 1];
	  	var statusHidden = document.KualiForm.elements[i - 2];
	  	if (e.checked && j != index) {
	  		if (confirm("You are changing the final version.  Are you sure?")) {
	  			e.checked = false;
	  			statusHidden.value = status.value;
	  			statusHidden.disabled = false;
	  			status.disabled = true;
	  		} else {
	  			cancelled = true;	
	  		}
	  	} else if (e.checked && j == index) {
	  		newFinalIndicator = e;
	  		newFinalStatus = status;
	  		newFinalStatusHidden = statusHidden;
	  	} else {
	  		statusHidden.value = status.value;
	  		statusHidden.disabled = false;
	  		status.disabled = true;
	  	}
	  	j++;
	  }
	}
	if (!cancelled && newFinalStatus != null) {
		newFinalStatus.disabled = false;
		newFinalStatusHidden.disabled = true;
	}
	if (cancelled && newFinalIndicator != null) {
		newFinalIndicator.checked = false;
	}
}

function getIndex(what) {
    for (var i = 0; i < document.KualiForm.elements.length; i++) {
        if (what == document.KualiForm.elements[i]) {
            return i;
          }
       }
       return -1;
       alert("leaveIndex");
}


function setupBudgetStatuses(document) {
	for (var i = 0; i < document.KualiForm.elements.length; i++) {
	  var e = document.KualiForm.elements[i];
	  if(e.type == 'checkbox') {
	  	var status = document.KualiForm.elements[i - 1];
	  	var statusHidden = document.KualiForm.elements[i - 2];
	  	if (e.checked) {
	  		statusHidden.disabled = true;
	  		status.disabled = false;
	  	} else {
	  		statusHidden.disabled = false;
	  		status.disabled = true;
	  	}
	  }
	}
}



function setupBudgetStatusSummary(document) {
	  var finalVersionFlag = document.getElementById('document.finalVersionFlag');
	  var temp = document.getElementById('hack');
	  var hackIndex = getIndex(temp);
	  var finalVersionFlagIndex = getIndex(finalVersionFlag);
	  var statusHidden = document.KualiForm.elements[hackIndex + 1];
	  var status = document.KualiForm.elements[hackIndex + 2];
	  var finalVersionFlagHidden = document.KualiForm.elements[finalVersionFlagIndex + 1];
	  if(finalVersionFlag.checked) {
	  	statusHidden.disabled=true;
	  	status.disabled=false;
	  } else {
	  	statusHidden.disabled=false;
	  	status.disabled=true;
	  }
	 }

function toggleFinalCheckboxSummary(document) {
	var completed = false;
	var finalVersionFlag = document.getElementById('document.finalVersionFlag');
	var finalVersionFlagIndex = getIndex(finalVersionFlag);
	var finalVersionFlagHidden = document.KualiForm.elements[finalVersionFlagIndex + 2];
	var temp = document.getElementById('hack');
	var hackIndex = getIndex(temp);
	var statusHidden = document.KualiForm.elements[hackIndex + 1];
	var status = document.KualiForm.elements[hackIndex + 2];
	if(status.value == '1'){
		completed = true;
		}
	if(completed) {
		finalVersionFlag.disabled = true;
		finalVersionFlagHidden.disabled = false;
		finalVersionFlagHidden.value = true;
	}else {
		finalVersionFlag.disabled = false;
		finalVersionFlagHidden.disabled = true;
		finalVersionFlagHidden.value = false;
	}
}
					

function toggleFinalCheckboxes(document) {
	var completed = false;
	var toggledElement;
	for (var i = 0; i < document.KualiForm.elements.length; i++) {
	  var e = document.KualiForm.elements[i];
	  if(e.type == 'select-one' && e.value == '1') {
	  	completed = true;
	  	toggledElement = e;
	  }
	}
	if (completed) {
		for (var i = 0; i < document.KualiForm.elements.length; i++) {
			var el = document.KualiForm.elements[i];
			if (el.type == 'checkbox') {
				var elStatus = document.KualiForm.elements[i - 1];
				if (elStatus != toggledElement) {
					el.disabled = true;
				} else {
					var elHidden = document.KualiForm.elements[i + 2];
					elHidden.value = true;
					elHidden.disabled = false;
					el.disabled = true;
				}
			}
		}
	} else {
		for (var i = 0; i < document.KualiForm.elements.length; i++) {
			var el = document.KualiForm.elements[i];
			var elHidden = document.KualiForm.elements[i + 2];
			if (el.type == 'checkbox') {
				elHidden.disabled = true;
				el.disabled = false;
			}
		}
	}
	
}

function toggleFinalCheckboxesAndDisable(document) {
	var completed = false;
	var toggledElement;
	for (var i = 0; i < document.KualiForm.elements.length; i++) {
	  var e = document.KualiForm.elements[i];
	  if(e.type == 'select-one' && e.value == '1') {
	  	completed = true;
	  	toggledElement = e;
	  }
	}
	if (completed) {
		for (var i = 0; i < document.KualiForm.elements.length; i++) {
			var el = document.KualiForm.elements[i];
			if (el.type == 'checkbox') {
				var elStatus = document.KualiForm.elements[i - 1];
				if (elStatus != toggledElement) {
					el.disabled = true;
				} else {
					var elHidden = document.KualiForm.elements[i + 2];
					elHidden.value = true;
					elHidden.disabled = true;
					el.disabled = true;
				}
			}
		}
	} else {
		for (var i = 0; i < document.KualiForm.elements.length; i++) {
			var el = document.KualiForm.elements[i];
			var elHidden = document.KualiForm.elements[i + 2];
			if (el.type == 'checkbox') {
				elHidden.disabled = true;
				el.disabled = true;
			}
		}
	}
	
}

function setupVersionsPage(document) {
	var completed = false;
	var toggledElement;
	for (var i = 0; i < document.KualiForm.elements.length; i++) {
	  var e = document.KualiForm.elements[i];
	  if(e.type == 'select-one' && e.value == '1') {
	  	completed = true;
	  	toggledElement = e;
	  }
	}
	if (completed) {
		for (var i = 0; i < document.KualiForm.elements.length; i++) {
			var el = document.KualiForm.elements[i];
			if (el.type == 'checkbox') {
				var elStatus = document.KualiForm.elements[i - 1];
				if (elStatus != toggledElement) {
					el.disabled = true;
					elStatus.disabled = true;
					elStatusHidden.disabled = false;
				} else {
					var elHidden = document.KualiForm.elements[i + 2];
					elHidden.value = true;
					elHidden.disabled = false;
					el.disabled = true;
				}
			}
		}
	} else {
		for (var i = 0; i < document.KualiForm.elements.length; i++) {
			var el = document.KualiForm.elements[i];
			var elHidden = document.KualiForm.elements[i + 2];
			if (el.type == 'checkbox') {
				elHidden.disabled = true;
				el.disabled = false;
				var elStatus = document.KualiForm.elements[i - 1];
				elStatus.disabled
			}
		}
	}
}

function confirmFinalizeVersion(document, numVersions) {
	for (var i = 0; i < document.KualiForm.elements.length; i++) {
		var e = document.KualiForm.elements[i];
		if (e.name == 'document.finalVersionFlag') {
			if (e.checked == true) {
				for (var j = 0; j < numVersions; j++) {
	  				var finalVersionFlag = document.getElementById('finalVersionFlag' + j);
	  				if (finalVersionFlag != null && finalVersionFlag.value == 'Yes') {
	  					if (confirm("You are changing the final version.  Are you sure?")) {
	  						var updateFinalVersion = document.getElementById('updateFinalVersion');
	  						updateFinalVersion.value = 'Yes';
	  					} else {
	  						e.checked = false;
	  					}
	  				}
				}
			} else {
				var updateFinalVersion = document.getElementById('updateFinalVersion');
	  			updateFinalVersion.value = 'No';
			}
		}
	}
	
}


function selectAllBudgetForms(document) {
    var j = 0;
	for (var i = 0; i < document.KualiForm.elements.length; i++) {
	  var e = document.KualiForm.elements[i];
	  if(e.type == 'checkbox') {
	    //alert(e.name)
	  	if (e.name == 'selectedBudgetPrintFormId') {
 		    if(e.disabled == false){
 		    	e.checked = true;
 		    }
	  		j++; 
	  	}
	  }
	}
}

function unselectAllBudgetForms(document) {
    var j = 0;
	for (var i = 0; i < document.KualiForm.elements.length; i++) {
	  var e = document.KualiForm.elements[i];
	  if(e.type == 'checkbox') {
	  	if (e.name == 'selectedBudgetPrintFormId') {
 		    if(e.disabled == false){
 		    	e.checked = false;
 		    }
	  		j++; 
	  	}
	  }
	}
}
//CustomAttributeService.js - put it in kra-config.xml
//function CustomAttributeService() { }
// CustomAttributeService._path = '../dwr'; 
// CustomAttributeService.getLookupReturnsForAjaxCall = function(p0, callback) { DWREngine._execute(CustomAttributeService._path, 'CustomAttributeService', 'getLookupReturnsForAjaxCall', p0, callback); } 

function selectAllResearchAreas(document) {
    var j = 0;
	for (var i = 0; i < document.KualiForm.elements.length; i++) {
	  var e = document.KualiForm.elements[i];
	  if(e.type == 'checkbox') {
	  	var name = 'document.protocol.protocolResearchAreas[' + j + '].selectResearchArea';
	  	if (e.name == name) {
 		    e.checked = true;
	  		j++; 
	  	}
	  }
	}
}
