/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var orgpath = "" ;
var session="";
$(document).ready(function() {  
   if(!FlashDetect.installed){       
       $('.mobilecopy').hide();
   }
   else{       
       $('.mobilecopy').fadeIn();
   }
   ExitMoveMobile();
   $('#copy').zclip({
                    path:'js/ZeroClipboard.swf',
                    copy:function(){                        
                        return $('#shareurl').text();
                    }
   });
   var NewLeft = $('.modal-window').width() * 0.5 - $('.Lbl_Title').width() * 0.5;
   $('.Lbl_Title').css("left", NewLeft);
});

function CopyTextClickFunction()
{
    $('#coptext').click(function(){
        $(this).select();
        try{           
            
            window.clipboardData.setData('text', lang.copyclip + document.getElementById('coptext').innerText );
            alert(lang.copyok);
        }
        catch(e){            
        }
    });
}

function ExitMoveMobile()
{
    $('.close-window').click(function(){  
        window.location.assign('content.php?path='+ orgpath +"&session="+session); 
    });    
}
