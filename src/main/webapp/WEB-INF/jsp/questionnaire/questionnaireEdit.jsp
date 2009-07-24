<%--
 Copyright 2006-2009 The Kuali Foundation
 
 Licensed under the Educational Community License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.osedu.org/licenses/ECL-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>
<%@ include file="/WEB-INF/jsp/kraTldHeader.jsp"%>

<kul:page showDocumentInfo="false" docTitle="Questionnaire"
	htmlFormAction="questionnaireMaint" transactionalDocument="false"
	headerTitle="Questionnaire" auditCount="0">
          
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
  <link rel="stylesheet" href="css/jquery/kuali-stylesheet.css" type="text/css" />
  <link rel="stylesheet" href="css/jquery/jquery.treeview.css" type="text/css" />
  <%-- link rel="stylesheet" href="http://dev.jquery.com/view/trunk/plugins/treeview/jquery.treeview.css" type="text/css" /--%>
  <script type="text/javascript" src="scripts/jquery/jquery.treeview.js"></script>
    <script type="text/javascript" src="scripts/questionnaire.js"></script>

    <a name="topOfForm"></a>

<%--
   <div id = "loading">
      <a href="#"><img src="static/images/jquery/ajax-loader.gif" /></a>
   </div> 
--%>
    <div align="center" style="margin:10px">
    <kul:tabTop defaultOpen="true" tabTitle="Questionnaire" tabErrorKey="questionnaire*">
        <kra-questionnaire:questionnaireCore />
        <kra-questionnaire:questionnaireQuestion />
        <kra-questionnaire:questionnaireUsage />
    </kul:tabTop>
        <kul:panelFooter />
                
        <div id="globalbuttons" class="globalbuttons">
        	   <input type="image" id="save" src="${ConfigProperties.kr.externalizable.images.url}buttonsmall_save.gif" /> 
               <a href='portal.do?methodToCall=refresh&amp;docFormKey=88888888&amp;anchor=&amp;docNum='  title="cancel">
                   <img src="kr/static/images/buttonsmall_cancel.gif" class="tinybutton" alt="cancel" title="cancel" border="0" />
				</a>           
</div>
    </div>
    
    
        <script>
        
        
   var i = 1;
  var j = 0;
  var ucount = 1;
  var removedNode;
  var cutNode;
  var copyNode;
  var sqlScripts = "edit";
  var maxCopyNodeIdx=0;
  var isCopy='false';
  var jsContextPath = "${pageContext.request.contextPath}";
  var sqls = [];
  var sqlidx = 0;
  var groupid = 0;
  var curgroup = 0;
  var loadIntervalId;
  var loadcomplete='true';
  var loadcount=0;
  $(document).ready(function(){
    $.ajaxSettings.cache = false; 
    $("#example").treeview({
               toggle: function() {
                   //alert ("toggle "+$(this).attr("id"));
                  },
              animated: "fast",
              collapsed: true,
              control: "#treecontrol"
        });
            
  
    });
        
 
     $("#save").click(function(){    
       alert ("save"+sqlScripts); 
       var qname = $('#newQuestionnaire\\.name').attr("value");
       var qdescription =$('#newQuestionnaire\\.description').attr("value");
       var qisfinal = $('#newQuestionnaire\\.isFinal').attr("checked");
       var questionnaireId = $('#newQuestionnaire\\.questionnaireId').attr("value");
       //if (qisfinal == 'on') {
       //    qisfinal = true;
       //} else {
       //    qisfinal = false;
       //}
       //alert ("save"+sqlScripts); 

       if (qname == '') {
           alert("Questionnaire Name is required");
       } else {       
       //TODO : FF seems to have trouble with "#;#"
       //alert ("save "+sqlScripts+"-"+sqlScripts.indexOf("#;#")); 
     if (sqlScripts.indexOf("#;#") > 1) {
        // if current sqlScripts is not in array yet
        // 10 should be fine to use as check
         //alert("save add to sqls");
         sqls[sqlidx++] = sqlScripts;
     }
     
     
      $.ajax({
         url: 'questionnaireAjax.do',
         type: 'GET',
         dataType: 'html',
         cache: false,
         data:'action=savebo&newQuestionnaire.name='+qname+'&newQuestionnaire.questionnaireId='+questionnaireId+'&newQuestionnaire.description='+qdescription+'&newQuestionnaire.isFinal='+qisfinal,
         async:false,
         timeout: 1000,
         error: function(){
            alert('Error loading XML document');
         },
         success: function(xml){
            //sqlScripts="createnew";
            $(xml).find('h3').each(function(){
                //var item_text = $(this).text();
                $('#newQuestionnaire\\.questionnaireId').attr("value",$(this).text().substring(9));
            });
         }
       });// .ajax
     
     
     //alert("save "+sqls.length);
	 for (var k=0 ; k < sqls.length;  k++) {
       sqlScripts = sqls[k];
       sqlScripts = sqlScripts.replace(/#;#/g,";;;");
       $.ajax({
         url: 'questionnaireAjax.do',
         type: 'GET',
         dataType: 'html',
         cache: false,
         data:'action=edit&sqlScripts='+sqlScripts+'&newQuestionnaire.questionnaireId='+questionnaireId,
         async:false,
         timeout: 1000,
         error: function(){
            alert('Error loading XML document');
         },
         success: function(xml){
            sqlScripts = "edit";
            $(xml).find('h3').each(function(){
                var item_text = $(this).text();
            });
         }
       }); // end ajax
      } // end for
      sqlidx=0; 
       } // eid if-then-else
       return false;
      }); 
   //}      
   
   
   // -- should be shared
      var moduleCodes = ['select','Award', 'Institute Proposal', 'Development Proposal', 'Subcontracts', 'Negotiations','Person','IRB','Annual Coi Disclosure'];
      var opArray = ['select', 'and', 'or'];
      var responseArray = ['select', 'Contains text value', 'Matches text', 'Less than number', 'Less than or equals number', 'Equals number', 'Greater than or equals number', 'Greater than number', 'Before date', 'After date'];
      var questionType = ['select','Yes/No', 'Yes/No/NA', 'Number', 'Date', 'Text', 'Lookup'];
   
      var responseOptions = $('<select name="CustomData"></select>');
      $('<option value="0" selected="selected">select</option>').appendTo(responseOptions);
      $('<option value="1">Contains text value</option>').appendTo(responseOptions);
      $('<option value="2">Matches text</option>').appendTo(responseOptions);
      $('<option value="3">Less than number</option>').appendTo(responseOptions);
      $('<option value="4">Less than or equals number</option>').appendTo(responseOptions);
      $('<option value="5">Equals number</option>').appendTo(responseOptions);
      $('<option value="6">Greater than or equals number</option>').appendTo(responseOptions);
      $('<option value="7">Greater than number</option>').appendTo(responseOptions);
      $('<option value="8">Before date</option>').appendTo(responseOptions);
      $('<option value="9">After date</option>').appendTo(responseOptions);
 
        $("#addUsage").click(function(){  
        // TODO : 1 header and one 'add' row, so has 2 more
        ucount = $("#usage-table").children('tbody:eq(0)').children('tr').size()-1;
        trtmp = $('<tr/>').attr("id","usage"+ucount);
        thtmp = $('<th class="infoline"/>').html(ucount);
        thtmp.appendTo(trtmp); 
        tdtmp = $('<td align="left" valign="middle">').html(moduleCodes[$("#newQuestionnaireUsage\\.moduleItemCode").attr("value")]);
        modulecode = $('<input type="hidden"/>').attr("value",$("#newQuestionnaireUsage\\.moduleItemCode").attr("value"));
        modulecode.prependTo(tdtmp);
        tdtmp.appendTo(trtmp);
        tdtmp = $('<td align="left" valign="middle">').html($("#newQuestionnaireUsage\\.questionnaireLabel").attr("value"));
        tdtmp.appendTo(trtmp);
        tdtmp = $('<td align="left" valign="middle">').html("1.00");
        tdtmp.appendTo(trtmp);
		inputtmp = $('<input type="image" id="deleteUsage" name="deleteUsage" src="static/images/tinybutton-delete1.gif" class="tinybutton">').attr("id","deleteUsage"+ucount).click(function() {
		     //alert("delete "+$(this).parents('tr:eq(0)').attr("id"))
		     
		addSqlScripts("delete U;"+$(this).parents('tr:eq(0)').children('td:eq(0)').children('input:eq(0)').attr("value") );
       // alert(sqlScripts); 
 // TODO : delete usage also need to update 'item number' in the first column
            curnode = $(this).parents('tr:eq(0)');
            //alert("size "+curnode.next().size());
            while (curnode.next().size() > 0) {
               curnode = curnode.next();
               //alert(Number(curnode.children('th:eq(0)').html())-1);
               curnode.children('th:eq(0)').html(Number(curnode.children('th:eq(0)').html())-1)
               //curnode=curnode.next();
		    } 
		     $(this).parents('tr:eq(0)').remove();	     
		     return false;
		});
        tdtmp = $('<td align="left" valign="middle">');
        $('<div align="center">').html(inputtmp).appendTo(tdtmp);
        tdtmp.appendTo(trtmp);
        ucount++;
        trtmp.appendTo($("#usage-table"));
        addSqlScripts("insert U;"+$("#newQuestionnaireUsage\\.moduleItemCode").attr("value")+";"+$("#newQuestionnaireUsage\\.questionnaireLabel").attr("value") );
       // alert(sqlScripts); 
        return false;
     }); 
     //$("#deleteUsage").click(function(){  
        //alert("delete usage");  
      //  return false;
     //}); 
   
     $("#rootSearch").click(function() {
         //alert($(this).parents('li:eq(0)').attr("id"));
         // TODO : IE problem.  after the node is moved up or down, then the "id" of the image got lost.
         // so, before figuring a solution, use this alternative to fin parents id.
         checkToAddQn(0);
         return false;
     });

   
   // -- end should be shared
   
   
     // load questions & usages
     // TODO : if edit data has '"', then it getElementById will only reach the character before '"'
     // Question 1067 description has '"', and this is also probably why 
     // it only saved up 1067.  total selected is 54, but only saved to q 43.
          //$('<span id="msg"/>').css("color","red").html("loading... please wait").appendTo($("#loading"));
          //$("#loading").show();  // not showing up ?
          //$("#loading").show();  // if there is an alert
    jumpToAnchor('topOfForm');
		var editdata = document.getElementById("editData").value;
		//alert(editdata);
   		var dataarray=editdata.split("#;#");
   		var firstidx = -1;
   		var idxArray = new Array(dataarray.length);
   		//alert (retdata+"-"+dataarray[0]);
        var questions = dataarray[0].split("#q#");
        // qqid/qid/seq/desc/qtypeid/qnum/cond/value
        //var parentnum = 0;
        var parentidx = 0;

  loadQuestion();
  //if (questions.length > loadcount) {
  //    loadIntervalId = setInterval ( "loadQuestion()", 5000 );
  //}

        
           
  function backuploadquestion() {      
	    for (var k=0 ; k < questions.length;  k++) {
	        if (questions[k].indexOf("parent-") == 0) {
	           parentnum = questions[k].substring(7);
	           for (l = 1 ; l <= k+1; l++) {
	              if ($("#qnum"+l).attr("value")) {
	                 if ($("#qnum"+l).attr("value") == parentnum) {
	                    parentidx = l;
	                 }
	              }
	           }
	        } else {
	        
	        field = questions[k].split("#f#");
            i++;
            var parenturl;
            var ischild='false';
            if (parentnum == 0) {
              parenturl = $('#example');
              parentidx=i;
              
            } else {
               if (field[2] == 1) {
                  parentidx = i -1;
                  alert("set parentidx"+parentidx);
               }
               parenturl = $("#qnaireid"+parentidx).children('ul:eq(0)');
              ischild='true';
            }  
            var listitem = getQuestionNew(field[3],field[4], "V1.01", ischild);
			var ultag = $('<ul></ul>');
            ultag.appendTo(listitem);
            var idx = listitem.attr("id").substring(8);
              //listitem.appendTo('ul#example');
              // last one no 'move dn'
           if (firstidx == -1) {
               firstidx = idx;
           }    
                 
           listitem.appendTo($(parenturl));
        // also need this to show 'folder' icon
             $('#example').treeview({
                add: listitem
             });

    // TODO : test idea of display page by page with hide().
        if (parentnum == 0) {
            addToGroup(listitem);
        }
       // $(listitem).attr("class","group"+groupid);
       // if ((idx % 20) == 0) {
       //     groupid++;
            //$(listitem).hide();
       // }

              //alert($(listitem).parents('ul:eq(0)').size());
              if ($(listitem).parents('ul:eq(0)').children('li').size() == 1) {
                 $("#moveup"+idx).hide();
                 $("#movedn"+idx).hide();
              } else {
                 //alert("prev "+listitem.prev().attr("id"));
                 $("#movedn"+idx).hide();
                 if (listitem.prev().size() > 0) {
                     $("#movedn"+listitem.prev().attr("id").substring(8)).show();
                 }
              }
                       
           if (parentnum > 0 && field[6] != 'null') {                 
                alert ("add req for parent "+$("#addrequirement"+i).parents('tr:eq(0)').size());
                var newResponse = getRequirementDeleteRow(responseArray[field[6]], field[7]);
                newResponse.appendTo($("#addrequirement"+i).parents('div:eq(0)').children('table:eq(0)').children('tbody'));
                $("#cond"+i).attr("value",field[6]);
                $("#condvalue"+i).attr("value",field[7]);
               $("#addrequirement"+i).parents('tr:eq(0)').remove();
           }
        // TODO : set up for insert 
        /* questionnairenumber from #questionnairenumber
         * questionId from #qid
         * sequenceNumber from $(this).parents('li:eq(0)').siblings().size() ?
         */
            
       // alert("questionnairenumber "+$("#questionNumber").attr("value")+" qid "+$("#qid"+$(this).attr("id").substring(5)).attr("value"));
        $("#qnum"+$(listitem).attr("id").substring(8)).attr("value",field[5]);
        $("#qid"+$(listitem).attr("id").substring(8)).attr("value",field[1]);
        $("#qseq"+$(listitem).attr("id").substring(8)).attr("value",field[2]);
	          // set up qdesc & qtypeid                                                   
      $("#qdesc"+$(listitem).attr("id").substring(8)).attr("value",field[3]);
      $("#qtypeid"+$(listitem).attr("id").substring(8)).attr("value",field[4]);
	    
	     } // end if-then-else
	    } // end for to set up questions
   } // backuploadq    
	  // TODO : only the first question is expanded
	  $("#listcontrol"+firstidx).click();  
	  $("#listcontrol"+firstidx).click();  

// TODO : test grouping questions
    //alert("groupidx = "+groupid+$(".group1:eq(0)").attr("id"));
    $(".group1").hide();
    $(".group2").hide();
    jumpToAnchor('topOfForm');
    
    $("#nextGroup").click(function() {
        $(".group"+curgroup).hide();
        if (curgroup++ == groupid) {
            curgroup = 0;
        } 
        $(".group"+curgroup).show();
       // alert($(".group"+curgroup+":eq(0)").attr("id"));
         $("#listcontrol"+$(".group"+curgroup+":eq(0)").attr("id").substring(8)).click();  
        jumpToAnchor('topOfForm');
        return false;

    });

    $("#backToTop").click(function() {
        $(".group"+curgroup).hide();
            curgroup = 0;
        $(".group"+curgroup).show();
       // alert($(".group"+curgroup+":eq(0)").attr("id"));
         $("#listcontrol"+$(".group"+curgroup+":eq(0)").attr("id").substring(8)).click();  
        jumpToAnchor('topOfForm');
        return false;

    });

     // load usage
     // quid/modulecode/label
     
     
     if (dataarray.length > 1) {
        var usages = dataarray[1].split("#u#");
   	    for (var k=0 ; k < usages.length;  k++) {
	        field = usages[k].split("#f#");
            trtmp = $('<tr/>').attr("id","usage"+ucount);
            thtmp = $('<th class="infoline"/>').html(ucount);
            thtmp.appendTo(trtmp); 
            //tdtmp = $('<td align="left" valign="middle">').html(field[1]);
        var tdtmp = $('<td align="left" valign="middle">').html(moduleCodes[field[1]]);
        var modulecode = $('<input type="hidden"/>').attr("value",field[1]);
        modulecode.appendTo(tdtmp);
            tdtmp.appendTo(trtmp);
            tdtmp = $('<td align="left" valign="middle">').html(field[2]);
            tdtmp.appendTo(trtmp);
            tdtmp = $('<td align="left" valign="middle">').html("1.00");
            tdtmp.appendTo(trtmp);
		    inputtmp = $('<input type="image" id="deleteUsage" name="deleteUsage" src="static/images/tinybutton-delete1.gif" class="tinybutton">').attr("id","deleteUsage"+ucount).click(function() {
		       addSqlScripts("delete U;"+$(this).parents('tr:eq(0)').children('td:eq(0)').children('input:eq(0)').attr("value") );
               //alert(sqlScripts); 
               curnode=	$(this).parents('tr:eq(0)');
               alert("size "+curnode.next().size());
            while (curnode.next().size() > 0) {
               curnode = curnode.next();
               alert(Number(curnode.children('th:eq(0)').html())-1);
               curnode.children('th:eq(0)').html(Number(curnode.children('th:eq(0)').html())-1)
              // curnode=curnode.next();
		    } 
               	     
		       $(this).parents('tr:eq(0)').remove();	     
		       return false;
		    });
            tdtmp = $('<td align="left" valign="middle">');
            $('<div align="center">').html(inputtmp).appendTo(tdtmp);
            tdtmp.appendTo(trtmp);
            ucount++;
            trtmp.appendTo($("#usage-table"));
   	    
   	    }  
   	    // end for load usages
     } // check dataarray.length
   
   
   function loadQuestion() {
   //alert("loadnodes");
     if (loadcomplete == 'true') {
      loadcomplete='false';
      var qlen = questions.length;
      if (qlen > loadcount) {
        var endidx=loadcount+20;
        if (endidx > qlen) {
            endidx = qlen;
        } else if (endidx == 20 && qlen > 40) {
           endidx = 40; 
        }
        var rootidx;
        var pnum0 = 0;
        var nodecount = 0;
   	    //for (var k=loadcount ; k < endidx;  k++) {
   	    k = loadcount+nodecount;
   	    while (k < qlen) {
   	        var curq = questions[k++];
              //alert("parent 0 "+k+"-"+curq+pnum0+"-"+nodecount+"-"+qlen);
	        if (curq.indexOf("parent-") == 0) {
	           //parentnum = curq.substring(7);
	           //for (var l = 1 ; l <= k+1; l++) {
	           //   if ($("#qnum"+l).attr("value")) {
	           //      if ($("#qnum"+l).attr("value") == parentnum) {
	           //         parentidx = l;
	            //     }
	            //  }
	           //}
	        } else {
	        
	        var field = curq.split("#f#");
	        var parentnum = field[8];
            i++;
            var parenturl;
            var ischild='false';
            if (parentnum == 0) {
             // if ((pnum0 == 20 && loadcount != 0) || (loadcount == 0 && pnum0 == 40)) {
             //   //alert("break");
             //    break;
             // }   
              parenturl = $('#example');
              pnum0++;
              rootidx=i;
             // alert("parent 0 "+pnum0+"-"+nodecount);
              
            } else {
               if (field[2] == 1) { // the first children
                  parentidx=i-1;
               } else {
	              for (var l = rootidx ; l <= i-1; l++) {
	                 if ($("#qnum"+l).attr("value")) {
	                    if ($("#qnum"+l).attr("value") == parentnum) {
	                       parentidx = l;
	                    }
	                 }
	              }
               }
               parenturl = $("#qnaireid"+parentidx).children('ul:eq(0)');
              ischild='true';
            }  
            nodecount++;
            var listitem = getQuestionNew(field[3],field[4], "V1.01", ischild);
			var ultag = $('<ul></ul>');
            ultag.appendTo(listitem);
            var idx = listitem.attr("id").substring(8);
              //listitem.appendTo('ul#example');
              // last one no 'move dn'
           if (firstidx == -1) {
               firstidx = idx;
           }    
                 
           listitem.appendTo($(parenturl));
        // also need this to show 'folder' icon
             $('#example').treeview({
                add: listitem
             });

    // TODO : test idea of display page by page with hide().
        if (parentnum == 0) {
            addToGroup(listitem);
            if (groupid > 0) {
                $(listitem).hide();
            }
        }
       // $(listitem).attr("class","group"+groupid);
       // if ((idx % 20) == 0) {
       //     groupid++;
            //$(listitem).hide();
       // }

              //alert($(listitem).parents('ul:eq(0)').size());
              if ($(listitem).parents('ul:eq(0)').children('li').size() == 1) {
                 $("#moveup"+idx).hide();
                 $("#movedn"+idx).hide();
              } else {
                 //alert("prev "+listitem.prev().attr("id"));
                 $("#movedn"+idx).hide();
                 if (listitem.prev().size() > 0) {
                     $("#movedn"+listitem.prev().attr("id").substring(8)).show();
                 }
              }
                       
           if (parentnum > 0 && field[6] != 'null') {                 
                alert ("add req for parent "+$("#addrequirement"+i).parents('tr:eq(0)').size());
                var newResponse = getRequirementDeleteRow(responseArray[field[6]], field[7]);
                newResponse.appendTo($("#addrequirement"+i).parents('div:eq(0)').children('table:eq(0)').children('tbody'));
                $("#cond"+i).attr("value",field[6]);
                $("#condvalue"+i).attr("value",field[7]);
               $("#addrequirement"+i).parents('tr:eq(0)').remove();
           }
        // TODO : set up for insert 
        /* questionnairenumber from #questionnairenumber
         * questionId from #qid
         * sequenceNumber from $(this).parents('li:eq(0)').siblings().size() ?
         */
            
       // alert("questionnairenumber "+$("#questionNumber").attr("value")+" qid "+$("#qid"+$(this).attr("id").substring(5)).attr("value"));
        //if (i < 4) {
        //   alert("node "+idx+"-"+field[3]);
        //}
        $("#qnum"+idx).attr("value",field[5]);
        $("#qid"+idx).attr("value",field[1]);
        $("#qseq"+idx).attr("value",field[2]);
	          // set up qdesc & qtypeid                                                   
      $("#qdesc"+idx).attr("value",field[3]);
      $("#qtypeid"+idx).attr("value",field[4]);
	    
	     } // end if-then-else
	    } // end for to set up questions
	    loadcount=loadcount+nodecount;
    } else {
       alert("load is done "+loadcount);
       clearInterval(loadIntervalId);
    }// end if
    if (loadcount == 40) {
    	  // TODO : only the first question is expanded
	  $("#listcontrol"+firstidx).click();  
	  $("#listcontrol"+firstidx).click();  
    
    } // end for
      loadcomplete='true';
    }// if loadcomplete
   
   } // loadquestion
   
   
     // hide loading image
          //$("#loading").hide();
   
    </script>
    
    
</kul:page>
