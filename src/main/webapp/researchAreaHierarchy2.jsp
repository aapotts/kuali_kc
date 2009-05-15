<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
                    "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>

  			<script language="JavaScript" type="text/javascript"
				src="dwr/engine.js"></script>

			<script language="JavaScript" type="text/javascript"
				src="dwr/util.js"></script>

			<script language="JavaScript" type="text/javascript"
				src="dwr/interface/CustomAttributeService.js"></script>

			<script language="JavaScript" type="text/javascript"
				src="dwr/interface/SponsorService.js"></script>


  <script src="scripts/jquery/jquery.js"></script>
  <link rel="stylesheet" href="css/jquery/screen.css" type="text/css" />
  <link rel="stylesheet" href="css/jquery/new_kuali.css" type="text/css" />
  <link rel="stylesheet" href="css/jquery/jquery.treeview.css" type="text/css" />
  <%-- link rel="stylesheet" href="http://dev.jquery.com/view/trunk/plugins/treeview/jquery.treeview.css" type="text/css" /--%>
  <script type="text/javascript" src="scripts/jquery/jquery.treeview.js"></script>
  
  
  
  
  <script>
  var node;
  var i = 0;
  var removedNode;
  $(document).ready(function(){
    $("#example").treeview();
    $("div#foo").append("Hello World!").css("color","red");
    $("#generate").click(function(){ 
    
       $.ajax({
         url: 'researchAreasLoad.jsp',
         type: 'GET',
         dataType: 'html',
         timeout: 1000,
         error: function(){
            alert('Error loading XML document');
         },
         success: function(xml){
            //alert("success"+xml);
            $(xml).find('h3').each(function(){
            var item_text = $(this).text();
            i++;
            //var item = $(this).find('item').text()
            //alert(item_text+"-")
            //var text1 = $('<span class="file"></span>').html(item_text);
            //alert(text1)
           // $('<li></li>').html($('<span class="file"></span>').html(item_text)).appendTo('ul#file31');
            var id = "item"+i;
            var tagId = "listcontrol"+i;
            var divId = "listcontent"+i;
            
           // if (i == 1) {
            var tag = $('<a style = "margin-left:2px;" ></a>').attr("id",tagId).html(item_text);
            var div = $('<div  class="hierarchydetail" style="margin-top:2px; "></div>').attr("id",divId);
            tag.click(
					function()
					{
					    //alert ("click "+tagId);
						$(".hierarchydetail:not(#"+divId+")").slideUp(300);
						div.slideToggle(300);
						loadChildrenRA(item_text, tagId);
					}
				);
//            } else {
//            var tag = $('<a id="listcontrol04" ></a>').attr("style","margin-left:2px;").html(item_text);
//            var div = $('<div id="listcontent04" class="hierarchydetail" style="margin-top:2px; "></div>');
//            }
             //<div class="hierarchydetail" id="listcontent02" style="margin-top:2px;">
            
            //var text = <a id="listcontrol02" style="margin-left:2px;">01. : AGRICULTURE, AGRICULTURE OPERATIONS, AND RELATED SCIENCES</a>
            var listitem = $('<li class="closed"></li>').attr("id",id).html(tag);
            //tag.appendTo(listitem);
            //listitem.appendTo('ul#file31');
            tableTag(item_text, id).appendTo(div)
            div.appendTo(listitem);
            listitem.appendTo('ul#browser');
           // setupListItem(item_text).appendTo('ul#browser');
        
            });
         }
        });    
     });  // generate
     
     			<!-- listcontent02 -->
				$("#listcontrol02").click(
					function()
					{
					    alert ("click 02");
						$(".hierarchydetail:not(#listcontent02)").slideUp(300);
						$("#listcontent02").slideToggle(300);
					}
				);
     			<!-- listcontent03 -->
				$("#listcontrol03").click(
					function()
					{
					    alert ("click 03");
						$(".hierarchydetail:not(#listcontent03)").slideUp(300);
						$("#listcontent03").slideToggle(300);
					}
				);
     			<!-- listcontent04 -->
				$("#listcontrol04").click(
					function()
					{
					    alert ("click 04");
						$(".hierarchydetail:not(#listcontent04)").slideUp(300);
						$("#listcontent04").slideToggle(300);
					}
				);
     
         
  // }); // $(document).ready


function tableTag(name, id) {

       var link = $('<a href="#" class="hidedetail"><img src="kr/static/images/tinybutton-hide.gif" align="absmiddle" border="0" width="45" height="15"></a>');
       var tag = $('<th  class="subelementheader" align="left"></th>').html(name);
       link.prependTo(tag);
       tag = $('<tr></tr>').html(tag);
       tag = $('<thead></thead>').html(tag);
       tag = $('<table width="100%" cellpadding="0" cellspacing="0" class="subelement"> </table>').html(tag);
       tbodyTag(name, id).appendTo(tag);
       return tag;
}

function tbodyTag(name, id) {

  var tblTag = $('<table cellpadding="0" cellspacing="0" class="elementtable" width="100%"></table>')
  var tag=$('<th colspan="4"></th>');
  var image = $('<a href="#"><img src="static/images/tinybutton-removenode.gif" width="79" height="15" border="0" alt="Remove Node" title="Remove this node and its child groups/sponsors"></a>&nbsp').click(function() {
                      var liId="li#"+id;
                      removedNode = $(liId);
                      alert("Remove node "+removedNode.attr("id"));
                      $(liId).remove();
                    }); 
  tag.html(image);
  image = $('<a href="#"><img src="static/images/tinybutton-cutnode.gif" width="79" height="15" border="0" alt="Cut Node" title="Cut this node and its child roups/sponsors.  (Node will not be removed until you paste it.)"></a>&nbsp').click(function() {
                      alert("Cut node");
                    }); 
  image.appendTo(tag);                  
  image = $('<a href="#"><img src="static/images/tinybutton-pastenode.gif" width="79" height="15" border="0" alt="Paste Node" title="Paste your previously cut node structure under this node"></a>').click(function() {

                if (removedNode) {
                      var parentNode = $("#"+id);
                      var ulTag = parentNode.children('ul');
                      if (ulTag.size() > 0) {                     
                           alert(ulTag.attr("id"));
                      } else {
                         alert("not found")
                          i++;
                          ulTag = $('<ul class="filetree"></ul>').attr("id","ul"+i);                         
                      }   
                      removedNode.appendTo(ulTag);
                      ulTag.appendTo(parentNode);
                   }                         
                }); 
                    
                    
                    
  image.appendTo(tag);     
  var notetag = $('<th style="text-align:right;"></th>').html("Node:");             
  tag = $('<tr></tr>').html(tag);
  notetag.prependTo(tag);
  tblTag.html(tag);
  
  // 2nd tr
  var trTag = $('<tr></tr>');
  var tdTag=$('<td class="infoline" style="width:60px;"></td>').html('&nbsp;');
  trTag.html(tdTag);
  var tdTag=$('<td class="infoline" style="width:100px;"></td>').html('<b>Parent Code</b>');
  tdTag.appendTo(trTag);
  var tdTag=$('<td class="infoline" style="width:100px;"></td>').html('<b>Research Code</b>');
  tdTag.appendTo(trTag);
  var tdTag=$('<td class="infoline"></td>').html('<b>Research Area</b>');
  tdTag.appendTo(trTag);
  var tdTag=$('<td class="infoline" style="width:65px;"></td>').html('<b>Action</b>');
  tdTag.appendTo(trTag);
   
  // 3rd tr
    var trTag1 = $('<tr></tr>');
    var tag1 = $('<th style="text-align:right;"></th>').html('Add:');
    var tdTag1 = $('<td></td>').html(getResearchAreaCode(name));
    trTag1.html(tag1);
    tdTag1.appendTo(trTag1);
    tdTag1 = $('<td></td>').html($('<input type="text" name="m2" value="" style="width:100%;" />').attr("id","researchCode"+i));
    tdTag1.appendTo(trTag1);
    tdTag1 = $('<td></td>').html($('<input type="text" name="m3" value="" style="width:100%;" />').attr("id","desc"+i));
    tdTag1.appendTo(trTag1);
    tag1 = $('<th class="infoline" style="text-align:center;"></th>');
    var addlink = $('<a href="#"><img src="static/images/tinybutton-add1.gif" width="40" height="15" border="0" title="Add this Sub-group"></a>').attr("id","addRA"+i).click(function() {
                         
          //alert("add node"+$(this).parents('tr:eq(0)').children('th').size());
          var trNode = $(this).parents('tr:eq(0)');
          //alert(trNode.children('td:eq(1)').children('input:eq(0)').attr("value")+"-"+trNode.children('td:eq(2)').children('input:eq(0)').attr("value"));

         if (trNode.children('td:eq(1)').children('input:eq(0)').attr("value") == "") {
           alert("must enter research area code");
         } else if (trNode.children('td:eq(2)').children('input:eq(0)').attr("value") == "") {
           alert("must enter research area");
         } else {
           var parentNode = $("#"+id);
           var ulTag = parentNode.children('ul');
           if (parentNode.children('ul').size() == 0) {
               i++;
               ulTag = $('<ul class="filetree"></ul>').attr("id","ul"+i);
            }
            
            ulTag.appendTo(parentNode);                                            
            var item_text = trNode.children('td:eq(1)').children('input:eq(0)').attr("value") +" : "+trNode.children('td:eq(2)').children('input:eq(0)').attr("value");
            var listitem = setupListItem(item_text);
            //alert(listitem.html());
            listitem.appendTo(ulTag);
          }  
                                           
         }); 
                
    tag1.html(addlink);
    tag1.appendTo(trTag1);
    
    
  trTag.appendTo(tblTag);
  trTag1.appendTo(tblTag);
  tag = $('<td class="subelementcontent"></td>').html(tblTag);
  //alert("1"+tag.html());
  tag = $('<tr></tr>').html(tag);
  //alert(tag.html());
  tag = $('<tbody></tbody>').html(tag);
  //alert(tag.html());
  return tag;
}

function setupListItem(name) {
            i++;
            var id1 = "item"+i;
            var tagId = "listcontrol"+i;
            var divId = "listcontent"+i;
            var tag = $('<a style = "margin-left:2px;" ></a>').attr("id",tagId).html(name);
            var div = $('<div  class="hierarchydetail" style="margin-top:2px; "></div>').attr("id",divId);
            tag.click(
					function()
					{
						$(".hierarchydetail:not(#"+divId+")").slideUp(300);
						div.slideToggle(300);
						loadChildrenRA(name, tagId);
					}
				);
			//alert(tag.html());	
            var listitem = $('<li class="closed"></li>').attr("id",id1).html(tag);
            tableTag(name, id1).appendTo(div)
            div.appendTo(listitem);
            //alert(listitem.html());
            return listitem;
}


function loadChildrenRA(nodeName, tagId) {
    var parentNode = $("#"+tagId);
    alert ("load subnodes for "+nodeName+"-"+parentNode.parents('li:eq(0)').attr("id")+"-" );
    var liNode = parentNode.parents('li:eq(0)');
    alert(liNode.children('ul').size());
    if (liNode.children('ul').size() == 0 ) {
        $.ajax({
         url: 'researchAreasLoad.jsp',
         type: 'GET',
         dataType: 'html',
         timeout: 1000,
         error: function(){
            alert('Error loading XML document');
         },
         success: function(xml){
            //alert("success"+xml);
            i++;
            var ulTag = $('<ul class="filetree"></ul>').attr("id","ul"+i);
            //alert(ulTag.html());
            
            ulTag.appendTo(parentNode);
            $(xml).find('h3').each(function(){
            var item_text = $(this).text();
            i++;
            //var item = $(this).find('item').text()
            //alert(item_text+"-")
            //var text1 = $('<span class="file"></span>').html(item_text);
            //alert(text1)
           // $('<li></li>').html($('<span class="file"></span>').html(item_text)).appendTo('ul#file31');
            var id = "item"+i;
            var tagId = "listcontrol"+i;
            var divId = "listcontent"+i;
            
           // if (i == 1) {
            var tag = $('<a style = "margin-left:2px;" ></a>').attr("id",tagId).html(item_text);
            var div = $('<div  class="hierarchydetail" style="margin-top:2px; "></div>').attr("id",divId);
            tag.click(
					function()
					{
					    //alert ("click "+tagId);
						$(".hierarchydetail:not(#"+divId+")").slideUp(300);
						div.slideToggle(300);
						loadChildrenRA(item_text, tagId);
					}
				);
//            } else {
//            var tag = $('<a id="listcontrol04" ></a>').attr("style","margin-left:2px;").html(item_text);
//            var div = $('<div id="listcontent04" class="hierarchydetail" style="margin-top:2px; "></div>');
//            }
             //<div class="hierarchydetail" id="listcontent02" style="margin-top:2px;">
            
            //var text = <a id="listcontrol02" style="margin-left:2px;">01. : AGRICULTURE, AGRICULTURE OPERATIONS, AND RELATED SCIENCES</a>
            var listitem = $('<li class="closed"></li>').attr("id",id).html(tag);
            //tag.appendTo(listitem);
            //listitem.appendTo('ul#file31');
            tableTag(item_text, id).appendTo(div)
            div.appendTo(listitem);
            //listitem.appendTo('ul#file31');
            listitem.appendTo(ulTag);
            if (i==1) {
            //alert (listitem.html());
            }
        
            });
         }
        });    
    }
    
} // end loadChildrenRA

function getResearchAreaCode(nodeName) {

        var endIdx = nodeName.indexOf(":");
        return nodeName.substring(0, endIdx - 1);    
}

   }); // $(document).ready

    </script>
  
</head>
<body>
  <ul id="example" class="filetree">
		<li><span class="folder">Folder 1</span>
			<ul>
				<li><span class="file">Item 1.1</span></li>
			</ul>
		</li>
		<li><span class="folder">Folder 2</span>
			<ul>
				<li><span class="folder">Subfolder 2.1</span>
					<ul>
						<li><span class="file">File 2.1.1</span></li>
						<li><span class="file">File 2.1.2</span></li>
					</ul>
				</li>
				<li><span class="file">File 2.2</span></li>
			</ul>
		</li>
		<li class="closed"><span class="folder">Folder 3 (closed at start)</span>
			<ul id="file31" class="filetree">
				<!-- <li><span class="file">File 3.1</span></li> -->
			</ul>
		</li>
		<li><span class="file">File 4</span></li>
	</ul>
	
	
	                                            <ul id="browser" class="filetree">
                                                 <li class="closed"><a id="listcontrol03" style="margin-left:2px;">01. : AGRICULTURE, AGRICULTURE OPERATIONS, AND RELATED SCIENCES</a>
                                                    <div class="hierarchydetail" id="listcontent03" style="margin-top:2px;">
														
                                                        <table width="100%" cellpadding="0" cellspacing="0" class="subelement">
                                                            <thead>
                                                                <tr>
                                                                    <th class="subelementheader" align="left">
                                                                        <a href="#" class="hidedetail"><img src="kr/static/images/tinybutton-hide.gif" align="absmiddle" border="0" width="45" height="15"></a>
                                                                        01. : AGRICULTURE, AGRICULTURE OPERATIONS, AND RELATED SCIENCES
                                                                    </th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <tr>
                                                                    <td class="subelementcontent">
                                                                		
                                                                        <table cellpadding="0" cellspacing="0" class="elementtable" width="100%">
                                                                            <tr>
                                                                            	<th style="text-align:right;">Node:</th>
                                                                                <th colspan="4">
                                                                                	<!--<a href="#"><img src="../images/arrow-up.gif" width="17" height="14" border="0" alt="Move Up" title="Move this node up within its group"></a>&nbsp;
                                                                                    <a href="#"><img src="static/images/arrow-down.gif" width="17" height="14" border="0" alt="Move Down" title="Move this node down within its group"></a>&nbsp;-->
                                                                                    <a href="#"><img src="static/images/tinybutton-removenode.gif" width="79" height="15" border="0" alt="Remove Node" title="Remove this node and its child groups/sponsors"></a>&nbsp;
                                                                                    <a href="#"><img src="static/images/tinybutton-cutnode.gif" width="79" height="15" border="0" alt="Cut Node" title="Cut this node and its child roups/sponsors.  (Node will not be removed until you paste it.)"></a>&nbsp;
                                                                                	<a href="#"><img src="static/images/tinybutton-pastenode.gif" width="79" height="15" border="0" alt="Paste Node" title="Paste your previously cut node structure under this node"></a>
                                                                                </th>
                                                                            </tr>
                                                                             <tr>
                                                                                <td class="infoline" style="width:60px;">&nbsp;
                                                                                    
                                                                                </td>
                                                                                <td class="infoline" style="width:100px;">
                                                                                    <b>Parent Code</b>
                                                                                </td>
                                                                                <td class="infoline" style="width:100px;">
                                                                                    <b>Research Code</b>
                                                                                </td>
                                                                                <td class="infoline">
                                                                                    <b>Research Area</b>
                                                                                </td> 
                                                                                <td class="infoline" style="width:65px;">
                                                                                   	<b>Action</b>
                                                                                </td>
                                                                            </tr>
                                                                            <tr>
                                                                                <th style="text-align:right;">
                                                                                    Edit:
                                                                                </th>
                                                                                <td>
                                                                                    000001
                                                                                </td>
                                                                                <td>
                                                                                    01.
                                                                                </td>
                                                                                <td>
                                                                                    <input type="text" name="y1" value="AGRICULTURE, AGRICULTURE OPERATIONS, AND RELATED SCIENCES" style="width:100%;" />
                                                                                </td>
                                                                                <th class="infoline" style="text-align:center;">
                                                                                    <!--<a href="#"><img src="../images/searchicon.gif" width="16" height="16" border="0" alt="Search for Group" title="Search for Group"></a>-->
                                                                                    <a href="#"><img src="../images/tinybutton-save1.gif" width="40" height="15" border="0" title="Add this Sub-group"></a>
                                                                                </th> 
                                                                            </tr>
                                                                            <tr>
                                                                                <th style="text-align:right;">
                                                                                    Add:
                                                                                </th>
                                                                                <td>
                                                                                    01.
                                                                                </td>
                                                                                <td>
                                                                                    <input type="text" name="y2" value="" style="width:100%;" />
                                                                                </td>
                                                                                <td>
                                                                                    <input type="text" name="y3" value="" style="width:100%;" />
                                                                                </td>
                                                                                <th class="infoline" style="text-align:center;">
                                                                                    <a href="#"><img src="../images/tinybutton-add1.gif" width="40" height="15" border="0" title="Add this Sub-group"></a>
                                                                                </th>  
                                                                            </tr>
                                                                        </table>
                                                                
                                                                	</td>
                                                                </tr>
                                                            </tbody>
                                                        </table>
                                                        
                                                    </div>
	                                           </li>
	                                          </ul>
	
	<div id="foo"/>
	<input type="submit" id="generate" value="Generate!"> 
	<div id="quote"></div>
	Sponsor <input type="text" name="document.sponsorCode" onblur="loadSponsorName1('document.sponsorCode', 'sponsorName');" id="document.sponsorCode" style="" class="" />

	<div id="sponsorName" >
                        
					</div>
</body>
 </html>