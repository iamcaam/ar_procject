var UIVMControl = {
    createNew:function(oInputHtml,oInputRelayAjax){ 
        var oHtml = oInputHtml;
        var GetVMStatusInterval;
        var VMIntervalStart;
        var VMListStart;
        var VMTaskStart;
        var SnapshotTaskStart; // 快照任務清單 新增內容 2017.01.25
        var SnapshotListStart; // SnapShot list 實作 2017.02.09 william
        var VMDiskCount;
        var oRelayAjax = oInputRelayAjax;                        
        var oUIVMContorl={};
        var oError = {};  
        var vmname = '';
        var vmid = '';
        var idLayer = ''; // 快照任務清單 新增內容 2017.01.25 
        var reg = /^\d+$/;
        var reg_mem = /^\d+[.]?[0-9]{0,2}$/;
        var reg_mem_1 = /^\d+[.]{1}$/;
        var reg_name = /^[a-zA-Z0-9_]*$/;
        var NowSortType;
        var NowSortDirection;
        var NowSortDOMID;
        var NowSortTaskType;
        var NowSortTaskDirection;
        var NowSortTaskDOMID;
        var NowSortSeedType;
        var NowSortSeedDirection;
        var NowSortSeedDOMID;
        var NowSortBornedType;
        var NowSortBornedDirection;
        var NowSortBornedDOMID;
        var NowSortSnapshotTaskType;    // 快照任務清單 新增內容 2017.02.07 
        var NowSortSnapshotTaskDirection;   // 快照任務清單 新增內容 2017.02.07 
        var NowSortSnapshotTaskDOMID;   // 快照任務清單 新增
        var PollingListTaskFlag;
        var PollingListFlag;
        var isRunningPollingList;
        var isRunningPollingTask;
        var isRunningPollingSnapshotTask;   // SnapShot task list 實作 2017.02.09 william
        var isRunningPollingSnapshotList;   // SnapShot list 實作 2017.02.09 william
        var arr_vm_not_delete = [];
        var org_name = '';
        var scrollSpeed = 150,repeat;
        var sslayervalue; // 新增快照層數 2017.11.22
        var current_sslayervalue; // 新增快照層數 2017.11.22
        
        var vm_expenddisk_data; // 2018.01.08 william 新增磁碟擴充功能
        var vm_expenddisk_diskid; // 2018.01.08 william 新增磁碟擴充功能
        var vm_expenddisk_size; // 2018.01.08 william 新增磁碟擴充功能
        var vm_expenddisk_type; // 2018.01.08 william 新增磁碟擴充功能
        var vm_expenddisk_orgsize; // 2018.01.08 william 新增磁碟擴充功能

        var vm_movedisk_data;
        var vm_movedisk_diskid; 

        var getuserarr;
        var getseedarr;
        var temp_vmname_for_clone;
        var temp_vmname_for_bornuser;
        var temp_vmname_for_vdtoorg;
        var temp_vmname_for_seed;
        var temp_username_for_vdtoorg;
        var importvd_org_name;
        var importcd_new_name;
        var import_raid_id;
        var userlist_checkbox_flage = -1;
        var vmusername = '';
        var clone_checkboxinfo;
        var clone_checkboxusername;
        var clone_checkboxno;
        var bornuser_checkboxinfo;
        var bornuser_checkboxusername;
        var bornuser_checkboxno;       
        // var textarea_type;  
        

        oUIVMContorl.Init = function(){               
            oError = ErrorHandle.createNew();   
            $('.btn_vm_operation').click(function(event){
                var btn_element = $(this);
                event.preventDefault();                       
                RebindVMActionButtonEvent(btn_element);
            });            
            PollingListFlag = true;
            PollingListTaskFlag = true;
            isRunningPollingList = false;
            isRunningPollingTask = false;
            isRunningPollingSnapshotTask = false;   // SnapShot task list 實作 2017.02.09 william
            isRunningPollingSnapshotList = false;   // SnapShot list 實作 2017.02.09 william
            BindDialogCloseEvent();
            InitVMSelectAllCheckBox();
            InitSelectAllSuspend();
            InitBornedSelectAllCheckBox();
            InitSeedSelectAllCheckBox();
           // InitTaskSelectAllCheckBox(); // 未來新增之項目 2016.12.23 william
            InitButtonForCreateVM();
            InitButtonForImportVM();
//            InitVMCreateRadio();
            InitVMTab();
//            InitVMButton();
//            InitVMProgressBar();   
            InitRefreshVMDiskButton();  
            Init_VMModify_Dialog_Item();
            InitVMCreateCombo();
            InitVMDeleteButton();
            InitVMRefreshButton();
            InitVMEnableAllButton();
            InitVMDisableAllButton();
            InitVMPoweronAllButton();
            InitVMShutdownAllButton();
            InitVMPoweroffAllButton();
            InitVMCreatButton();
            InitVMFastCloneButton();
            InitVMCloneButton();
            InitVMCreateDiskButton();
            Init_VMCreateDisk_Dialog_Item(); // 新增磁碟類型  2017.09.04 william
            InitVMCreateNICButton();
            InitVMModifyInfoButton();
            InitVMModifySeedButton();
            InitVMTaskButtons();
            InitButtonForSuspend();
            VMHederClickSort();
            TaskHederClickSort();
            SeedHederClickSort();
            BornedHeaderClickSort();
            VMListStart = false;
            VMTaskStart = false;            
            SnapshotTaskStart = false; // 快照任務清單 新增內容 2017.01.25 
            SnapshotListStart = false; // SnapShot list 實作 2017.02.09 william
            InitTakeSnapshotButton(); // 快照任務清單 新增內容 2017.01.26 
            InitStopViewSnapshotButton(); // 快照任務清單 新增內容 2017.02.06 
            SnapshotTaskHederClickSort(); // 快照任務清單 新增內容 2017.02.07 
            InitVMSSTaskButtons(); // 快照任務清單 新增內容 2017.02.07 
            InitModifySnapshotLayerButton(); // 新增快照層數 2017.11.22
            
            InitVMExpandDiskButton(); // 2018.01.08 william 新增磁碟擴充功能 
            VM_langStyleChange(); // 2018.01.08 william 列表標題及內容的文字位置調整

            Dialog_MoveUserDisk_forVD();
            InitVMMoveDiskButton();

            Dialog_UserList();
            Dialog_SeedList();
            Dialog_UserList_cb()
            InitButtonForVMCreateUserList();
            InitButtonForVMModifyUserList();
            InitButtonForBornUserUserList();
            InitButtonForVDCreateUserList();
            InitButtonForCloneUserList();
            InitUserSelectAllCheckBox();
            InitBornUserSelectAllCheckBox();
            InitButtonAllUserRename();
            $('#btn_close_DialogUserList').click(function (event) {
                event.preventDefault();
                $( "#dialog_VM_userlist" ).dialog('close');                    
            });   
            $('#btn_close_DialogSeedList').click(function (event) {
                event.preventDefault();
                $( "#dialog_VM_seedlist" ).dialog('close');                    
            });               
            $('#btn_close_UserList_cb').click(function (event) {
                event.preventDefault();
                $( "#dialog_VM_userlist_checkbox" ).dialog('close');                    
            }); 

//            VMStatus();
            //  新增快照層數  2017.11.22 william
            $('.label_SSLayer_warning_1').hide();
            $('#input_SSLayer').on("input", function () {
                current_sslayervalue = $('#input_SSLayer').val();
                if(current_sslayervalue > 500) {
                    $('.label_SSLayer_warning_1').css({"font-weight" : "bold","color" : "red","margin" : "0 5px", "position": "relative", "left": "205px"})
                    .text("("+ LanguageStr_VM.VM_SnapShotLayer_Recommendation+")")
                    .show();
                } 
                else {
                    $('.label_SSLayer_warning_1').hide();
                }
            });  
            //  新增快照層數  2017.11.22 william
                $('.label_SSLayer_warning_2').hide();
                $('#SnapshotUsedLayer_input').on("input", function () {
                    current_sslayervalue = $('#SnapshotUsedLayer_input').val();
                    if(current_sslayervalue > 500) {
                        $('.label_SSLayer_warning_2').css({"font-weight" : "bold","color" : "red","margin" : "0 5px"})
                        .text("("+ LanguageStr_VM.VM_SnapShotLayer_Recommendation+")")
                        .show();
                    } 
                    else {
                        $('.label_SSLayer_warning_2').hide();
                    }
                }); 

            $('#input_VM_User_forClone').on("input", function () {                
                $('#input_seed_dest_name').val('');
                $('#input_seed_dest_name').val($('#label_clone_source').text() + '_' + $('#input_VM_User_forClone').val());   
            }); 

            limitText( $('#input_VMName'), 32);
            limitText( $('#input_VM_User'), 32);
            limitText( $('#input_Mofify_VMName'), 32);            
            limitText( $('#input_VD_Assign_User'), 32);
            limitText( $('#label_Mofify_Seed_Name'), 32);
            limitText( $('#input_seed_dest_name'), 32);
            limitText( $('#input_VM_User_forClone'), 32);

        };      
        
        repeater = function(add,slider) {
            var val = slider.slider("option","value");
            var max = slider.slider("option","max");
            var min = $("#slider-range-cpu-clock").slider("option","min");            
           
            if(add)
            {
                if(val < max)
                    slider.slider("option","value",slider.slider("option","value") + 100);            
            }
            else
            {
                if(val > min)
                    slider.slider("option","value",slider.slider("option","value") - 100);            
            }
            //$("#now-cpu-clock" ).text( $("#slider-range-cpu-clock").slider("option","value") + "MHz" );
         
            repeat = setTimeout(function() {
                repeater(add,slider);
            }, scrollSpeed);
        };
        
        function Init_VMModify_Dialog_Item()
        {
            $('.iCheckRadioMemoryAllocate').iCheck({  
                radioClass: 'iradio_flat-blue'    
            });
            $('.iCheckRadioModifyUSB').iCheck({  
                radioClass: 'iradio_flat-blue'    
            });
            $('#tab-vm-modify-container').easytabs();
            $('#combo_add_nic').scombobox({
                editAble:false,
                wrap:false       
            });
            $('#combo_add_vswitch').scombobox({
                editAble:false,
                wrap:false       
            });
            $('#combo_Modify_cpu_cores').scombobox({
                editAble:false,
                wrap:false       
            });    
            //  新增CPU類型  2017.09.01 william
            $('#combo_Modify_cpu_type').scombobox({
                editAble:false,
                wrap:false       
            });               
            $('#combo_modify_usb_redirect').scombobox({
                editAble:false,
                wrap:false       
            }); 
            $('#combo_Modify_audio').scombobox({
                editAble:false,
                wrap:false       
            }); 
            $( "#slider-modify-range-cpu-clock" ).slider({
                range: "min",
                value: cpu_mhz,
                min: 500,
                step:100,
                max: cpu_mhz,
                change: function( event, ui ) {     
                    $( "#now-modify-cpu-clock" ).text( add_comma_of_number(ui.value) + " MHz" );
                },
                slide: function( event, ui ) {     
                    $( "#now-modify-cpu-clock" ).text( add_comma_of_number(ui.value) + " MHz" );
                }
            });
            $('#icheckmodifycpulimit').iCheck({  
                checkboxClass: 'icheckbox_minimal-blue'    
            });
            $('#icheckmodifypause').iCheck({  
                checkboxClass: 'icheckbox_minimal-blue'    
            });
            $('#icheckmodifycpulimit').on('ifChanged', function(event){
                var element = $(this);
                if(element.prop('checked')){
                    $('#slider-modify-range-cpu-clock').slider( "option", "disabled", false );
                    enable_custom_button($('.btn_cpu_clock'));
                }
                else{
                    $( "#slider-modify-range-cpu-clock" ).slider('option','max',cpu_mhz*$('#combo_Modify_cpu_cores').scombobox('val'));
                    $( "#slider-modify-range-cpu-clock" ).slider('option','value',cpu_mhz*$('#combo_Modify_cpu_cores').scombobox('val'));
                    $('#slider-modify-range-cpu-clock').slider( "option", "disabled", true );
                    disable_custom_button($('.btn_cpu_clock'));
                }
            });  
            $('#combo_Modify_cpu_cores').scombobox('change',function(event){
                $( "#slider-modify-range-cpu-clock" ).slider('option','max',cpu_mhz*$('#combo_Modify_cpu_cores').scombobox('val'));
                $( "#slider-modify-range-cpu-clock" ).slider('option','value',cpu_mhz*$('#combo_Modify_cpu_cores').scombobox('val'));
                $("#now-modify-cpu-clock" ).text( add_comma_of_number($("#slider-modify-range-cpu-clock").slider("option","value")) + " MHz" );       
            });
            
        }
        
        function BindDialogCloseEvent(){
             $( "#dialog_import_vm" ).dialog({
                close: function( event, ui ) {
                    event.preventDefault();                    
                    oUIVMContorl.VMList(true,true);
                    StartPolling(false,true);
                }
            });
            $( "#dialog_modify_vm" ).dialog({
                close: function( event, ui ) {
                    event.preventDefault();                    
                    oUIVMContorl.VMList(false,false);
                    StartPolling(true,true);
                }
            });
            $( "#dialog_modify_vm_boot" ).dialog({
                close: function( event, ui ) {
                    event.preventDefault();                    
                    oUIVMContorl.VMList(false,false);
                    StartPolling(true,true);
                }
            });
            $("#dialog_modify_seed" ).dialog({
                close: function( event, ui ) {
                    event.preventDefault();                    
                    oUIVMContorl.VMList(false,false);
                    StartPolling(true,true);
                }
            });
             $("#dialog_vm_snapshot" ).dialog({ // 手動快照dialog視窗開啟 2017.02.24 william
                close: function( event, ui ) {
                    event.preventDefault();                    
                    SnapshotListStart = false;
                }
            });
        }
        
    function InitVMRefreshButton(){
        $('.vm_refresh').click(function(event){
            event.preventDefault();
            StopPolling();
            oUIVMContorl.VMList(true,true);
            StartPolling(false,true);
        });
    }

    /* 新增任務的checkbox顯示 2016.12.23*/
   /*  未來新增之項目 2016.12.23 william
    function InitTaskSelectAllCheckBox(){
        $('.iCheckBoxTaskAllSelect').iCheck({  
            checkboxClass: 'icheckbox_minimal-blue'    
        });
        $('.iCheckBoxTaskAllSelect').on('ifChanged', function(event){
            var element = $(this);
            if(element.prop('checked')){
                $('.iCheckBoxTaskSelect').iCheck('check');
            }
            else{
                $('.iCheckBoxTaskSelect').iCheck('uncheck');
            }
        });
    }
    */
    function InitSeedSelectAllCheckBox(){
        $('.iCheckBoxSeedAllSelect').iCheck({  
            checkboxClass: 'icheckbox_minimal-blue'    
        });
        $('.iCheckBoxSeedAllSelect').on('ifChanged', function(event){
            var element = $(this);
            if(element.prop('checked')){
                $('.iCheckBoxSeedSelect').iCheck('check');
            }
            else{
                $('.iCheckBoxSeedSelect').iCheck('uncheck');
            }
            EnableDisableSelectSeedActionButton();
        });
    }
    
    function EnableDisableSelectSeedActionButton(){        
        if($('.iCheckBoxSeedSelect:checked').length > 0){
            $('.btn_seed_select_operation').button( "enable" );
        }
        else{
            $('.btn_seed_select_operation').button( "disable" );
        }
            
    }
    
    function InitBornedSelectAllCheckBox(){
        $('.iCheckBoxBornedAllSelect').iCheck({  
            checkboxClass: 'icheckbox_minimal-blue'    
        });
        $('.iCheckBoxBornedAllSelect').on('ifChanged', function(event){
            var element = $(this);
            if(element.prop('checked')){
                $('.iCheckBoxBornedSelect').iCheck('check');
                $('.iCheckBoxDisable').iCheck('uncheck');
            }
            else{
                $('.iCheckBoxBornedSelect').iCheck('uncheck');
            }
            EnableDisableSelectBornedActionButton();
        });
    }
    
    function EnableDisableSelectBornedActionButton(){        
        if($('.iCheckBoxBornedSelect:checked').length > 0){
            $('.btn_borned_select_operation').button( "enable" );
        }
        else{
            $('.btn_borned_select_operation').button( "disable" );
        }
            
    }
    
    function InitVMSelectAllCheckBox(){
        $('.iCheckBoxAllSelect').iCheck({  
            checkboxClass: 'icheckbox_minimal-blue'    
        });
        $('.iCheckBoxAllSelect').on('ifChanged', function(event){
            var element = $(this);
            if(element.prop('checked')){
                $('.iCheckBoxVMSelect').iCheck('check');
                $('.iCheckBoxDisable').iCheck('uncheck');
            }
            else{
                $('.iCheckBoxVMSelect').iCheck('uncheck');
            }
            EnableDisableSelectVMActionButton();
        });
    }        
    
    function EnableDisableSelectVMActionButton(){        
        if($('.iCheckBoxVMSelect:checked').length > 0){
            $('.btn_vm_select_operation').button( "enable" );
        }
        else{
            $('.btn_vm_select_operation').button( "disable" );
        }
            
    }
    
    function InitSelectAllSuspend(){
        $('.SelectAllVMSuspend').iCheck({  
            checkboxClass: 'icheckbox_minimal-blue'    
        });
        $('.SelectAllVMSuspend').on('ifChanged', function(event){   
            //lock checkbox
            //$('.iCheckBoxVMSuspend').addClass('disable');
            var element = $(this);
            if(element.prop('checked')){
                $('.iCheckBoxVMSuspend').iCheck('check');                
            }
            else{
                $('.iCheckBoxVMSuspend').iCheck('uncheck');
            }
            SuspendSelectVM('iCheckBoxVMSuspend',element.prop('checked'));
        });    
        $('.SelectAllBornedSuspend').iCheck({  
            checkboxClass: 'icheckbox_minimal-blue'    
        });
        $('.SelectAllBornedSuspend').on('ifChanged', function(event){   
            //lock checkbox
            //$('.iCheckBoxBornedSuspend').addClass('disable');
            var element = $(this);
            if(element.prop('checked')){
                $('.iCheckBoxBornedSuspend').iCheck('check');                
            }
            else{
                $('.iCheckBoxBornedSuspend').iCheck('uncheck');
            }
            SuspendSelectVM('iCheckBoxBornedSuspend',element.prop('checked'));
        });     
    }
    
    function SuspendSelectVM(checkboxclass,ischeck){
        oHtml.blockPage();
        var sets = [];
        $.each($('.' + checkboxclass),function(index,element){   
            var vd_name = $(element).attr('id').slice(9);
            ChangeVMNametoID(vd_name);        
            sets.push({VDID:vmid,Suspend:ischeck});            
        });
        VMModifyAutoSuspend(sets,true);
    }
    
    function InitVMTaskButtons(){
        $('#btn_clear_task').click(function(event){
            event.preventDefault();
            // var idarr = [];
            // $.each(VMListCloneTable,function(index,element){
            //     if(element['status'] < 0 || element['status'] === 10){
            //         idarr.push(element['id']);
            //     }                
            // });
            VMClearTask();
        });
        $('#btn_refresh_task').click(function(event){
            event.preventDefault();
            StopPolling();
            oUIVMContorl.VMListTask(true);
            StartPolling(true,false);            
        });
    }    
    
    function InitVMDeleteButton(){
        var delete_ask = LanguageStr_VM.VM_Delete.replace("@",VMorVDI);
        $('#btn_delete_all_vm').click(function(event){
            event.preventDefault();
            var confirmable = confirm(delete_ask);
            if (confirmable === true) {
                DeleteSelectVM('iCheckBoxVMSelect',VMListName);
            }
        });
        $('#btn_delete_all_borned').click(function(event){
            event.preventDefault();
            var confirmable = confirm(delete_ask);
            if (confirmable === true) {
                DeleteSelectVM('iCheckBoxBornedSelect',BornedListName);
            }
        });
        $('#btn_delete_all_seed').click(function(event){
            event.preventDefault();
            var confirmable = confirm(delete_ask);
            if (confirmable === true) {
                DeleteSelectSeed();
            }
        });
    }
    
    function DeleteSelectSeed(){
        var namearr = [];
        arr_vm_not_delete = [];
        $.each($('.iCheckBoxSeedSelect:checked'),function(index,element){   
            if(SeedListName[$(element).attr('id')]['vdi_count'] === 0)
            {                                                                
                namearr.push($(element).attr('id'));
            }
            else{
                arr_vm_not_delete.push($(element).attr('id'));
            }
        });
        var fail_name = '';
        if(arr_vm_not_delete.length > 0){
            $.each(arr_vm_not_delete,function(index,element){
                if(index > 2){
                    fail_name += '...etc';
                    return false;
                }
                if(index !== 0)
                    fail_name += ',';
                fail_name += '"' + element + '"';
            });           
            var delete_warning = (LanguageStr_VM.VM_Delete_VD_First).replace("&", fail_name);
            var delete_warning = delete_warning.replace("@",VMorVDI);
            alert(delete_warning);
        }
        if(namearr.length > 0)
            VMDelete(namearr);        
    }
    
    function DeleteSelectVM(checkboxclass,ListName){        
        var namearr = [];
        arr_vm_not_delete = [];
        var vm_stauts = 5;
        $.each($('.' + checkboxclass + ':checked'),function(index,element){   
            vm_stauts = ListName[$(element).attr('id')]['state'];
            if(vm_stauts === 5 || vm_stauts === 6 || vm_stauts === 0 || vm_stauts === -1 || vm_stauts === -3 || vm_stauts === -5)            
            {                                                                
                namearr.push($(element).attr('id'));
            }
            else{
                arr_vm_not_delete.push($(element).attr('id'));
            }
        });
        var fail_name = '';
        if(arr_vm_not_delete.length > 0){
            $.each(arr_vm_not_delete,function(index,element){
                if(index > 2){
                    fail_name += '...etc';
                    return false;
                }
                if(index !== 0)
                    fail_name += ',';
                fail_name += '"' + element + '"';
            });     
            var delete_warning = (LanguageStr_VM.VM_Delete_Shutdown_First).replace("&", fail_name);
            alert(delete_warning);
        }
        if(namearr.length > 0)
            VMDelete(namearr);            
    }
    
    function InitVMPoweronAllButton(){
        var poweron_ask = LanguageStr_VM.VM_Poweron_ask.replace("@",VMorVDI);
        $('#btn_poweron_all_vm').click(function(event){
            event.preventDefault();            
            var confirmable = confirm(poweron_ask);
            if (confirmable === true) {                
                PoweronSelectVM('iCheckBoxVMSelect',VMListName);
                $('.iCheckBoxVMSelect').iCheck('uncheck');
                $('.iCheckBoxAllSelect').iCheck('uncheck');
            }
        });
        $('#btn_poweron_all_borned').click(function(event){
            event.preventDefault();            
            var confirmable = confirm(poweron_ask);
            if (confirmable === true) {                
                PoweronSelectVM('iCheckBoxBornedSelect',BornedListName);
                $('.iCheckBoxBornedSelect').iCheck('uncheck');
                $('.iCheckBoxBornedAllSelect').iCheck('uncheck');
            }
        });
    }
    
    function PoweronSelectVM(checkboxclass,ListName){
        var namearr = [];
        $.each($('.' + checkboxclass + ':checked'),function(index,element){   
            if(ListName[$(element).attr('id')]['state'] !== -1)
            {           
                namearr[index] = ListName[$(element).attr('id')]['vdid'];                
            }
        });
        VMStart(namearr);
    }
    
    function InitVMShutdownAllButton(){
        var shutdown_ask = LanguageStr_VM.VM_Shutdown_ask.replace("@",VMorVDI);
        $('#btn_shutdown_all_vm').click(function(event){
            event.preventDefault();            
            var confirmable = confirm(shutdown_ask);
            if (confirmable === true) {
                ShutdownSelectVM('iCheckBoxVMSelect',VMListName);
            }
        });  
        $('#btn_shutdown_all_borned').click(function(event){
            event.preventDefault();            
            var confirmable = confirm(shutdown_ask);
            if (confirmable === true) {
                ShutdownSelectVM('iCheckBoxBornedSelect',BornedListName);
            }
        }); 
    }
    
    function ShutdownSelectVM(checkboxclass,ListName){
        var namearr = [];
        $.each($('.' + checkboxclass + ':checked'),function(index,element){   
            if(ListName[$(element).attr('id')]['state'] !== -1)
            {                                                
                namearr[index] = ListName[$(element).attr('id')]['vdid'];
            }
        });
        VMShutdown(namearr);
    }
    
    function InitVMPoweroffAllButton(){
        var poweroff_ask = LanguageStr_VM.VM_Poweroff_ask.replace("@",VMorVDI);
        $('#btn_poweroff_all_vm').click(function(event){
            event.preventDefault();            
            var confirmable = confirm(poweroff_ask);
            if (confirmable === true) {
                PoweroffSelectVM('iCheckBoxVMSelect',VMListName);
            }
        });
        $('#btn_poweroff_all_borned').click(function(event){
            event.preventDefault();            
            var confirmable = confirm(poweroff_ask);
            if (confirmable === true) {
                PoweroffSelectVM('iCheckBoxBornedSelect',BornedListName);
            }
        });
    }
    
    function PoweroffSelectVM(checkboxclass,ListName){
        var namearr = [];
        $.each($('.' + checkboxclass + ':checked'),function(index,element){   
            if(ListName[$(element).attr('id')]['state'] !== -1)
            {                                                      
                namearr[index] = ListName[$(element).attr('id')]['vdid'];
            }
        });
        VMClose(namearr);
    }
    
    function InitVMEnableAllButton(){
        var enable_ask = LanguageStr_VM.VM_Enable_ask.replace("@",VMorVDI);
        $('#btn_enable_all_vm').click(function(event){
            event.preventDefault();            
            var confirmable = confirm(enable_ask);
            if (confirmable === true) {
                EnableSelectVM('iCheckBoxVMSelect',VMListName);
            }
        });
        $('#btn_enable_all_borned').click(function(event){
            event.preventDefault();            
            var confirmable = confirm(enable_ask);
            if (confirmable === true) {
                EnableSelectVM('iCheckBoxBornedSelect',BornedListName);
            }
        });
    }
    
    function EnableSelectVM(checkboxclass,ListName){
        var namearr = [];
        $.each($('.' + checkboxclass + ':checked'),function(index,element){
            namearr[index] = ListName[$(element).attr('id')]['vdid'];
        });
        VMEnable(namearr);
    }
    
    function InitVMDisableAllButton(){
        var disable_ask = LanguageStr_VM.VM_Disable_ask.replace("@",VMorVDI);
        $('#btn_disable_all_vm').click(function(event){
            event.preventDefault();            
            var confirmable = confirm(disable_ask);
            if (confirmable === true) {
                DisableSelectVM('iCheckBoxVMSelect',VMListName);
            }
        });
        $('#btn_disable_all_borned').click(function(event){
            event.preventDefault();            
            var confirmable = confirm(disable_ask);
            if (confirmable === true) {
                DisableSelectVM('iCheckBoxBornedSelect',BornedListName);
            }
        });
    }    
    
    function DisableSelectVM(checkboxclass,ListName){       
        var namearr = [];
        var arr_vm_not_disable = [];
        $.each($('.' + checkboxclass + ':checked'),function(index,element){   
            if(ListName[$(element).attr('id')]['state'] === 5)
            {                                                                
                namearr.push(ListName[$(element).attr('id')]['vdid']);
            }
            else{
                arr_vm_not_disable.push($(element).attr('id'));
            }
        });
        var fail_name = '';
        if(arr_vm_not_disable.length > 0){
            $.each(arr_vm_not_disable,function(index,element){
                if(index > 2){
                    fail_name += '...etc';
                    return false;
                }
                if(index !== 0)
                    fail_name += ',';
                fail_name += '"' + element + '"';
            });     
            var disable_warning = (LanguageStr_VM.VM_Disable_Shutdown_First).replace("&", fail_name);
            alert(disable_warning);
        }   
        if(namearr.length > 0)
            VMDisable(namearr);
    }
    
    function InitVMFastCloneButton(){
        $('#btn_create_vdi').click(function(event){
            event.preventDefault();
            userlist_checkbox_flage = 2;
            SelectUserNameRename('iCheckBoxBornUserSelect');            
            // var dest_name= $("#textarea_snapshot_dest_name").val ();
            var dest_name = TransformTextarea($("#textarea_snapshot_dest_name").val());
            var description = $('#input_create_vdi_VMDescription').val();
            var name_check = [];
            var namearr = [];          
            for(var nameindex in VMCheckName){
                name_check[nameindex] = VMCheckName[nameindex];
            }            
            if(dest_name.length === 0)
            {
                alert(LanguageStr_VM.VM_Destination_Warning);
                return false;
            }
            dest_name = dest_name.replace(/\r\n/g, "");
            dest_name = dest_name.replace(/\n/g, "");
            var dest_name_arr = dest_name.split(',');
            var check_name = true;
            $.each(dest_name_arr,function(index,name){
                if(!VerifyVMInput(name))
                {
                    var name_warning = (LanguageStr_VM.VM_Clone_Name_Warning).replace("&",name);
                    alert(name_warning);
                    check_name = false;
                    return false;
                }     
                if(reg.test(name))
                {
                    var name_warning_1 = (LanguageStr_VM.VM_Clone_Name_Warning_1).replace("&",name);                    
                    alert(name_warning_1);
                    check_name = false;
                    return false;
                }
            });
            if(!check_name)
                return false;
            var check_duplicate = true;
            $.each(dest_name_arr,function(index,name){                
                var findVMIndex = name_check.indexOf(name);
                if(findVMIndex !== -1)
                {
                    var name_warning_2 = (LanguageStr_VM.VM_Clone_Name_Warning_2).replace("&",name);                    
                    alert(name_warning_2);
                    check_duplicate = false;
                    return false;
                } 
                else{
                    name_check.push(name);
                    namearr.push(name);
                }
            });
            if(!check_duplicate)
                return false;          
            description = description.replace(/"/g, '\\"');
            VMClone(2,SeedListName[vmname]['vdid'],namearr,description);
        });
    }
    
    function InitVMCloneButton(){                    
        $('#btn_clone_vm').click(function(event){
            event.preventDefault();
            userlist_checkbox_flage = 1;
            SelectUserNameRename('iCheckBoxUserSelect');
            var dest_name = '';
            var clone_type = $('.iCheckRadioClone:checked').val();
            if( clone_type === "0") {                
                // dest_name= $("#textarea_dest_name").val();
                dest_name = TransformTextarea($("#textarea_dest_name").val());
            } else {                
                // dest_name= $("#input_seed_dest_name").val();
                temp_username_for_vdtoorg = $("#input_VM_User_forClone").val();
                dest_name= TransformTextarea($("#input_seed_dest_name").val() + ":" + temp_username_for_vdtoorg);
            }
                
            var description = $('#input_clone_VMDescription').val();
            var name_check = [];
            var namearr = [];
            for(var nameindex in VMCheckName){
                name_check[nameindex] = VMCheckName[nameindex];
            }            
            if(dest_name.length === 0)
            {
                alert(LanguageStr_VM.VM_Destination_Warning);
                return false;
            }     
            if( clone_type === "0"){
                dest_name = dest_name.replace(/\r\n/g, "");
                dest_name = dest_name.replace(/\n/g, "");
                var dest_name_arr = dest_name.split(',');
            }
            else{
                var dest_name_arr = [];
                dest_name_arr[0]=dest_name;
            }
            var check_name = true;
            $.each(dest_name_arr,function(index,name){
                if(!VerifyVMInput(name))
                {
                    var name_warning = (LanguageStr_VM.VM_Clone_Name_Warning).replace("&",name);
                    alert(name_warning);
                    check_name = false;
                    return false;
                }     
                if(reg.test(name))
                {
                    var name_warning_1 = (LanguageStr_VM.VM_Clone_Name_Warning_1).replace("&",name);
                    alert(name_warning_1);
                    check_name = false;
                    return false;
                }
            });
            if(!check_name)
                return false;
            var check_duplicate = true;
            $.each(dest_name_arr,function(index,name){                
                var findVMIndex = name_check.indexOf(name);
                if(findVMIndex !== -1)
                {
                    var name_warning_2 = (LanguageStr_VM.VM_Clone_Name_Warning_2).replace("&",name);                    
                    alert(name_warning_2);
                    check_duplicate = false;
                    return false;
                } 
                else{
                    name_check.push(name);
                    namearr.push(name);
                }
            });
            if(!check_duplicate)
                return false;            
            description = description.replace(/"/g, '\\"');
            // 2017.11.22 william 雲桌面轉移至雲主機
            if( clone_type === "3")
                VMClone(clone_type,BornedListName[vmname]['vdid'],namearr,description);
            else
                VMClone(clone_type,VMListName[vmname]['vdid'],namearr,description);
        })
    }
    
    function InitRefreshVMDiskButton(){
        $('.btn_refresh_vm_disk').click(function(event){
            event.preventDefault();
            VMInfo(org_name,true,false);
        })
    }

    function InitVMCreatButton(){
        $('#btn_create_vm').click(function (event) {            
            event.preventDefault();
            var name = $('#input_VMName').val();           
            var description = $('#input_VMDescription').val();
            if(name.length === 0)
            {
                alert(LanguageStr_VM.VM_Name_Empty_Warning);
                return false;
            }         
            if(!VerifyVMInput(name))
            {
                var name_warning = (LanguageStr_VM.VM_Clone_Name_Warning).replace("&",name);
                alert(name_warning);
                return false;
            }         
            if(reg.test(name))
            {
                var name_warning_1 = (LanguageStr_VM.VM_Clone_Name_Warning_1).replace("&",name);
                alert(name_warning_1);
                return false;
            }
            var findVMIndex = VMCheckName.indexOf(name);
            if(findVMIndex !== -1)
            {
                var name_warning_2 = (LanguageStr_VM.VM_Clone_Name_Warning_2).replace("&",name);                    
                alert(name_warning_2);
                return false;
            }
            var memory = $('#input_VMMemory').val();
            if(memory.length === 0 )
            {
                alert(LanguageStr_VM.VM_Memory_Integer_Warning);
                return false;
            }
            if(!reg_mem.test(memory))
            {
                alert(LanguageStr_VM.VM_Memory_Integer_Warning);
                return false;
            }
            if(reg_mem_1.test(memory))
            {
                alert(LanguageStr_VM.VM_Memory_Integer_Warning);
                return false;
            }
            memory=Math.round(memory*1024);            
            var cpu_cores = $('#combo_cpu_cores').scombobox('val');
            var cpu_type = $('#combo_cpu_type').scombobox('val'); // 新增CPU類型  2017.09.01 william
            var iso_path = $('#combo_iso').scombobox('val');
            var raid_id = $('#combo_raidid_createvd').scombobox('val');
            // alert(iso_path);
            var usb_type = $( ".iCheckRadioUSB:checked").val();
            var usb_redirect_count = $('#combo_usb_redirect').scombobox('val');
            if(iso_path === 'none')
                iso_path = '';            
            var boot_disk_size = 0;
            var boot_disk_path = "";
            var init_memory=memory;
            var cpu_clock = -1;
            var check_memory_allocation = $( ".iCheckRadioMemoryAllocateCreate:checked").val();
            if(check_memory_allocation === '0')
                init_memory=512*1024;                
//            var check_val = $( ".iCheckRadioBootDisk:checked").val();            
//            if(check_val === 'manually'){
                boot_disk_size = $('#input_VMBootDiskSize').val();
                if(boot_disk_size.length === 0 )
                {
                    alert(LanguageStr_VM.VM_NewDisk_Integer_Warning);
                    return false;
                }
                if(!reg.test(boot_disk_size))
                {
                    alert(LanguageStr_VM.VM_NewDisk_Integer_Warning);
                    return false;
                }
//            }
//            else{
//                boot_disk_path = $('#combo_img').scombobox('val');
//            }            
            description = description.replace(/"/g, '\\"');    
            if($('#icheckcpulimit').prop('checked')){
                cpu_clock = $("#slider-range-cpu-clock").slider("option","value");
            }                            
            //  新增快照層數  2017.11.22 william
            var sslayer = $("#input_SSLayer").val(); 
            if(sslayer.length === 0)
            {
                alert(LanguageStr_VM.VM_SnapShotLayer_Empty_Warning);
                return false;
            }   
            if(sslayer < 30)
            {
                alert(LanguageStr_VM.VM_SnapShotLayer_Error1);
                return false;
            }                 
            if(sslayer > 1000)
            {
                alert(LanguageStr_VM.VM_SnapShotLayer_Error2);
                return false;
            }                

            var username = $('#input_VM_User').val();  
            if(username.length === 0)
            {
                alert(LanguageStr_VM.User_Name_Empty_Warning);
                return false;
            }         
            if(!VerifyVMInput(username))
            {
                var name_warning = (LanguageStr_VM.User_Name_Warning).replace("&",username);
                alert(name_warning);
                return false;
            }         
            if(reg.test(username))
            {
                var name_warning_1 = (LanguageStr_VM.User_Name_Warning_1).replace("&",username);
                alert(name_warning_1);
                return false;
            }
                                                                    
            VMCreate(username,name,cpu_cores,cpu_type,memory,boot_disk_size,boot_disk_path,0,iso_path,description,init_memory,usb_type,usb_redirect_count,$('#combo_audio').scombobox('val'),$('#combo_nic').scombobox('val'),cpu_clock,$('#icheckpause').prop('checked'),$('#combo_vswitch').scombobox('val'),sslayer, raid_id); // 新增CPU類型  2017.09.01, 新增快照層數  2017.11.22 william
        });
    }

    function VerifyVMInput(Input_Data)
    {       
       if(reg_name.test(Input_Data))
           return true;
       else
           return false;
    };
    
    function InitVMModifySeedButton()
    {
        $('#btn_modify_vm_seed').click(function(event){
           event.preventDefault();           
           var description = $('#input_Modify_Seed_Description').val();
           var name = $('#label_Mofify_Seed_Name').val();                 
            if(name !== org_name){
                if(name.length === 0)
                {
                    alert(LanguageStr_VM.VM_Name_Empty_Warning);
                    return false;
                }         
                if(!VerifyVMInput(name))
                {
                    var name_warning = (LanguageStr_VM.VM_Clone_Name_Warning).replace("&",name);
                    alert(name_warning);
                    return false;
                }         
                if(reg.test(name))
                {
                    var name_warning_1 = (LanguageStr_VM.VM_Clone_Name_Warning_1).replace("&",name);
                    alert(name_warning_1);
                    return false;
                }            
            }
           description = description.replace(/"/g, '\\"');
           VMModifySeed(name,description);
        });        
    }
    
    function InitVMModifyInfoButton()
    {
        $('#btn_modify_vm_info').click(function(event){
            event.preventDefault();
            var cpu_cores = $('#combo_Modify_cpu_cores').scombobox('val');
            var cpu_type = $('#combo_Modify_cpu_type').scombobox('val'); // 新增CPU類型  2017.09.01 william
            var memory = $('#input_Modify_VMMemory').val();
            var description = $('#input_VMModifyDescription').val();
            var usb_type = $( ".iCheckRadioModifyUSB:checked").val();
            var usb_redirect_count = $('#combo_modify_usb_redirect').scombobox('val');
            var name = $('#input_Mofify_VMName').val(); 
            var cpu_clock = -1;            
            if(name !== org_name){
                if(name.length === 0)
                {
                    alert(LanguageStr_VM.VM_Name_Empty_Warning);
                    return false;
                }         
                if(!VerifyVMInput(name))
                {
                    var name_warning = (LanguageStr_VM.VM_Clone_Name_Warning).replace("&",name);
                    alert(name_warning);
                    return false;
                }         
                if(reg.test(name))
                {
                    var name_warning_1 = (LanguageStr_VM.VM_Clone_Name_Warning_1).replace("&",name);
                    alert(name_warning_1);
                    return false;
                }            
//                var findVMIndex = VMCheckName.indexOf(name);
//                if(findVMIndex !== -1)
//                {
//                    var name_warning_2 = (LanguageStr_VM.VM_Clone_Name_Warning_2).replace('&',name);                    
//                    alert(name_warning_2);
//                    return false;
//                }
            }
            if(memory.length === 0 )
            {
                alert(LanguageStr_VM.VM_Memory_Integer_Warning);
                return false;
            }
            if(!reg_mem.test(memory))
            {
                alert(LanguageStr_VM.VM_Memory_Integer_Warning);
                return false;
            }
            if(reg_mem_1.test(memory))
            {
                alert(LanguageStr_VM.VM_Memory_Integer_Warning);
                return false;
            }
            memory=memory*1024;
            // var init_memory=memory;
            // var check_memory_allocation = $( ".iCheckRadioMemoryAllocate:checked").val();            
            // if(check_memory_allocation === '0')
            //     init_memory=512*1024; 
            // var target = $('#combo_modify_iso select').data('target');
            var iso_path = $('#combo_modify_iso').scombobox('val');            
            if(iso_path === 'none')
                iso_path = '';
            description = description.replace(/"/g, '\\"');
            if($('#icheckmodifycpulimit').prop('checked')){
                cpu_clock = $("#slider-modify-range-cpu-clock").slider("option","value");
            }            
            VMModifyInfo(name,cpu_cores,cpu_type,memory,iso_path,description,usb_type,usb_redirect_count,$('#combo_Modify_audio').scombobox('val'),cpu_clock,$('#icheckmodifypause').prop('checked')); // 新增CPU類型  2017.09.01 william
        });     
        $('#btn_modify_vm_boot').click(function(event){
            event.preventDefault();            
            var target = $('#combo_iso_boot select').data('target');
            var iso_path = $('#combo_iso_boot').scombobox('val');
            if(iso_path === 'none')
                iso_path = '';            
            VMModifyISO(vmname,iso_path);
        });
        $('#btn_close_modify_vm_boot').click(function (event) {
            event.preventDefault();
            $("#dialog_modify_vm_boot").dialog('close');                    
        });   
    }
    
    function InitVMCreateNICButton()
    {
        $('#btn_add_vm_nic').click(function(event){
            event.preventDefault();
            VMAddNIC(vmid,$('#combo_add_nic').scombobox('val'),$('#combo_add_vswitch').scombobox('val'));
        });
    }
    
    function InitVMCreateDiskButton(){
        $('#btn_add_vm_new_disk').click(function(event){
            event.preventDefault();            
            var vm_add_disk_size = $('#input_VMAddDiskSize').val();
            var vm_add_disk_type = $('#combo_disk_type').scombobox('val'); // 新增磁碟類型  2017.09.04 william
            var vm_add_disk_raidid = $('#combo_raidid_modifyvd').scombobox('val'); // 新增磁碟類型  2017.09.04 william
            if(vm_add_disk_size.length === 0 )
            {
                alert(LanguageStr_VM.VM_NewDisk_Integer_Warning);
                return false;
            }
            if(!reg.test(vm_add_disk_size))
            {
                alert(LanguageStr_VM.VM_NewDisk_Integer_Warning);
                return false;
            }                        
            VMAddDisk(org_name,vm_add_disk_size,vm_add_disk_type, vm_add_disk_raidid); // 新增磁碟類型  2017.09.04 william
        });
    }

    function Init_VMCreateDisk_Dialog_Item() { // 新增磁碟類型  2017.09.04 william    
        $('#combo_disk_type').scombobox({
            editAble:false,
            wrap:false       
        });
        
        $('#combo_disk_type').css({"width": "120px","height": "30px","line-height": "30px","display": "inline-block"});
    }
    
    function InitVMCreateRadio(){
         $('.iCheckRadioBootDisk').iCheck({  
            radioClass: 'iradio_flat-blue'    
        });
        $('.iCheckRadioBootDisk').on('ifChecked', function(event){
            VMChangeCreateDiskModeEnableDisableElement($(this).val());
        });  
    }
    
    function VMChangeCreateDiskModeEnableDisableElement(check_val){           
        switch(check_val){
            case 'manually':                                
                $('#input_VMBootDiskSize').prop('disabled', false);                                               
                $('#combo_img').scombobox('disabled', true); 
                break;
            case 'input-file':                
                $('#combo_img').prop('disabled', false);                                               
                $('#input_VMBootDiskSize').scombobox('disabled', true); 
                break;
        }
    }      
    
    function InitVMCreateCombo(){
        $('.iCheckRadioMemoryAllocateCreate').iCheck({  
            radioClass: 'iradio_flat-blue'    
        });
        $('.iCheckRadioUSB').iCheck({  
            radioClass: 'iradio_flat-blue'    
        });
        $('#combo_cpu_cores').scombobox({
            editAble:false,
            wrap:false       
        });       
        // 新增CPU類型  2017.09.01 william
        $('#combo_cpu_type').scombobox({
            editAble:false,
            wrap:false       
        });        
        $('#combo_memory').scombobox({
            editAble:false,
            wrap:false       
        });
        $('#combo_usb_redirect').scombobox({
            editAble:false,
            wrap:false       
        });
        $('#combo_audio').scombobox({
            editAble:false,
            wrap:false       
        });
        $('#combo_nic').scombobox({
            editAble:false,
            wrap:false       
        });
        $('#combo_vswitch').scombobox({
            editAble:false,
            wrap:false       
        });
        $('#icheckcpulimit').iCheck({  
            checkboxClass: 'icheckbox_minimal-blue'    
        });
        $('#icheckpause').iCheck({  
            checkboxClass: 'icheckbox_minimal-blue'    
        });
        $('#icheckcpulimit').on('ifChanged', function(event){
            var element = $(this);
            if(element.prop('checked')){
                $('#slider-range-cpu-clock').slider( "option", "disabled", false );
                enable_custom_button($('.btn_cpu_clock'));
                
            }
            else{
                $("#slider-range-cpu-clock" ).slider('option','max',cpu_mhz*$('#combo_cpu_cores').scombobox('val'));
                $("#slider-range-cpu-clock" ).slider('option','value',cpu_mhz*$('#combo_cpu_cores').scombobox('val'));
                $('#slider-range-cpu-clock').slider( "option", "disabled", true );
                disable_custom_button($('.btn_cpu_clock'));
            }
        });        
        $( "#slider-range-cpu-clock" ).slider({
            range: "min",
            value: cpu_mhz,
            min: 500,
            step:100,
            max: cpu_mhz,
            change: function( event, ui ) {     
                $( "#now-cpu-clock" ).text( add_comma_of_number(ui.value) + " MHz" );
            },
            slide: function( event, ui ) {     
                $( "#now-cpu-clock" ).text( add_comma_of_number(ui.value) + " MHz" );
            }
        });             
        $("#now-cpu-clock" ).text( add_comma_of_number($("#slider-range-cpu-clock").slider("option","value")) + " MHz" );       
        $('#combo_cpu_cores').scombobox('change',function(event){
        $( "#slider-range-cpu-clock" ).slider('option','max',cpu_mhz*$('#combo_cpu_cores').scombobox('val'));
        $( "#slider-range-cpu-clock" ).slider('option','value',cpu_mhz*$('#combo_cpu_cores').scombobox('val'));
        $("#now-cpu-clock" ).text( add_comma_of_number($("#slider-range-cpu-clock").slider("option","value")) + " MHz" );       
        });
        $('#combo_nic').scombobox('change',function(event){
            event.preventDefault();
            switch($('#combo_nic').scombobox('val')){
                case '1':
                    // $('#nic_warning').hide();
                    break;                          
                case '10':
                    // $('#nic_warning').show();
                    break;
            }        
        });        
        $('.btn_cpu_clock').bind('mousedown', function() {  
            if($(this).hasClass('log_btn'))
            {
                if($(this).hasClass('modify'))
                    repeater($(this).hasClass('add'),$("#slider-modify-range-cpu-clock"));
                else
                    repeater($(this).hasClass('add'),$("#slider-range-cpu-clock"));
            }
        }).bind('mouseup mouseleave', function() {
            clearTimeout(repeat);
        });
        $('#btn_close_create_vm').click(function (event) {
            event.preventDefault();
            $( "#dialog_create_vm" ).dialog('close');                    
        });        
    }
        
    function InitButtonForSuspend(){
        $('#btn_chg_auto_suspend').click(function(event){
            event.preventDefault();
            SetSuspendSetting($('#spinner_suspend').spinner('value')*60);
        });
        $('#btn_clear_chg_auto_suspend').click(function(event){
            event.preventDefault();
            ListSuspendSetting();
        });
    }    
        
    function InitButtonForCreateVM(){
        $('#btn_create_vm_dialog').click(function(event){
            event.preventDefault();            
            VMListIsos();
            ResetValueVMCreateDialog();
            $( "#dialog_create_vm" ).dialog('open');
        });
    }    
    
    function InitButtonForImportVM(){
        $('#btn_import_vm').click(function(event){
            event.preventDefault(); 
            StopPolling();
            ListImportVD(true);
            //$( "#dialog_import_vm" ).dialog('open');
            //$(".btn-vd-import").button();
        });
    }            
    
    function ListImportVD(bopendialog){
        oHtml.blockPage();        
        var request = oRelayAjax.VMListImport();     
        Import_Get_Raid_ID();       
        CallBackListImportVD(request,bopendialog);
    }
     
    function CallBackListImportVD(request,bopendialog){
        request.done(function(msg, statustext, jqxhr) {  
            oHtml.stopPage();
            if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                Logout_Another_Admin_Login();
                return false;
            }       
            else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                window.location.href = 'index.php';
                return false;
            }
            if(msg.length > 0){
                var HtmlStr = oHtml.CreateImportVDsHtml(msg);
                $('#table-import_vm').find("tr:gt(0)").remove();
                $('#table-import_vm').append(HtmlStr);
                $(".btn-vd-import").button();
                $(".btn-vd-import").click(function(event){ 
                    event.preventDefault(); 
                    var import_ask = LanguageStr_VM.VM_Import_Ask.replace("@",VMorVDI);
                    var confirmable = confirm(import_ask);
                    if (confirmable === true){
                        var element = $(this);
                        var org_name = element.attr('id');                        
                        var new_name = $("#import"+org_name).val();                        
                        var raid_id  = $("#combo_raidid_Import" + org_name).scombobox('val');
                        if(new_name.length === 0)
                        {
                            alert(LanguageStr_VM.VM_Name_Empty_Warning);
                            return false;
                        }         
                        if(!VerifyVMInput(new_name))
                        {
                            var name_warning = (LanguageStr_VM.VM_Clone_Name_Warning).replace("&",new_name);
                            alert(name_warning);
                            return false;
                        }         
                        if(reg.test(new_name))
                        {
                            var name_warning_1 = (LanguageStr_VM.VM_Clone_Name_Warning_1).replace("&",new_name);
                            alert(name_warning_1);
                            return false;
                        }
                        // ImportVD(org_name,new_name);
                        importvd_org_name = org_name;
                        importcd_new_name = new_name;
                        import_raid_id    = raid_id;
                        $( "#dialog_import_vm" ).dialog('close');
                        ImportUserListflag = true;
                        temp_username_for_import = '';
                        CreateVM_GetUserList();
                        $( "#dialog_VM_userlist" ).dialog('open');
                    }
                });
                if(bopendialog){                    
                    $( "#dialog_import_vm" ).dialog('open');                    
                }                 

                var html_str = CreateImportRaidIDHtml(AjaxData_for_Import);
                $('.get_import_raidid').append(html_str);
                $('.get_import_raidid').scombobox({
                    editAble:false,
                    wrap:false       
                });  
            }
            else{        
                if(bopendialog){    
                    alert((LanguageStr_VM.VM_Import_No_VD).replace("@",VMorVDI));
                    StartPolling(true,true);
                }
                else{
                    $('#table-import_vm').find("tr:gt(0)").remove();
                }
            }                
        });
        request.fail(function(jqxhr, textStatus) {                  
            oHtml.stopPage();
            oError.CheckAuth(jqxhr.status,ActionStatus.VMListImport);           
        });
    }
     
        function CreateImportRaidIDHtml(AjaxData){
            var HtmlStr = '';
            var i =1; 
            $.each( AjaxData, function(index, element) {
                if(element['Status'] != '' || element['Status'].indexOf("creating") !=-1 || element['Status'].indexOf("deleting") !=-1 || element['Status'].indexOf("adding") != -1) { 
                    HtmlStr += '<option value="'+ element['RAIDID'] + '">'+ element['RAIDID'] + '</option>';                        
                }                   
            });
            return HtmlStr;            
        }; 

    function ImportVD(name,newname, raidid){
        oHtml.blockPage();        
        var request = oRelayAjax.ImportVM(name,newname,raidid);             
        CallBackImportVD(request,newname);
    }
    
    function CallBackImportVD(request,newname){
        request.done(function(msg, statustext, jqxhr) {                         
            if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                Logout_Another_Admin_Login();
                return false;
            }     
            else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                window.location.href = 'index.php';
                return false;
            }
            // if(msg['Result'] === 2){
            //     alert((LanguageStr_VM.VM_Clone_Name_Warning_2).replace("&",newname));
            // }
            // else if(msg['Result'] !== 0){
            //     alert((LanguageStr_VM.VM_Import_Fail).replace("＠",VMorVDI)) + '( Error :' + msg['Result'] + ')';
            // }            
            oUIVMContorl.VMListTask(true);                
            $( "#dialog_import_vm" ).dialog('close');   
            $('#tab-vm-container').easytabs('select', '#tabs1-task-list'); 
            // ListImportVD(false);
        });
        request.fail(function(jqxhr, textStatus) {                  
            oHtml.stopPage();
            $( "#dialog_VM_userlist" ).dialog('close');
            if(jqxhr.status == 409){
                alert((LanguageStr_VM.VM_Clone_Name_Warning_2).replace("&",newname));
            }
            else if (jqxhr.status == 400){
                var str = LanguageStr_VM.VM_Import_Fail;
                var error_msg = str.replace("@",VMorVDI);
                alert(error_msg);
            }
            else{
                oError.CheckAuth(jqxhr.status,ActionStatus.VMImport);  
                oUIVMContorl.VMListTask(true);              
                $( "#dialog_import_vm" ).dialog('close');     
                $('#tab-vm-container').easytabs('select', '#tabs1-task-list'); 
            }
            
            // ListImportVD(false);
        });
    }
    
    function ResetValueVMCreateDialog()
    {
        $('#input_VMName').val('');
        $('#input_VMMemory').val('');
        $('#input_VMBootDiskSize').val('');
        $("#input_VMDescription").val('');
        $("#input_SSLayer").val(30); //  新增快照層數  2017.11.22 william
        $('.label_SSLayer_warning_1').hide();
        $('#radio-manually').iCheck('check');
        $('#radio-fix-create').iCheck('check');
        $('#combo_usb_redirect').scombobox('val',0);
        $('#combo_audio').scombobox('val',1);
        $('#combo_nic').scombobox('val',1);
        // $('#nic_warning').hide();
        $('#combo_cpu_cores').scombobox('val',2);        
        $('#combo_cpu_type').scombobox('val',1); // 新增CPU類型  2017.09.01 william
        $('#slider-range-cpu-clock').slider( "option", "disabled", true );
        disable_custom_button($('.btn_cpu_clock'));
        $('#icheckcpulimit').iCheck('uncheck');
        $("#slider-range-cpu-clock").slider("option","max",cpu_mhz*2);
        $("#slider-range-cpu-clock").slider("option","value",cpu_mhz*2);
        $("#now-cpu-clock" ).text( add_comma_of_number($("#slider-range-cpu-clock").slider("option","value")) + " MHz" );        
        $('#input_VM_User').val('');
        Get_Raid_ID($('#raidid_createvd'), -1, false);
    }    
    
    function InitVMProgressBar(){
        var progressbar_install = $( "#progressbar_install" ),
        progressLabel = $( ".progress-label" ),
        progressbar_backup = $( "#progressbar_backup" ),
        progressbar_recovery = $('#progressbar_recovery');

        progressbar_install.progressbar({
          value: 0
        });

        progressbar_backup.progressbar({
          value: 0
        });

        progressbar_recovery.progressbar({
          value: 0
        });

        progressLabel.text('');
    }

    function InitVMButton(){
        $('#btn_vm_install').click(function(event){
            event.preventDefault();
            var InstallAddr = $('#VMInstallAddr').val();
            if(InstallAddr.length === 0)
            {
                alert('Please input value in Install Address.');
                return false;
            }
            var confirmable = confirm("Do you want to execute VDI Installation now?");
            if (confirmable === true) {
                VMInstall(InstallAddr);
            }
        });
        $('#btn_vm_recovery').click(function(event) {
            event.preventDefault();
            var confirmable = confirm("Do you want to execute VDI Restore now?");
            if (confirmable === true) {
                VMRecovery();
            }
        });
        $('#btn_vm_backup').click(function(event) {
            event.preventDefault();
            var confirmable = confirm("Do you want to execute VDI Backup now?");
            if (confirmable === true) {
                VMBackup();
            }
        });
    }

    function InitVMTab(){
        $('#tab-vm-container').easytabs();
    };                         

    // 新增CPU類型  2017.09.01, 新增快照層數  2017.11.22 william
    function VMCreate(username, name,cpu_cores,cpu_type,memory,boot_disk_size,boot_disk_path,boot_disk_type,iso_path,description,init_memory,usb_type,usb_redirect_count,audio,nic_speed,cpu_clock,auto_suspend,vswitch,sslayer,raid_id){ 
        StopPolling();
        oHtml.blockPage();        
        var request = oRelayAjax.VMCreate(username, name,cpu_cores,cpu_type,memory,boot_disk_size,boot_disk_path,boot_disk_type,iso_path,description,init_memory,usb_type,usb_redirect_count,audio,nic_speed,cpu_clock,auto_suspend,vswitch,sslayer,raid_id);  
        CallBackVMCreate(request);
    };

    function CallBackVMCreate(request){
        request.done(function(msg, statustext, jqxhr) {                 
            if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                Logout_Another_Admin_Login();
                return false;
            }
            else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                window.location.href = 'index.php';
                return false;
            }          
            $( "#dialog_create_vm" ).dialog('close');
            oUIVMContorl.VMList(true,true);      
            //StartPolling(false,true);
            oUIVMContorl.VMListTask(true);    
            StartPolling(true,true);
            $('#tab-vm-container').easytabs('select', '#tabs1-task-list');
        });
        request.fail(function(jqxhr, textStatus) {                  
            oHtml.stopPage();
            if(jqxhr.status == 400){
                var fail_msg = (LanguageStr_VM.VM_Create_Fail).replace("@",VMorVDI);
                alert(fail_msg);
            }
            else if(jqxhr.status == 409){
                var fail_msg = (LanguageStr_VM.VM_Name_Duplicate).replace("@",VMorVDI);
                alert(fail_msg);
            }
            else{
                oError.CheckAuth(jqxhr.status,ActionStatus.VMCreate);
            }
            StartPolling(true,true);
        });
    }
    
    function VMModifyISO(name,path){
        StopPolling();
        oHtml.blockPage();
        var request = oRelayAjax.ModifyVMISO(vmid,path);  
        CallBackVMModifyISO(request,name);
    }
    
    function CallBackVMModifyISO(request,name){
        request.done(function(msg, statustext, jqxhr) {                 
            if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                Logout_Another_Admin_Login();
                return false;
            }   
            else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                window.location.href = 'index.php';
                return false;
            }
            setTimeout(function(){VMInfo_Boot(vmid);},100);                                         
        });
        request.fail(function(jqxhr, textStatus) {
            if(jqxhr.status == 400){
                setTimeout(function(){VMInfo_Boot(vmid);},100);     
            }   
            else{               
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.VMModifyInfo);   
                //StartPolling(true,true);
            }
        });
    }
    
     // 新增CPU類型  2017.09.01 william
    function VMModifyInfo(name,cpu_cores,cpu_type,memory,iso_path,desc,usb_type,usb_redirect_count,audio,cpu_clock,auto_suspend){
        StopPolling();
        oHtml.blockPage();
        // var bol_change_name = 0;
        // if(org_name !== name)
        //     bol_change_name = 1;        
        var request = oRelayAjax.ModifyVMInfo(vmid,org_name,name,cpu_cores,cpu_type,memory,iso_path,desc,usb_type,usb_redirect_count,audio,cpu_clock,auto_suspend);  
        CallBackVMModifyInfo(request,name);
    }
    
    function CallBackVMModifyInfo(request,name){
        request.done(function(msg, statustext, jqxhr) {                 
            if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                Logout_Another_Admin_Login();
                return false;
            }
            else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                window.location.href = 'index.php';
                return false;
            }
//             if(msg['result'] === -99){                
//                 var name_warning_2 = (LanguageStr_VM.VM_Clone_Name_Warning_2).replace("&",name);                    
//                 alert(name_warning_2);
//                 $('#input_Mofify_VMName').val(org_name);
//                 setTimeout(function(){VMInfo(org_name,true,false);},1000);
//                 return false;
//             }
// //            oUIVMContorl.VMList(true,false);
// //            StartPolling(true,true);
//             if(msg['result'] === -1){                                          
//                 alert(LanguageStr_VM.VM_Modify_Name_Fail);  
//                 $('#input_Mofify_VMName').val(org_name);
//                 setTimeout(function(){VMInfo(org_name,true,false);},1000);            
//             }
//             else{
                if(name !== org_name)
                    oUIVMContorl.VMList(true,false);       
                setTimeout(function(){VMInfo(name,true,false);},1000);                   
            // }            
        });
        request.fail(function(jqxhr, textStatus) {    
            oHtml.stopPage();
            if(jqxhr.status == 400){
                alert(LanguageStr_VM.VM_Modify_Name_Fail);  
                $('#input_Mofify_VMName').val(org_name);
                setTimeout(function(){VMInfo(org_name,true,false);},1000); 
            }
            else if(jqxhr.status == 409){
                var name_warning_2 = (LanguageStr_VM.VM_Clone_Name_Warning_2).replace("&",name);                    
                alert(name_warning_2);
                $('#input_Mofify_VMName').val(org_name);
                setTimeout(function(){VMInfo(org_name,true,false);},1000);                
            }
            else
                oError.CheckAuth(jqxhr.status,ActionStatus.VMModifyInfo);   
            //StartPolling(true,true);
        });
    }
    
    function VMModifySeed(name,desc){
        StopPolling();
        oHtml.blockPage();
        var request = oRelayAjax.ModifyVMSeed(vmid,name,desc);  
        CallBackVMModifySeed(request,name);
    }
    
    function CallBackVMModifySeed(request,name){
        request.done(function(msg, statustext, jqxhr) {                 
            if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                Logout_Another_Admin_Login();
                return false;
            }
            else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                window.location.href = 'index.php';
                return false;
            }
                       
            setTimeout(function(){VMSeedModifyInfo(name);},1000);  
            if(name !== org_name)
                oUIVMContorl.VMList(true,false);       
            // }
           // StartPolling(true,true);
            
        });
        request.fail(function(jqxhr, textStatus) {  
            oHtml.stopPage();
            if(jqxhr.status == 409){                
                var name_warning_2 = (LanguageStr_VM.VM_Clone_Name_Warning_2).replace("&",name);                    
                alert(name_warning_2);
                $('#label_Mofify_Seed_Name').val(org_name);
                setTimeout(function(){VMSeedModifyInfo(org_name);},1000);
                return false;
            }   
            else if(jqxhr.status == 400){
                alert(LanguageStr_VM.VM_Modify_Name_Fail);                
                $('#label_Mofify_Seed_Name').val(org_name);
                setTimeout(function(){VMSeedModifyInfo(org_name);},1000);   
            }           
            else
                oError.CheckAuth(jqxhr.status,ActionStatus.VMModifyInfo); 
            StartPolling(true,true);
        });
    }
    
    function VMBackup()
    {
        oHtml.blockPage();
        EnableDisableVMControl(false);
        var request = oRelayAjax.BackupVM();             
        CallBackVMBackup(request);
    }

    function CallBackVMBackup(request){
        request.done(function(msg, statustext, jqxhr) {          
            oHtml.stopPage();
            if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                Logout_Another_Admin_Login();
                return false;
            }
            else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                window.location.href = 'index.php';
                return false;
            }
            setTimeout( function(){VMStatus();}, 1000 );                  
        });
        request.fail(function(jqxhr, textStatus) {   
            oHtml.stopPage();
            oError.CheckAuth(jqxhr.status,ActionStatus.VMBackup);
        });
    };

    function VMInstall(InstallAddr)
    {
        oHtml.blockPage();
        EnableDisableVMControl(false);
        var request = oRelayAjax.InstallVM(InstallAddr);             
        CallBackVMInstall(request);
    }

    function CallBackVMInstall(request){
        request.done(function(msg, statustext, jqxhr) {          
            oHtml.stopPage();
            if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                Logout_Another_Admin_Login();
                return false;
            }
            else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                window.location.href = 'index.php';
                return false;
            }
            setTimeout( function(){VMStatus();}, 1000 );
        });
        request.fail(function(jqxhr, textStatus) {   
            oHtml.stopPage();
            oError.CheckAuth(jqxhr.status,ActionStatus.VMInstall);
        });
    };

    function VMRecovery()
    {
        StopPolling();
        oHtml.blockPage();
        EnableDisableVMControl(false);
        var request = oRelayAjax.RecoveryVM();  
        request.done(function(msg, statustext, jqxhr) {          
            oHtml.stopPage();
            if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                Logout_Another_Admin_Login();
                return false;
            }
            else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                window.location.href = 'index.php';
                return false;
            }
            NowSortTaskType = null;
            oUIVMContorl.VMListTask(true);    
            StartPolling(true,false);
            $('#tab-vm-container').easytabs('select', '#tabs1-task-list');
        });
        request.fail(function(jqxhr, textStatus) {   
            oHtml.stopPage();
            oError.CheckAuth(jqxhr.status,ActionStatus.VMRecovery);
        });
    }

    function VMStatus()
    {
        var request = oRelayAjax.VMStatus();  
        CallBackVMStatus(request);
    }

    function CallBackVMStatus(request){
        request.done(function(msg, statustext, jqxhr) {                 
            oHtml.stopPage();
            if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                Logout_Another_Admin_Login();
                return false;
            }
            else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                window.location.href = 'index.php';
                return false;
            }
            switch(msg['type']){
                case 0:
                    ClearVMStatusInteval(); 
                    ResetAllProgress();
                    EnableDisableVMControl(true);
                    break;
                case 1:
                    ProcessVMBackupStatus(msg);
                    break;
                case 2:
                    ProcessVMRecoveryStatus(msg);
                    break;
                case 3:                        
                    ProcessVMInstallStatus(msg);
                    break;
            }

        });
        request.fail(function(jqxhr, textStatus) {  
            if(CurrentVMWorkType !== -1){
                ClearVMStatusInteval();
                return;
            }
            oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.VMStatus);                
        });
    }

    function ProcessVMInstallStatus(AjaxData)
    {            
        switch(AjaxData['jobst']){
            case -6:
                EnableDisableVMControl(true);
                ClearVMStatusInteval();
                ResetAllProgress();
                alert("Fail to VDI Installation, because VDI System has been rebooted.");
                return;
            case -1:
            case -3:
            case -4:
            case -5:
                EnableDisableVMControl(true);
                ClearVMStatusInteval();
                ResetAllProgress();
                alert("Installation Address can't get install image(Error Code :" + AjaxData['jobst'] +").");
                return;
            case 0:
            case 1:                    
                if(!VMIntervalStart){
                    EnableDisableVMControl(false);
                    $("#VMInstallAddr").val(AjaxData['url']);
                    GetVMStatusInterval = setInterval(function(){VMStatus();}, 8000);
                    VMIntervalStart = true;
                    CurrentVMWorkType=3;
                }
                $("#div_progress_vm_install").show();
                $("#progressbar_install").progressbar( "option", "value", AjaxData['process'] );
                $("#label_install_progress").text( AjaxData['process'] + '%' );
                break;
            case 2:                    
                ClearVMStatusInteval();
                EnableDisableVMControl(true);                    
                ResetAllProgress();
                $('#dialog_vm_msg_reboot').html('<h3>VM Installation has completed successfully. VDI System is rebooting.</h3><h3>Please wait several minutes then click Refresh Button(ex. "F5") to continue.</h3>');
                $( "#dialog_vm_message_reboot" ).dialog('open');
                break;  
            case 3:                    
                ClearVMStatusInteval();
                EnableDisableVMControl(true);                    
                ResetAllProgress();
                $('#dialog_vm_msg').html('<h3>VM Installation has completed successfully.</h3>');
                $( "#dialog_vm_message" ).dialog('open');
                break;
        }           
    }

    function ProcessVMBackupStatus(AjaxData)
    {            
        switch(AjaxData['jobst']){
            case -6:
                EnableDisableVMControl(true);
                ClearVMStatusInteval();
                ResetAllProgress();
                alert("Fail to VM Backup, because VDI System has been rebooted.");
                return;
            case -1:
                ClearVMStatusInteval();
                EnableDisableVMControl(true);                    
                ResetAllProgress();
                alert("VM Backup's source is not found.");
                return;
            case -2:           
                ClearVMStatusInteval();
                EnableDisableVMControl(true);                    
                ResetAllProgress();
                alert("VM Backup is failure.");
                return;
            case 0:
            case 1:                    
                if(!VMIntervalStart){
                    EnableDisableVMControl(false);
                    $("#VMInstallAddr").val('');
                    GetVMStatusInterval = setInterval(function(){VMStatus();}, 8000);
                    VMIntervalStart = true;
                    CurrentVMWorkType=1;
                }
                $("#div_progress_vm_backup").show();
                $("#progressbar_backup").progressbar( "option", "value", AjaxData['process'] );
                $("#label_backup_progress").text( AjaxData['process'] + '%' );
                break;
            case 2:              
                ClearVMStatusInteval();
                EnableDisableVMControl(true);                    
                ResetAllProgress();
                $('#dialog_vm_msg_reboot').html('<h3>VM Backup has completed successfully. VDI System is rebooting.</h3><h3>Please wait several minutes then click Refresh Button(ex. "F5") to continue.</h3>');
                $( "#dialog_vm_message_reboot" ).dialog('open');
                break;  
            case 3:                    
                ClearVMStatusInteval();
                EnableDisableVMControl(true);                    
                ResetAllProgress();
                $('#dialog_vm_msg').html('<h3>VM Backup has completed successfully.</h3>');
                $( "#dialog_vm_message" ).dialog('open');
                break;
        }           
    }

    function ProcessVMRecoveryStatus(AjaxData)
    {            
        switch(AjaxData['jobst']){
            case -6:
                ClearVMStatusInteval();
                EnableDisableVMControl(true);                    
                ResetAllProgress();
                alert("Fail to VM Recovery, because VDI System has been rebooted.");
                return;
            case -1:
                ClearVMStatusInteval();
                EnableDisableVMControl(true);                    
                ResetAllProgress();
                alert("VM Recovery's source is not found.");
                return;
            case -2:                
                ClearVMStatusInteval();
                EnableDisableVMControl(true);                    
                ResetAllProgress();
                alert("VM Recovery is failure.");
                return;
            case 0:
            case 1:                    
                if(!VMIntervalStart){
                    EnableDisableVMControl(false);
                    $("#VMInstallAddr").val('');
                    GetVMStatusInterval = setInterval(function(){VMStatus();}, 8000);
                    VMIntervalStart = true;
                    CurrentVMWorkType = 2;
                }
                $("#div_progress_vm_recovery").show();
                $("#progressbar_recovery").progressbar( "option", "value", AjaxData['process'] );
                $("#label_recovery_progress").html( AjaxData['process'] + '%' );
                break;
            case 2:                    
                ClearVMStatusInteval();
                EnableDisableVMControl(true);                    
                ResetAllProgress();
                $('#dialog_vm_msg_reboot').html('<h3>VM Restore has completed successfully. VDI System is rebooting.</h3><h3>Please wait several minutes then click Refresh Button(ex. "F5") to continue.</h3>');
                $( "#dialog_vm_message_reboot" ).dialog('open');
                break;   
            case 3:             
                ClearVMStatusInteval();
                EnableDisableVMControl(true);                    
                ResetAllProgress();
                $('#dialog_vm_msg').html('<h3>VM Restore has completed successfully.</h3>');
                $( "#dialog_vm_message" ).dialog('open');
                break;
        }           
    }                

    function ClearVMStatusInteval()
    {
        if(typeof(GetVMStatusInterval) !== 'undefined')
            clearInterval(GetVMStatusInterval);
        VMIntervalStart = false;
        CurrentVMWorkType = -1;
        CurrentVMWorkRebootCount = 0;
    }
        
        function ResetAllProgress()
        {
            $("#div_progress_vm_install").hide();
            $("#div_progress_vm_backup").hide();
            $("#div_progress_vm_recovery").hide();
            $("#progressbar_install" ).progressbar( "option", "value", 0 );
            $("#label_install_progress" ).text( '' );
            $("#progressbar_backup" ).progressbar( "option", "value", 0 );
            $("#label_backup_progress" ).text( '' );
            $("#progressbar_recovery" ).progressbar( "option", "value", 0 );
            $("#label_recovery_progress" ).text( '' );
        }
        
        function EnableDisableVMControl(isEnable)
        {
            $("#btn_vm_install").button( "option", "disabled", !isEnable );
            $("#btn_vm_backup").button( "option", "disabled", !isEnable );
            $("#btn_vm_recovery").button( "option", "disabled", !isEnable );
            $("#VMInstallAddr").prop("disabled", !isEnable);
        }                      
        
        oUIVMContorl.List = function(){
            VMListStart = true;
            VMTaskStart=true;
            SnapshotTaskStart = true; // 快照任務清單 新增內容 2017.01.25 
            SnapshotListStart = true; // SnapShot list 實作 2017.02.09 william
            oUIVMContorl.VMList(true,true);
            oUIVMContorl.VMListTask(true);
            oUIVMContorl.VMListSnapshotTask(true); // 快照任務清單 新增內容 2017.01.25 
            ListSuspendSetting();
        };
        
        oUIVMContorl.ListTest = function(){            
            oUIVMContorl.VMList(true,false);            
        };

        oUIVMContorl.VMList = function(isCreateNewTable,bolStartPolling){            
            oHtml.blockPage();        
            // var request = oRelayAjax.VMList(); // 桌面設定-新增VD檔案大小實作 2017.05.03 william      
            var request = oRelayAjax.VMSizeList();
            CallBackVMList(request,isCreateNewTable,bolStartPolling);
        };
        
        function CallBackVMList(request,isCreateNewTable,bolStartPolling){
            request.done(function(msg, statustext, jqxhr) {
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }      
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                $('#btn_create_vm_dialog').button("option", "disabled", false );
                $('#btn_import_vm').button("option", "disabled", false );
                $('#btn_chg_auto_suspend').button( "option", "disabled", false );
                $('btn_clear_chg_auto_suspend').button( "option", "disabled", false );
                if(isCreateNewTable){                                       
                    VMListNew(msg);             
                    setTimeout( function(){oHtml.stopPage(); },500);                        
                }  
                else{               
                    VMListOnlyModify(msg);
                    oHtml.stopPage(); 
                }
                Modify_Count_Status();
                if(bolStartPolling){
                    PollingListFlag = true;
                    PollingVMList(true);
                }
            });
            request.fail(function(jqxhr, textStatus) {  
                if(jqxhr.status == 500){
                    oHtml.stopPage();
                    $('#btn_create_vm_dialog').button( "option", "disabled", true );
                    $('#btn_import_vm').button( "option", "disabled", true );
                    $('#btn_chg_auto_suspend').button( "option", "disabled", true );
                    $('btn_clear_chg_auto_suspend').button( "option", "disabled", true );
                    $('#Div_VMList').html('');
                    $('#Div_SeedList').html('');
                    $('#Div_Borned_List').html('');
                    $('#Div_VM_Clone_List').html('');
                    alert(LanguageStr_langSysCfgUserMgt.CreateRAID_alert);
                }
                if(bolStartPolling){
                    PollingListFlag = true;
                    PollingVMList(true);
                }
            });
        }                                       
        
        function CheckForPollingList(){ 
            if(!VMListStart){
                isRunningPollingList = false;
                return false;
            }
            if(PollingListFlag){
                setTimeout(function(){PollingVMList(false);},8000); // 避免頻繁的Request,將5秒改成8秒 2017.03.10 william
            }
            else{
                setTimeout(function(){CheckForPollingList();},1000);
            }
        }
                   
        function PollingVMList(checkRunning){
            if(checkRunning && isRunningPollingList)
                return false;
            isRunningPollingList = true;
//            console.log("Send Polling List"); 
            var request = oRelayAjax.VMList(); // 桌面設定-新增VD檔案大小實作 2017.05.03 william
            // var request = oRelayAjax.VMSizeList();
            CallBackPollingVMList(request);
        }
        
        function CallBackPollingVMList(request){
            request.done(function(msg, statustext, jqxhr) {    
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                        Logout_Another_Admin_Login();
                        return false;
                    } 
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                try{
                    var total_length = VMListTable.length+SeedListTable.length+BornedListTable.length;
                    var now_length = msg['Org'].length + msg['Seed'].length + msg['User'].length;
                    if(now_length != total_length){
                        // VMListNew(msg);
                        isRunningPollingList = false;
                        oUIVMContorl.VMList(true,true);
                    }            
                    else{
                        VMListOnlyModify(msg);
                        Modify_Count_Status();
                        CheckForPollingList();    
                    }                         
                }
                catch(e){
                    CheckForPollingList();
                }
            });
            request.fail(function(jqxhr, textStatus) {     
                CheckForPollingList();
            });
        }
        
        function Modify_Count_Status(){
            $("#vd_total_count").text(CountTotalVM);
            $("#vd_poweron_count").text(CountPoweronVM);
            $("#vd_online_count").text(CountOnlineVM);
            $("#vd_suspend_count").text(CountSuspendVM);
        }
        
        function ChangeVMNametoID(name)
        {           
            if (typeof(VMListName[name])!== 'undefined'){
                vmid = VMListName[name]['vdid'];
                // alert('Org:' + vmid);
                return;
            }
            if (typeof(SeedListName[name])!== 'undefined')   
            {
                vmid = SeedListName[name]['vdid'];
                // alert('Seed:' + vmid);
                return;
            }
            if (typeof(BornedListName[name])!== 'undefined')   
            {
                vmid = BornedListName[name]['vdid'];
                // alert('Borned:' + vmid);
                return;
            }
        }        

        function GetSoundDisplay(soundType){
            var sounds = {1:"ich6", 2:"ac97"};
            return sounds[soundType];
        }

        function VMListNew(msg){
            oHtml.CreateVMListTable(msg);
            if(typeof(NowSortType)=== 'undefined' || NowSortSeedType === null)
            {                            
                $('.label_vm_arrow').html('');
                $('#vmname1').html('&#9650;');
                var th = $('.VMList_Name');
                th.removeClass('desc');
                th.addClass('asc');
                NowSortDirection = SortDirection.ASC;
                NowSortType = SortType.String;
                NowSortDOMID = 'name';
            }
            if(typeof(NowSortSeedType)=== 'undefined' || NowSortSeedType === null)
            {
                $('.label_seed_arrow').html('');
                $('#seedname1').html('&#9650;');
                var th = $('.SeedList_Name');
                th.removeClass('desc');
                th.addClass('asc');
                NowSortSeedDirection = SortDirection.ASC;
                NowSortSeedType = SortType.String;
                NowSortSeedDOMID = 'name';
            }      
            if(typeof(NowSortBornedType)=== 'undefined'  || NowSortBornedType === null)
            {
                $('.label_borned_arrow').html('');
                $('#bornedname1').html('&#9650;');
                var th = $('.BornedList_Name');
                th.removeClass('desc');
                th.addClass('asc');
                NowSortBornedDirection = SortDirection.ASC;
                NowSortBornedType = SortType.String;
                NowSortBornedDOMID = 'name';
            }   
            SortList(NowSortDirection,NowSortType,NowSortDOMID,VMListTable); 
            SortList(NowSortSeedDirection,NowSortSeedType,NowSortSeedDOMID,SeedListTable);
            SortList(NowSortBornedDirection,NowSortBornedType,NowSortBornedDOMID,BornedListTable);
            $('#Div_VMList').html(oHtml.CreateVMListHtmlByTable());
            //  新增原生桌面的備註tooltip顯示 2016.12.20 by william
            $('#Div_VMList').tooltip({
                position: { my: "left top+0", at: "left bottom", collision: "flipfit" }
            });
            $('#Div_SeedList').html(oHtml.CreateSeedListHtmlByTable());  
            //  新增種子桌面的備註tooltip顯示 2016.12.20 by william
            $('#Div_SeedList').tooltip({
                position: { my: "left top+0", at: "left bottom", collision: "flipfit" }
            });
            $('#Div_Borned_List').html(oHtml.CreateBornedListHtmlByTable());
            //  新增用戶桌面的備註tooltip顯示 2016.12.19 by william
            $('#Div_Borned_List').tooltip({
                position: { my: "left top+0", at: "left bottom", collision: "flipfit" }
            });

            $('.iCheckBoxAllSelect').iCheck('uncheck');
            $('.iCheckBoxSeedAllSelect').iCheck('uncheck');
            $('.iCheckBoxBornedAllSelect').iCheck('uncheck');
            /*  未來新增之項目 2016.12.23 william
            $('.iCheckBoxTaskAllSelect').iCheck('uncheck'); //2016.12.23 william

            $('.iCheckBoxTaskSelect').iCheck({  //2016.12.23 william
                checkboxClass: 'icheckbox_minimal-blue'    
            });
            */
            $('.iCheckBoxVMSelect').iCheck({  
                checkboxClass: 'icheckbox_minimal-blue'    
            });                         
            $('.iCheckBoxSeedSelect').iCheck({  
                checkboxClass: 'icheckbox_minimal-blue'    
            });
            $('.iCheckBoxBornedSelect').iCheck({  
                checkboxClass: 'icheckbox_minimal-blue'    
            }); 
            $('.iCheckBoxSuspend').iCheck({  
                checkboxClass: 'icheckbox_minimal-blue'    
            });  
            
            $('.iCheckBoxDisable').iCheck('disable');
            EnableDisableSelectSeedActionButton();
            EnableDisableSelectVMActionButton();
            EnableDisableSelectBornedActionButton();
            $('.iCheckBoxVMSuspend').on('ifClicked', function(event){                            
                var element = $(this);
                ActionClickSuspendCheck(element);
            });
            $('.iCheckBoxBornedSuspend').on('ifClicked', function(event){                            
                var element = $(this);
                ActionClickSuspendCheck(element);
            });
            $('.iCheckBoxVMSelect').on('ifChanged', function(event){                            
                EnableDisableSelectVMActionButton();
            });
            $('.iCheckBoxSeedSelect').on('ifChanged', function(event){                            
                EnableDisableSelectSeedActionButton();
            });
            $('.iCheckBoxBornedSelect').on('ifChanged', function(event){                            
                EnableDisableSelectBornedActionButton();
            });
            $('.btn-operation-vm').click(function(event){                               
                event.preventDefault();
                var btn_element = $(this);
                vmname = btn_element.data('operation');
                ChangeVMNametoID(vmname);
            });     
        }                
        
        function ActionClickSuspendCheck(element){            
            var sets = [];
            var vd_name = element.attr('id').slice(9);
            var ischeck = !element.prop('checked');             
            ChangeVMNametoID(vd_name);                       
            sets.push({VDID:vmid,Suspend:ischeck});
            VMModifyAutoSuspend(sets,true);
        }
        
        function VMListOnlyModify(msg){
            function calc_count(element){
                CountTotalVM++;
                if(element['Online'] === true)
                    CountOnlineVM++;
                if(element['State'] === 1)
                    CountPoweronVM++;
                if(element['State'] === 3)
                    CountSuspendVM++;
            }
            CountTotalVM=0;
            CountPoweronVM=0;
            CountOnlineVM=0;
            CountSuspendVM=0;
            $.each(msg['Org'],function(vmindex,vmelement){     
                calc_count(vmelement)
                if($('#VMRow' + vmelement['VDName']) !== null){
                    var row_css_id_type = '';
                    // if(vmelement['seed'] === 0){                                
                    //     if(vmelement['seed_src'].length > 0){
                    //         BornedListName[vmelement['name']]['suspend'] = vmelement['Suspend'];
                    //         BornedListName[vmelement['name']]['desc'] = vmelement['Desc'];
                    //         row_css_id_type = 'Borned';                        
                    //     }
                    //     else{
                    VMListName[vmelement['VDName']]['suspend'] = vmelement['Suspend'];
                    VMListName[vmelement['VDName']]['desc'] = vmelement['Desc'];
                    row_css_id_type = 'VM';
                    //     }
                    // }
                    // else
                    //     SeedListName[vmelement['name']]['desc'] = vmelement['Desc'];
                    // if(vmelement['seed'] === 0)
                    $('#VMRow' + vmelement['VDName'] + ' .' + row_css_id_type + 'List_Desc_Row').html(vmelement['Desc']);
                        // else
                        //     $('#VMRow' + vmelement['name'] + ' .SeedList_Description_Row').html(vmelement['desc']);
                    // if(vmelement['seed'] === 0){
                        // if((vmelement['seed_src'].length > 0 && BornedListName[vmelement['name']]['online'] !== vmelement['Online']) || (vmelement['seed_src'].length === 0 && VMListName[vmelement['name']]['online'] !== vmelement['Online']))
                    if(VMListName[vmelement['VDName']]['online'] !== vmelement['Online'])
                    {                      
                        VMListName[vmelement['VDName']]['online'] = vmelement['Online'];      
                        $('#VMRow' + vmelement['VDName'] + ' .' + row_css_id_type + 'List_Online_Row').html(oHtml.GetVMOnlineStatus(vmelement['Online']));
                    }
                    var newram = vmelement['RAM']/1024;
                    newram = new Number(newram);
                    newram = newram.toFixed(2);
                    if(VMListName[vmelement['VDName']]['ram'] !== newram)
                    {
                        VMListName[vmelement['VDName']]['ram'] = newram;                              
                        $('#VMRow' + vmelement['VDName'] + ' .' + row_css_id_type + 'List_RAM_Row').html(newram);
                    }
                    // }
                    // if(vmelement['seed'] === 0 && typeof(vmelement['state']) !== 'undefined' && $('#VMRow' + vmelement['name']).data('status') !== vmelement['State']){
                    if(typeof(vmelement['State']) !== 'undefined' && $('#VMRow' + vmelement['VDName']).data('status') !== vmelement['State']){
                        row_css_id_type = '';
                        var checkbox_item  = '';
                        // if(vmelement['seed_src'].length > 0){
                        //     checkbox_item = '#' + vmelement['name'] + '.iCheckBoxBornedSelect';
                        // }                                    
                        // else{
                        checkbox_item = '#' + vmelement['VDName'] + '.iCheckBoxVMSelect';
                        // }
                        if(vmelement['State'] === -2){
                            $(checkbox_item).addClass('iCheckBoxDisable');
                            $(checkbox_item).iCheck('disable');
                        }
                        else{
                            $(checkbox_item).removeClass('iCheckBoxDisable');
                            $(checkbox_item).iCheck('enable');
                        }
                        // if(vmelement['seed_src'].length > 0){
                        //     BornedListName[vmelement['name']]['state'] = vmelement['State'];
                        //     BornedListName[vmelement['name']]['state_display'] = oHtml.GetVMStatus(vmelement['state'],vmelement['online']); 
                        //     row_css_id_type = 'Borned';
                        // }
                        // else{
                        VMListName[vmelement['VDName']]['state'] = vmelement['State'];
                        VMListName[vmelement['VDName']]['state_display'] = oHtml.GetVMStatus(vmelement['State'],vmelement['Online']); 
                        row_css_id_type = 'VM';
                        // }
                        $('#VMRow' + vmelement['VDName']).data('status',vmelement['State']);
                        $('#VMRow' + vmelement['VDName'] + ' .' + row_css_id_type + 'List_Status_Row').html(oHtml.GetVMStatus(vmelement['State'],vmelement['Online']));                                
                        // var tmphtml =oHtml.CreateVMDropDownHtml(vmelement['VDName'],vmelement['State'],vmelement['seed'],vmelement['seed_src']);               
                        var tmphtml =oHtml.CreateVMDropDownHtml(vmelement['VDName'],vmelement['State'],0,'');                                                 
                        $('#' + row_css_id_type + 'ActionRow' + vmelement['VDName']).html(tmphtml);                                
                        $('#' + row_css_id_type + 'ActionRow' + vmelement['VDName'] + ' .btn-operation-vm').click(function(event){
                            event.preventDefault();
                            var btn_element = $(this);
                            vmname = btn_element.data('operation');
                            ChangeVMNametoID(vmname);
                        });                            
                    }
                }
            });
            $.each(msg['Seed'],function(vmindex,vmelement){   
                calc_count(vmelement)
                if($('#VMRow' + vmelement['VDName']) !== null){                  
                    SeedListName[vmelement['VDName']]['desc'] = vmelement['Desc'];
                    SeedListName[vmelement['VDName']]['state'] = vmelement['State'];
                    row_css_id_type = 'VM';
                    $('#VMRow' + vmelement['VDName'] + ' .SeedList_Description_Row').html(vmelement['Desc']); 
                    var stateDisplay = oHtml.GetVMStatus(vmelement['State'],vmelement['Online']);
                    $('#VMRow' + vmelement['VDName'] + ' .' + 'SeedList_Status_Row').html(stateDisplay);         
                    var tmphtml =oHtml.CreateSeedDropDownHtml(vmelement['VDName'],vmelement['State']);                                            
                    $('#' + row_css_id_type + 'ActionRow' + vmelement['VDName']).html(tmphtml);                                
                    $('#' + row_css_id_type + 'ActionRow' + vmelement['VDName'] + ' .btn-operation-vm').click(function(event){
                        event.preventDefault();
                        var btn_element = $(this);
                        vmname = btn_element.data('operation');
                        ChangeVMNametoID(vmname);
                    });                                                             
                }
            });
            $.each(msg['User'],function(vmindex,vmelement){     
                calc_count(vmelement)
                if($('#VMRow' + vmelement['VDName']) !== null){
                    var row_css_id_type = 'Borned';                   
                    BornedListName[vmelement['VDName']]['suspend'] = vmelement['Suspend'];
                    BornedListName[vmelement['VDName']]['desc'] = vmelement['Desc'];
                            row_css_id_type = 'Borned';                    
                    $('#VMRow' + vmelement['VDName'] + ' .' + row_css_id_type + 'List_Desc_Row').html(vmelement['Desc']);
                    if(BornedListName[vmelement['VDName']]['online'] !== vmelement['Online']){                        
                        BornedListName[vmelement['VDName']]['online'] = vmelement['Online'];
                        $('#VMRow' + vmelement['VDName'] + ' .' + row_css_id_type + 'List_Online_Row').html(oHtml.GetVMOnlineStatus(vmelement['Online']));
                    }    
                    var newram = vmelement['RAM']/1024;
                    newram = new Number(newram);
                    newram = newram.toFixed(2);
                    if(BornedListName[vmelement['VDName']]['ram'] !== newram){
                        BornedListName[vmelement['VDName']]['ram'] = newram;
                        $('#VMRow' + vmelement['VDName'] + ' .' + row_css_id_type + 'List_RAM_Row').html(newram);
                    }               
                    if(typeof(vmelement['State']) !== 'undefined' && $('#VMRow' + vmelement['VDName']).data('status') !== vmelement['State']){
                        row_css_id_type = '';
                        var checkbox_item  = '';                        
                        checkbox_item = '#' + vmelement['VDName'] + '.iCheckBoxBornedSelect';
                        if(vmelement['State'] === -2){
                            $(checkbox_item).addClass('iCheckBoxDisable');
                            $(checkbox_item).iCheck('disable');
                        }
                        else{
                            $(checkbox_item).removeClass('iCheckBoxDisable');
                            $(checkbox_item).iCheck('enable');
                        }                        
                        BornedListName[vmelement['VDName']]['state'] = vmelement['State'];
                        BornedListName[vmelement['VDName']]['state_display'] = oHtml.GetVMStatus(vmelement['State'],vmelement['Online']); 
                        row_css_id_type = 'Borned';                        
                        $('#VMRow' + vmelement['VDName']).data('status',vmelement['State']);
                        $('#VMRow' + vmelement['VDName'] + ' .' + row_css_id_type + 'List_Status_Row').html(oHtml.GetVMStatus(vmelement['State'],vmelement['Online']));                         
                        var tmphtml =oHtml.CreateVMDropDownHtml(vmelement['VDName'],vmelement['State'],0,vmelement['SeedName']);
                        $('#' + row_css_id_type + 'ActionRow' + vmelement['VDName']).html(tmphtml);                                
                        $('#' + row_css_id_type + 'ActionRow' + vmelement['VDName'] + ' .btn-operation-vm').click(function(event){
                            event.preventDefault();
                            var btn_element = $(this);
                            vmname = btn_element.data('operation');
                            ChangeVMNametoID(vmname);
                        });                            
                    }
                }
            });
        }
        
        function VMHederClickSort(){
            $('.VMHeader').click(function(event){
                    StopPolling();
                    event.preventDefault();
                    var th = $(this);
                    var eSortType = SortType.String;
                    var eSortDirection = SortDirection.ASC;                    
                    if(th.attr('id') === "state" || th.attr('id') === "online" || th.attr('id') === "ram" || th.attr('id') === "suspend" || th.attr('id') === 'all_size') // 桌面設定-新增VD檔案大小實作 2017.05.03 william
                        eSortType = SortType.Num;
                    if(th.hasClass('asc')){
                        $('.VMHeader').removeClass('asc');
                        $('.VMHeader').removeClass('desc');
                        $('.label_vm_arrow').html('');
                        $('#vm' + th.attr('id') + '1').html('&#9660;');
//                        th.removeClass('asc');
                        th.addClass('desc');
                        eSortDirection = SortDirection.Desc;
                    }
                    else{
                        $('.VMHeader').removeClass('asc');
                        $('.VMHeader').removeClass('desc');
                        $('.label_vm_arrow').html('');
                        $('#vm' + th.attr('id') + '1').html('&#9650;');
                        //th.removeClass('desc');
                        th.addClass('asc');
                        eSortDirection = SortDirection.ASC;
                    }
                    NowSortType = eSortType;
                    NowSortDirection = eSortDirection;
                    var sort_id = th.attr('id');
                    if(sort_id=='suspend'){
                        if(!($('.iCheckBoxVMSuspend:checked').length !=0 && $('.iCheckBoxVMSuspend:checked').length != $('.iCheckBoxVMSuspend').length)){
                            StartPolling(true,true);
                            return;
                        }
                    }
                    // if(th.attr('id') === 'username')
                    //     sort_id = 'name';
                    if(th.attr('id') === 'all_size') // 桌面設定-新增VD檔案大小實作 2017.05.04 william
                        sort_id = 'allsize';                        
                    NowSortDOMID = sort_id;                    
                    SortList(eSortDirection,eSortType,sort_id,VMListTable);                        
                    $('#Div_VMList').html(oHtml.CreateVMListHtmlByTable());                                        
                    $('.btn-operation-vm').click(function(event){                               
                        event.preventDefault();
                        var btn_element = $(this);
                        vmname = btn_element.data('operation');
                        ChangeVMNametoID(vmname);
                    });
                    $('.iCheckBoxAllSelect').iCheck('uncheck');
                    $('.iCheckBoxVMSelect').iCheck({  
                        checkboxClass: 'icheckbox_minimal-blue'    
                    });
                    InitRowSuspendCheck('iCheckBoxVMSuspend');                                     
                    EnableDisableSelectVMActionButton();
                    $('.iCheckBoxVMSelect').on('ifChanged', function(event){                            
                        EnableDisableSelectVMActionButton();
                    });                    
                    StartPolling(true,true);
                });            
        }         
        
        function SeedHederClickSort(){
            $('.SeedHeader').click(function(event){                
                event.preventDefault();
                var th = $(this);
                var eSortType = SortType.String;
                var eSortDirection = SortDirection.ASC;                    
                if(th.attr('id') === "vdi_count" || th.attr('id') === "ram" || th.attr('id') === 'all_size' || th.attr('id') === 'state_display') // 桌面設定-新增VD檔案大小實作 2017.05.03 william
                    eSortType = SortType.Num;
                if(th.hasClass('asc')){
                    $('.SeedHeader').removeClass('asc');
                    $('.SeedHeader').removeClass('desc');
                    $('.label_seed_arrow').html('');
                    $('#seed' + th.attr('id') + '1').html('&#9660;');
//                    th.removeClass('asc');
                    th.addClass('desc');
                    eSortDirection = SortDirection.Desc;
                }
                else{
                    $('.SeedHeader').removeClass('asc');
                    $('.SeedHeader').removeClass('desc');
                    $('.label_seed_arrow').html('');
                    $('#seed' + th.attr('id') + '1').html('&#9650;');
//                    th.removeClass('desc');
                    th.addClass('asc');
                    eSortDirection = SortDirection.ASC;
                }
                NowSortSeedType = eSortType;
                NowSortSeedDirection = eSortDirection;
                NowSortSeedDOMID = th.attr('id');
                var sort_id = th.attr('id'); // 桌面設定-修改 2017.05.04 william
                if(th.attr('id') === 'all_size') // 桌面設定-新增VD檔案大小實作 2017.05.04 william
                    sort_id = 'allsize'; 
                SortList(eSortDirection,eSortType,sort_id,SeedListTable);                        
                $('#Div_SeedList').html(oHtml.CreateSeedListHtmlByTable());  
                //2016.12.19 william 新增
                $('.btn-operation-vm').click(function(event){                               
                    event.preventDefault();
                    var btn_element = $(this);
                    vmname = btn_element.data('operation');
                    ChangeVMNametoID(vmname);
                });
                $('.iCheckBoxSeedAllSelect').iCheck('uncheck');
                $('.iCheckBoxSeedSelect').iCheck({  
                    checkboxClass: 'icheckbox_minimal-blue'    
                });         
                EnableDisableSelectSeedActionButton();
                $('.iCheckBoxSeedSelect').on('ifChanged', function(event){                            
                    EnableDisableSelectSeedActionButton();
                });
                
                StartPolling(true,true);
                           
            });            
        }        
        
        function TaskHederClickSort(){
            $('.TaskHeader').click(function(event){
                    StopPolling();
                    event.preventDefault();
                    var th = $(this);
                    var eSortType = SortType.String;
                    var eSortDirection = SortDirection.ASC;                    
                    if(th.attr('id') === "status")
                        eSortType = SortType.Num;
                    if(th.hasClass('asc')){
                        $('.TaskHeader').removeClass('asc');
                        $('.TaskHeader').removeClass('desc');
                        $('.label_task_arrow').html('');
                        $('#task' + th.attr('id') + '1').html('&#9660;');
                        th.addClass('desc');
                        eSortDirection = SortDirection.Desc;
                    }
                    else{
                        $('.TaskHeader').removeClass('asc');
                        $('.TaskHeader').removeClass('desc');
                        $('.label_task_arrow').html('');
                        $('#task' + th.attr('id') + '1').html('&#9650;');
                        th.addClass('asc');
                        eSortDirection = SortDirection.ASC;
                    }
                    NowSortTaskType = eSortType;
                    NowSortTaskDirection = eSortDirection;
                    NowSortTaskDOMID = th.attr('id');
                    SortList(eSortDirection,eSortType,th.attr('id'),VMListCloneTable);                        
                    $('#Div_VM_Clone_List').html(oHtml.CreateVMTaskHtmlBTable()); 
                    // 2016.12.23 william
                    /* 未來新增之項目 2016.12.23 william
                    $('.iCheckBoxTaskAllSelect').iCheck('uncheck');
                    $('.iCheckBoxTaskSelect').iCheck({  
                        checkboxClass: 'icheckbox_minimal-blue'    
                    });         
                    */
                    $.each(VMListCloneTable,function(index,element){
                        if(element['status'] >= 0 && element['status'] !== 3){
                            $('#progress' + index).progressbar({
                                value: Math.floor( element['process'] )
                            });                               
                            $('#progressText' + index).text(element['process'] + '%');                            
                        }
                    });                    
                    StartPolling(true,true);
                });            
        }
        
        function BornedHeaderClickSort(){
            $('.BornedHeader').click(function(event){
                StopPolling();
                event.preventDefault();
                var th = $(this);
                var eSortType = SortType.String;
                var eSortDirection = SortDirection.ASC;                    
                if(th.attr('id') === "state" || th.attr('id') === "online" || th.attr('id') === "ram" || th.attr('id') === "suspend" || th.attr('id') === 'all_size') // 桌面設定-新增VD檔案大小實作 2017.05.03 william
                    eSortType = SortType.Num;
                if(th.hasClass('asc')){
                    $('.BornedHeader').removeClass('asc');
                    $('.BornedHeader').removeClass('desc');
                    $('.label_borned_arrow').html('');
                    $('#borned' + th.attr('id') + '1').html('&#9660;');
                    th.addClass('desc');
                    eSortDirection = SortDirection.Desc;
                }
                else{
                    $('.BornedHeader').removeClass('asc');
                    $('.BornedHeader').removeClass('desc');
                    $('.label_borned_arrow').html('');
                    $('#borned' + th.attr('id') + '1').html('&#9650;');                    
                    th.addClass('asc');
                    eSortDirection = SortDirection.ASC;
                }
                NowSortBornedType = eSortType;
                NowSortBornedDirection = eSortDirection;
                var sort_id = th.attr('id');
                if(sort_id=='suspend'){
                    if(!($('.iCheckBoxBornedSuspend:checked').length !=0 && $('.iCheckBoxBornedSuspend:checked').length != $('.iCheckBoxBornedSuspend').length)){
                        StartPolling(true,true);
                        return;
                    }
                }
                // if(th.attr('id') === 'username')
                //     sort_id = 'name'; 
                if(th.attr('id') === 'all_size') // 桌面設定-新增VD檔案大小實作 2017.05.04 william
                    sort_id = 'allsize';                                        
                NowSortBornedDOMID = sort_id;    
                SortList(NowSortBornedDirection,NowSortBornedType,sort_id,BornedListTable);                        
                $('#Div_Borned_List').html(oHtml.CreateBornedListHtmlByTable());                                
                $('.btn-operation-vm').click(function(event){                               
                    event.preventDefault();
                    var btn_element = $(this);
                    vmname = btn_element.data('operation');
                    ChangeVMNametoID(vmname);
                });
                $('.iCheckBoxBornedAllSelect').iCheck('uncheck');
                $('.iCheckBoxBornedSelect').iCheck({  
                    checkboxClass: 'icheckbox_minimal-blue'    
                });
                InitRowSuspendCheck('iCheckBoxBornedSuspend');                
                EnableDisableSelectBornedActionButton();
                $('.iCheckBoxBornedSelect').on('ifChanged', function(event){                            
                    EnableDisableSelectBornedActionButton();
                });

                StartPolling(true,true);
            }); 
        }
        
        function SortList(eSortDirection,eSortType,sSortVariable,List){                    
            switch ( eSortDirection )
            {
                case SortDirection.ASC:
                    switch(eSortType)
                    {
                        case SortType.Num:
                            List.sort(function(a,b)
                            {	                                
                                return (a[sSortVariable] - b[sSortVariable]);
                            });
                            break;
                        case SortType.String:
                            List.sort( function(a,b)
                            {                                                 
                                return a[sSortVariable].localeCompare(b[sSortVariable]);
                            });
                            break;
                    }
                    break;

                case SortDirection.Desc:
                    switch(eSortType)
                    {
                        case SortType.Num:
                            List.sort( function(a,b)
                            {                                                              
                                return (b[sSortVariable] - a[sSortVariable]);
                            });
                            break;
                        case SortType.String:
                            List.sort( function(a,b)
                            {    
                                return b[sSortVariable].localeCompare(a[sSortVariable]);
                            });
                            break;
                    }
                    break;               
            }                    
        }
        
        function InitRowSuspendCheck(class_name){
            $('.' + class_name).iCheck({  
                checkboxClass: 'icheckbox_minimal-blue'    
            }); 
            $('.' + class_name).on('ifClicked', function(event){                            
                var element = $(this);
                ActionClickSuspendCheck(element);
            });
        }        
        
        function RebindVMActionButtonEvent(btn_element){            
            
            if(btn_element.hasClass('vm-poweron')){
                var poweron_ask = LanguageStr_VM.VM_Poweron_ask.replace("@",VMorVDI);
                var confirmable = confirm(poweron_ask);
                if (confirmable === true) {
                    var namearr = [];
                    namearr[0] = vmid;                                        
                    VMStart(namearr);
                }
            }
            else if(btn_element.hasClass('vm-shutdown')){
                var shutdown_ask = LanguageStr_VM.VM_Shutdown_ask.replace("@",VMorVDI);
                var confirmable = confirm(shutdown_ask);
                if (confirmable === true) {
                    var namearr = [];
                    namearr[0] = vmid; 
                    VMShutdown(namearr);
                }
            }
            else if(btn_element.hasClass('vm-close')){
                var poweroff_ask = LanguageStr_VM.VM_Poweroff_ask.replace("@",VMorVDI);
                var confirmable = confirm(poweroff_ask);
                if (confirmable === true) {
                    var namearr = [];
                    namearr[0] = vmid;
                    VMClose(namearr);
                }
            }
            else if(btn_element.hasClass('vm-delete')){
                var delete_ask = LanguageStr_VM.VM_Delete.replace("@",VMorVDI);
                var confirmable = confirm(delete_ask);
                if (confirmable === true) {
                   var namearr = [];
                   namearr[0] = vmid;
                   VMDelete(namearr);
                }
            } 
            else if(btn_element.hasClass('vm-modify')){                
                oHtml.blockPage();
                VMInfo(vmname,true,true);
                ChangeVMNametoUserName(vmname);
                // VMListDiskImg(true);
                InitVMModifyDialog();
                $( "#dialog_modify_vm" ).dialog('open');
            } 
            else if(btn_element.hasClass('vm-modify-boot')){                                                
                VMInfo_Boot(vmid);
                $( "#dialog_modify_vm_boot" ).dialog('open');
            } 
            else if(btn_element.hasClass('vm-inquiry')){
                $('#label_info_VMName').text(vmname);
                VMInfo(vmname,false,true);
                $( "#dialog_vm_info" ).dialog('open');
            }
            else if(btn_element.hasClass('vm-reset')){
                var reset_ask = LanguageStr_VM.VM_ResetPWD_ask.replace("@",VMorVDI);
                var confirmable = confirm(reset_ask);
                if (confirmable === true) {
                   VMResetPWD(vmid);
                }
            }
            else if(btn_element.hasClass('vm-enable')){
                var enable_ask = LanguageStr_VM.VM_Enable_ask.replace("@",VMorVDI);
                var confirmable = confirm(enable_ask);
                if (confirmable === true) {
                   var namearr = [];
                   namearr[0] = vmid; 
                   VMEnable(namearr);
                }
            }
            else if(btn_element.hasClass('vm-disable')){
                var disable_ask = LanguageStr_VM.VM_Disable_ask.replace("@",VMorVDI);
                var confirmable = confirm(disable_ask);
                if (confirmable === true) {
                   var namearr = [];
                   namearr[0] = vmid; 
                   VMDisable(namearr);
                }
            }            
            else if(btn_element.hasClass('vm-clone')){
                $("#label_clone_source").text(vmname);
                $("#textarea_dest_name").val('');
                $("#input_seed_dest_name").val('');
                $("#input_clone_VMDescription").val(VMListName[vmname]['desc']);
                $("#radio-normal-clone").iCheck('check');
                var title = LanguageStr_VM.VM_Clone.replace("@",VMorVDI);
//                $("#div_seed_dest").css("height","0px");
                $('#btn_VD_get_user').show();               
                $("#div_seed_dest").hide();
                $(".div_clone_dest").show();
                $("#usernameforClone").hide();
                $("#dialog_vm_clone").dialog('option', 'title', title);
                $("#dialog_vm_clone" ).dialog('open');
                temp_vmname_for_clone = vmname;
                $('.iCheckBoxUserAllSelect').iCheck('uncheck');
                CreateVM_GetUserList_withcheckbox();
            }
            else if(btn_element.hasClass('vm-export')){
                var export_ask = LanguageStr_VM.VM_Export.replace("&",VMorVDI).replace("@",VMorVDI);
                var confirmable = confirm(export_ask);
                if (confirmable === true) {
                   var namearr = [];
                   namearr[0] = vmid;
                   VMExport(namearr);
                }
            } 
            else if(btn_element.hasClass('vm-make-seed')){
                $("#label_clone_source").text(vmname);
                $("#textarea_dest_name").val('');
                $("#input_seed_dest_name").val('');
                $("#input_clone_VMDescription").val(VMListName[vmname]['desc']);
                $("#radio-seed-clone").iCheck('check');
//                $("#div_seed_dest").css("height","30px");
                $('#btn_VD_get_user').hide();
                $("#div_seed_dest").show();
                $(".div_clone_dest").hide();
                $("#usernameforClone").hide();
                $("#dialog_vm_clone").dialog('option', 'title', LanguageStr_VM.VM_Make_Seed);
                $("#dialog_vm_clone").dialog('open');
                temp_vmname_for_seed = vmname;
                SeedUserListflag     = true;
            }
            else if(btn_element.hasClass('vm-create-vdi')){
                $("#label_snapshot_source").text(vmname);
                $("#textarea_snapshot_dest_name").val('');   
                $("#input_create_vdi_VMDescription").val(SeedListName[vmname]['desc']);
                $("#dialog_vm_create_vdi" ).dialog('open');
                temp_vmname_for_bornuser = vmname;
                $('.iCheckBoxBornUserAllSelect').iCheck('uncheck');
                CreateVM_GetUserList_withcheckbox();
            }
            else if(btn_element.hasClass('vm-delete-seed')){ 
                if(SeedListName[vmname]['vdi_count'] !== 0){
                    var delete_warning = (LanguageStr_VM.VM_Delete_Borned_Warning).replace("@",VMorVDI);
                    delete_warning = delete_warning.replace("&",VMorVDI);
                    alert(delete_warning);                    
                }
                else{
                    var confirmable = confirm(LanguageStr_VM.VM_Delete_Seed_ask);
                    if (confirmable === true) {
                        var namearr = [];
                        namearr[0] = vmname;
                        VMDelete(namearr);
                    }
                }                    
            }
            else if(btn_element.hasClass('vm-modify-seed')){                
                VMSeedModifyInfo(vmname);                
                $('#label_Mofify_Seed_Name').val(vmname);
                $("#dialog_modify_seed" ).dialog('open');
                
            }
            else if(btn_element.hasClass('vm-RebornUser')){                
                SeedRebornUser(vmname, 0);                
            }      
            else if(btn_element.hasClass('vm-RenewUser')){                
                SeedRebornUser(vmname, 1);                
            }                  
            else if(btn_element.hasClass('vm-VDRecovery')){                
                VDRecovery(vmname);                
            }      
            else if(btn_element.hasClass('vm-overwriteseed')){                
                //alert(SeedNameArray);               
                CreateSeedListHtml(vmname); 
                $( "#dialog_VM_seedlist" ).dialog('open');
            }                 
           else if(btn_element.hasClass('vm-snapshot')){ /* 開啟手動SnapShot視窗  2017.01.26 william */                      
                $('#Takess_desc_input').val('');
                $("#SnapshotUsedLayer_input").val('');
                GetVMSnapshotList(vmid, true); // SnapShot&Schedule&Backup  2017.01.26 william        
                // 新增快照層數  2017.11.22 william  
                GetSnapshotLayer(vmid);    
                $('.label_SSLayer_warning_2').hide();
                $(".Takess_Username").text(vmname);  
                $(".Takess_VDName").text(vmname);           
                $("#dialog_vm_snapshot" ).dialog('open');
                SnapshotListStart = true;
            }
           else if(btn_element.hasClass('vm-backup')){ /* 開啟手動SnapShot視窗  2017.01.26 william */                      
                $("#H5_SubTitle_SysMgr_Service").click();
                $("#H5_SubTitle_SysMgr_VMSetting").removeClass('MenuButtonClickHover');
                $('#tabs1-Backup_Setting').css('display','none');
                $('#tabs1-Backup_schedule').css('display','none');
                $('#tabs1-bktask-list').css('display','none');                
                $('#tab-service-container').easytabs('select', '#tabs1-service_Backup');
                $('#tab-Backup-container').easytabs('select', '#tabs1-Backup_Manager');
    if (!BackupMgtEnableFlag) {
        $("#dialog_bkStorageConnCheck").dialog('open');
    }
                BackupListStart = true;
            }
            else if(btn_element.hasClass('vm-SnapshotDelete')){ /* 手動SnapShot 動作下拉式清單-刪除 2017.01.26 william */   
               // var delete_ask = LanguageStr_VM.VM_Delete.replace("@",VMorVDI);
               // var confirmable = confirm(delete_ask);
               // if (confirmable === true) {
                //    var namearr = [];
                //    namearr[0] = vmid;
                   VMDeleteSnapshot(idLayer);
               // }
            }
            else if(btn_element.hasClass('vm-SnapshotRollback')){ /* 手動SnapShot 動作下拉式清單-還原 2017.01.26 william */   
                   VMSnapshotRollback(idLayer);
            }
            else if(btn_element.hasClass('vm-SnapshotView')){ /* 手動SnapShot 動作下拉式清單-檢視 2017.01.26 william */   
                   VMSnapshotView(idLayer);
            }
            // 2017.11.22 william 雲桌面轉移至雲主機
            else if(btn_element.hasClass('vm-uservd-clone')){
                changeUserVDCloneDialogStyle();
                $("#label_clone_source").text(vmname);
                temp_username_for_vdtoorg = '';
                temp_vmname_for_vdtoorg = vmname;
                $("#textarea_dest_name").val('');
                $("#input_seed_dest_name").val('');
                $("#input_VM_User_forClone").val('');                
                $("#input_clone_VMDescription").val(BornedListName[vmname]['desc']);
                $("#radio-uservd-clone").iCheck('check');
                var title = LanguageStr_VM.VM_Clone_UserVD.replace("@",VMorVDI);
//                $("#div_seed_dest").css("height","0px");
                $("#div_seed_dest").show();
                $(".div_clone_dest").hide();
                $('#btn_VD_get_user').show();
                $('#usernameforClone').show();
                $("#dialog_vm_clone").dialog('option', 'title', title);
                $("#dialog_vm_clone" ).dialog('open');
            }            
        }        

        function changeUserVDCloneDialogStyle() {
            $("#sourcenameforClone").text(LanguageStr_VM.Virtual_Host_Name_Str);            
            switch(SysCfg_lang_ver){
                case 'zh-tw':
                    $("#input_VM_User_forClone").css("margin-left","22px");        
                    $("#input_clone_VMDescription").css("margin-left","46px");     
                    break;
                case 'zh-cn': 
                    $("#input_VM_User_forClone").css("margin-left","22px");        
                    $("#input_clone_VMDescription").css("margin-left","46px");     
                    break;
                case 'en-us':
                    $("#input_VM_User_forClone").css("margin-left","52px");        
                    $("#input_clone_VMDescription").css("margin-left","66px");        
                    
                    break;       
            }                 
        }
        
        function InitVMModifyDialog()
        {            
            $('#input_Mofify_VMName').val(vmname); 
            $('#input_VMAddDiskSize').val('');              
            $('#combo_disk_type').scombobox('val',0); // 新增磁碟類型  2017.09.04 william         
            $('#input_VD_Assign_User').val('');       
            Get_Raid_ID($('#raidid_modifyvd'), -1, false);
        }
        
        oUIVMContorl.ClearAllVMInteval = function(){
            NowSortType = null;
            NowSortSeedType = null;
            NowSortTaskType = null;
            NowSortBornedType = null;
            NowSortSnapshotTaskType = null; // 快照任務清單 新增內容 2017.02.07 
            oUIVMContorl.ClearVMListInterval();
            oUIVMContorl.ClearVMTaskInterval();
            oUIVMContorl.ClearSnapshotTaskInterval(); // 快照任務清單 新增內容 2017.01.25 
        };
        
        oUIVMContorl.ClearVMListInterval = function(){
//            if(typeof(GetVMListInterval) !== 'undefined'){                
////                alert('clear');
//                clearInterval(GetVMListInterval);
//            }
            VMListStart = false;
        };
        
        function VMSeedModifyInfo(name){
            oHtml.blockPage();
            org_name = name;
            var request = oRelayAjax.VMInfo(vmid);  
            CallBackVMSeedModifyInfo(request);
        }
        
        function CallBackVMSeedModifyInfo(request,isModify,isChangeTab){
            request.done(function(msg, statustext, jqxhr) {                                  
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }    
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                oHtml.stopPage();                 
                $('#input_Modify_Seed_Description').val(msg['Desc']);                                 
            });
            request.fail(function(jqxhr, textStatus) {                  
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.VMInfo);                
            });
        }
        
        function VMInfo(name,isModify,isChangeTab){
            oHtml.blockPage();
            org_name = name;
            var request = oRelayAjax.VMInfo(vmid);  
            CallBackVMInfo(request,isModify,isChangeTab);
        }
        
        function CallBackVMInfo(request,isModify,isChangeTab){
            request.done(function(msg, statustext, jqxhr) {                                  
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }     
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                var max_memory = msg['RAM'];
                var init_memory = msg['RAM'];
                var init_memory_mode = 0;
                if(max_memory === init_memory){
                    init_memory_mode = 1;
                }                          
                // max_memory = max_memory.replace(' KiB','');
                max_memory =  Math.round(max_memory*100/1024);
                max_memory = max_memory/100;
                VMDiskCount = msg['Disks'].length;
                if(isModify){
                    $('#div_vm_nic_list').html(oHtml.CreateVMNICListHtml(msg,isModify));
                    $('#div_vm_disk_list').html(oHtml.CreateVMDiskListHtml(msg,isModify));
                    $('#div_modify_install_iso').html(oHtml.CreateModifyIsoCombo(msg));                    
                    $('#combo_modify_iso').scombobox({
                        editAble:false,
                        wrap:false                           
                    });                
                    $('#combo_Modify_cpu_cores').scombobox('val',msg['CpuCt']);                                
                    $('#combo_Modify_cpu_type').scombobox('val',msg['CPUType']); // 新增CPU類型  2017.09.01 william
                    $("#slider-modify-range-cpu-clock").slider("option","max",cpu_mhz * msg['CpuCt']);
                    if(msg['CPU'] !== -1){
                        $('#icheckmodifycpulimit').iCheck('check');
                        $('#slider-modify-range-cpu-clock').slider( "option", "disabled", false );
                        $("#slider-modify-range-cpu-clock").slider("option","value",msg['CPU']);
                        enable_custom_button($('.btn_cpu_clock'));                        
                    }
                    else{
                        $('#icheckmodifycpulimit').iCheck('uncheck');
                        $('#slider-modify-range-cpu-clock').slider( "option", "disabled", true );
                        $("#slider-modify-range-cpu-clock").slider("option","value",cpu_mhz * msg['CpuCt']);
                        disable_custom_button($('.btn_cpu_clock'));                                                
                    }
                    if(msg['USBType'] == 2)
                        $('#radio-usb-modify-2').iCheck('check');
                    else
                        $('#radio-usb-modify-3').iCheck('check');
                    if(msg['Suspend'] === false)
                        $('#icheckmodifypause').iCheck('uncheck');
                    else
                        $('#icheckmodifypause').iCheck('check');
                    Change_VM_Table_Suspend_CheckBox_by_name(org_name,msg['Suspend']);
                    $("#now-modify-cpu-clock" ).text( add_comma_of_number($("#slider-modify-range-cpu-clock").slider("option","value")) + " MHz" );        
                    $('#combo_modify_usb_redirect').scombobox('val',msg['USBRedirCt']);
                    $('#combo_Modify_audio').scombobox('val',msg['Sound']);
                    if(init_memory_mode === 1)
                        $('#radio-fix').iCheck('check');
                    else
                        $('#radio-dynamic').iCheck('check');
                    $('#input_Modify_VMMemory').val(max_memory);    
                    $('#input_VMModifyDescription').val(msg['Desc']);
                    
                    RebindVMDeleteNICButton();
                    RebindVMDeleteDiskButton();
                    RebindVMExpandDiskButton(); // 2018.01.08 william 新增磁碟擴充功能
                    RebindVMMoveDiskButton(); 
                    if(VMDiskCount === 6) // 2018.01.08 william 新增磁碟擴充功能 3 -> 6
                        $('#btn_add_vm_new_disk').button('disable');
                    else
                        $('#btn_add_vm_new_disk').button('enable');
                    if(isChangeTab)
                        $('#tab-vm-modify-container').easytabs('select', '#tabs1-vm-properties');
                    // 2018.01.08 william 新增磁碟擴充功能
                    CheckDiskTypeCount();
                    $('#input_VD_Assign_User').val(vmusername);
                }
                else{                    
                    $('#div_vm_info_nic_list').html(oHtml.CreateVMNICListHtml(msg,isModify));
                    $('#div_vm_info_disk_list').html(oHtml.CreateVMDiskListHtml(msg,isModify));
                    $('#label_info_VMMemory').text(max_memory);
                    if(init_memory_mode === 1)
                        $('#label_info_VMMemoryAllocation').text('Static');
                    else
                        $('#label_info_VMMemoryAllocation').text('Dynamic');
                    $('#label_info_CPU_Cores').text(msg['CpuCt']);
                    $('#label_info_CPU_Type').text(VMCPUTypeStatus(msg['CPUType'])); // 新增CPU類型  2017.09.01 william
                    var cpu_clock_info = LanguageStr_VM.VM_UnLimited;
                    if(msg['CPU'] === -1){
                        cpu_clock_info += '(' + add_comma_of_number((cpu_mhz*msg['CpuCt'])) + ' MHz)';
                    }
                    else{
                        cpu_clock_info = LanguageStr_VM.VM_Limited;
                        cpu_clock_info += '(' + add_comma_of_number(msg['CPU']) + ' MHz)';
                    }
                    $('#label_info_CPU_Clock').text(cpu_clock_info);
                    $('#label_info_USBRediect').text(msg['USBRedirCt']);
                    if(msg['USBType'] == 2)
                        $('#label_info_USBType').text("USB 2.0");
                    else
                        $('#label_info_USBType').text("USB 3.0");
                    $('#label_info_desc').text(msg['Desc']);
                    $('#label_info_audio').text(GetSoundDisplay(msg['Sound']));
                    $('#label_info_auto_suspend').text(msg['Suspend']===false?LanguageStr_VM.VM_Disable:LanguageStr_VM.VM_Enable);
                    var install_iso = 'None';
                    var source_split_arr;
                    $.each(msg['CDRom'], function(index, element){
                        install_iso = element['Name'];
                        return false;                        
                    });
                    $('#label_info_install_iso').text(install_iso);
                    if(isChangeTab)
                        $('#tab-vm-info-container').easytabs('select', '#tabs1-vm-info-properties');
                }         
                oHtml.stopPage();
            });
            request.fail(function(jqxhr, textStatus) {                  
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.VMInfo);                
            });
        }
        
        function VMCPUTypeStatus(type){ // 新增CPU類型  2017.09.04 william
            switch(type){
                case 0 :
                    return LanguageStr_VM.VM_CPUType_fullvirtual;
                case 1 :
                    return LanguageStr_VM.VM_CPUType_Halfvirtual;
            }
        }

        function VMInfo_Boot(name){
            StopPolling();
            oHtml.blockPage();           
            var request = oRelayAjax.VMInfo(name);  
            CallBackVMInfo_Boot(request);
        }
        
        function CallBackVMInfo_Boot(request){
            request.done(function(msg, statustext, jqxhr) {                                  
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }   
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                $('#div_install_iso_boot').html(oHtml.CreateModifyIsoComboBoot(msg));                    
                $('#combo_iso_boot').scombobox({
                    editAble:false,
                    wrap:false                           
                });                                   
                oHtml.stopPage();
            });
            request.fail(function(jqxhr, textStatus) {                  
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.VMInfo);                
            });
        }
        
        function Change_VM_Table_Suspend_CheckBox_by_name(name,suspend){
            if(name in VMListName){
                VMListName[name][suspend] = suspend;
                
            }
            else if(name in BornedListName){
                BornedListName[name][suspend] = suspend;
            }
            else
                return false;
            if(suspend === false)
                $('#VMSuspend' + name).iCheck('uncheck');
            else
                $('#VMSuspend' + name).iCheck('check');
        }
        
        function RebindVMDeleteDiskButton(){
            $('.btn-vm-delete-disk').button();
            $('.btn-vm-delete-disk').click(function(event){
                event.preventDefault();
                var btn_element = $(this);                                
                VMDeleteDisk(org_name,btn_element.attr('id'));
            });
            
        }
        
        // 2018.01.08 william 新增磁碟擴充功能
        function RebindVMExpandDiskButton() {    
            $('.btn-vm-expend-disk').button();    
            $('.btn-vm-expend-disk').click(function(event) {
                event.preventDefault();                  
                var btn_element = $(this);
                vm_expenddisk_data = btn_element.attr('id').split(":");                    
                vm_expenddisk_diskid = vm_expenddisk_data[0];                    
                vm_expenddisk_type = vm_expenddisk_data[1];   
                vm_expenddisk_orgsize = vm_expenddisk_data[2];   
                $('#input_ExpendVMDiskSize').val(''); // 清空input內容
                $('.ExpendVMDiskOrgSize').text(add_comma_of_number(add_two_fixed_float(vm_expenddisk_orgsize)));                
                $("#dialog_create_expand_disk").dialog('open');                               
            });    
        } 

        function RebindVMMoveDiskButton() {
            $('.btn-vm-move-disk').button();    
            $('.btn-vm-move-disk').click(function(event) {
                event.preventDefault();  
                $('.move_raid_alert').hide();  
                var btn_element = $(this);
                vm_movedisk_data = btn_element.attr('id').split(":");                      
                vm_movedisk_diskid = vm_movedisk_data[0];                    
                $("#dialog_moveraid_forVD").dialog('open');  
                Get_Raid_ID($('#raidid_move_forVD'), vm_movedisk_data[1], true);                                                                             
            });               
        }

        // 2018.01.08 william 新增磁碟擴充功能
        function InitVMExpandDiskButton() {
            $('#Expanddisk_Btn_Confirm').click(function(event) {
                event.preventDefault();
                vm_expenddisk_size = Number($('#input_ExpendVMDiskSize').val()) + Number(vm_expenddisk_orgsize);
                // console.log(vm_expenddisk_size);
                VMExpandDisk(vmid, vm_expenddisk_diskid, vm_expenddisk_size, vm_expenddisk_type);
                $("#dialog_create_expand_disk").dialog('close');    
            });     
        }        

        // 2018.01.08 william 新增磁碟擴充功能
        function VMExpandDisk(idvd, iddisk, size, type) {
            StopPolling();
            oHtml.blockPage();
            var request = oRelayAjax.ExpandDisk(idvd, iddisk, size, type);  
            CallBackVMExpandDisk(request);
        }

        // 2018.01.08 william 新增磁碟擴充功能
        function CallBackVMExpandDisk(request){
            request.done(function(msg, statustext, jqxhr) {                 
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }

                VMInfo(org_name,true,false);         
            });
            request.fail(function(jqxhr, textStatus) {    
                oHtml.stopPage();
                if(jqxhr.status == 400){
                    alert(LanguageStr_VM.VM_Modify_Name_Fail);  
                    $('#input_Mofify_VMName').val(org_name);
                    setTimeout(function(){VMInfo(org_name,true,false);},1000); 
                }
            // else if(jqxhr.status == 409){
            //     var name_warning_2 = (LanguageStr_VM.VM_Clone_Name_Warning_2).replace("&",name);                    
            //     alert(name_warning_2);
            //     $('#input_Mofify_VMName').val(org_name);
            //     setTimeout(function(){VMInfo(org_name,true,false);},1000);                
            // }
            // else
            //     oError.CheckAuth(jqxhr.status,ActionStatus.VMModifyInfo);   
            });
        }

    function Dialog_MoveUserDisk_forVD() {
        $("#dialog_moveraid_forVD").dialog({
            title: LanguageStr_langSysCfgUserMgt.MoveUserDisk,
            modal: true,
            resizable: false,
            draggable: true,
            autoOpen: false,
            height: 350,
            width: 500,
            dialogClass: "no-close",
            closeOnEscape: false
        });
        
        $('#btn_close_forVD_moveraid').click(function (event) {
            event.preventDefault();
            $("#dialog_moveraid_forVD").dialog('close');
        });   
    }        

        function InitVMMoveDiskButton() {
            $('#btn_moveraid_forVD_confirm').click(function(event) {
                event.preventDefault();
                var raidid = $('#combo_raidid_move_forVD').scombobox('val');
                VMMoveDisk(vmid,vm_movedisk_diskid, raidid);
                $("#dialog_moveraid_forVD").dialog('close');    
            });     
        }            

        function VMMoveDisk(idvd, diskid, raidid) {
            StopPolling();
            oHtml.blockPage();
            var request = oRelayAjax.VMMoveDisk(idvd, diskid, raidid);  
            CallBackVMMoveDisk(request);
        }        

        function CallBackVMMoveDisk(request){
            request.done(function(msg, statustext, jqxhr) {                 
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }

                VMInfo(org_name,true,false);         
            });
            request.fail(function(jqxhr, textStatus) {    
                oHtml.stopPage();
                if(jqxhr.status == 400){
                    alert(LanguageStr_VM.VM_Modify_Name_Fail);  
                    $('#input_Mofify_VMName').val(org_name);
                    setTimeout(function(){VMInfo(org_name,true,false);},1000); 
                }
            // else if(jqxhr.status == 409){
            //     var name_warning_2 = (LanguageStr_VM.VM_Clone_Name_Warning_2).replace("&",name);                    
            //     alert(name_warning_2);
            //     $('#input_Mofify_VMName').val(org_name);
            //     setTimeout(function(){VMInfo(org_name,true,false);},1000);                
            // }
            // else
            //     oError.CheckAuth(jqxhr.status,ActionStatus.VMModifyInfo);   
            });
        }        

        // 2018.01.08 william 新增磁碟擴充功能
        function CheckDiskTypeCount() {        
            //console.log("IDE:" + IDE_type_count + ", SCSI:" + SCSI_type_count + ", VirtIO:" + VirtIO_type_count);
            switch($('#combo_disk_type').scombobox('val')){
                case '0':
                    if(IDE_type_count == 2) {
                        $('#btn_add_vm_new_disk').button('disable'); 
                        $('.disk_type_warning').text(LanguageStr_VM.VM_Disktype_warning);   

                    } else {
                        $('#btn_add_vm_new_disk').button('enable');
                        $('.disk_type_warning').text('');
                    } 
                    break;
                case '1': 
                    if(SCSI_type_count == 2) {
                        $('#btn_add_vm_new_disk').button('disable'); 
                        $('.disk_type_warning').text(LanguageStr_VM.VM_Disktype_warning);   
                    } else {
                        $('#btn_add_vm_new_disk').button('enable');
                        $('.disk_type_warning').text('');
                    } 
                    break;
                case '2':
                    if(VirtIO_type_count == 2) {
                        $('#btn_add_vm_new_disk').button('disable'); 
                        $('.disk_type_warning').text(LanguageStr_VM.VM_Disktype_warning);  
                    } else {
                        $('#btn_add_vm_new_disk').button('enable');
                        $('.disk_type_warning').text('');
                    }  
                    break;       
            } 

            $('#combo_disk_type').scombobox('change',function(event) {
                if($('#combo_disk_type').scombobox('val') === '0') {
                    if(IDE_type_count == 2) {
                        $('#btn_add_vm_new_disk').button('disable'); 
                        $('.disk_type_warning').text(LanguageStr_VM.VM_Disktype_warning);  
                    } else {
                        $('#btn_add_vm_new_disk').button('enable');
                        $('.disk_type_warning').text('');
                    }                
                } 
                else if($('#combo_disk_type').scombobox('val') === '1') {
                    if(SCSI_type_count == 2) {
                        $('#btn_add_vm_new_disk').button('disable'); 
                        $('.disk_type_warning').text(LanguageStr_VM.VM_Disktype_warning);  
                    } else {
                        $('#btn_add_vm_new_disk').button('enable');
                        $('.disk_type_warning').text('');
                    }                    
                }
                else if($('#combo_disk_type').scombobox('val') === '2') {
                    if(VirtIO_type_count == 2) {
                        $('#btn_add_vm_new_disk').button('disable'); 
                        $('.disk_type_warning').text(LanguageStr_VM.VM_Disktype_warning);  
                    } else {
                        $('#btn_add_vm_new_disk').button('enable');
                        $('.disk_type_warning').text('');
                    }                    
                }                        
            });   
        }

        function RebindVMDeleteNICButton(){
            $('.btn-vm-delete-nic').button();
            $('.btn-vm-delete-nic').click(function(event){
                event.preventDefault();
                var btn_element = $(this);  
                var confirmable = confirm(LanguageStr_VM.VM_Delete_NIC_ask);
                if (confirmable === true) {
                    VMDeleteNIC(vmid,btn_element.attr('id'));
                }
            });
        }
        
        function StopPolling(){
            PollingListFlag = false;
            PollingListTaskFlag = false;
        }
        
        function StartPolling(isPollingList,isPollingTask){
            if(isPollingList)
                PollingListFlag = true;
            if(isPollingTask)
                PollingListTaskFlag = true;
        }
        
        function VMAddDisk(name,size,type,raidid){ // 新增磁碟類型  2017.09.04 william
            StopPolling();
            oHtml.blockPage();
            var request = oRelayAjax.AddVMDisk(vmid,size,type,raidid);  
            CallBackVMAddDisk(request);
        }       
        
        function CallBackVMAddDisk(request){
            request.done(function(msg, statustext, jqxhr) {                                  
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }  
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                StartPolling(true,true);
                oUIVMContorl.VMListTask(false);
                setTimeout( function(){VMInfo(org_name,true,false);},1000);
            });
            request.fail(function(jqxhr, textStatus) {  
                oHtml.stopPage();
                if(jqxhr.status == 400){
                    setTimeout( function(){VMInfo(org_name,true,false);},1000);
                }   
                else             
                {
                    oError.CheckAuth(jqxhr.status,ActionStatus.VMDeleteDisk);
                }
                StartPolling(true,true);
            });
        }
        
        function VMDeleteDisk(name,target){
            StopPolling();
            oHtml.blockPage();
            var request = oRelayAjax.DeleteVMDisk(vmid,target);  
            CallBackVMDeleteDisk(request);
        }       
        
        function CallBackVMDeleteDisk(request){
            request.done(function(msg, statustext, jqxhr) {                                  
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }    
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                StartPolling(true,true);
                oUIVMContorl.VMListTask(false);
                setTimeout( function(){VMInfo(org_name,true,false);},1000);
            });
            request.fail(function(jqxhr, textStatus) {     
                oHtml.stopPage();
                if(jqxhr.status == 400){
                    setTimeout( function(){VMInfo(org_name,true,false);},1000);
                }   
                else{
                    oError.CheckAuth(jqxhr.status,ActionStatus.VMDeleteDisk);  
                }
                StartPolling(true,true);
            });
        }
        
        function VMAddNIC(name,speed,vswitch){
            StopPolling();
            oHtml.blockPage();
            var request = oRelayAjax.AddVMNIC(name,speed,vswitch);  
            CallBackVMAddNIC(request);
        };
        
        function CallBackVMAddNIC(request){
            request.done(function(msg, statustext, jqxhr) {                                  
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }     
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                StartPolling(true,true);
                setTimeout( function(){VMInfo(org_name,true,false);},1000);
            });
            request.fail(function(jqxhr, textStatus) {                  
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.VMDeleteNIC);  
                StartPolling(true,true);
            });
        }
        
        function VMDeleteNIC(name,mac){
            StopPolling();
            oHtml.blockPage();
            var request = oRelayAjax.DeleteVMNIC(name,mac);  
            CallBackVMDeleteNIC(request);
        };
        
        function CallBackVMDeleteNIC(request){
            request.done(function(msg, statustext, jqxhr) {                                  
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }     
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                VMInfo(org_name,true,false);
                StartPolling(true,true);
            });
            request.fail(function(jqxhr, textStatus) {                  
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.VMDeleteNIC);  
                StartPolling(true,true);
            });
        }
        
        function VMStart(namearr){
            StopPolling();
            oHtml.blockPage();
            var request = oRelayAjax.StartVM(namearr);  
            CallBackVMStart(request);
        };
        
        function CallBackVMStart(request){
            request.done(function(msg, statustext, jqxhr) {                                  
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                if(msg["over_poweron"] === 1){
                    alert('You can only poweron '+ msg['Max_VD'] + ' VD at the same time.');
                }
                StartPolling(true,true);
                setTimeout(function(){oHtml.stopPage();},5000); 
            });
            request.fail(function(jqxhr, textStatus) {                  
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.VMStart);  
                StartPolling(true,true);
            });
        }
        
        function VMShutdown(namearr){
            StopPolling();
            oHtml.blockPage();
            var request = oRelayAjax.ShutdownVM(namearr);  
            CallBackVMShutdown(request);
        };
        
        function CallBackVMShutdown(request){
            request.done(function(msg, statustext, jqxhr) {                 
                 
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
//                setTimeout( oUIVMContorl.VMList(true), 4000 );                
                StartPolling(true,true);
                setTimeout(function(){oHtml.stopPage();},5000);
            });
            request.fail(function(jqxhr, textStatus) {      
                //alert(jqxhr.responseText);
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.VMShutdown);                
                StartPolling(true,true);
            });
        }
        
        function VMModifyAutoSuspend(setarr,refresh){
            StopPolling();
            oHtml.blockPage();
            var request = oRelayAjax.SuspendVM(setarr);  
            CallBackVMAutoSuspend(request,refresh);
        }
        
        function CallBackVMAutoSuspend(request,refresh){
            request.done(function(msg, statustext, jqxhr) {                                  
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }       
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                if(refresh){                                     
                    oUIVMContorl.VMList(true,true);
                    StartPolling(false,true);
                }
                else{                
                    //oHtml.stopPage();
                    oUIVMContorl.VMList(false,true);
                    StartPolling(false,true);     
                    //release lock check
                    //setTimeout(function(){oHtml.stopPage();$('.iCheckBoxSuspend').removeClass('disable');},800);                                       
                }
            });
            request.fail(function(jqxhr, textStatus) {      
                //alert(jqxhr.responseText);                
                oError.CheckAuth(jqxhr.status,ActionStatus.VMSuspend);                
                oUIVMContorl.VMList(true,true);
                StartPolling(false,true);
            });
        }
        
        function VMClose(name){
            StopPolling();        
            oHtml.blockPage();
            var request = oRelayAjax.CloseVM(name);  
//            console.log('Send Close');
            CallBackVMClose(request);
        };
        
        function CallBackVMClose(request){
            request.done(function(msg, statustext, jqxhr) {                 
                 
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                StartPolling(true,true);
                setTimeout(function(){oHtml.stopPage();},5000);
            });
            request.fail(function(jqxhr, textStatus) {                  
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.VMShutdown);     
                StartPolling(true,true);
            });
        }
        
        function VMResetPWD(name){
            StopPolling();
            oHtml.blockPage();
            var request = oRelayAjax.ResetVMPWD(name);  
            CallBackVMResetPWD(request);
        };
        
        function CallBackVMResetPWD(request){
            request.done(function(msg, statustext, jqxhr) {                 
                oHtml.stopPage(); 
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }   
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                StartPolling(true,true);
            });
            request.fail(function(jqxhr, textStatus) {                  
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.VMResetPWD);                
                StartPolling(true,true);
            });
        }
        
        function VMDelete(name){
            StopPolling();
            if(name.length > 0)
            {
                tmp_name_arr=name[0];
                name.splice(0,1);
                ChangeVMNametoID(tmp_name_arr);                                           
                oHtml.blockPage();
                var request = oRelayAjax.DeleteVM(vmid);  
//                console.log("Delete " + tmp_name_arr[0]);
                CallBackVMDelete(request,name);
            }     
        };
        
        function CallBackVMDelete(request,name){
            request.done(function(msg, statustext, jqxhr) {                                  
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }    
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
//                console.log("Complete Delete");
                               
                if(name.length === 0)
                {
                    oHtml.stopPage();
                    oUIVMContorl.VMList(true,true);
                    StartPolling(false,true);
                }
                else
                    VMDelete(name); 
            });
            request.fail(function(jqxhr, textStatus) {                  
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.VMDelete);
                VMDelete(name);
            });
        }
        
        function SeedRebornUser(name, type) {
            StopPolling();
            ChangeVMNametoID(name);                                           
            oHtml.blockPage();
            var request = oRelayAjax.RebornUser(vmid, type);  
            CallBackSeedRebornUser(request);
        }

        function CallBackSeedRebornUser(request) {
            request.done(function(msg, statustext, jqxhr) {                                  
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2) {
                    Logout_Another_Admin_Login();
                    return false;
                } else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3) {
                    window.location.href = 'index.php';
                    return false;
                }

                oHtml.stopPage();                            
                NowSortTaskType = null;
                oUIVMContorl.VMListTask(true);    
                StartPolling(true,false);
                $('#tab-vm-container').easytabs('select', '#tabs1-task-list');
                // alert("Reborn success " + "("+ jqxhr.status + ")");
                // alert("Error Code: " + jqxhr.status);
            });
            request.fail(function(jqxhr, textStatus) {                  
                oHtml.stopPage();
                alert("Error Code: " + jqxhr.status);
            });
        }

        function VDRecovery(name) {
            StopPolling();
            ChangeVMNametoID(name);                                           
            oHtml.blockPage();
            var request = oRelayAjax.VDRecovery(vmid);  
            CallBackVDRecovery(request);
        }

        function CallBackVDRecovery(request) {
            request.done(function(msg, statustext, jqxhr) {                                  
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2) {
                    Logout_Another_Admin_Login();
                    return false;
                } else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3) {
                    window.location.href = 'index.php';
                    return false;
                }
                oHtml.stopPage();
                NowSortTaskType = null;
                oUIVMContorl.VMListTask(true);    
                StartPolling(true,false);
                $('#tab-vm-container').easytabs('select', '#tabs1-task-list');
            });
            request.fail(function(jqxhr, textStatus) {                  
                oHtml.stopPage();
                alert("Error Code: " + jqxhr.status);
            });
        }



        function VMEnable(name){
            StopPolling();
            oHtml.blockPage();
            var request = oRelayAjax.EnableVM(name);  
            CallBackVMEnable(request);
        }
        
        function CallBackVMEnable(request){
            request.done(function(msg, statustext, jqxhr) {                                  
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }  
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                oUIVMContorl.VMList(false,false);
                StartPolling(true,true);
                setTimeout(function(){oHtml.stopPage();},2000); 
            });
            request.fail(function(jqxhr, textStatus) {                  
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.VMDelete);                
                StartPolling(true,true);
            });
        }
        
        function VMDisable(name){
            StopPolling();
            oHtml.blockPage();
            var request = oRelayAjax.DisableVM(name);  
            CallBackVMDisable(request);
        }
        
        function CallBackVMDisable(request){
            request.done(function(msg, statustext, jqxhr) {                 
                 
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }      
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                oUIVMContorl.VMList(false,false);
                StartPolling(true,true);
                setTimeout(function(){oHtml.stopPage();},2000); 
            });
            request.fail(function(jqxhr, textStatus) {                  
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.VMDelete);                
                StartPolling(true,true);
            });
        }
        
        function VMClone(type,srcname,destnamearr,description){
            StopPolling();
            oHtml.blockPage();
            var request = oRelayAjax.CloneVM(type,srcname,destnamearr,description);  
            CallBackVMClone(request);
        }
        
        function CallBackVMClone(request){
            request.done(function(msg, statustext, jqxhr) {                                  
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }  
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                oHtml.stopPage();                 
                $( "#dialog_vm_clone" ).dialog('close');
                $( "#dialog_vm_create_vdi" ).dialog('close');                  
                NowSortTaskType = null;
                oUIVMContorl.VMListTask(true);    
                StartPolling(true,false);
                $('#tab-vm-container').easytabs('select', '#tabs1-task-list');
            });
            request.fail(function(jqxhr, textStatus) {                  
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.VMClone);  
                StartPolling(true,true);
                // 2017.11.22 william 雲桌面轉移至雲主機
                if(jqxhr.status==400) {
                    $( "#dialog_vm_clone" ).dialog('close');
                    $( "#dialog_vm_create_vdi" ).dialog('close');        
                    error_msg = LanguageStr_VM.VM_Clone_UserVD_Failure.replace("@",VMorVDI);
                    alert(error_msg);
                    return false;    
                }                
            });
        }
        
        function VMExport(name){
            StopPolling();
            oHtml.blockPage();
            var request = oRelayAjax.ExportVM(vmid);  
            CallBackVMExport(request);
        }
        
        function CallBackVMExport(request){
            request.done(function(msg, statustext, jqxhr) {                                  
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }   
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                // if(msg['Fails'].length > 0){
                //     var fail_names = '';
                //     $.each( msg['Fails'], function(index, element){     
                //         if(index !== 0)
                //             fail_names += ',';
                //         fail_names += element;
                //     });
                //     alert((LanguageStr_VM.VM_Export_Fail).replace("@",VMorVDI).replace("&",fail_names));
                // }
                // else{
                //     alert((LanguageStr_VM.VM_Export_Success).replace("&",VDIIP).replace("@",VMorVDI))
                // }
                oHtml.stopPage();                
                oUIVMContorl.VMListTask(true);    
                StartPolling(true,true);
                $('#tab-vm-container').easytabs('select', '#tabs1-task-list');           
            });
            request.fail(function(jqxhr, textStatus) {                  
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.VMExport);                
                StartPolling(true,true);
            });
        }
        
        function VMClearTask(){
            StopPolling();
            oHtml.blockPage();
            var request = oRelayAjax.VMClearTask();  
            CallBackVMClearTask(request);
        }
        
        function CallBackVMClearTask(request){
            request.done(function(msg, statustext, jqxhr) {                                  
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }   
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                oHtml.stopPage();                                                 
                oUIVMContorl.VMListTask(true);          
                StartPolling(true,false);
            });
            request.fail(function(jqxhr, textStatus) {                  
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.VMDeleteTask);  
                StartPolling(true,true);
            });
        }
        
        oUIVMContorl.ClearVMTaskInterval = function(){                    
            VMTaskStart = false;
        };
        
        oUIVMContorl.VMListTask = function(bolStartPolling){         
            oHtml.blockPage();
            var request = oRelayAjax.VMListTask();  
            CallBackVMListTask(request,bolStartPolling);
        };
        
        function CallBackVMListTask(request,bolStartPolling){
            request.done(function(msg, statustext, jqxhr) {                                 
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }  
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                oHtml.stopPage();                                          
                oHtml.CreateVMTaskTable(msg);
                if(typeof(NowSortTaskType)=== 'undefined' || NowSortTaskType === null)
                {
                    $('.label_task_arrow').html('');
                    $('#taskstart_time1').html('&#9660;');
                    var th = $('.VMList_Start_Time');
                    th.removeClass('asc');
                    th.addClass('desc');
                    NowSortTaskDirection = SortDirection.Desc;
                    NowSortTaskType = SortType.String;
                    NowSortTaskDOMID = 'start_time';
                }                        
                SortList(NowSortTaskDirection,NowSortTaskType,NowSortTaskDOMID,VMListCloneTable); 
                $('#Div_VM_Clone_List').html(oHtml.CreateVMTaskHtmlBTable());
                //  新增任務的備註tooltip顯示 2016.12.20 by william
                $('#Div_VM_Clone_List').tooltip({
                    position: { my: "left top+0", at: "left bottom", collision: "flipfit" }
                });
                $.each(VMListCloneTable,function(index,element){
                    if(element['status'] >= 0 && element['status'] !== 3){
                        $('#progress' + index).progressbar({
                            value: Math.floor( element['process'] )
                        });                               
                        $('#progressText' + index).text(element['process'] + '%');                            
                    }
                }); 
                if(bolStartPolling){
                    PollingListTaskFlag = true;
                    PollingVMListTask(true);
                }
            });
            request.fail(function(jqxhr, textStatus) {      
                if(bolStartPolling){
                    PollingListTaskFlag = true;
                    PollingVMListTask(true);
                }
            });
        }
        
        function CheckForPollingListTask(){ 
            if(!VMTaskStart){
                isRunningPollingTask = false;
                return false;
            }
            if(PollingListTaskFlag){
                setTimeout(function(){PollingVMListTask(false);},8000); // 避免頻繁的Request,將5秒改成8秒 2017.03.10 william
            }
            else{
                setTimeout(function(){CheckForPollingListTask();},1000);
            }
        }
        
        function PollingVMListTask(checkRunning){
            if(checkRunning && isRunningPollingTask)
                return false;
            isRunningPollingTask = true;
//            console.log("Send Task List"); 
            var request = oRelayAjax.VMListTask();  
            CallBackPollingVMListTask(request);
        }
        
        function CallBackPollingVMListTask(request){
            request.done(function(msg, statustext, jqxhr) {  
                 if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                        Logout_Another_Admin_Login();
                        return false;
                    } 
                    else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                try{
                    $.each(msg,function(index,element){
                        if(typeof(element['StartTime']) !== 'undefined'){
                            var start_time = element['StartTime'];
                            if(VMListCloneDestName[element['ID']]['start_time'] !== start_time)
                            {
                                VMListCloneDestName[element['ID']]['start_time'] = start_time;
                                $('#start_time' + element['ID']).text(start_time);
                            }                            
                        }
                        if(typeof(element['EndTime']) !== 'undefined'){
                            var end_time = element['EndTime'];
                            if(VMListCloneDestName[element['ID']]['end_time'] !== end_time)
                            {
                                VMListCloneDestName[element['ID']]['end_time'] = end_time;
                                $('#end_time' + element['ID']).text(end_time);
                            }                            
                        }
                        if(VMListCloneDestName[element['ID']]['status'] !== element['State']){
                            VMListCloneDestName[element['ID']]['status'] = element['State'];
                            $('#clonestatus' + element['ID']).text(oHtml.CreateVMStatusText(element['status']));
                            // if(element['State'] === 10 || element['State'] < 0)
                            //     $('#divprogress' + element['ID']).html('');
                        }
                        if(element['State'] === 2){
                            VMListCloneDestName[element['ID']]['process'] = element['Process'];                           
                        }
                    });
                    SortList(NowSortTaskDirection,NowSortTaskType,NowSortTaskDOMID,VMListCloneTable); 
                    $('#Div_VM_Clone_List').html(oHtml.CreateVMTaskHtmlBTable());
                    $.each(VMListCloneTable,function(index,element){
                        if(element['status'] === 2){
                            $('#progress' + index).progressbar({
                                value: Math.floor( element['process'] )
                            });                               
                            $('#progressText' + index).text(element['process'] + '%');                            
                        }
                        else{
                            $('#divprogress' + index).html('');
                        }
                    });
                    CheckForPollingListTask();     
                }
                catch(e){
                    CheckForPollingListTask();
                }
            });
            request.fail(function(jqxhr, textStatus) {     
                CheckForPollingListTask();
            });
        }
               
        function ListSuspendSetting(){
            oHtml.blockPage();
            var request = oRelayAjax.ListSuspendSetting();  
            CallBackListSuspendSetting(request);
        }   
        
        function CallBackListSuspendSetting(request){
            request.done(function(msg, statustext, jqxhr) {                                 
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }       
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                oHtml.stopPage();                                          
                $('#spinner_suspend').spinner('value',msg['Suspend']/60);
            });
            request.fail(function(jqxhr, textStatus) {                      
            });
        }
        
        function SetSuspendSetting(mins){
            oHtml.blockPage();
            var request = oRelayAjax.SetSuspendSetting(mins);  
            CallBackSetSuspendSetting(request);
        }   
        
        function CallBackSetSuspendSetting(request){
            request.done(function(msg, statustext, jqxhr) {                                 
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }    
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                ListSuspendSetting();
            });
            request.fail(function(jqxhr, textStatus) {                                                                
                ListSuspendSetting();
            });
        }
        
        function VMListDiskImg(isModify){
            StopPolling();
            oHtml.blockPage();
            var request = oRelayAjax.VMListDiskImgs();  
            CallBackVMListDiskImg(request,isModify);
        };
        
        function CallBackVMListDiskImg(request,isModify){
            request.done(function(msg, statustext, jqxhr) {                                  
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }   
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                if(isModify){
//                    $('#div_modify_existing_disk').html(oHtml.CreateDiskImgCombo(msg,isModify));
//                    $('#combo_modify_img').scombobox({
//                        editAble:false,
//                        wrap:false       
//                    });             
//                    if(msg['DiskImgs'].length === 0){
//                        $('#btn_add_vm_existing_disk').button("disable");
//                        $('#combo_modify_img').scombobox('disabled',true);
//                    }    
                    oHtml.stopPage();   
                    StartPolling(true,true);
                }
                else{                    
//                    $('#div_existing_disk').html(oHtml.CreateDiskImgCombo(msg,isModify));
//                    $('#combo_img').scombobox({
//                        editAble:false,
//                        wrap:false       
//                    });             
//                    if(msg['DiskImgs'].length === 0){
//                        $('#radio-input-file').iCheck('disable');
//                        $('#combo_img').scombobox('disabled',true);
//                    }                
                    VMListIsos();
                }                    
            });
            request.fail(function(jqxhr, textStatus) {                  
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.VMListImg); 
                StartPolling(true,true);
            });
        }
        
        function VMListIsos(){
            StopPolling();
            if(typeof for_boot == 'undefined') {
                for_boot=false;
            }
            oHtml.blockPage();
            var request = oRelayAjax.VMListIsos();  
            CallBackVMListIsos(request);
        };
        
        function CallBackVMListIsos(request,for_boot){
            request.done(function(msg, statustext, jqxhr) {                                  
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }     
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                $('#div_install_iso').html(oHtml.CreateIsoCombo(msg));
                $('#combo_iso').scombobox({
                    editAble:false,
                    wrap:false       
                });                
                oHtml.stopPage();
                StartPolling(true,true);
            });
            request.fail(function(jqxhr, textStatus) {                  
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.VMListImg);    
                StartPolling(true,true);
            });
        }
         // VD手動快照介面功能實作 2017.01.25   
        /*=================== VD手動快照 (1)新增 2017.01.25 Start ===================*/ 
        function InitTakeSnapshotButton() {
            $('#btn_Take_Snapshot').click(function (event) {            
                event.preventDefault();  

                var desc = $('#Takess_desc_input').val();   
                desc = desc.replace(/"/g, '\\"');    
                       
                VMSnapshotCreate(vmid,desc);
            });
        }   

        function VMSnapshotCreate(vmid, desc) {
            StopPolling();
            oHtml.blockPage();
            var request = oRelayAjax.TakeSnapshot(vmid, desc);  
            CallBackVMSnapshotCreate(request);
        };

        function CallBackVMSnapshotCreate(request){
            request.done(function(msg, statustext, jqxhr) {                 
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }          
                // $( "#dialog_vm_snapshot" ).dialog('close');
                 oUIVMContorl.VMList(true,true);      
                 oUIVMContorl.VMListSnapshotTask(true);    
                 StartPolling(true,true);
                 $('#Takess_desc_input').val('');
                // $('#tab-vm-container').easytabs('select', '#tabs1-sstask-list'); // 跳到snapshot任務清單
            });
            request.fail(function(jqxhr, textStatus) {                  
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.VMSnapshotCreate);
            });
        }      
        /*=================== VD手動快照 (1)新增 2017.01.25 End ===================*/ 
        /*=================== VD手動快照 (2)刪除 2017.01.25 Start ===================*/   
        function VMDeleteSnapshot(idLayer){
            StopPolling();
            oHtml.blockPage();
            var request = oRelayAjax.DeleteSnapshot(idLayer);  
            CallBackVMDeleteSnapshot(request, name);
        };      

        function CallBackVMDeleteSnapshot(request,name){
            request.done(function(msg, statustext, jqxhr) {                                  
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }    
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
//                console.log("Complete Delete");

                // $( "#dialog_vm_snapshot" ).dialog('close');
                 oUIVMContorl.VMList(true,true);      
                 oUIVMContorl.VMListSnapshotTask(true);    
                 StartPolling(true,true);
                 // $('#tab-vm-container').easytabs('select', '#tabs1-sstask-list'); // 跳到snapshot任務清單

                /*               
                if(name.length === 0)
                {
                    oHtml.stopPage();
                //    oUIVMContorl.VMList(true,true);
                    StartPolling(false,true);
                }
                else
                    VMDeleteSnapshot(name); 
                 */   
            });
            request.fail(function(jqxhr, textStatus) {                  
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.VMDeleteSnapshot);
                VMDeleteSnapshot(name);
            });
        }        
        /*=================== VD手動快照 (2)刪除 2017.01.25 End ===================*/  
        /*=================== VD手動快照 (3)清單(查詢) 2017.01.25 Start ===================*/                    
        function GetVMSnapshotList(vmid, bolStartPolling){
            oHtml.blockPage();
            var request = oRelayAjax.listSnapshot(vmid);  
            CallBackGetSnapshot(request, bolStartPolling, vmid);
        };   

        function CallBackGetSnapshot(request, bolStartPolling, vmid){
            oHtml.blockPage();
            request.done(function(msg, statustext, jqxhr) {                                
                oHtml.stopPage();
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;                
                }
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                var rtnHtml = oHtml.processSnapshotData(msg); // UI畫面顯示功能實作 2017.01.25    
                $('#Takess_ListBottom').empty(); // UI介面功能實作 2017.01.25    
                $('#Takess_ListBottom').append(rtnHtml); // UI介面功能實作 2017.01.25 
                GetSnapshotIdLayer(); 
               // $('#Takess_ListBottom').html(rtnHtml); // UI介面功能實作 2017.01.25  

                if(bolStartPolling){
                    PollingListFlag = true;
                    PollingVMSnapshotList(true, vmid);
                }
            });
            request.fail(function(jqxhr, textStatus) { // UI介面功能實作 2017.01.25
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.GetVMSnapshotList);

                if(bolStartPolling){
                    PollingListFlag = true;
                    PollingVMSnapshotList(true, vmid);
                }

            });
        };

        function CheckForPollingSnapshotList(){ 
            if(!SnapshotListStart){
                isRunningPollingSnapshotList = false;
                return false;
            }
            if(PollingListFlag){
                setTimeout(function(){PollingVMSnapshotList(false, vmid);},8000);   // 避免頻繁的Request,將5秒改成8秒 2017.03.10 william
            }
            else{
                setTimeout(function(){CheckForPollingSnapshotList();},1000);
            }
        }

        function PollingVMSnapshotList(checkRunning, vmid){
            if(checkRunning && isRunningPollingSnapshotList)
                return false;
            isRunningPollingSnapshotList = true;
            var request = oRelayAjax.listSnapshot(vmid);  
            CallBackPollingVMSnapshotList(request);
        }

        function CallBackPollingVMSnapshotList(request){
            request.done(function(msg, statustext, jqxhr) {  
                 if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                        Logout_Another_Admin_Login();
                        return false;
                    } 
                    else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                try{
                    var rtnHtml = oHtml.processSnapshotData(msg);
                    $('#Takess_ListBottom').empty(); 
                    $('#Takess_ListBottom').append(rtnHtml); 
                    GetSnapshotIdLayer(); 

                    CheckForPollingSnapshotList();     
                }
                catch(e){
                    CheckForPollingSnapshotList();
                }
            });
            request.fail(function(jqxhr, textStatus) {     
                CheckForPollingSnapshotList();
            });
        }  

        function GetSnapshotIdLayer() {
            $('.SnapShotDropBtn').click(function(event){                               
                event.preventDefault();

                var btn_element = $(this);

                idLayer = btn_element.attr('id');
            });  
        }          
        /*=================== VD手動快照 (3)清單(查詢)  2017.01.25 End ===================*/  
        /*=================== VD手動快照 (4)Task 2017.01.25 Start ===================*/ 
        function InitVMSSTaskButtons(){
            $('#btn_clear_sstask').click(function(event){
                event.preventDefault();
                VMClearSnapShotTask();
            });
            $('#btn_refresh_sstask').click(function(event){
                event.preventDefault();
                StopPolling();
                oUIVMContorl.VMListSnapshotTask(true);
                StartPolling(true,false);            
            });
        } 

        function VMClearSnapShotTask(){
            StopPolling();
            oHtml.blockPage();
            var request = oRelayAjax.VMClearSnapShotTask();  
            CallBackVMClearSnapShotTask(request);
        }

        function CallBackVMClearSnapShotTask(request){
            request.done(function(msg, statustext, jqxhr) {                                  
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }   
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                oHtml.stopPage();                                                 
                oUIVMContorl.VMListSnapshotTask(true);          
                StartPolling(true,true);
            });
            request.fail(function(jqxhr, textStatus) {                  
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.VMDeleteSnapShotTask);  
                StartPolling(true,true);
            });
        }

        oUIVMContorl.ClearSnapshotTaskInterval = function(){                    
            SnapshotTaskStart = false;
        };
        
        oUIVMContorl.VMListSnapshotTask = function(bolStartPolling){         
            oHtml.blockPage();
            var request = oRelayAjax.VMListSnapshotTask();  
            CallBackVMListSnapshotTask(request,bolStartPolling);
        };
        
        function CallBackVMListSnapshotTask(request,bolStartPolling){
            request.done(function(msg, statustext, jqxhr) {                                 
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }  
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                oHtml.stopPage();                                          
                oHtml.CreateVMSnapshotTaskTable(msg);
            //     if(typeof(NowSortSnapshotTaskType)=== 'undefined' || NowSortSnapshotTaskType === null)
            //     {
            //         $('.label_sstask_arrow').html('');
            //         $('#sstaskSSListStartTime1').html('&#9660;');
            //         var th = $('.VMSnapshotList_Task_StartTime');
            //         th.removeClass('asc');
            //         th.addClass('desc');
            //         NowSortSnapshotTaskDirection = SortDirection.Desc;
            //         NowSortSnapshotTaskType = SortType.String;
            //         NowSortSnapshotTaskDOMID = 'SSListStartTime';
            //     }                        
            //    SortList(NowSortSnapshotTaskDirection,NowSortSnapshotTaskType,NowSortSnapshotTaskDOMID,VMListSnapshotCloneTable); 
                $('#Div_VMSnapshot_Clone_List').html(oHtml.CreateVMSnapshotTaskHtmlBTable());
                $('#Div_VMSnapshot_Clone_List').tooltip({
                    position: { my: "left top+0", at: "left bottom", collision: "flipfit" }
                });
                // $.each(VMListSnapshotCloneTable,function(index,element){
                //     if(element['status'] >= 0 && element['status'] !== 3){
                //         $('#progress' + element['id']).progressbar({
                //             value: Math.floor( element['process'] )
                //         });                               
                //         $('#progressText' + element['id']).text(element['process'] + '%');                            
                //     }
                // }); 
                if(bolStartPolling){
                    PollingListTaskFlag = true;
                    PollingVMListSnapshotTask(true);
                }
            });
            request.fail(function(jqxhr, textStatus) {      
                if(bolStartPolling){
                    PollingListTaskFlag = true;
                    PollingVMListSnapshotTask(true);
                }
            });
        }
        
        function CheckForPollingListSnapshotTask(){ 
            if(!SnapshotTaskStart){
                isRunningPollingSnapshotTask = false;
                return false;
            }
            if(PollingListTaskFlag){
                setTimeout(function(){PollingVMListSnapshotTask(false);},8000); // 避免頻繁的Request,將5秒改成8秒 2017.03.10 william
            }
            else{
                setTimeout(function(){CheckForPollingListSnapshotTask();},1000);
            }
        }
        
        function PollingVMListSnapshotTask(checkRunning){
            if(checkRunning && isRunningPollingSnapshotTask)
                return false;
            isRunningPollingSnapshotTask = true;
//            console.log("Send Task List"); 
            var request = oRelayAjax.VMListSnapshotTask();  
            CallBackPollingVMListSnapshotTask(request);
        }
        
        function CallBackPollingVMListSnapshotTask(request){
            request.done(function(msg, statustext, jqxhr) {  
                 if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                        Logout_Another_Admin_Login();
                        return false;
                    } 
                    else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }
                try{
                    $.each(msg,function(index,element){
                        if(typeof(element['StartTime']) !== 'undefined'){
                            var start_time = element['StartTime'];
                            if(VMListSnapshotCloneDestName[element['ID']]['start_time'] !== start_time)
                            {
                                VMListSnapshotCloneDestName[element['ID']]['start_time'] = start_time;
                                $('#start_time' + element['ID']).text(start_time);
                            }                            
                        }
                        if(typeof(element['EndTime']) !== 'undefined'){
                            var end_time = element['EndTime'];
                            if(VMListSnapshotCloneDestName[element['ID']]['end_time'] !== end_time)
                            {
                                VMListSnapshotCloneDestName[element['ID']]['end_time'] = end_time;
                                $('#end_time' + element['ID']).text(end_time);
                            }                            
                        }
                        if(VMListSnapshotCloneDestName[element['ID']]['state'] !== element['State']){
                            VMListSnapshotCloneDestName[element['ID']]['state'] = element['State'];
                            $('#ssclonestatus' + element['ID']).text(oHtml.CreateVMStatusText(element['State']));
                            // if(element['State'] === 10 || element['State'] < 0)
                            //     $('#divprogress' + element['ID']).html('');
                        }
                        if(VMListSnapshotCloneDestName[element['ID']]['layerdate'] !== element['LayerDate']){
                            VMListSnapshotCloneDestName[element['ID']]['layerdate'] = element['LayerDate'];
                            $('#layerdate' + element['ID']).text(oHtml.CreateVMStatusText(element['layerdate']));
                            // if(element['State'] === 10 || element['State'] < 0)
                            //     $('#divprogress' + element['ID']).html('');
                        }
                        // if(element['State'] >= 0 && element['State'] !== 3){
                        //     VMListCloneDestName[element['ID']]['process'] = element['Process'];
                        //     $('#progress' + element['ID']).progressbar('option','value',Math.floor(element['Process']));
                        //     $('#progressText' + element['ID']).text(element['Process'] + '%');
                        // }
                    });
    //               SortList(NowSortSnapshotTaskDirection,NowSortSnapshotTaskType,NowSortSnapshotTaskDOMID,VMListSnapshotCloneTable); 
                    $('#Div_VMSnapshot_Clone_List').html(oHtml.CreateVMSnapshotTaskHtmlBTable());
                    // $.each(VMListSnapshotCloneTable,function(index,element){
                    //     if(element['status'] >= 0 && element['status'] !== 3){
                    //         $('#progress' + element['id']).progressbar({
                    //             value: Math.floor( element['process'] )
                    //         });                               
                    //         $('#progressText' + element['id']).text(element['process'] + '%');                            
                    //     }
                    // });
                    CheckForPollingListSnapshotTask();     
                }
                catch(e){
                    CheckForPollingListSnapshotTask();
                }
            });
            request.fail(function(jqxhr, textStatus) {     
                CheckForPollingListSnapshotTask();
            });
        }      
     
        function SnapshotTaskHederClickSort(){
            $('.SSTaskHeader').click(function(event){
                    StopPolling();
                    event.preventDefault();
                    var th = $(this);
                    var eSortType = SortType.String;
                    var eSortDirection = SortDirection.ASC;                    
                    if(th.attr('id') === "state" || th.attr('id') === "Type")
                        eSortType = SortType.Num;
                    if(th.hasClass('asc')){
                        $('.SSTaskHeader').removeClass('asc');
                        $('.SSTaskHeader').removeClass('desc');
                        $('.label_sstask_arrow').html('');
                        $('#sstask' + th.attr('id') + '1').html('&#9660;');
                        th.addClass('desc');
                        eSortDirection = SortDirection.Desc;
                    }
                    else{
                        $('.SSTaskHeader').removeClass('asc');
                        $('.SSTaskHeader').removeClass('desc');
                        $('.label_sstask_arrow').html('');
                        $('#sstask' + th.attr('id') + '1').html('&#9650;');
                        th.addClass('asc');
                        eSortDirection = SortDirection.ASC;
                    }
                    NowSortSnapshotTaskType = eSortType;
                    NowSortSnapshotTaskDirection = eSortDirection;
                    NowSortSnapshotTaskDOMID = th.attr('id');
                    SortList(eSortDirection,eSortType,th.attr('id'),VMListSnapshotCloneTable);                        
                    $('#Div_VMSnapshot_Clone_List').html(oHtml.CreateVMSnapshotTaskHtmlBTable()); 
                    // $.each(VMListSnapshotCloneTable,function(index,element){
                    //     if(element['status'] >= 0 && element['status'] !== 3){
                    //         $('#progress' + element['id']).progressbar({
                    //             value: Math.floor( element['process'] )
                    //         });                               
                    //         $('#progressText' + element['id']).text(element['process'] + '%');                            
                    //     }
                    // });                    
                    StartPolling(true,true);
                });            
        }
        /*=================== VD手動快照 (4)Task 2017.01.25 End ===================*/        
        /*=================== VD手動快照 (5)Rollback 2017.02.02 Start ===================*/
        function VMSnapshotRollback(idLayer) { 
            StopPolling();
            oHtml.blockPage();
            var request = oRelayAjax.RollbackSnapshot(idLayer);  
            CallBackVMSnapshotRollback(request);
        };

        function CallBackVMSnapshotRollback(request){
            request.done(function(msg, statustext, jqxhr) {                 
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }          
                // $( "#dialog_vm_snapshot" ).dialog('close');
                 oUIVMContorl.VMList(true,true);      
                 oUIVMContorl.VMListSnapshotTask(true);    
                 StartPolling(true,true);
                 // $('#tab-vm-container').easytabs('select', '#tabs1-sstask-list'); // 跳到snapshot任務清單
            });
            request.fail(function(jqxhr, textStatus) {                  
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.VMSnapshotRollback);
                // if(jqxhr.status == 400){
                //     var fail_msg = (LanguageStr_VM.VM_Create_Fail).replace("@",VMorVDI);
                //     alert(fail_msg);
                // }
                // else if(jqxhr.status == 409){
                //     var fail_msg = (LanguageStr_VM.VM_Name_Duplicate).replace("@",VMorVDI);
                //     alert(fail_msg);
                // }
                // else{
                //     oError.CheckAuth(jqxhr.status,ActionStatus.VMSnapshotCreate);
                // }
            //  StartPolling(true,true);
            });
        }               
        /*=================== VD手動快照 (5)Rollback 2017.02.02 End ===================*/            
        /*=================== VD手動快照 (6)檢視 2017.02.02 Start ===================*/
        function VMSnapshotView(idLayer) {
            StopPolling();
            oHtml.blockPage();
            var request = oRelayAjax.ViewSnapshot(idLayer);  
            CallBackVMSnapshotView(request);
        };

        function CallBackVMSnapshotView(request){
            request.done(function(msg, statustext, jqxhr) {                 
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }          
                 // $( "#dialog_vm_snapshot" ).dialog('close');
                 oUIVMContorl.VMList(true,true);      
                 oUIVMContorl.VMListSnapshotTask(true);    
                 StartPolling(true,true);
                 // $('#tab-vm-container').easytabs('select', '#tabs1-sstask-list'); // 跳到snapshot任務清單
            });
            request.fail(function(jqxhr, textStatus) {                  
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.VMSnapshotView);
                // if(jqxhr.status == 400){
                //     var fail_msg = (LanguageStr_VM.VM_Create_Fail).replace("@",VMorVDI);
                //     alert(fail_msg);
                // }
                // else if(jqxhr.status == 409){
                //     var fail_msg = (LanguageStr_VM.VM_Name_Duplicate).replace("@",VMorVDI);
                //     alert(fail_msg);
                // }
                // else{
                //     oError.CheckAuth(jqxhr.status,ActionStatus.VMSnapshotCreate);
                // }
            //  StartPolling(true,true);
            });
        }                
        /*=================== VD手動快照 (6)檢視 2017.02.02 End ===================*/                   
        /*=================== VD手動快照 (7)停止檢視 2017.02.02 Start ===================*/  
        function InitStopViewSnapshotButton() {
            $('#btn_StopView_snapshot').click(function (event) {            
                event.preventDefault();  
                VMSnapshotStopView(vmid);
            });
        }  

        function VMSnapshotStopView(vmid) {
            StopPolling();
            oHtml.blockPage();
            var request = oRelayAjax.StopViewSnapshot(vmid);  
            CallBackVMSnapshotStopView(request);
        };

        function CallBackVMSnapshotStopView(request){
            request.done(function(msg, statustext, jqxhr) {                 
                if(typeof(msg['status']) !== "undefined" && msg['status'] == 2){
                    Logout_Another_Admin_Login();
                    return false;
                }
                else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3){
                    window.location.href = 'index.php';
                    return false;
                }          
                 // $( "#dialog_vm_snapshot" ).dialog('close');
                 oUIVMContorl.VMList(true,true);      
                 oUIVMContorl.VMListSnapshotTask(true);    
                 StartPolling(true,true);
                 // $('#tab-vm-container').easytabs('select', '#tabs1-sstask-list'); // 跳到snapshot任務清單
            });
            request.fail(function(jqxhr, textStatus) {                  
                oHtml.stopPage();
                oError.CheckAuth(jqxhr.status,ActionStatus.VMSnapshotStopView);
                // if(jqxhr.status == 400){
                //     var fail_msg = (LanguageStr_VM.VM_Create_Fail).replace("@",VMorVDI);
                //     alert(fail_msg);
                // }
                // else if(jqxhr.status == 409){
                //     var fail_msg = (LanguageStr_VM.VM_Name_Duplicate).replace("@",VMorVDI);
                //     alert(fail_msg);
                // }
                // else{
                //     oError.CheckAuth(jqxhr.status,ActionStatus.VMSnapshotCreate);
                // }
            //  StartPolling(true,true);
            });
        }              
        /*=================== VD手動快照 (7)停止檢視 2017.02.02 End ===================*/                  
        /*=================== 新增快照層數 2017.11.22 Start ===================*/    
        function GetSnapshotLayer(vmid){
            oHtml.blockPage();
            var request = oRelayAjax.SnapshotLayerList(vmid);  
            CallBackGetSnapshotLayer(request);
        };  

        function CallBackGetSnapshotLayer(request) {
            request.done(function (msg, statustext, jqxhr) {
                if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
                    Logout_Another_Admin_Login();
                    return false;
                }
                else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
                    window.location.href = 'index.php';
                    return false;
                }
                sslayervalue = Number(msg['SSLayerMax']);     
                $("#SnapshotUsedLayer_input").val(sslayervalue);  
            });
            request.fail(function (jqxhr, textStatus) {
            });
        }        

        function InitModifySnapshotLayerButton() {
            $('#btn_modify_ss_layer').click(function (event) {            
                event.preventDefault();  
                sslayervalue = $("#SnapshotUsedLayer_input").val();

                if(sslayervalue.length === 0)
                {
                    alert(LanguageStr_VM.VM_SnapShotLayer_Empty_Warning);
                    return false;
                }                   
                if(sslayervalue < 30)
                {
                    alert(LanguageStr_VM.VM_SnapShotLayer_Error1);
                    return false;
                }      
                if(sslayervalue > 1000)
                {
                    alert(LanguageStr_VM.VM_SnapShotLayer_Error2);
                    return false;
                }                                 
                ModifySnapshotLayer(vmid,sslayervalue);
            });
        }   

        function ModifySnapshotLayer(vmid,sslayervalue){
            var jsondata = '{';
            jsondata += '"SSLayerMax" : ' + sslayervalue;
            jsondata += '}';
            $('.Div_WaitAjax').dialog( 'open' );            
            var request = oRelayAjax.ModifySnapshotLayer(vmid,jsondata);  
            CallBackModifySnapshotLayer(request);
        };          

        function CallBackModifySnapshotLayer(request) {
            request.done(function (msg, statustext, jqxhr) {
                if (typeof (msg['status']) !== "undefined" && msg['status'] == 2) {
                    Logout_Another_Admin_Login();
                    return false;
                }
                else if (typeof (msg['status']) !== "undefined" && msg['status'] == 3) {
                    window.location.href = 'index.php';
                    return false;
                }
                $('.Div_WaitAjax').dialog( 'close' );
                sslayervalue = Number(msg['SSLayerMax']);     
                $("#SnapshotUsedLayer_input").val(sslayervalue);  
            });
            request.fail(function (jqxhr, textStatus) {
                $('.Div_WaitAjax').dialog( 'close' );
            });
        }

        /*=================== 新增快照層數 2017.11.22 End ===================*/

        /* 2018.01.08 william 列表標題及內容的文字位置調整，針對中英文版式樣作不同的顯示標頭位置對齊 */         
        function VM_langStyleChange() {    
            
            switch(SysCfg_lang_ver){
                case 'zh-tw':
                    VM_langStyle_zh_tw();
                    break;
                case 'zh-cn': 
                    VM_langStyle_zh_cn();
                    break;
                case 'en-us':
                    
                    break;       
            } 
        }

    function VM_langStyle_zh_tw() {
        // 原生桌面 - title
        $(".VMList_ItemNo").css({"left": "11px","top": "0px"});                   // 項次
        $(".VMList_Select").css({"top": "0px"});                                  // 全選
        $(".VMList_Username").css({"top": "0px", "width": "210px"});              // 用戶名稱
        $(".VMList_Name").css({"left": "300px","top": "0px","width": "210px"});   // 桌面名稱
        $(".VMList_Auto_Suspend_Select").css({"top": "0px"});                     // 自動暫停-全選
        $(".VMList_Auto_Suspend").css({"top": "0px"});                            // 自動暫停
        $(".VMList_Status").css({"top": "0px","left": "708px"});                  // 桌面狀態
        $(".VMList_Online").css({"left": "859px","top": "0px","width": "125px"}); // 上線狀態 
        $(".VMList_RAM").css({"left": "1035px","top": "0px","width": "130px"});   // 記憶體
        $(".VMList_Action").css({"left": "1190px","top": "0px"});                 // 動作
        $(".VMList_CreateTime").css({"left": "1353px","top": "0px"});             // 建立時間
        $(".VMList_AllSize").css({"left": "1565px","top": "0px"});                // 檔案大小
        $(".VMList_Desc").css({"left": "1700px","top": "0px"});                   // 備註

        // 種子桌面 - title
        $(".SeedList_Select").css({"top": "0px"});                                    // 全選
        $(".SeedList_Name").css({"top": "0px"});                                      // 桌面名稱
        $(".SeedList_VDICount").css({"left": "475px","top": "0px","width": "180px"}); // 用戶桌面數量 
        $(".SeedList_RAM").css({"left": "655px","top": "0px"});                       // 記憶體
        $(".SeedList_CreateTime").css({"left": "885px","top": "0px"});                // 建立時間
        $(".SeedList_AllSize").css({"left": "1100px","top": "0px"});                   // 檔案大小
        $(".SeedList_Action").css({"left": "1225px","top": "0px"});                   // 動作
        $(".SeedList_Description").css({"top": "0px"});                               // 備註

        // 用戶桌面 - title
        $(".BornedList_Select").css({"top": "0px"});                                  // 全選
        $(".BornedList_Username").css({"left": "87px","top": "0px"});                 // 用戶名稱
        $(".BornedList_Name").css({"top": "0px"});                                    // 桌面名稱
        $(".BornedList_Auto_Suspend_Select").css({"top": "0px"});                     // 自動暫停-全選
        $(".BornedList_Auto_Suspend").css({"top": "0px"});                            // 自動暫停
        $(".BornedList_Status").css({"left": "708px","top": "0px"});                  // 桌面狀態
        $(".BornedList_Online").css({"left": "859px","top": "0px","width": "120px"}); // 上線狀態 
        $(".BornedList_RAM").css({"left": "1037px","top": "0px"});                    // 記憶體
        $(".BornedList_Action").css({"left": "1194px","top": "0px"});                 // 動作
        $(".BornedList_SeedSrc").css({"left": "1340px","top": "0px"});                // 桌面來源 
        $(".BornedList_CreateTime").css({"left": "1606px","top": "0px"});             // 建立時間
        $(".BornedList_AllSize").css({"left": "1820px","top": "0px"});                // 檔案大小
        $(".BornedList_Desc").css({"left": "1955px","top": "0px"});                   // 備註

        $(".VMList_Task_ItemNo").css({"top": "0px"});
        $(".VMList_Type").css({"top": "0px"});
        $(".VMList_Source_Name").css({"top": "0px"});
        $(".VMList_Dest_Name").css({"top": "0px"});
        $(".VMList_Task_Progress").css({"top": "0px"});
        $(".VMList_Task_Status").css({"top": "0px"});
        $(".VMList_Start_Time").css({"top": "0px"});
        $(".VMList_End_Time").css({"top": "0px"});
    }

    function VM_langStyle_zh_cn() {
        // 原生桌面 - title
        $(".VMList_ItemNo").css({"left": "11px","top": "0px"});                   // 項次
        $(".VMList_Select").css({"top": "0px"});                                  // 全選
        $(".VMList_Username").css({"top": "0px", "width": "210px"});              // 用戶名稱
        $(".VMList_Name").css({"left": "300px","top": "0px","width": "210px"});   // 桌面名稱
        $(".VMList_Auto_Suspend_Select").css({"top": "0px"});                     // 自動暫停-全選
        $(".VMList_Auto_Suspend").css({"top": "0px"});                            // 自動暫停
        $(".VMList_Status").css({"top": "0px","left": "708px"});                  // 桌面狀態
        $(".VMList_Online").css({"left": "859px","top": "0px","width": "125px"}); // 上線狀態 
        $(".VMList_RAM").css({"left": "1052px","top": "0px","width": "130px"});   // 記憶體
        $(".VMList_Action").css({"left": "1190px","top": "0px"});                 // 動作
        $(".VMList_CreateTime").css({"left": "1353px","top": "0px"});             // 建立時間
        $(".VMList_AllSize").css({"left": "1565px","top": "0px"});                // 檔案大小
        $(".VMList_Desc").css({"left": "1700px","top": "0px"});                   // 備註

        // 種子桌面 - title
        $(".SeedList_Select").css({"top": "0px"});                                    // 全選
        $(".SeedList_Name").css({"top": "0px"});                                      // 桌面名稱
        $(".SeedList_VDICount").css({"left": "475px","top": "0px","width": "180px"}); // 用戶桌面數量 
        $(".SeedList_RAM").css({"left": "675px","top": "0px"});                       // 記憶體
        $(".SeedList_CreateTime").css({"left": "885px","top": "0px"});                // 建立時間
        $(".SeedList_AllSize").css({"left": "1100px","top": "0px"});                   // 檔案大小
        $(".SeedList_Action").css({"left": "1225px","top": "0px"});                   // 動作
        $(".SeedList_Description").css({"top": "0px"});                               // 備註

        // 用戶桌面 - title
        $(".BornedList_Select").css({"top": "0px"});                                  // 全選
        $(".BornedList_Username").css({"left": "87px","top": "0px"});                 // 用戶名稱
        $(".BornedList_Name").css({"top": "0px"});                                    // 桌面名稱
        $(".BornedList_Auto_Suspend_Select").css({"top": "0px"});                     // 自動暫停-全選
        $(".BornedList_Auto_Suspend").css({"top": "0px"});                            // 自動暫停
        $(".BornedList_Status").css({"left": "708px","top": "0px"});                  // 桌面狀態
        $(".BornedList_Online").css({"left": "859px","top": "0px","width": "120px"}); // 上線狀態 
        $(".BornedList_RAM").css({"left": "1053px","top": "0px"});                    // 記憶體
        $(".BornedList_Action").css({"left": "1191px","top": "0px"});                 // 動作
        $(".BornedList_SeedSrc").css({"left": "1340px","top": "0px"});                // 桌面來源 
        $(".BornedList_CreateTime").css({"left": "1606px","top": "0px"});             // 建立時間
        $(".BornedList_AllSize").css({"left": "1820px","top": "0px"});                // 檔案大小
        $(".BornedList_Desc").css({"left": "1955px","top": "0px"});                   // 備註

        $(".VMList_Task_ItemNo").css({"top": "0px"});
        $(".VMList_Type").css({"top": "0px"});
        $(".VMList_Source_Name").css({"top": "0px"});
        $(".VMList_Dest_Name").css({"top": "0px"});
        $(".VMList_Task_Progress").css({"top": "0px"});
        $(".VMList_Task_Status").css({"top": "0px"});
        $(".VMList_Start_Time").css({"top": "0px"});
        $(".VMList_End_Time").css({"top": "0px"});
    }

    function VM_langStyle_en_us() {

    }    

    // 1. Get data
    function CreateVM_GetUserList() {
        VMGetUserDataList();
        
    }
    function CreateVM_GetUserList_withcheckbox() {
        VMGetUserDataList();
        
    }    
    // 1-1.
    function CreateSeedListHtml(name) {
        var HtmlStr = "";
        var no = 1;
        var i;
        getseedarr = [];

        ChangeVMNametoID(name); 

        for(i = 0; i < SeedNameArray.length; i++) {
            HtmlStr += '<div class="Dialog_Seeddata_row">';
            HtmlStr += '<div class="Dialog_no_Seed">' + no + '</div>';
            getseedarr = SeedNameArray[i].split(":");    
            HtmlStr += '<div class="Dialog_column_Seed">' + getseedarr[0] + '</div>';
            HtmlStr += '<div class="Dialog_Seed_action">';        
            HtmlStr += '<a id="overwriteseed:' + getseedarr[1] + ":" + vmid + '" class="btn-overwriteseed btn-jquery" style="font-size: smaller">' + LanguageStr_langSysCfgUserMgt.OverWrite + '</a>';
            HtmlStr += '</div>';
            HtmlStr += '</div>';
            no++;
        }

        $('#Dialog_Seedlistview_content').empty();
        $('#Dialog_Seedlistview_content').html(HtmlStr);   
        if(SeedNameArray.length > 0){
            setTimeout( function(){            
            // alert($('#listview_content>.userdata_row:eq(0)').outerWidth());
            // alert(element.offsetWidth);
            var totalWidth = $('#Dialog_Seedlistview_content>.Dialog_Seeddata_row:eq(0)').outerWidth();           
            var noWidth = totalWidth*0.13;
            var nameWidth = totalWidth*0.5;
            var userrnableWidth = totalWidth*0.17 - 5;
            // alert(totalWidth*0.1);
            $('.Dialog_Seedlistview_title > .Dialog_Seedrow_title > .Dialog_no_Seed').outerWidth(noWidth);
            $('.Dialog_Seedlistview_title > .Dialog_Seedrow_title > .Dialog_column_Seed').outerWidth(nameWidth);
            $('.Dialog_Seedlistview_title > .Dialog_Seedrow_title > .Dialog_Seed_action').outerWidth(userrnableWidth); },50);
        }     
        $('.btn-overwriteseed').button();
        BindOverwriteSeedButton();
    };    


    function CreateVMUserListHtml() {
        var HtmlStr = "";
        var no = 1;
        var i;
        getuserarr = [];
        
        for(i = 0; i < UserNameArray.length; i++) {
            HtmlStr += '<div class="Dialog_userdata_row">';
            HtmlStr += '<div class="Dialog_no_user">' + no + '</div>';
            getuserarr = UserNameArray[i].split(":");    
            HtmlStr += '<div class="Dialog_column_user" style=" display: block; text-overflow: ellipsis; overflow:hidden; white-space:nowrap; ">' + getuserarr[0] + '</div>';
            HtmlStr += '<div class="Dialog_action">';
            if(VMUserListflag) {
                HtmlStr += '<a id="modify_VD_Assign_User:' + getuserarr[1] + ":" + getuserarr[0] + '" class="btn-Assign_User btn-jquery" style="font-size: smaller">' + LanguageStr_langSysCfgUserMgt.Assign + '</a>';
                $('.Dialog_action').show();
            }        
            HtmlStr += '</div>';
            HtmlStr += '</div>';
            no++;
        }

        $('#Dialog_listview_content').empty();
        $('#Dialog_listview_content').html(HtmlStr);
        
        $('.btn-Assign_User').button();
        BindVDAssignUserButton();
        if(UserNameArray.length > 0){
            setTimeout( function(){            
            // alert($('#listview_content>.userdata_row:eq(0)').outerWidth());
            // alert(element.offsetWidth);
            var totalWidth = $('#Dialog_listview_content>.Dialog_userdata_row:eq(0)').outerWidth();           
            var noWidth = totalWidth*0.13;
            var nameWidth = totalWidth*0.7;
            var userrnableWidth = totalWidth*0.17 - 5;
            // alert(totalWidth*0.1);
            $('.Dialog_listview_title > .Dialog_row_title > .Dialog_no_user').outerWidth(noWidth);
            $('.Dialog_listview_title > .Dialog_row_title > .Dialog_column_user').outerWidth(nameWidth);
            $('.Dialog_listview_title > .Dialog_row_title > .Dialog_action').outerWidth(userrnableWidth); },50);
        }     
        if(!VMUserListflag) {            
            // $(element).is(":visible");
            $('.Dialog_action').hide();
            GetUserName();
        } 
    }; 

    function CreateVMUserCloneWithCheckboxListHtml() {
        var HtmlStr = "";
        var no = 1;
        var i;
        getuserarr = [];
        
        for(i = 0; i < UserNameArray.length; i++) {
            HtmlStr += '<div class="data_row_cb">';
            HtmlStr += '<div class="no_user_cb">' + no + '</div>';
            getuserarr = UserNameArray[i].split(":");                
            HtmlStr += '<div class="select_user_cb">';
            HtmlStr += '<input type="checkbox" id="'+ temp_vmname_for_clone + "_" + getuserarr[0]+ ":" + no + '" class="iCheckBoxUserSelect">';
            HtmlStr += '</div>';
            HtmlStr += '<div class="column_user_cb" style=" display: block; text-overflow: ellipsis; overflow:hidden; white-space:nowrap; ">' + getuserarr[0] + '</div>';            

            HtmlStr += '<div class="column_vd_cb">';
            HtmlStr += '<input id="input_VMName_for_clone' + no + '" class="listview_inputext" name="' + getuserarr[0] + '" value="" type="text">';
            HtmlStr += '</div>';


            HtmlStr += '</div>';
            no++;
        }

        $('#listview_content_cb_clone').empty();
        $('#listview_content_cb_clone').html(HtmlStr);
        
        $('.iCheckBoxUserSelect').iCheck({  
            checkboxClass: 'icheckbox_minimal-blue'    
        });
        
        $('.listview_inputext').prop('disabled', true);
        InitCloneUserSelectCheckBox();
        if(UserNameArray.length > 0){
            var totalWidth = $('#listview_content_cb_clone>.data_row_cb:eq(0)').outerWidth();
            var noWidth = totalWidth*0.13;
            var columnUserWidth = totalWidth*0.39;
            var selectUserWidth = totalWidth*0.08;
            var columnVDWidth = totalWidth*0.4 -5;
            // alert(totalWidth*0.1);
            $('.listview_title_clone > .row_title_cb > .no_user_cb').outerWidth(noWidth);
            $('.listview_title_clone > .row_title_cb > .select_user_cb').outerWidth(selectUserWidth);
            $('.listview_title_clone > .row_title_cb > .column_user_cb').outerWidth(columnUserWidth);
            $('.listview_title_clone > .row_title_cb > .column_vd_cb').outerWidth(columnVDWidth);                      
        }
    };     


    function CreateVMUserBornUserWithCheckboxListHtml() {
        var HtmlStr = "";
        var no = 1;
        var i;
        getuserarr = [];
        
        for(i = 0; i < UserNameArray.length; i++) {
            HtmlStr += '<div class="data_row_cb">';
            HtmlStr += '<div class="no_user_cb">' + no + '</div>';
            getuserarr = UserNameArray[i].split(":");                
            HtmlStr += '<div class="select_user_cb">';
            HtmlStr += '<input type="checkbox" id="'+ temp_vmname_for_bornuser + "_" + getuserarr[0] + ":" + no + '" class="iCheckBoxBornUserSelect">';
            HtmlStr += '</div>';
            HtmlStr += '<div class="column_user_cb" style=" display: block; text-overflow: ellipsis; overflow:hidden; white-space:nowrap; ">' + getuserarr[0] + '</div>';            

            HtmlStr += '<div class="column_vd_cb">';
            HtmlStr += '<input id="input_VMName_for_bornuser' + no + '" class="listview_inputext" name="' + getuserarr[0] + '" value="" type="text">';
            HtmlStr += '</div>';


            HtmlStr += '</div>';
            no++;
        }

        $('#listview_content_cb_bornuser').empty();
        $('#listview_content_cb_bornuser').html(HtmlStr);
        
        $('.iCheckBoxBornUserSelect').iCheck({  
            checkboxClass: 'icheckbox_minimal-blue'    
        });
        
        $('.listview_inputext').prop('disabled', true);
        InitBornUserUserSelectCheckBox();
        if(UserNameArray.length > 0){
            var totalWidth = $('#listview_content_cb_bornuser>.data_row_cb:eq(0)').outerWidth();
            var noWidth = totalWidth*0.13;
            var columnUserWidth = totalWidth*0.39;
            var selectUserWidth = totalWidth*0.08;
            var columnVDWidth = totalWidth*0.4 -5;
            // alert(totalWidth*0.1);
            $('.listview_title_born > .row_title_cb > .no_user_cb').outerWidth(noWidth);
            $('.listview_title_born > .row_title_cb > .select_user_cb').outerWidth(selectUserWidth);
            $('.listview_title_born > .row_title_cb > .column_user_cb').outerWidth(columnUserWidth);
            $('.listview_title_born > .row_title_cb > .column_vd_cb').outerWidth(columnVDWidth);
        }
    };    

    function GetUserName() {
        $(".Dialog_userdata_row > .Dialog_column_user").click(function(event) {
            var btn_element = $(this);
            //do something
            $(".Dialog_column_user").removeClass("SelectedUser");
            btn_element.addClass("SelectedUser"); 
            // alert(btn_element.text());                 

            if(VDUserListflag) {
                VDUserListflag = false;
                //temp_username_for_vdtoorg = btn_element.text();      

                if(SeedUserListflag) {
                    SeedUserListflag = false;
                    $('#input_seed_dest_name').val(temp_vmname_for_seed + "_seed_" + btn_element.text());
                } else {
                    $("#input_VM_User_forClone").val(btn_element.text());
                    $('#input_seed_dest_name').val(temp_vmname_for_vdtoorg + "_" + btn_element.text());
                }                
            } else if(ImportUserListflag) {
                ImportUserListflag = false;
                temp_username_for_import = btn_element.text();   
                ImportVD(importvd_org_name,importcd_new_name, import_raid_id);
            } else {
                $('#input_VM_User').val(btn_element.text());
            }
            
            $( "#dialog_VM_userlist" ).dialog('close');
        });    
    }

    // 2.Create dialog
    function Dialog_UserList() {
        $("#dialog_VM_userlist").dialog({
            title: LanguageStr_VM.VM_UserList_title,
            modal: true,
            resizable: false,
            draggable: true,
            autoOpen: false,
            height: 380,
            width: 500,
            dialogClass: "no-close",
            closeOnEscape: false
        });
    }

    function Dialog_SeedList() {
        $("#dialog_VM_seedlist").dialog({
            title: LanguageStr_VM.VM_SeedList_title,
            modal: true,
            resizable: false,
            draggable: true,
            autoOpen: false,
            height: 350,
            width: 500,
            dialogClass: "no-close",
            closeOnEscape: false
        });
    }

    function Dialog_UserList_cb() {
        $("#dialog_VM_userlist_checkbox").dialog({
            title: LanguageStr_VM.VM_UserList_title,
            modal: true,
            resizable: false,
            draggable: true,
            autoOpen: false,
            height: 350,
            width: 500,
            dialogClass: "no-close",
            closeOnEscape: false
        });
    }    

    // 3. open dialog
    function InitButtonForVMCreateUserList(){
        $('#btn_get_user').click(function(event){
            event.preventDefault();
            // ResetValueUserCreateDialog();
            VMUserListflag = false;
            CreateVM_GetUserList();
            $( "#dialog_VM_userlist" ).dialog('open');
        });
    }   

    function InitButtonForVDCreateUserList(){
        $('#btn_VD_get_user').click(function(event){
            event.preventDefault();
            // ResetValueUserCreateDialog();
            VMUserListflag = false;
            VDUserListflag = true;
            CreateVM_GetUserList();
            $( "#dialog_VM_userlist" ).dialog('open');
        });
    }   

    function InitButtonForVMModifyUserList(){
        $('#btn_VMModify_get_user').click(function(event){
            event.preventDefault();
            // ResetValueUserCreateDialog();
            VMUserListflag = true;
            CreateVM_GetUserList();
            $( "#dialog_VM_userlist" ).dialog('open');
        });
    }   

    function InitButtonForBornUserUserList(){
        $('#btn_BornUser_getuserlist').click(function(event){
            event.preventDefault();
            userlist_checkbox_flage = 2;
            $('.iCheckBoxUserAllSelect').iCheck('uncheck');
            CreateVM_GetUserList_withcheckbox();
            $( "#dialog_VM_userlist_checkbox" ).dialog('open');
        });
    }     

    function InitButtonForCloneUserList(){
        $('#btn_clone_getuserlist').click(function(event){
            event.preventDefault();
            userlist_checkbox_flage = 1;
            $('.iCheckBoxUserAllSelect').iCheck('uncheck');
            CreateVM_GetUserList_withcheckbox();
            $( "#dialog_VM_userlist_checkbox" ).dialog('open');
        });
    }         

    function InitButtonAllUserRename(){
        $('#btn_UserList_cb_confirm').click(function(event){
            event.preventDefault();
            $("#textarea_dest_name").val('');
            $("#textarea_snapshot_dest_name").val('');
            SelectUserNameRename('iCheckBoxUserSelect');
            $( "#dialog_VM_userlist_checkbox" ).dialog('close');
        });
    }    

    function BindVDAssignUserButton() {
        $('.btn-Assign_User').click(function(event){
            event.preventDefault();
            var btn_element = $(this);
            var id_userinfo = btn_element.attr('id').split(":");
            var id_user = id_userinfo[1];
            var new_user = id_userinfo[2];
            AssignUser(id_user, new_user);
        });     

    }

    function BindOverwriteSeedButton() {
        $('.btn-overwriteseed').click(function(event){
            event.preventDefault();
            var btn_element = $(this);
            var id_seedinfo = btn_element.attr('id').split(":");
            var id_seed   = id_seedinfo[1];
            var id_vd     = id_seedinfo[2];
            // alert(name_seed + " and " + id_seed);
            OverWriteSeed(id_seed, id_vd);
        });     

    }

    function OverWriteSeed(id_seed, id_vd) {
        StopPolling();
        oHtml.blockPage();  
        var request = oRelayAjax.OverWriteSeed(id_seed, id_vd);  
        CallBackOverWriteSeed(request);
    }

    function CallBackOverWriteSeed(request){
        request.done(function(msg, statustext, jqxhr) {                 
            if(typeof(msg['status']) !== "undefined" && msg['status'] == 2) {
                Logout_Another_Admin_Login();
                return false;
            }
            else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3) {
                window.location.href = 'index.php';
                return false;
            }
            oHtml.stopPage();  
            $( "#dialog_VM_seedlist" ).dialog('close');   
            // alert("OverWrite Seed Success " + "(" + jqxhr.status + ")");

            NowSortTaskType = null;
            oUIVMContorl.VMListTask(true);    
            StartPolling(true,false);
            $('#tab-vm-container').easytabs('select', '#tabs1-task-list');     
 
        });
        request.fail(function(jqxhr, textStatus) {    
            oHtml.stopPage();
            alert("Error Code: " + jqxhr.status);
            // if(jqxhr.status == 400) {
            //     alert(LanguageStr_VM.VM_Modify_Name_Fail);  
            //     $('#input_Mofify_VMName').val(org_name);
            //     setTimeout(function(){VMInfo(org_name,true,false);},1000); 
            // }
            // else if(jqxhr.status == 409) {
            //     var name_warning_2 = (LanguageStr_VM.VM_Clone_Name_Warning_2).replace("&",name);                    
            //     alert(name_warning_2);
            //     $('#input_Mofify_VMName').val(org_name);
            //     setTimeout(function(){VMInfo(org_name,true,false);},1000);               
            // }
            // else {
            //     oError.CheckAuth(jqxhr.status,ActionStatus.VMModifyInfo);   
            // }
                

        });
    }

    function AssignUser(id_user, new_user) {
        StopPolling();
        oHtml.blockPage();  
        var request = oRelayAjax.VDAssigntoUser(vmid,id_user);  
        CallBackAssignUser(request, new_user);
    }

    function CallBackAssignUser(request,name){
        request.done(function(msg, statustext, jqxhr) {                 
            if(typeof(msg['status']) !== "undefined" && msg['status'] == 2) {
                Logout_Another_Admin_Login();
                return false;
            }
            else if(typeof(msg['status']) !== "undefined" && msg['status'] == 3) {
                window.location.href = 'index.php';
                return false;
            }
            oHtml.stopPage();  
            $( "#dialog_VM_userlist" ).dialog('close');  
            vmusername = name;
            $('#input_VD_Assign_User').val(vmusername);

            if(name !== org_name)
                oUIVMContorl.VMList(true,false);       
            setTimeout(function() { VMInfo(name,true,false); },1000);              
 
        });
        request.fail(function(jqxhr, textStatus) {    
            oHtml.stopPage();
            alert("Error Code: " + jqxhr.status);
            if(jqxhr.status == 400) {
                alert(LanguageStr_VM.VM_Modify_Name_Fail);  
                $('#input_Mofify_VMName').val(org_name);
                setTimeout(function(){VMInfo(org_name,true,false);},1000); 
            }
            else if(jqxhr.status == 409) {
                var name_warning_2 = (LanguageStr_VM.VM_Clone_Name_Warning_2).replace("&",name);                    
                alert(name_warning_2);
                $('#input_Mofify_VMName').val(org_name);
                setTimeout(function(){VMInfo(org_name,true,false);},1000);               
            }
            else {
                oError.CheckAuth(jqxhr.status,ActionStatus.VMModifyInfo);   
            }
                

        });
    }

    function InitUserSelectAllCheckBox(){
        $('.iCheckBoxUserAllSelect').iCheck({  
            checkboxClass: 'icheckbox_minimal-blue'    
        });
        $('.iCheckBoxUserAllSelect').on('ifChanged', function(event){
            var element = $(this);
            if(element.prop('checked')) {
                $('.iCheckBoxUserSelect').iCheck('check');
                $('.listview_inputext').prop('disabled', false);
            } else {
                $('.iCheckBoxUserSelect').iCheck('uncheck');
                $('.listview_inputext').prop('disabled', true);    
                $('.listview_inputext').val('');            
            }
            // EnableDisableSelectSeedActionButton();
        });
    }

    function InitBornUserSelectAllCheckBox(){
        $('.iCheckBoxBornUserAllSelect').iCheck({  
            checkboxClass: 'icheckbox_minimal-blue'    
        });
        $('.iCheckBoxBornUserAllSelect').on('ifChanged', function(event){
            var element = $(this);
            if(element.prop('checked')) {
                $('.iCheckBoxBornUserSelect').iCheck('check');
                $('.listview_inputext').prop('disabled', false);
            } else {
                $('.iCheckBoxBornUserSelect').iCheck('uncheck');
                $('.listview_inputext').prop('disabled', true);                
                $('.listview_inputext').val('');
            }
            // EnableDisableSelectSeedActionButton();
        });
    }    

    function VMGetUserDataList() {
        oHtml.blockPage();
        var request = Ajax_GetUserList();
        request.done(function (AjaxData, statustext, jqXHR) {        
            UserNameArray = []; // for create VM Dialog 
            $.each(AjaxData, function (index, element) {
                UserNameArray.push(element['Name'] + ":" + element['ID']);
            });
            CreateVMUserListHtml();
            CreateVMUserCloneWithCheckboxListHtml();
            CreateVMUserBornUserWithCheckboxListHtml();
            limitText( $('.listview_inputext'), 32);
            oHtml.stopPage();
        });
        request.fail(function (jqXHR, textStatus) {
            oHtml.stopPage();
            alert("Error Code: " + jqXHR.status);               
        });
    }

    function SelectUserNameRename(checkboxclass) {        
        var str_textarea_input     = "";
        var clone_textarea_Size    = $("#textarea_dest_name").val().trim().length;
        var BornUser_textarea_Size = $("#textarea_snapshot_dest_name").val().trim().length;
        var clone_textarea_val     = $("#textarea_dest_name").val();
        var BornUser_textarea_val  = $("#textarea_snapshot_dest_name").val();        
        var clone_sourcename       = temp_vmname_for_clone;
        var BornUser_sourcename    = temp_vmname_for_bornuser;  
        var temp_size;
        var temp_addname           = "";
        
        /*
        if(userlist_checkbox_flage == 1) {
            temp_size = clone_textarea_Size;
            temp_addname = clone_sourcename + "_";
        } else if(userlist_checkbox_flage == 2) {
            temp_size = BornUser_textarea_Size;
            temp_addname = BornUser_sourcename + "_";
        }
        */
        /*
        $.each($('.' + checkboxclass + ':checked'),function(index,element) {                           
            if(index === $('.' + checkboxclass + ':checked').length - 1)                    
                str_textarea_input += temp_addname + $(element).attr('id');
            else
                str_textarea_input += temp_addname + $(element).attr('id') + ',';
        });
        */
        /*
        if(userlist_checkbox_flage == 1) {
            if(temp_size == 0) {
                $("#textarea_dest_name").val( str_textarea_input );
            } else {
                $("#textarea_dest_name").val( clone_textarea_val + ',' + str_textarea_input );
            }
        } else if(userlist_checkbox_flage == 2) {
            if(temp_size == 0) {
                $("#textarea_snapshot_dest_name").val(str_textarea_input);
            } else {
                $("#textarea_snapshot_dest_name").val( BornUser_textarea_val + ',' + str_textarea_input );
            }                        
        }    
        */    


        


        if(userlist_checkbox_flage == 1) {

        $.each($('.' + checkboxclass + ':checked'),function(index,element) {                           
            if(index === $('.' + checkboxclass + ':checked').length - 1) {
                clone_checkboxinfo = $(element).attr('id').split(":");
                clone_checkboxno   = clone_checkboxinfo[1];  
                str_textarea_input += $('#input_VMName_for_clone'+ clone_checkboxno).val() + ":" + $('#input_VMName_for_clone'+ clone_checkboxno).attr('name');
            } else {
                clone_checkboxinfo = $(element).attr('id').split(":");
                clone_checkboxno   = clone_checkboxinfo[1];  
                str_textarea_input += $('#input_VMName_for_clone'+ clone_checkboxno).val() + ":" + $('#input_VMName_for_clone'+ clone_checkboxno).attr('name') + ',';
            }
                
        });



            $("#textarea_dest_name").val( str_textarea_input );

        } else if(userlist_checkbox_flage == 2) {
        $.each($('.' + checkboxclass + ':checked'),function(index,element) {                           
            if(index === $('.' + checkboxclass + ':checked').length - 1) {
                bornuser_checkboxinfo = $(element).attr('id').split(":");
                bornuser_checkboxno   = bornuser_checkboxinfo[1];  
                str_textarea_input += $('#input_VMName_for_bornuser'+ bornuser_checkboxno).val() + ":" + $('#input_VMName_for_bornuser'+ bornuser_checkboxno).attr('name');
            } else {
                bornuser_checkboxinfo = $(element).attr('id').split(":");
                bornuser_checkboxno   = bornuser_checkboxinfo[1];  
                str_textarea_input += $('#input_VMName_for_bornuser'+ bornuser_checkboxno).val() + ":" + $('#input_VMName_for_bornuser'+ bornuser_checkboxno).attr('name') + ',';
            }
                
        });


            $("#textarea_snapshot_dest_name").val(str_textarea_input);                     
        }
    }



    function InitCloneUserSelectCheckBox(){
        $('.iCheckBoxUserSelect').on('ifChanged', function(event){
            var element = $(this);
            if(element.prop('checked')){
                clone_checkboxinfo     = element.attr('id').split(":");
                clone_checkboxusername = clone_checkboxinfo[0];
                clone_checkboxno       = clone_checkboxinfo[1];                
                $('#input_VMName_for_clone'+ clone_checkboxno).iCheck('check');
                $('#input_VMName_for_clone'+ clone_checkboxno).prop('disabled', false);
                $('#input_VMName_for_clone'+ clone_checkboxno).val(clone_checkboxusername);
            }
            else{
                clone_checkboxinfo     = element.attr('id').split(":");
                clone_checkboxusername = clone_checkboxinfo[0];
                clone_checkboxno       = clone_checkboxinfo[1];                  
                $('#input_VMName_for_clone'+ clone_checkboxno).iCheck('uncheck');
                $('#input_VMName_for_clone'+ clone_checkboxno).prop('disabled', true);
                $('#input_VMName_for_clone'+ clone_checkboxno).val('');
            }
            // EnableDisableSelectSeedActionButton();
        });
    }

    function InitBornUserUserSelectCheckBox(){
        $('.iCheckBoxBornUserSelect').on('ifChanged', function(event){
            var element = $(this);
            if(element.prop('checked')){
                bornuser_checkboxinfo     = element.attr('id').split(":");
                bornuser_checkboxusername = bornuser_checkboxinfo[0];
                bornuser_checkboxno       = bornuser_checkboxinfo[1];                
                $('#input_VMName_for_bornuser'+ bornuser_checkboxno).iCheck('check');
                $('#input_VMName_for_bornuser'+ bornuser_checkboxno).prop('disabled', false);
                $('#input_VMName_for_bornuser'+ bornuser_checkboxno).val(bornuser_checkboxusername);
            }
            else{
                bornuser_checkboxinfo     = element.attr('id').split(":");
                bornuser_checkboxusername = bornuser_checkboxinfo[0];
                bornuser_checkboxno       = bornuser_checkboxinfo[1];                   
                $('#input_VMName_for_bornuser'+ bornuser_checkboxno).iCheck('uncheck');
                $('#input_VMName_for_bornuser'+ bornuser_checkboxno).prop('disabled', true);
                $('#input_VMName_for_bornuser'+ bornuser_checkboxno).val('');
            }
            // EnableDisableSelectSeedActionButton();
        });
    }



        // reference ChangeVMNametoID
        function ChangeVMNametoUserName(name)
        {           
            if (typeof(VMListUserName[name])!== 'undefined'){
                vmusername = VMListUserName[name]['username'];
                return;
            }
            // if (typeof(SeedListName[name])!== 'undefined')   
            // {
            //     vmid = SeedListName[name]['vdid'];
            //     // alert('Seed:' + vmid);
            //     return;
            // }
            if (typeof(BornedListUserName[name])!== 'undefined')   
            {
                vmusername = BornedListUserName[name]['username'];
                return;
            }
        }     


        function TransformTextarea(name) {        
            var temp_textarea_arr = [];   
            var temp_taarr_info;
            var temp_taarr_vdname;
            var temp_taarr_username;
            var tmp_taarr_class;
            var Str_name = "";
            var i;

            temp_textarea_arr = name.split(',');

            for(i = 0; i < temp_textarea_arr.length; i++) {
                temp_taarr_info     = temp_textarea_arr[i].split(":");
                temp_taarr_vdname   = temp_taarr_info[0];
                temp_taarr_username = temp_taarr_info[1];
                
                if(i === temp_textarea_arr.length - 1)
                    Str_name += temp_taarr_vdname;
                else
                    Str_name += temp_taarr_vdname + ',';    

                tmp_taarr_class = new GetTextareaUserNameClass(temp_taarr_vdname,temp_taarr_username);                   
                TextAreaUserName[temp_taarr_vdname] = tmp_taarr_class;  
            }

            return Str_name;
        }  

        return oUIVMContorl;
    }




};


