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
	htmlFormAction="questionnaireNew" transactionalDocument="false"
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




    <div align="center" style="margin:10px">
        <kul:tabTop defaultOpen="true" tabTitle="Questionnaire Details, Content & Use " tabErrorKey="questionnaire*">
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
  var node;
  var i = 1;
  var j = 0;
  var ucount = 1;
  var removedNode;
  var cutNode;
  var copyNode;
  var sqlScripts = "createnew";
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
  
  
  
   function addQuestion() {
     //alert($("#document\\.newMaintainableObject\\.businessObject\\.questionnaireQuestions\\[0\\]\\.questionId").attr("value"));
     //var qid = $("#document\\.newMaintainableObject\\.businessObject\\.questionnaireQuestions\\[0\\]\\.questionId");
     var qid = $("#newQuestionId");
     //alert ("qid "+qid.attr("id"));
     if (qid.attr("value").trim() == "") {
        alert("Please enter question description");
       // var branches = $("<li><span class='folder'>New Sublist</span><ul>" + 
		//	"<li><span class='file'>Item1</span></li>" + 
		//	"<li><span class='file'>Item2</span></li></ul></li>").appendTo("#example");
        var branches = $("<li><span class='folder'>New Sublist</span></li>").appendTo("#example");
		$("#example").treeview({
			add: branches
		});
        
        
     } else {
     //alert("add q"+qid.attr("value"));
     
                 i++;

            var listitem = getQuestionNew("Test Question",1,"V.1.01",'false');
			var ultag = $('<ul></ul>');
            ultag.appendTo(listitem);
            
            
            listitem.appendTo('ul#example');
        
        // also need this to show 'folder' icon
        $('#example').treeview({
           add: listitem
        });

        var intag = $('<input type ="text"></input>').attr("value",qid.attr("value").trim());
        var name = "document\.newMaintainableObject\.businessObject\.questionnaireQuestions["+j+"]\.questionId";
        intag.attr("id",name).attr("name",name);
        j++;
        var trtmp = $('<tr></tr>');
        var tdtmp = $('<td></td>').html(intag);
        trtmp.html(tdtmp);
        trtmp.appendTo($("#question-table"));
     
     
     } //end else
   } // end addQuestion
      
 
    $(document).ready(function(){
        
       //alert("qncount= "+$("#qncount").attr("value"));
       // kind of set up questions when return from lookup.  still has long way to go.
       $("#newQuestionId").attr("value","10");
       	for(var k= 1; k <= $("#qncount").attr("value"); k++) {
       	//for(var k= 1; k <= 100; k++) {
       	   addQuestion();
       	}
        //alert(k)
  
    });
   
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
     
   
     // need to modify this for questionnaire
  //$(document).ready(function(){
  
     $("#save").click(function(){    
       var qname = $('#newQuestionnaire\\.name').attr("value");
       var qnaireid = $('#newQuestionnaire\\.questionnaireId').attr("value");
       var qdescription =$('#newQuestionnaire\\.description').attr("value");
       var qisfinal = $('#newQuestionnaire\\.isFinal').attr("checked");
       //alert ("save"+qname+qdescription+$('#newQuestionnaire\\.isFinal').attr("checked")); 
       if (qname == '') {
           alert("Questionnaire Name is required");
       } else {
       //TODO : FF seems to have trouble with "#;#"

     if (sqlScripts.indexOf("#;#") > 1) {
        // if current sqlScripts is not in array yet
        // 10 should be fine to use as check
         sqls[sqlidx++] = sqlScripts;
     }
     
     // Save new questionnaire bo
     $.ajax({
         url: 'questionnaireAjax.do',
         type: 'GET',
         dataType: 'html',
         cache: false,
         data:'action=savebo&newQuestionnaire.name='+qname+'&newQuestionnaire.questionnaireId='+qnaireid+'&newQuestionnaire.description='+qdescription+'&newQuestionnaire.isFinal='+qisfinal,
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
     
     
	 for (var k=0 ; k < sqls.length;  k++) {
       sqlScripts = sqls[k];
       sqlScripts = sqlScripts.replace(/#;#/g,";;;");
       qnaireid = $('#newQuestionnaire\\.questionnaireId').attr("value");
       //alert(sqls.length+"-"+k)
       // TODO : should consider to send newquestionnaire itself to save.
       // need to resend newquestionnaire because it might be changed.
       // also need to deal with 'description' which is varchar2(2000)
       // we can not assume that is is not over 2000, so need to think about it.
       $.ajax({
         url: 'questionnaireAjax.do',
         type: 'GET',
         dataType: 'html',
         cache: false,
         data:'action=new&sqlScripts='+sqlScripts+'&newQuestionnaire.questionnaireId='+qnaireid,
         async:false,
         timeout: 1000,
         error: function(){
            alert('error when saving');
         },
         success: function(xml){
            sqlScripts="createnew";
            $(xml).find('h3').each(function(){
                //var item_text = $(this).text();
                //$('#newQuestionnaire\\.questionnaireId').attr("value",$(this).text().substring(9));
            });
         }
       });// .ajax
      } // end for
      sqlidx=0; 
       } // if-then-else
       return false;
       }); 
      
   //}      
   
   
   
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
        alert(sqlScripts); 
 // TODO : delete usage also need to update 'item number' in the first column
            curnode = $(this).parents('tr:eq(0)');
            alert("size "+curnode.next().size());
            while (curnode.next().size() > 0) {
               curnode = curnode.next();
               alert(Number(curnode.children('th:eq(0)').html())-1);
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
        //alert(sqlScripts); 
        return false;
     }); 
     $("#deleteUsage").click(function(){  
        alert("delete usage");  
        return false;
     }); 
   
     $("#rootSearch").click(function() {
         //alert($(this).parents('li:eq(0)').attr("id"));
         // TODO : IE problem.  after the node is moved up or down, then the "id" of the image got lost.
         // so, before figuring a solution, use this alternative to fin parents id.
         checkToAddQn(0);
         return false;
     });

      $("#addRootQn").click(function() {
            i++;
            var listitem = getQuestionNew($("#newqdesc0").attr("value"),$("#newqtypeid0").attr("value"), "V1.01", "false");
			var ultag = $('<ul></ul>');
            ultag.appendTo(listitem);
            var idx = listitem.attr("id").substring(8);

              listitem.appendTo('#example');
              //listitem.appendTo('ul#example');
              // last one no 'move dn'
                 $("#movedn"+idx).hide();
                  alert($("#example").children('li').size());
                 if ($("#example").children('li').size() == 1) {
                     // TODO : the first one
                      $("#moveup"+idx).hide();
                 }
                 
                 if (listitem.prev().size() > 0) {
                    $("#movedn"+listitem.prev().attr("id").substring(8)).show();
                 }
        
        // also need this to show 'folder' icon
        $('#example').treeview({
           add: listitem
        });

        //var intag = $('<input type ="text"></input>').attr("value",$("#newQuestionId").attr("value").trim());
       // var name = "document\.newMaintainableObject\.businessObject\.questionnaireQuestions["+j+"]\.questionId";
        //intag.attr("id",name).attr("name",name);
       // j++;
        //var trtmp1 = $('<tr></tr>');
       // var tdtmp1 = $('<td></td>').html(intag);
        //trtmp1.html(tdtmp1);
        //trtmp1.appendTo($("#question-table"));
        // TODO : set up for insert 
        /* questionnairenumber from #questionnairenumber
         * questionId from #qid
         * sequenceNumber from $(this).parents('li:eq(0)').siblings().size() ?
         */
       // $(listitem).parents('ul:eq(0)').parents('li:eq(0)').size() == 0 : check whetehr it is at the top level
            parentNum = 0;
       // alert("questionnairenumber "+$("#questionNumber").attr("value")+" qid "+$("#qid"+$(this).attr("id").substring(5)).attr("value"));
        $("#qnum"+$(listitem).attr("id").substring(8)).attr("value",$("#questionNumber").attr("value"));
       // alert("parents li "+$(this).attr("id").substring(5)+" "+$("#qnum"+$(this).attr("id").substring(5)).attr("value"));
        var qid = $("#newqid0").attr("value");
        $("#qid"+$(listitem).attr("id").substring(8)).attr("value",qid);

      // set up qdesc & qtypeid                                                   
    //  $("#qdesc"+$(listitem).attr("id").substring(8)).attr("value",$("#newqdesc0").attr("value"));
    //  $("#qtypeid"+$(listitem).attr("id").substring(8)).attr("value",$("#newqtypeid0").attr("value"));

        var seqnum = Number($(listitem).siblings().size())+1;
        $("#qseq"+$(listitem).attr("id").substring(8)).attr("value",seqnum);
        var qnum = $("#questionNumber").attr("value");
        var insertValues = "insert into Q"+qid +","+qnum+","+ parentNum+",'N','','',"+seqnum+",user,sysdate)"
        addSqlScripts(insertValues);
        $("#questionNumber").attr("value",Number($("#questionNumber").attr("value"))+1)
        return false;
      });

// shared function
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

   
    </script>
    
    
</kul:page>
