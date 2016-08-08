<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="addr" type="java.lang.String" required="true" description="地址"%>
<%@ attribute name="lng" type="java.lang.Double" required="true" description="经度"%>
<%@ attribute name="lat" type="java.lang.Double" required="true" description="纬度"%>
<div class="control-group">
    <label class="control-label">&nbsp;&nbsp;</label>
    <div class="controls">
        <a id="mapButton" href="javascript:" class="btn">地图</a>
        <span class="help-inline">可通过地图获取坐标（此功能受天朝网络限制）</span>
    </div>
</div>
<div class="control-group">
    <label class="control-label">纬度:</label>
    <div class="controls">
        <form:input id="lat" path="latitude" htmlEscape="false" maxlength="255" class="required"/>
    </div>
</div>
<div class="control-group">
    <label class="control-label">经度:</label>
    <div class="controls">
        <form:input id="lng" path="longitude" htmlEscape="false" maxlength="255" class="required"/>
    </div>
</div>
<script type="text/javascript">
	$("#mapButton").click(function(){
		top.$.jBox.open("iframe:${ctx}/tag/mapselect?addr="+"${addr}"+"&lng="+"${lng}"+"&lat="+"${lat}", "选择地点", 700, 570, {
            buttons:{"确定":"ok", "关闭":true}, submit:function(v, h, f){
                if (v=="ok"){
                	var lat = h.find("iframe")[0].contentWindow.$("#us3-lat").val().trim();
                	var lng = h.find("iframe")[0].contentWindow.$("#us3-lon").val().trim();
	                $("#lat").val(Number(lat).toFixed(6));
	                $("#lng").val(Number(lng).toFixed(6));
                }
            }, loaded:function(h){
                $(".jbox-content", top.document).css("overflow-y","hidden");
            },
            opacity: 0.5
        });
	});
</script>