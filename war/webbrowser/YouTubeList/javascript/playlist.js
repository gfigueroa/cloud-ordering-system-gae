// JavaScript Document
var params = { allowScriptAccess: "always" };
var atts = { id: "video" };
var myarray = new Array(8);
var videoState = 10;
var index = 0;
var swfWidth = '640',		// 指定 YouTube 影片的寬
	swfHeight = '480';      // 指定 YouTube 影片的高

$(function(){
	var thumbSize = 'large',		// 設定要取得的縮圖是大圖還是小圖
								// 大圖寬高為 480X360；小圖寬高為 120X90
		imgWidth = '120',		// 限制圖片的寬
		imgHeight = '90',		// 限制圖片的高
		autoPlay = '&autoplay=0',	// 是否載入 YouTube 影片後自動播放；若不要自動播放則設成 0
		fullScreen = '&fs=0';		// 是否允許播放 YouTube 影片時能全螢幕播放

	$('.playlist>li>a').each(function(){
		// 取得要連結轉換的網址及訊息內容
		var _this =  $(this),
			_url = _this.attr('href'),
			_info = _this.text(),
			_type = 0 ;  // 設定要取得的縮圖大小
 
		// 取得 vid
		var vid = _url.match('[\\?&]v=([^&#]*)')[1];
 
		myarray[index] = vid;
		index ++;
		// 取得縮圖
		var thumbUrl = "http://img.youtube.com/vi/"+vid+"/" + _type + ".jpg";
 
		// 把目前超連結的內容轉換成圖片並加入 click 事件
		_this.html('<img src="'+thumbUrl+'" alt="'+_info+'" title="'+_info+'" width="'+imgWidth+'" height="'+imgHeight+'" />').click(function(){
			return false;
		}).focus(function(){
			this.blur();
		}).click(function(){
			// 當點擊到圖片時就轉換成 YouTube 影片
    		swfobject.embedSWF("http://www.youtube.com/v/"+vid+"?enablejsapi=1&playerapiid=video&version=3",
                       "video", swfWidth , swfHeight , "8", null, null, params, atts); 	
					
			return false;
		});
	});
	
 
	// 先載入第一個影片
	index = 0;
	$('.playlist>li>a').eq(index).click();
	//setTimeout("changeTheViedo()",100);
	
});

function changeTheVideo()
{
	if(VedioState == 0)
	{
		swfobject.embedSWF("http://www.youtube.com/v/3EDq7rZjyhA?enablejsapi=1&playerapiid=video&version=3",
                     "video", swfWidth , swfHeight , "8", null, null, params, atts);
	}
	
}


function onYouTubePlayerReady(playerId) {
  ytplayer = document.getElementById("video");
  ytplayer.addEventListener("onStateChange", "onytplayerStateChange");
  ytplayer.playVideo();
}

function onytplayerStateChange(newState) {
   if(newState == 0)
   {
	   if((index+1)>= myarray.length)
	   {
		   index = 0;
	   }else
	   {
		   index++;
	   }
	   swfobject.embedSWF("http://www.youtube.com/v/"+myarray[index]+"?enablejsapi=1&playerapiid=video&version=3",
                     "video", swfWidth , swfHeight , "8", null, null, params, atts);
   }
}	