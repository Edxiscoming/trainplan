var portal={
		common:{
			dialogmsg:{//portal.common.dialogmsg
				REQUESTFAIL:"请求发送失败",
				SYSYEMERROR:"系统发生异常",
				DATALOADFAIL:"数据加载失败",
				GETDATEFAIL:"获取当前日期失败",
				DATETIMEOVER:"你选择的时间跨度超过9周，请重新选择",
				DATEERROR:"请选择正确的时间范围"
			},
			tip:{//portal.common.tip
				APPNAMETEXT:" 应用名称不能为空，由中文、字母、数字、下划线组成，长度不能大于20",
				VERSIONTEXT:"版本不能为空，由数字和小数点组成，长度为3到10",
				APPDESCRIPIONTEXT:"应用描述长度不能大于500",
				SUBDOMAINTEXT:"二级域名仅允许由数字，字母组成，长度为4到18位."
			}
		},
		application:{//portal.application.dialogmsg
				dialogmsg:{
					ONLYONEAPP:"只能实施监控一个应用 !",
					CREATESUCESS:"创建应用基础信息成功",
					CREATEFAIL:"创建应用基础信息失败",
					UPLOADFAIL:"上传文件失败!",
					UPLOADERROR:"请先上传应用包,在编辑运行环境信息"
				},
				tip:{//portal.application.tip
					FILETEXT:"请上传应用包附件，且不能超过100M",
					VMSNUMVALIDATE:"虚拟机数量为必填项，输入为数字，范围：1~9",
					VMSNUMVALIDATE_2:"虚拟机数量为必填项，输入为数字，范围：1~9，必须小于弹性伸缩上限",
					UPLIMITVALIDATE:"弹性伸缩上限为必填项，输入为数字，范围：1~9",
					UPLIMITVALIDATE_2:"弹性伸缩上限必须大于弹性伸缩下限",
			
					MIGRATETEXT:"请选择需要迁移的应用."
				}
			
		},
		monitor:{
				dialogmsg:{//portal.monitor.dialogmsg
					SELECTSTRATEGY:"请选择监控策略!"
				},
				tip:{//portal.monitor.tip
					LABEL_STRATEGYEXPRESSION_T:"持续时间必须为正整数或0",
					LABEL_STRATEGYEXPRESSION_N:"阈值必须为正整数或0",
					LABEL_STRATEGYEXPRESSION:"请添加策略条件",
					LABEL_INPUT_STRATEGYDESCRIPT:"策略描述信息长度不能大于500",
					_LABEL_STRATEGYNAME:"策略名称由数字、字母、下划线组成长度不能大于100"
					
				}
			
		}
		
		
	}
