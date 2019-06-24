<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'importList.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="css/common.css" />
	<script type="text/javascript" src="js/jquery-easyui-1.2.6/jquery-1.7.2.min.js"></script>
	<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.2.6/themes/default/easyui.css" />
	<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.2.6/themes/icon.css" />
	<script type="text/javascript" src="js/jquery-easyui-1.2.6/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="js/jquery-easyui-1.2.6/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="js/commons.js"></script>
	
	<link rel="stylesheet" type="text/css" href="css/uploadify.css"   />
	<script type="text/javascript" src="js/swfobject.js"></script>
	<script type="text/javascript" src="js/jquery.uploadify.v2.1.0.min.js"></script>
	
	<script type="text/javascript">
			$(function(){
				//文件上传uploadify
				$('#fileInput').uploadify({   
			 		'uploader': 'js/swf/uploadify.swf',
			        'script': 'importdata-upload',   //指定服务端处理类的入口
			        'scriptData' :{'templateId':$('#templates').combobox('getValue')}, 
			        'folder': 'default',   
			        'fileDataName': 'fileInput', //和input的name属性值保持一致就好，Struts2就能处理了    
			        'queueID': 'fileQueue',  
			        'auto': false,//是否选取文件后自动上传   
			        'multi': true,//是否支持多文件上传
			        'fileDesc' : 'Excel文档',
			        'fileExt' : '*.xls',
			        'sizeLimit' : 1024*1024,//最大1M
        			'simUploadLimit' : 1,//每次最大上传文件数  
			        'buttonImg': 'images/preview.png',//按钮上的图片 
			        'buttonText': ' ',//按钮上的文字 
					'onComplete': function (event, queueID, fileObj, response, data) {
							var result = eval("(" +response + ")" );
							var status = result.status;
							var msg = result.message;
							if(status == "ok"){
								$.messager.alert("提示信息",msg);
								$('#importDialog').dialog('close');
								$('#t_importdata').datagrid('load',null);
							}else{
								$.messager.alert("提示信息",msg);
							}
							
			         }    
		        });  	
					
				/**
				 *	初始化数据表格  
				 */
				$('#t_importdata').datagrid({
						idField:'id' ,		
						title:'数据列表' ,
						fit:true ,
						height:450 ,
						url:'importdata-list' ,
						fitColumns:true ,  
						striped: true ,					//隔行变色特性 
						loadMsg: '数据正在加载,请耐心的等待...' ,
						rownumbers:true ,
						frozenColumns:[[				//冻结列特性 ,不要与fitColumns 特性一起使用 
								{
									field:'ck' ,
									width:50 ,
									checkbox: true
								}
							]],
						columns:[[
							{
								field:'importid' ,
								title:'主表id' ,
								width:100 ,
								hidden:true
							},{
								field:'importDataType' ,
								title:'数据类型' ,
								width:100 ,
								sortable : true 
							},{
								field:'importDate' ,
								title:'导入时间' ,
								width:100 ,
								sortable : true 
							},{
								field:'importStatus' , 
								title:'导入标志' ,
								width:100 ,
								formatter:function(value , record , index){
										if(value == 1){
											return '导入成功' ;
										} else if( value == 0){
											return '导入失败' ; 
										}
								}
							},{
								field:'handleDate' , 
								title:'处理时间' ,
								width:100
							},{
								field:'handleStatus' , 
								title:'处理标志' ,
								width:100 ,
								formatter:function(value , record , index){
										if(value == 1){
											return '已处理' ;
										} else if( value == 0){
											return '未处理' ; 
										}
								}
							}
						]] ,
						pagination: true , 
						pageSize: 10 ,
						pageList:[5,10,15,20,50],
						toolbar:[
							{
								text:'导入模板',
								iconCls:'icon-add',
								handler:function(){
									$('#templateDialog').dialog('open');
								}
							},
							{
								text:'导入明细查看',
								iconCls:'icon-edit',
								handler:function(){
									//动态创建datagrid
									$('#divDataGrid').html('<table id="tatalTb"></table>');
									var arr = $('#t_importdata').datagrid('getSelections');
									if(arr.length != 1){
										$.messager.alert('提示信息','只能选择一条记录进行查看！');
										return;
									}else{
										$('#importDetailsDialog').dialog('open');
										$.ajax({
											url:'importdata-columns',
											type:'POST',
											dataType:'json',
											data:{templateId:arr[0].importDataType},
											success:function(data){
												$('#tatalTb').datagrid({													
						                            url: 'importdata-columndatas',
						                            fitColumns: true,
						                            idField:"appId",
						                            columns:data,
						                            queryParams:{importDataId:arr[0].importid},
													toolbar:[
														{
															text:'确认导入',
															iconCls:'icon-add',
															handler:function(){
																//只有未处理并且没有错误代码的数据允许确认导入
																var detailDatas = $('#tatalTb').datagrid('getData');
																for(var i = 0;i<detailDatas.total;i++){
																	var cgbz = detailDatas.rows[i].cgbz;
																	var hcode = detailDatas.rows[i].hcode;
																	if(cgbz!="未处理" || (hcode.indexOf("#000")>=0)){
																		$.messager.alert("提示信息","已处理或数据有误，请核对后再重新导入！");
																		return;
																	}
																}
															
																$.ajax({
																	url:'importdata-doimport',
																	type:"POST",
																	dataType:'json',
																	data:{importDataId:arr[0].importid},
																	success:function(data){
																		var status = data.status;
																		var message = data.message;
																		if(status == "ok"){
																			$.messager.alert("提示信息",message);
																			$('#tatalTb').datagrid('reload',null);
																		}else{
																			$.messager.alert("提示信息",message);
																		}
																	}
																});
															}
														},{
															text:'返回',
															iconCls:'icon-back',
															handler:function(){
																$('#importDetailsDialog').dialog('close');
															}
														}
													]

												});
											}
											
										});
									}
								}
							}
						]
				});
				
				$('#okBtn').click(function(){
					//获取选择模板ID
					var str = $('#templates').combobox('getValue');
					$('#templateDialog').dialog('close');
					$('#importDialog').dialog('open');
					var val = "download?templateId=" + str;
					$('#downloadTemplate').attr('href',val);
				});
				
				$('#uploadBtn').click(function(){
					var val = $('#templates').combobox('getValue');
					$('#fileInput').uploadifySettings('scriptData',{'templateId':val});
					$('#fileInput').uploadifyUpload();
				});
				
		});
			
	</script>
  </head>
  
  <body>
		<div id="lay" class="easyui-layout" style="width: 100%;height:100%" >				
				<div region="center" >
					<table id="t_importdata"></table>
				</div>
		</div>
		
		<div id="templateDialog" title="选择模板" modal=true draggable=false
			class="easyui-dialog" closed=true style="width: 350px;height:220px"
		>
			<form action="">
				<table>
					<tr>
						<td>选择模板：</td>
						<td>
							<input id="templates" name="templates" class="easyui-combobox" panelHeight="auto"
								url="importdata-templates" valueField="templateId" textField="templateName" value="" />
							<a id="okBtn" class="easyui-linkbutton">确定</a>	
						</td>
					</tr>
				
				</table>
			</form>
		</div>
		
		<div id="importDialog" title="导入Excel" modal=true  draggable=false class="easyui-dialog" closed=true style="width:350px;height:220px;">
 				<form id="importForm" action="importdata-upload" method="post">
    				<table>
    				    <tr>
    						<td>下载模板:</td>
    						<td>
    							<a id = "downloadTemplate" >导入模板</a>
    						</td>
    					</tr>
    					<tr>
    						<td>浏览:</td>
    						<td>
    							<input id="fileInput" name="fileInput" type="file" />
    						</td>
    					</tr>
    					<tr>
    						<td colspan="2">
    							<div id="fileQueue"></div><a id="uploadBtn" class="easyui-linkbutton" >导入</a>
    						</td>
    					</tr>
    				</table>
    			</form>
 		</div>
 		<div id="importDetailsDialog" title="导入明细查看" modal=true  draggable=false class="easyui-dialog" closed=true style="width:800px;height:500px;">
 				<div id="divDataGrid"></div>
 		</div>
  </body>
</html>
