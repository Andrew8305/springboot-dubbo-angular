<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
            $('.pagination').twbsPagination({
                startPage: ${page.pageNum},
                totalPages: ${page.pages},
                onPageClick: function (event, page) {
                    var pageNum = $("#pageNum");
                    if (pageNum.val() != page) {
                        pageNum.val(page);
                        $("#searchForm").submit();
                    }
                }
            });
		});
        function page() {
            $("#searchForm").submit();
            return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/user/list">用户列表</a></li>
		<shiro:hasPermission name="sys:user:edit"><li><a href="${ctx}/sys/user/form">用户添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="sysUser" action="${ctx}/sys/user/list" method="post" class="breadcrumb form-search ">
		<input id="pageNum" name="pageNum" type="hidden" value="${page.pageNum}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li><label>登录名：</label><form:input path="loginName" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li><label>姓&nbsp;&nbsp;&nbsp;名：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li class="btns">
                <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
            </li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th class="sort-column login_name">登录名</th><th class="sort-column name">姓名</th><th>电话</th><th>手机</th><shiro:hasPermission name="sys:user:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="sysUser">
			<tr>
				<td><a href="${ctx}/sys/user/form?id=${sysUser.id}">${sysUser.loginName}</a></td>
				<td>${sysUser.name}</td>
				<td>${sysUser.phone}</td>
				<td>${sysUser.mobile}</td>
				<shiro:hasPermission name="sys:user:edit"><td>
    				<a href="${ctx}/sys/user/form?id=${sysUser.id}">修改</a>
					<a href="${ctx}/sys/user/delete?id=${sysUser.id}" onclick="return confirmx('确认要删除该用户吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination"></div>
</body>
</html>