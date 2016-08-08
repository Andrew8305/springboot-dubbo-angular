<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>角色管理</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/role/">角色列表</a></li>
		<shiro:hasPermission name="sys:role:edit"><li><a href="${ctx}/sys/role/form">角色添加</a></li></shiro:hasPermission>
	</ul>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<tr><th>角色名称</th><th>系统数据</th><th>是否可用</th><shiro:hasPermission name="sys:role:edit"><th>操作</th></shiro:hasPermission></tr>
		<c:forEach items="${list}" var="sysRole">
			<tr>
				<td><a href="${ctx}/sys/role/form?id=${sysRole.id}">${sysRole.name}</a></td>
				<td>${fns:getDictLabel(sysRole.sysData, 'yes_no', '是')}</td>
				<td>${fns:getDictLabel(sysRole.useable, 'yes_no', '是')}</td>
				<shiro:hasPermission name="sys:role:edit"><td>
					<a href="${ctx}/sys/role/assign?id=${sysRole.id}">分配</a>
					<c:if test="${(sysRole.sysData eq fns:getDictValue('是', 'yes_no', '1') && fns:getUser().admin)||!(sysRole.sysData eq fns:getDictValue('是', 'yes_no', '1'))}">
						<a href="${ctx}/sys/role/form?id=${sysRole.id}">修改</a>
					</c:if>
					<a href="${ctx}/sys/role/delete?id=${sysRole.id}" onclick="return confirmx('确认要删除该角色吗？', this.href)">删除</a>
				</td></shiro:hasPermission>	
			</tr>
		</c:forEach>
	</table>
</body>
</html>