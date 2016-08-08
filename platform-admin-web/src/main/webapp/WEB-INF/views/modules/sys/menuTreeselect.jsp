<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/include/treeview.jsp" %>
<div id="menuTreeselect" class="ztree" style="margin:15px;"></div>
<script type="text/javascript">
    var menuTreeselectSetting = {
        view: {selectedMulti: false, dblClickExpand: false, nameIsHTML: true, showIcon: false, showTitle: false},
        data: {simpleData: {enable: true}},
        callback: {
            onClick: function (event, treeId, treeNode) {
                menuTreeselect.expandNode(treeNode);
                if (!treeNode.isParent) {
                    cookie('menuId', treeNode.id, {path: '/'});
                }
            }
        }
    };
    var menuTreeselectNodes = [
        <c:forEach items="${fns:getMenuList()}" var="sysMenu">
            <c:if test="${sysMenu.isShow eq '1'}">
                {
                    id: "${sysMenu.id}",
                    pId: "${not empty sysMenu.parent.id ? sysMenu.parent.id : 0}",
                    name: "<i class=\"icon-${not empty sysMenu.icon ? sysMenu.icon : 'file'}\"></i>&nbsp;${not empty sysMenu.parent.id ? sysMenu.name : ''}",
                    url: "${not empty sysMenu.href && fn:indexOf(sysMenu.href, '://') eq -1 ? ctx : ''}${not empty sysMenu.href ? sysMenu.href : 'javascript:'}",
                    target: "${not empty sysMenu.target ? sysMenu.target:'_self'}"
                },
            </c:if>
        </c:forEach>
        {
            id: new Date().getTime(),
            pId: "0",
            name: "<i class=\"icon-home\"></i>&nbsp;进入主页",
            url: "${ctx}",
            target: "_self"
        },
        {
            id: new Date().getTime(),
            pId: "0",
            name: "<i class=\"icon-share\"></i>&nbsp;退出登录",
            url: "${ctx}/logout",
            target: "_self"
        }];
    var menuTreeselect = $.fn.zTree.init($("#menuTreeselect"), menuTreeselectSetting, menuTreeselectNodes);//<c:if test="${empty parentId}">
    menuTreeselectNodes = menuTreeselect.getNodesByParam("level", 1);
    for (var i = 0; i < menuTreeselectNodes.length; i++) {
        menuTreeselect.expandNode(menuTreeselectNodes[i], true, false, false);
    }//</c:if>
    var menuTreeselectNode = menuTreeselect.getNodeByParam("id", '${parentId != '' ? parentId : cookie.menuId.value}');
    menuTreeselect.selectNode(menuTreeselectNode, true);
    menuTreeselect.expandNode(menuTreeselectNode, true, false, false);
</script>