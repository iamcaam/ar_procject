var UILogControl = {
    createNew:function(oInputHtml,oInputRelayAjax){
        var oHtml = oInputHtml;
        var NowSortLogField;
        var NowSortLogDirection;
        var oRelayAjax = oInputRelayAjax;        
        var oUILogContorl={};
        var oError = {};         
        var prev_total_page;
        var bGetLog;
        oUILogContorl.Init = function(){   
            oError = ErrorHandle.createNew(); 
            bGetLog = false;
            NowSortLogField = 'logtime';
            NowSortLogDirection = SortDirection.Desc;
             $('.LogList_Time').addClass('desc');
            $('#select_log_page').scombobox({
                editAble:false,
                wrap:false,
                listMaxWidth: '200px'
            });  
            $('#select_log_level').scombobox({
                editAble:false,
                wrap:false
            });
            $('#select_log_type').scombobox({
                editAble:false,
                wrap:false
            });            
            $('#select_log_page .scombobox-list').css('top', -$('#select_log_page .scombobox-list').height());
            BindPageBtnEvent();
            LogHederClickSort();
            prev_total_page = -1;
            disable_custom_button($('#btn_page_previous'));
//            $('#select_log_page').scombobox('change',function(){
//                if(bGetLog)
//                    Ajax_GetLog();
//            });
            $('#select_log_level').scombobox('change',function(){
                if(bGetLog)
                    Ajax_GetLog();
            });
            $('#select_log_type').scombobox('change',function(){
                if(bGetLog)
                    Ajax_GetLog();
            });
            /* SnapShot&Schedule&Backup  2017.01.11 william */
            $('#btn_page_refresh').tooltip({
                position: { my: "right+45% top+0", at: "right bottom", collision: "flipfit" },
                // open: function (event, ui) {
                //     setTimeout(function () {
                //         $(ui.tooltip).hide();
                //     }, 1000);
                // }
            });    
            $('#btn_page_next').tooltip({  /* SnapShot&Schedule&Backup  2017.01.11 william */
                position: { my: "right+45% top+0", at: "right bottom", collision: "flipfit" },
            }); 
            $('#btn_page_previous').tooltip({  /* SnapShot&Schedule&Backup  2017.01.11 william */
                position: { my: "right+45% top+0", at: "right bottom", collision: "flipfit" },
            });          
        };    
        oUILogContorl.PageInit = function(){
            bGetLog=false;
            $('#select_log_level').scombobox('val',0);
            $('#select_log_type').scombobox('val',0);
            $('#select_log_page').scombobox('val',1);
            $('.LogHeader').removeClass('asc');
            $('.LogHeader').removeClass('desc');
            $('.LogList_Time').addClass('desc');
            NowSortLogField = 'logtime';
            NowSortLogDirection = SortDirection.Desc;
            $('.label_log_arrow').html('');
            $('#arrowlogtime').html('&#9660;');   
            bGetLog=true;
            Ajax_GetLog();
        };
        function BindPageBtnEvent(){
            $('#btn_page_previous').click( function(e) 
            {                        
                e.preventDefault();
                var element = $(this);                
                if(element.hasClass('log_btn')){
                    oHtml.blockPage();
                    var now_page = parseInt($('#select_log_page').scombobox('val'));
                    if(now_page > 1){                        
                        bGetLog  =false;
                        $('#select_log_page').scombobox('val',now_page-1);
                    }
                    Ajax_GetLog();
                    bGetLog=true;
                }
            });
            $('#btn_page_next').click( function(e) 
            {        
                e.preventDefault();                   
                var element = $(this);
                if(element.hasClass('log_btn')){                    
                    oHtml.blockPage();                
                    var now_page = parseInt($('#select_log_page').scombobox('val'));
                    bGetLog  =false;
                    $('#select_log_page').scombobox('val',now_page+1);
                    Ajax_GetLog();
                    bGetLog = true;
                }
            });
            $('#btn_page_refresh').click( function(e) 
            {        
                e.preventDefault();  
                Ajax_GetLog();
            });            
            
        }
        function LogHederClickSort(){
            $('.LogHeader').click(function(event){                    
                event.preventDefault();
                var th = $(this);    
                NowSortLogField = th.attr('id');
                NowSortLogDirection = SortDirection.ASC;                                            
                if(th.hasClass('asc')){
                    $('.LogHeader').removeClass('asc');
                    $('.LogHeader').removeClass('desc');
                    $('.label_log_arrow').html('');
                    $('#arrow' + th.attr('id')).html('&#9660;');
                    th.addClass('desc');
                    NowSortLogDirection = SortDirection.Desc;
                }
                else{
                    $('.LogHeader').removeClass('asc');
                    $('.LogHeader').removeClass('desc');
                    $('.label_log_arrow').html('');
                    $('#arrow' + th.attr('id')).html('&#9650;');                        
                    th.addClass('asc');
                    NowSortLogDirection = SortDirection.ASC;
                }
                Ajax_GetLog();    
            });            
        }         
        function enable_disable_btn_page(btn_element,bolenable){
            if(bolenable){
                enable_custom_button($(btn_element));
            }
            else{
                disable_custom_button($(btn_element));
            }
            
        }
        
        function Ajax_GetLog(){                     
            oHtml.blockPage();
            var level = $('#select_log_level').scombobox('val');
            var type = $('#select_log_type').scombobox('val');
            var page = $('#select_log_page').scombobox('val');
            var order_field = '';
            var order_asc = NowSortLogDirection === SortDirection.ASC ? 1 : 0;
            switch(NowSortLogField){
                case 'loglevel':
                    order_field = 0;
                    break;
                case 'logtype':
                    order_field = 1;
                    break;
                case 'logtime':
                    order_field = 2;
                    break;
                case 'logsrcip':
                    order_field = 3;
                    break;
                case 'logriser':
                    order_field = 4;
                    break;
                case 'logcode':
                    order_field = 5;
                    break;
            }
            var request = oRelayAjax.listlog(page,level,type,order_field,order_asc);             
            Callback_Ajax_GetLog(request);
        }                
        
        function Callback_Ajax_GetLog(request){
            request.done(function(msg, statustext, jqxhr) {  
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                var per_page_count = msg['PageCount'];
                var total_page = msg['TotalPage'];
                var now_page = msg['NowPage'];
                var display_count = msg['DisplayCount'];
                var total_count = msg['TotalCount'];                
                var display_str = LanguageStr_Log.Log_Display_Item + ': ';
                var start_item = 0;
                var end_item = 0;
                if(display_count !== 0){
                    start_item = (now_page-1)*per_page_count + 1;                
                    end_item = start_item + display_count - 1;
                }
                var log_html = '';
                var i = 0;
                display_str += start_item + '-' + end_item;
                display_str += ',' + LanguageStr_Log.Log_total  + ': ' + total_count;
                $('#label_log_display').text(display_str);                
                enable_disable_btn_page('#btn_page_previous',now_page == 1 ? false : true);                
                enable_disable_btn_page('#btn_page_next',now_page == total_page ? false : true);
                if(prev_total_page !== total_page){
                    $('#label_last_page').text('/'+total_page);
                    log_html += '<select id="select_log_page">';
                    for( i =1 ; i <= total_page ; i++){
                        log_html += "<option value='" + i + "'>" + i + "</option>";
                    }
                    log_html += '</select>';                    
                    $('#div_select_log').html(log_html);                    
                    $('#select_log_page').scombobox({
                        editAble:false,
                        wrap:false,
                        listMaxWidth: '200px'
                    }); 
                    $('#select_log_page').scombobox('val',now_page);
                    $('#select_log_page .scombobox-list').css('top', -$('#select_log_page .scombobox-list').height());
                    $('#select_log_page').scombobox('change',function(){
                        if(bGetLog)
                            Ajax_GetLog();
                    });
                }              
                prev_total_page = total_page;
                $('#Div_VM_Log_List').html(oHtml.CreateLogHtml(msg,start_item)); // 新增log list項次 2016.12.21 william 
                $('#Div_VM_Log_List').tooltip({
                    position: { my: "left top+0", at: "left bottom", collision: "flipfit" }
                });
                oHtml.stopPage();
            });
            request.fail(function(jqxhr, textStatus) {  
                oHtml.stopPage();
            });
        }
        
        return oUILogContorl;
    }
       
};

$(document).ready(function ()   //  新增標頭位置對齊(tw) 2016.12.8 by william
{
    var log_Header_label_width = $(".LogList_Level").text().length;

    if(log_Header_label_width == 152){
        $("#log_RowHeader").css({"padding-top": "4px"});

        $(".LogList_Level").css({"left": "95px"});
    }
});