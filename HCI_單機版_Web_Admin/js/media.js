/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function() {  
    autoplay();
    VideoPlayPause();
});

function autoplay () {
    document.querySelector('video').play();
}
function VideoPlayPause()
{
    $('#video1').click(function(){
        if($('#video1').get(0).paused){                       
            $('#video1').get(0).play();
        }
        else{            
            $('#video1').get(0).pause();
        }
    });
}

