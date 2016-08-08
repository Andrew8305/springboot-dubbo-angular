<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>菜单管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 3}).show();
		});
    	function updateSort() {
			loading('正在提交，请稍等...');
	    	$("#listForm").attr("action", "${ctx}/sys/menu/updateSort");
	    	$("#listForm").submit();
    	}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/menu/">菜单列表</a></li>
		<shiro:hasPermission name="sys:menu:edit"><li><a href="${ctx}/sys/menu/form">菜单添加</a></li></shiro:hasPermission>
	</ul>
	<sys:message content="${message}"/>
	<form id="listForm" method="post">
		<table id="treeTable" class="table table-striped table-bordered table-condensed hide">
			<thead><tr><th>名称</th><th>链接</th><th style="text-align:center;">排序</th><th>可见</th><th>权限标识</th><shiro:hasPermission name="sys:menu:edit"><th>操作</th></shiro:hasPermission></tr></thead>
			<tbody><c:forEach items="${list}" var="sysMenu">
				<tr id="${sysMenu.id}" pId="${sysMenu.parent.id ne '1'?sysMenu.parent.id:'0'}">
					<td nowrap><i class="fa fa-${not empty sysMenu.icon?sysMenu.icon:' hide'}"></i><a href="${ctx}/sys/menu/form?id=${sysMenu.id}">${sysMenu.name}</a></td>
					<td title="${sysMenu.href}">${sysMenu.href}</td>
					<td style="text-align:center;">
						<shiro:hasPermission name="sys:menu:edit">
							<input type="hidden" name="ids" value="${sysMenu.id}"/>
							<input name="sorts" type="text" value="${sysMenu.sort}" style="width:50px;margin:0;padding:0;text-align:center;">
						</shiro:hasPermission><shiro:lacksPermission name="sys:menu:edit">
							${sysMenu.sort}
						</shiro:lacksPermission>
					</td>
					<td>${sysMenu.isShow eq '1'?'显示':'隐藏'}</td>
					<td title="${sysMenu.permission}">${sysMenu.permission}</td>
					<shiro:hasPermission name="sys:menu:edit"><td nowrap>
						<a href="${ctx}/sys/menu/form?id=${sysMenu.id}">修改</a>
						<a href="${ctx}/sys/menu/delete?id=${sysMenu.id}" onclick="return confirmx('要删除该菜单及所有子菜单项吗？', this.href)">删除</a>
						<a href="${ctx}/sys/menu/form?parent.id=${sysMenu.id}">添加下级菜单</a>
					</td></shiro:hasPermission>
				</tr>
			</c:forEach></tbody>
		</table>
		<shiro:hasPermission name="sys:menu:edit"><div class="form-actions pagination-left">
			<input id="btnSubmit" class="btn btn-primary" type="button" value="保存排序" onclick="updateSort();"/>
		</div></shiro:hasPermission>
	 </form>
</body>
</html>