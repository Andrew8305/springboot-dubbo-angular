<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>坐标选择</title>
	<meta name="decorator" content="blank"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script type="text/javascript" src='http://maps.google.com/maps/api/js?sensor=false&libraries=places'></script>
    <script src="${ctxStatic}/jquery-locationpicker-plugin/dist/locationpicker.jquery.min.js"></script>
    <style type="text/css">
        .gm-style img {max-width: none;}
        .gm-style label {width: auto; display:inline;}
    </style>
    <script type="text/javascript">

    </script>
</head>
<body>

<div class="form-horizontal" style="width: 97%;">
    <div class="clearfix">&nbsp;</div>
    <div class="control-group">
        <label class="control-label" style="width: 80px;">地址:</label>
        <div class="controls"><input type="text" class="form-control input-xxlarge" id="us3-address"/></div>
    </div>
    <div id="us3" style="width: 100%; height: 400px;"></div>
    <div class="clearfix">&nbsp;</div>
    <div class="m-t-small" hidden>
        <label class="p-r-small col-sm-1 control-label">Lat.:</label>

        <div class="col-sm-3"><input type="text" class="form-control" style="width: 110px" id="us3-lat"/></div>
        <label class="p-r-small col-sm-2 control-label">Long.:</label>

        <div class="col-sm-3"><input type="text" class="form-control" style="width: 110px" id="us3-lon"/></div>
    </div>
    <div class="clearfix"></div>
    <script>
        $('#us3').locationpicker({
            location: {latitude: ${lat}, longitude: ${lng}},
            radius: 0,
            zoom: 17,
            inputBinding: {
                latitudeInput: $('#us3-lat'),
                longitudeInput: $('#us3-lon'),
                locationNameInput: $('#us3-address')
            },
            enableAutocomplete: true,
            onlocationnotfound: function(locationName) {
                alertx("无法找到地址 '" + locationName + "'。");
            }
        });
    </script>
</div>
</body>