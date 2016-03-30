<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>

<%
	String musicType = request.getParameter("m_type") != null ? request.getParameter("m_type") : "a";
	if(musicType.equals("a")){
%>
<!-- 流行福音 -->
<div class="playlist_form">  
   <ul class="playlist" id="playlist">
    	<li><a href="http://www.youtube.com/watch?v=WhxI5J6JujY" >大時代的魔咒</a></li>
    	<li><a href="http://www.youtube.com/watch?v=3EDq7rZjyhA">出發</a></li>
        <li><a href="http://www.youtube.com/watch?v=a5J66V2PxGQ">天上人間</a></li>
        <li><a href="http://www.youtube.com/watch?v=R1a8turuqi0">我不是猴子變的</a></li>
        <li><a href="http://www.youtube.com/watch?v=9FMHFiWbvNE">在天堂相遇</a></li>
        <li><a href="http://www.youtube.com/watch?v=nL8S-QUPO_c">天國八福</a></li>
		<li><a href="http://www.youtube.com/watch?v=0uRHLVQWngw">四律饒舌</a></li>
        <li><a href="http://www.youtube.com/watch?v=Z74hUn5rnCg">台灣我愛你</a></li>
        <li><a href="http://www.youtube.com/watch?v=QODhAOLy0ZI">讓台灣可愛起來</a></li>
        <li><a href="http://www.youtube.com/watch?v=rcbVkSqbm9Y">雲上的愛</a></li>
        <li><a href="http://www.youtube.com/watch?v=bWz1RoDuFdE">彩虹天堂</a></li>
	</ul>
</div>
<%
	}else if(musicType.equals("b")){
%>
<!-- 讚美 -->
<div class="playlist_form">  
   <ul class="playlist" id="playlist">
    	<li><a href="http://www.youtube.com/watch?v=E3r5uG5CK70" >以利亞的日子</a></li>
    	<li><a href="http://www.youtube.com/watch?v=fbVfrT0dC-0">喜樂泉源</a></li>
        <li><a href="http://www.youtube.com/watch?v=E4m64F2b2EQ">全心呼求</a></li>
        <li><a href="http://www.youtube.com/watch?v=fiTFwTLMX24">揚聲歡呼讚美</a></li>
        <li><a href="http://www.youtube.com/watch?v=WQWgyjv6nCk">讓讚美飛揚</a></li>
        <li><a href="http://www.youtube.com/watch?v=OYmbmbt5a_M">來慶賀</a></li>
		<li><a href="http://www.youtube.com/watch?v=a1Hn4YByXg4">和撒那</a></li>
        <li><a href="http://www.youtube.com/watch?v=oRgtSPqMypk">每個人都讚美主</a></li>
	</ul>
</div>
<%
	}else if(musicType.equals("c")){
%>
<!-- 救恩 -->
<div class="playlist_form">  
   <ul class="playlist" id="playlist">
    	<li><a href="http://www.youtube.com/watch?v=hjBz_dPzti4" >新造的人</a></li>
    	<li><a href="http://www.youtube.com/watch?v=rUn-FrNGrv0">不停湧出來</a></li>
        <li><a href="http://www.youtube.com/watch?v=aMdPX-vb3jQ">恩典之路</a></li>
        <li><a href="http://www.youtube.com/watch?v=1eRrXZW8S10">獻上感恩祭</a></li>
        <li><a href="http://www.youtube.com/watch?v=uUEIVdkkzIQ">感恩</a></li>
        <li><a href="http://www.youtube.com/watch?v=GTCRaetvqPo">感謝</a></li>
		<li><a href="http://www.youtube.com/watch?v=5Qt1LTSruLM">感謝天父</a></li>
        <li><a href="http://www.youtube.com/watch?v=raZJ00GEdxM">我在這</a></li>
	</ul>
</div>
<% 
	}else if(musicType.equals("d")){
%>
<!-- 醫治安慰 -->
<div class="playlist_form">  
   <ul class="playlist" id="playlist">
    	<li><a href="http://www.youtube.com/watch?v=3H2FcHYjd1w" >奇妙神蹟</a></li>
    	<li><a href="http://www.youtube.com/watch?v=sF0DfztzzkM">試煉中的歌吟</a></li>
        <li><a href="http://www.youtube.com/watch?v=dhq176FvlfY">耶和華的膀臂環繞我 </a></li>
        <li><a href="http://www.youtube.com/watch?v=VVZ6_bsqLaA">主是平安</a></li>
        <li><a href="http://www.youtube.com/watch?v=mcvMC8AxGuA">除你以外</a></li>
        <li><a href="http://www.youtube.com/watch?v=Cc-VAs6TG5c">全新的你</a></li>
		<li><a href="http://www.youtube.com/watch?v=oCD1BFBsztM">無人能像你 </a></li>
        <li><a href="http://www.youtube.com/watch?v=8Mn-OzBf0Dg">讓神兒子的愛圍繞你</a></li>
	</ul>
</div>
<%
	}else if(musicType.equals("e")){
%>
<!-- 團契 -->
<div class="playlist_form">  
   <ul class="playlist" id="playlist">
    	<li><a href="http://www.youtube.com/watch?v=QdkAfeNAYgk" >Nothing is impossible</a></li>
    	<li><a href="http://www.youtube.com/watch?v=sXSsMWmWVyQ">平安如江河</a></li>
        <li><a href="http://www.youtube.com/watch?v=nwfdieKrTQs">我的燈需要油</a></li>
        <li><a href="http://www.youtube.com/watch?v=9MO4eScvC2w">有主在我船上</a></li>
        <li><a href="http://www.youtube.com/watch?v=RQr4TefyGjc">奇妙奇妙真奇妙</a></li>
        <li><a href="http://www.youtube.com/watch?v=fOTkDtyhWdo">我要向高山舉目</a></li>
		<li><a href="http://www.youtube.com/watch?v=-B4QsKsDr4Y">我是主的羊</a></li>
        <li><a href="http://www.youtube.com/watch?v=bS55DUqDWok">認識你真好</a></li>
	</ul>
</div>
<%
	}else{
%>
<!-- 節慶 -->
<div class="playlist_form">  
   <ul class="playlist" id="playlist">
    	<li><a href="http://www.youtube.com/watch?v=WvvPQjlqJn4" >伊甸園的誓言</a></li>
    	<li><a href="http://www.youtube.com/watch?v=km-LfmPY57I">執手同遊</a></li>
        <li><a href="http://www.youtube.com/watch?v=H00YvhuqYWw">愛是從神而來</a></li>
        <li><a href="http://www.youtube.com/watch?v=FIqOGHToDxQ">手心</a></li>
        <li><a href="http://www.youtube.com/watch?v=6IAHjgrtZNU">最幸福的一對</a></li>
        <li><a href="http://www.youtube.com/watch?v=OD7Pvwz4cjY">耶和華祝福滿滿</a></li>
		<li><a href="http://www.youtube.com/watch?v=iEmaW8W7nFY">神聖的時刻</a></li>
        <li><a href="http://www.youtube.com/watch?v=jmhV2wDJoz0">彩虹下的約定</a></li>
	</ul>
</div>
<% } %>
