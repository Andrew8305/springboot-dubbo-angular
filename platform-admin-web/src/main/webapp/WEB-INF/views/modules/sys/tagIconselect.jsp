<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>图标选择</title>
	<meta name="decorator" content="blank"/>
    <style type="text/css">
    	.page-header {clear:both;margin:0 20px;padding-top:20px;}
		.the-icons {padding:25px 10px 15px;list-style:none;}
		.the-icons li {float:left;width:22%;line-height:25px;margin:2px 5px;cursor:pointer;}
		.the-icons i {margin:1px 5px;} .the-icons li:hover {background-color:#efefef;}
        .the-icons li.active {background-color:#0088CC;color:#ffffff;}
        /*.the-icons li:hover i{font-size:20px;}*/
    </style>
    <script type="text/javascript">
	    $(document).ready(function(){
	    	$("#icons li").click(function(){
	    		$("#icons li").removeClass("active");
	    		$("#icons li i").removeClass("icon-white");
	    		$(this).addClass("active");
	    		$(this).children("i").addClass("icon-white");
	    		$("#icon").val($(this).text());
	    	});
	    	$("#icons li").each(function(){
	    		if ($(this).text()=="${value}"){
	    			$(this).click();
	    		}
	    	});
	    	$("#icons li").dblclick(function(){
	    		top.$.jBox.getBox().find("button[value='ok']").trigger("click");
	    	});
	    });
    </script>
</head>
<body>
<input type="hidden" id="icon" value="${value}" />
<div id="icons">
		
	    <h2 class="page-header"> Web 应用的图标</h2>
	    
	    <ul class="the-icons">

	      <li><i class="fa fa-link"></i> link</li>
	      <li><i class="fa fa-adjust"></i> adjust</li>
	      <li><i class="fa fa-asterisk"></i> asterisk</li>
	      <li><i class="fa fa-barcode"></i> barcode</li>

	      <li><i class="fa fa-bell"></i> bell</li>
	      <li><i class="fa fa-book"></i> book</li>
	      <li><i class="fa fa-bookmark"></i> bookmark</li>
	      <li><i class="fa fa-briefcase"></i> briefcase</li>

	      <li><i class="fa fa-bullhorn"></i> bullhorn</li>
	      <li><i class="fa fa-calendar"></i> calendar</li>
	      <li><i class="fa fa-camera"></i> camera</li>
	      <li><i class="fa fa-certificate"></i> certificate</li>

	      <li><i class="fa fa-check"></i> check</li>
	      <li><i class="fa fa-cog"></i> cog</li>
	      <li><i class="fa fa-comment"></i> comment</li>
	      <li><i class="fa fa-download"></i> download</li>

	      <li><i class="fa fa-edit"></i> edit</li>
	      <li><i class="fa fa-envelope"></i> envelope</li>

	      <li><i class="fa fa-film"></i> film</li>

	      <li><i class="fa fa-filter"></i> filter</li>
	      <li><i class="fa fa-fire"></i> fire</li>
	      <li><i class="fa fa-flag"></i> flag</li>

	      <li><i class="fa fa-folder-open"></i> folder-open</li>
	      <li><i class="fa fa-gift"></i> gift</li>
	      <li><i class="fa fa-glass"></i> glass</li>
	      <li><i class="fa fa-globe"></i> globe</li>

	      <li><i class="fa fa-headphones"></i> headphones</li>
	      <li><i class="fa fa-heart"></i> heart</li>
	      <li><i class="fa fa-home"></i> home</li>

	      <li><i class="fa fa-inbox"></i> inbox</li>
	      <li><i class="fa fa-leaf"></i> leaf</li>
	      <li><i class="fa fa-lock"></i> lock</li>

	      <li><i class="fa fa-magnet"></i> magnet</li>
	      <li><i class="fa fa-map-marker"></i> map-marker</li>
	      <li><i class="fa fa-minus"></i> minus</li>

	      <li><i class="fa fa-music"></i> music</li>

	      <li><i class="fa fa-pencil"></i> pencil</li>

	      <li><i class="fa fa-plane"></i> plane</li>
	      <li><i class="fa fa-plus"></i> plus</li>
	      <li><i class="fa fa-print"></i> print</li>

	      <li><i class="fa fa-random"></i> random</li>
	      <li><i class="fa fa-refresh"></i> refresh</li>
	      <li><i class="fa fa-remove"></i> remove</li>

	      <li><i class="fa fa-retweet"></i> retweet</li>
	      <li><i class="fa fa-road"></i> road</li>
	      <li><i class="fa fa-search"></i> search</li>
	    
	      <li><i class="fa fa-share"></i> share</li>
	      <li><i class="fa fa-share-alt"></i> share-alt</li>
	      <li><i class="fa fa-shopping-cart"></i> shopping-cart</li>
	      <li><i class="fa fa-signal"></i> signal</li>

	      <li><i class="fa fa-star"></i> star</li>
	      <li><i class="fa fa-tag"></i> tag</li>
	      <li><i class="fa fa-tags"></i> tags</li>

	      <li><i class="fa fa-tasks"></i> tasks</li>
	      <li><i class="fa fa-thumbs-down"></i> thumbs-down</li>
	      <li><i class="fa fa-thumbs-up"></i> thumbs-up</li>

	      <li><i class="fa fa-tint"></i> tint</li>
	      <li><i class="fa fa-trash"></i> trash</li>
	      <li><i class="fa fa-upload"></i> upload</li>
	      <li><i class="fa fa-user"></i> user</li>

	      <li><i class="fa fa-volume-off"></i> volume-off</li>
	      <li><i class="fa fa-volume-down"></i> volume-down</li>
	      <li><i class="fa fa-volume-up"></i> volume-up</li>

	      <li><i class="fa fa-wrench"></i> wrench</li>
		  <li><i class="fa fa-bed"></i>hotel</li>
	    </ul>
	
	  
	    <h2 class="page-header">文本编辑器图标</h2>
	  
	    <ul class="the-icons">
	      <li><i class="fa fa-file"></i> file</li>
	      <li><i class="fa fa-repeat"></i> repeat</li>
	      <li><i class="fa fa-text-height"></i> text-height</li>
	      <li><i class="fa fa-text-width"></i> text-width</li>

	      <li><i class="fa fa-align-left"></i> align-left</li>
	      <li><i class="fa fa-align-center"></i> align-center</li>
	      <li><i class="fa fa-align-right"></i> align-right</li>
	      <li><i class="fa fa-align-justify"></i> align-justify</li>

	      <li><i class="fa fa-font"></i> font</li>
	      <li><i class="fa fa-bold"></i> bold</li>

	      <li><i class="fa fa-italic"></i> italic</li>
	      <li><i class="fa fa-th-large"></i> th-large</li>
	      <li><i class="fa fa-th"></i> th</li>
	      <li><i class="fa fa-th-list"></i> th-list</li>

	      <li><i class="fa fa-list"></i> list</li>
	      <li><i class="fa fa-list-alt"></i> list-alt</li>
	    </ul>


	    <h2 class="page-header">指示方向的图标</h2>
	  
	    <ul class="the-icons">
	      <li><i class="fa fa-arrow-down"></i> arrow-down</li>
	      <li><i class="fa fa-arrow-left"></i> arrow-left</li>
	      <li><i class="fa fa-arrow-right"></i> arrow-right</li>
	      <li><i class="fa fa-arrow-up"></i> arrow-up</li>
	    
	      <li><i class="fa fa-chevron-down"></i> chevron-down</li>
	      <li><i class="fa fa-chevron-left"></i> chevron-left</li>
	      <li><i class="fa fa-chevron-right"></i> chevron-right</li>
	      <li><i class="fa fa-chevron-up"></i> chevron-up</li>
	    </ul>


	    <h2 class="page-header">视频播放器图标</h2>
	  
	    <ul class="the-icons">
	      <li><i class="fa fa-play-circle"></i> play-circle</li>
	      <li><i class="fa fa-play"></i> play</li>
	      <li><i class="fa fa-pause"></i> pause</li>
	      <li><i class="fa fa-stop"></i> stop</li>
	    
	      <li><i class="fa fa-step-backward"></i> step-backward</li>
	      <li><i class="fa fa-fast-backward"></i> fast-backward</li>
	      <li><i class="fa fa-backward"></i> backward</li>
	      <li><i class="fa fa-forward"></i> forward</li>

	      <li><i class="fa fa-fast-forward"></i> fast-forward</li>
	      <li><i class="fa fa-step-forward"></i> step-forward</li>
	      <li><i class="fa fa-eject"></i> eject</li>
	    </ul>
	
	<br/><br/>
</div>
</body>