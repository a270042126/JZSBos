<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理分区</title>
<!-- 导入jquery核心类库 -->
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/jquery-1.8.3.js"></script>
<!-- 导入easyui类库 -->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/ext/portal.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/default.css">	
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/ext/jquery.portal.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/ext/jquery.cookie.js"></script>
<script
	src="${pageContext.request.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"
	type="text/javascript"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.ocupload-1.1.2.js"></script>	
<script type="text/javascript">
	function doAdd(){
		$('#addSubareaWindow').window("open");
	}
	
	function doEdit(){
		var rows = $("#grid").datagrid("getSelections");
		if(rows.length == 0){
			//没有选中，提示
			$.messager.alert("提示信息","请选择需要修改的记录！","warning");
		}else if(rows.length > 1){
			$.messager.alert("提示信息","只能选择一个记录！","warning");
		}else{
			var rowData = rows[0];
			$('#editSubareaWindow').window("open");
			$("#editSubareaForm").form("load",rowData);
			$('#editSubareaForm input[name="region.id"]').val(rowData.region.id);
			$('#editSubareaForm .combo-text.validatebox-text').val(rowData.region.name);
		}
	}
	
	function doDelete(){
		//获得选中的行
		var rows = $("#grid").datagrid("getSelections");
		if(rows.length == 0){
			//没有选中，提示
			$.messager.alert("提示信息","请选择需要删除的记录！","warning");
		}else{
			var array = new Array();
			//选中了记录,获取选中行的id
			for(var i=0;i<rows.length;i++){
				var id = rows[i].id;
				array.push(id);
			}
			var ids = array.join(",");
			//发送请求，传递ids参数
			window.location.href = '${pageContext.request.contextPath}/subarea/delete.action?ids='+ids;
		}
	}
	
	function doSearch(){
		$('#searchWindow').window("open");
		//清空查询条件
		document.getElementById("searchForm").reset();
	}
	
	function doExport(){
		window.location.href = "${pageContext.request.contextPath}/subarea/exportXls.action";
	}
	
	//工具栏
	var toolbar = [ {
		id : 'button-search',	
		text : '查询',
		iconCls : 'icon-search',
		handler : doSearch
	}, {
		id : 'button-add',
		text : '增加',
		iconCls : 'icon-add',
		handler : doAdd
	}, {
		id : 'button-edit',	
		text : '修改',
		iconCls : 'icon-edit',
		handler : doEdit
	},{
		id : 'button-delete',
		text : '删除',
		iconCls : 'icon-cancel',
		handler : doDelete
	},{
		id : 'button-import',
		text : '导入',
		iconCls : 'icon-redo'
	},{
		id : 'button-export',
		text : '导出',
		iconCls : 'icon-undo',
		handler : doExport
	}];
	// 定义列
	var columns = [ [ {
		field : 'id',
		checkbox : true,
	}, {
		field : 'showid',
		title : '分拣编号',
		width : 120,
		align : 'center',
		formatter : function(data,row ,index){
			return row.id;
		}
	},{
		field : 'province',
		title : '省',
		width : 120,
		align : 'center',
		formatter : function(data,row ,index){
			return row.region.province;
		}
	}, {
		field : 'city',
		title : '市',
		width : 120,
		align : 'center',
		formatter : function(data,row ,index){
			return row.region.city;
		}
	}, {
		field : 'district',
		title : '区',
		width : 120,
		align : 'center',
		formatter : function(data,row ,index){
			return row.region.district;
		}
	}, {
		field : 'addresskey',
		title : '关键字',
		width : 120,
		align : 'center'
	}, {
		field : 'startnum',
		title : '起始号',
		width : 100,
		align : 'center'
	}, {
		field : 'endnum',
		title : '终止号',
		width : 100,
		align : 'center'
	} , {
		field : 'single',
		title : '单双号',
		width : 100,
		align : 'center'
	} , {
		field : 'position',
		title : '位置',
		width : 200,
		align : 'center'
	} ] ];
	
	$(function(){
		// 先将body隐藏，再显示，不会出现页面刷新效果
		$("body").css({visibility:"visible"});
		
		// 收派标准数据表格
		$('#grid').datagrid( {
			iconCls : 'icon-forward',
			fit : true,
			border : true,
			rownumbers : true,
			striped : true,
			pageList: [30,50,100],
			pagination : true,
			toolbar : toolbar,
			url : "${pageContext.request.contextPath}/subarea/pageQuery.action",
			idField : 'id',
			columns : columns,
			onDblClickRow : doDblClickRow
		});
		
		// 添加、修改分区
		$('#addSubareaWindow').window({
	        title: '添加修改分区',
	        width: 600,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 400,
	        resizable:false
	    });
		
		// 添加、修改分区
		$('#editSubareaWindow').window({
	        title: '添加修改分区',
	        width: 600,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 400,
	        resizable:false
	    });
		
		// 查询分区
		$('#searchWindow').window({
	        title: '查询分区',
	        width: 400,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 400,
	        resizable:false
	    });

	});
	
	function doDblClickRow(rowIndex, rowData){
		$('#editSubareaWindow').window("open");//打开修改窗口
		$("#editSubareaForm").form("load",rowData);
		$('#editSubareaForm input[name="region.id"]').val(rowData.region.id);
		$('#editSubareaForm .combo-text.validatebox-text').val(rowData.region.name);
	}
	
	
	$(function(){
		$("#button-import").upload({
            action: '${pageContext.request.contextPath}/subarea/importXls.action',  
            name: 'myFile',
            onComplete: function(data) {
            	if(data == '1'){
            		//上传成功
            		$.messager.alert("提示信息","分区数据导入成功！","info");
            	}else{
            		//失败
            		$.messager.alert("提示信息","分区数据导入失败！","warning");
            	}
            }
		});
	});
</script>	
</head>
<body class="easyui-layout" style="visibility:hidden;">
	<div region="center" border="false">
    	<table id="grid"></table>
	</div>
	<!-- 添加 修改分区 -->
	<div class="easyui-window" title="分区添加修改" id="addSubareaWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >保存</a>
				<script type="text/javascript">
					$(function(){
						//绑定事件
						$("#save").click(function(){
							//校验表单输入项
							var v = $("#addSubareaForm").form("validate");
							if(v){
								//校验通过，提交表单
								$("#addSubareaForm").submit();
							}
						});
					})
				</script>
			</div>
		</div>
		
		<div style="overflow:auto;padding:5px;" border="false">
			<form id="addSubareaForm"
				 action="${pageContext.request.contextPath }/subarea/add.action" 
				method="post">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">分区信息</td>
					</tr>
					<tr>
						<td>分拣编码</td>
						<td><input type="text" name="id" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>选择区域</td>
						<td>
							<input class="easyui-combobox" name="region.id"  
    							data-options="valueField:'id',textField:'name',url:'${pageContext.request.contextPath }/region/listajax.action'" />  
						</td>
					</tr>
					<tr>
						<td>关键字</td>
						<td><input type="text" name="addresskey" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>起始号</td>
						<td><input type="text" name="startnum" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>终止号</td>
						<td><input type="text" name="endnum" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>单双号</td>
						<td>
							<select class="easyui-combobox" name="single" style="width:150px;">  
							    <option value="0">单双号</option>  
							    <option value="1">单号</option>  
							    <option value="2">双号</option>  
							</select> 
						</td>
					</tr>
					<tr>
						<td>位置信息</td>
						<td><input type="text" name="position" class="easyui-validatebox" required="true" style="width:250px;"/></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<!-- 添加 修改分区 -->
	<div class="easyui-window" title="分区添加修改" id="editSubareaWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="edit" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >保存</a>
				<script type="text/javascript">
					$(function(){
						//绑定事件
						$("#edit").click(function(){
							//校验表单输入项
							var v = $("#editSubareaForm").form("validate");
							if(v){
								//校验通过，提交表单
								$("#editSubareaForm").submit();
							}
						});
					})
				</script>
			</div>
		</div>
		
		<div style="overflow:auto;padding:5px;" border="false">
			<form id="editSubareaForm"
				 action="${pageContext.request.contextPath }/subarea/edit.action" 
				method="post">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">分区信息</td>
					</tr>
					<tr>
						<td>分拣编码</td>
						<td><input type="text" name="id" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>选择区域</td>
						<td>
							<input class="easyui-combobox" name="region.id"  
    							data-options="valueField:'id',textField:'name',url:'${pageContext.request.contextPath }/region/listajax.action'" />  
						</td>
					</tr>
					<tr>
						<td>关键字</td>
						<td><input type="text" name="addresskey" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>起始号</td>
						<td><input type="text" name="startnum" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>终止号</td>
						<td><input type="text" name="endnum" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>单双号</td>
						<td>
							<select class="easyui-combobox" name="single" style="width:150px;">  
							    <option value="0">单双号</option>  
							    <option value="1">单号</option>  
							    <option value="2">双号</option>  
							</select> 
						</td>
					</tr>
					<tr>
						<td>位置信息</td>
						<td><input type="text" name="position" class="easyui-validatebox" required="true" style="width:250px;"/></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<!-- 查询分区 -->
	<div class="easyui-window" title="查询分区窗口" id="searchWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div style="overflow:auto;padding:5px;" border="false">
			<form id="searchForm">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">查询条件</td>
					</tr>
					<tr>
						<td>省</td>
						<td><input type="text" name="region.province"/></td>
					</tr>
					<tr>
						<td>市</td>
						<td><input type="text" name="region.city"/></td>
					</tr>
					<tr>
						<td>区（县）</td>
						<td><input type="text" name="region.district"/></td>
					</tr>
					<tr>
						<td>关键字</td>
						<td><input type="text" name="addresskey"/></td>
					</tr>
					<tr>
						<td colspan="2"><a id="btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a> 
							<script type="text/javascript">
								$(function(){
									//工具方法，可以将指定的表单中的输入项目序列号为json数据
									$.fn.serializeJson=function(){  
							            var serializeObj={};  
							            var array=this.serializeArray();
							            $(array).each(function(){  
							                if(serializeObj[this.name]){  
							                    if($.isArray(serializeObj[this.name])){  
							                        serializeObj[this.name].push(this.value);  
							                    }else{  
							                        serializeObj[this.name]=[serializeObj[this.name],this.value];  
							                    }  
							                }else{  
							                    serializeObj[this.name]=this.value;   
							                }  
							            });  
							            return serializeObj;  
							        }; 
							        
							        //绑定事件
									$("#btn").click(function(){
										var p = $("#searchForm").serializeJson();//{id:xx,name:yy,age:zz}
										//重新发起ajax请求，提交参数
										$("#grid").datagrid("load",p);
										//关闭查询窗口
										$("#searchWindow").window("close");
									});
								});
							</script>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>