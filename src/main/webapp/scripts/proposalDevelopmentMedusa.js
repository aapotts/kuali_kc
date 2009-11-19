
    /*
	 * Load first level area of research when page is initially loaded
	 */
    function loadFirstLevel(){ 
      
      $.ajax({
        url: 'proposalDevelopmentMedusaViewAjax.do',
        type: 'GET',
        dataType: 'html',
        cache: false,
        data:'medusaBean.moduleName=' + $("#medusaBean\\.moduleName").attr("value") + '&medusaBean.moduleIdentifier=' + $("#medusaBean\\.moduleIdentifier").attr("value"),
        async:false,
        timeout: 1000,
        error:processError,
        success:processData 
       });  
      // return false;
    }  // generate
    
    function loadMedusaTree(){
    	
    	$.ajax({
            url: 'proposalDevelopmentMedusaViewAjax.do',
            type: 'GET',
            dataType: 'html',
            data:'awardNumber='+getAwardNumber(liNode)+'&addRA=E',
            cache: false,
            async: false,
            timeout: 1000,
            error:processError,
            success: displayTree
           });
    }
