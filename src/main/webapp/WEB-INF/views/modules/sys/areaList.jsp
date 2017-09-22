<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>区域管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			var tpl = $("#treeTableTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
			var data = ${fns:toJson(list)}, rootId = "0";
			
			
			addRow("#treeTableList", tpl, data, rootId, true);
			$("#treeTable").treeTable({expandLevel : 1});
			
			//给重置按钮添加单击事件
			$("#btn").click(function(){
				$("#inputCode").val("");
				$("#areaId").val("");
				$("#areaName").val("");
			});
			//查询按钮的单击事件
			$("#btnSubmit").click(function(){
		        $("#searchForm").submit();
			});
		});
		function addRow(list, tpl, data, pid, root){
			for (var i=0; i<data.length; i++){
				var row = data[i];
				if(row.type=='4'){
					$("#"+row.id).remove();
// 					tpl= $("#treeTableTpl").html().replace("/<a href='${ctx}//sys//area//form/?parentId={{row.code}}'>添加下级区域</a>","");
				}
				if ((${fns:jsGetVal('row.parentId')}) == pid){
					$(list).append(Mustache.render(tpl, {
						dict: {
							type: getDictLabel(${fns:toJson(fns:getDictList('sys_area_type'))}, row.type)
						}, pid: (root?0:pid), row: row
					}));
					addRow(list, tpl, data, row.code);
				}
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/area/">查询行政区域</a></li>
		<shiro:hasPermission name="sys:area:edit"><li><a href="${ctx}/sys/area/form">行政区域添加</a></li></shiro:hasPermission>
	</ul>
	<p style="font-weight: bold;background-color: #ddd;padding: 3px;">行政区域查询条件</p>
	<form:form id="searchForm" modelAttribute="area" action="${ctx}/sys/area/" method="post" class="breadcrumb form-search ">
		<ul class="ul-form">
<!-- 		    <li><label>区域编码：</label> -->
<%-- 		    	<form:input id="inputCode" path="code" htmlEscape="false" maxlength="50" class="input-medium" /> --%>
<!-- 	    	</li> -->
			<li><label>区域名称：</label>
				<sys:treeselect id="area" name="code" value="${area.code}" labelName="name" labelValue="${area.name}" 
 				title="区域" url="/sys/area/treeData" cssClass="input-small" allowClear="true"/>
			</li>
			<input type="hidden" value="1" name="type" id="type"/>
			<li class="btns">
			    <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<input id="btn" class="btn btn-primary" type="button" value="重置"/>
				<a href="${ctx}/sys/area/form?id=${area.id}" id="btnSubmit" class="btn btn-primary" type="button" value="新增">新增</a>
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	
	<sys:message content="${message}"/>
	<p style="font-weight: bold;background-color: #ddd;padding: 3px;">行政区域列表</p>
	<table id="treeTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>区域名称</th><th>区域编码</th><th>区域类型</th><th>备注</th><shiro:hasPermission name="sys:area:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody id="treeTableList"></tbody>
	</table>
	<script type="text/template" id="treeTableTpl">
		<tr id="{{row.code}}" pId="{{pid}}">
			<td><a href="${ctx}/sys/area/form?id={{row.id}}">{{row.name}}</a></td>
			<td>{{row.code}}</td>
			<td>{{dict.type}}</td>
			<td>{{row.remarks}}</td>
			<shiro:hasPermission name="sys:area:edit"><td>
				<a href="${ctx}/sys/area/form?id={{row.id}}">修改</a>
				<a href="${ctx}/sys/area/delete?code={{row.code}}" onclick="return confirmx('要删除该区域及所有子区域项吗？', this.href)">删除</a>
                <a href="${ctx}/sys/area/form?parentId={{row.code}}" id="{{row.id}}">添加下级区域</a>
			</td></shiro:hasPermission>
		</tr>
	</script>
</body>
</html>