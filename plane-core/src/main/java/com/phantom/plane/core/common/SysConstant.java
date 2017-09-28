package com.phantom.plane.core.common;


import java.io.Serializable;

import com.phantom.plane.core.utils.PropertyManager;
import com.phantom.plane.core.utils.StringUtil;

/**
 * 框架常量类
 * 与框架相关参数使用该类
 * @author GreenZHAO
 *
 */
public class SysConstant implements Serializable{
	private static final long serialVersionUID = -5338061128474385804L;
	
	// 成功标示
	public static final String SUCCESS_CODE="200";
	// 失败标示
	public static final Integer FAILED_CODE = 400;
	// 业务主动提示失败标示
	public static final Integer FAILED_CODE_BUSINESS = 500;
	// 超时标示
	public static final String TIMEOUT_CODE="301";
	// 消息标识
	public static final String INFO_CODE="205";
	// 成功消息
	public static final String SUCCESS_MESSAGE="操作成功";
	// 失败消息
	public static final String FAILED_MESSAGE="操作失败";
	// 超时消息
	public static final String TIMEOUT_MESSAGE="会话超时";
	// 文件不存在消息
	public static final String FILE_NOT_FOUND = "请求失败,未找到该文件";
	
	// Http Request(存放ThreadLocal中的Key值)
	public static final String HTTP_REQUEST = "request";
	public static final String HTTP_RESPONSE = "response";
	public static final String CONTENT_TYPE_DIY = "HTML";
	// 工作流:节点图片点击事件触发JS
	public static final String FLOW_IMAGE_CLICK = "flowImageClick";
	// 工作流:会签任务跳转要求EL名称
//	public static final String WORKFLOW_TASK_ASSIGN_EL_NAME = "TASK_SIGN_RESULT_EL_NAME";
	public static final String WORKFLOW_PROPERTY_NAME = "ZrarWorkflow";
	// 异常配置文件名称
	public static final String FILE_ERROR_CONFIG = "error-config";
	
	// 同步检查JSON数据KEY
	public static final String EXIST_CHECK_KEY = "exist";
	public static final String EXIST_CHECK_MESSAGE = "message";
	
	// 在线用户
	public static final String ONLINE_USER = "onlineUserList";

	public static final Boolean DEVELOP_MODEL = "true".equals(PropertyManager.getProperty("DevelopModel"));
	public static final String UPLOAD_DIR = PropertyManager.getProperty("CommonUploadFileUrl");
	public static final String UPLOAD_DIR_WORKFLOW = PropertyManager.getProperty("WorkflowUploadFileUrl");
	public static final Boolean DEFINITION_COMBINED = "true".equals(PropertyManager.getProperty("DefinitionCombinedSwitch"));
	
	// 大文件流标识
	public static final String IS_BIG_FILE = "IsBigFile";
	// 大文件流标识
	public static final String BIG_FILE = "BigFile";
	/**是否断点续传大文件*/
	public static final String BIG_FILE_IS_PART = "BigFileIsPart";

	public static final String BIG_FILE_SIZE = "BigFileSize";

	public static final String BIG_FILE_START = "BigFileStart";

	public static final String BIG_FILE_END = "BigFileEnd";

	public static String BIG_FILE_NAME = "BigFileName";
	public static final int NTKOLF_BLOCKSIZE = 131072;// 块大小，注意这个是保留大小，不能修改！

	// 系统类型
	public static final String SYSTEM_TYPE = PropertyManager.getProperty("SystemType");
	/**系统默认的分页条数*/
	public static final Integer DEFAULT_PAGESIZE = 
		StringUtil.isNotNull(PropertyManager.getProperty("DefaultPageSize")) ?
		Integer.valueOf(PropertyManager.getProperty("DefaultPageSize")) : 10;
	// 系统类型
	public static final Boolean NEED_TOKEN = Boolean.valueOf(PropertyManager.getProperty("NeedToken"));
	
	//文件加密固死密钥
	public static final String SECRET_KEY_OF_FILE = "WnJhcjgkODBFNOJBOEAzUA==";
	//获取临时文件key
	public static final String TEMP_FILE = "TempFile";
}
