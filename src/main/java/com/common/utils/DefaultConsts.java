package com.common.utils;


import java.util.Locale;

/**
 * @author back_cloud_003 2014年2月22日
 */
public class DefaultConsts {

	private DefaultConsts(){}

	/**
	 * 默认国际化
	 */
	public static final Locale DEFAULT_LOCALE = Locale.CHINA;

	public static final String DEFAULT_ALL_COMPAY_CODE = "*";

	public static final String LOCALE_CN = "zh_CN";
	public static final String LOCALE_EN = "en_US";

	public static final String CURRENCY_CNY = "CNY";

	/**
	 * 默认字符集
	 */
	public static final String DEFAULT_CHARSET = "UTF-8";


	/**
	 * 国旅运通公司代码
	 */
	public static final String AMEX_COMPANY_CODE = "citsamex";

	/**
	 * APP系统
	 */
	public static final String SYSTEM_KEY_APP = "SYSTEM_APP";

	public static final Double MAX_PRICE = 10000000D;
	
	/**
	 * NTC任务组
	 */
	public static final String NTC_TASK_GROUP="TA_TASK";
	
	/**
	 * NTC 任务生成
	 */
	public static final String NTC_PROCESS_INSTANCE_CREATE_TASK="taTaskCreateTask";
	
	/**
	 * NTC 刷新任务待处理人列表
	 */
	public static final String NTC_REFRESH_CANDIDATE_TASK="refreshCandidateTask";
	
	/**
	 * 自动出票结果发送消息任务
	 */
	public static final String AUTO_TICKET_RECEIVE_MSG_TASK="autoTicketNtcProcessMsgTask";
	
	/**
	 * 国内机票IUR任务
	 */
	public static final String DOMAIR_IUR_TASK="domAirIURReceiveTask";
	
	/**
	 * 自动做账任务
	 */
	public static final String AUTO_SALESFOLDER_TASK="autoSalesFolderReceiveTask";

	/**
	 * 退票自动做账任务
	 */
	public static final String REFUND_AUTO_SALESFOLDER_TASK="refundAutoSalesFolderReceiveTask";

	/**
	 * 新NTC系统标记
	 */
	public static final String NEW_NTC_SYSTEM = "NEW_NTC_SYSTEM";
	
	/**
	 * 新NTC启动流程
	 */
	public static final String TA_PROCESS_CREATE_TASK="taProcessCreateTask";
	
	public static final String TASK_CREATE_FAILED_RESULT="FAILED";
	
	public static final String PARAM_PS_MIS_NAME="PSXMLBOOKINGMISs";

	public static final String PARSM_PS_REFUND_TAX_NAME = "PSXMLREFUNDTAXs";

	public static final String AUTO_KAO_TASK="autoKAOReceiveTask";
	
	public static final String COMMON_TASK="commonTask";
	
	public static final String FIELD_KEY_GROUP="group";
	
	public static final String FIELD_KEY_COMPANY="company";
	
	/**
	 * 自动做账重试任务
	 */
	public static final String ASF_RETRY_TASK="asfRetryTask";
	
	public static final String COUNTRY_CODE_CN = "CN";
	
	/**
	 * 国内机票任务组
	 * */
	public static final String DOM_AIR_TASK_GROUP = "DOM_AIR";
	
	/**
	 * 国内机票取消PNR任务
	 * */
	public static final String DOMAIR_AUTO_CANCEL_PNR_TASK="domAirAutoCancelPnrTask";
	
	/**
	 * 国内机票退票任务（AXO前台）
	 */
	public static final String DOMAIR_AUTO_REFUND_TICKET_TASK ="domAirAutoRefundTicketTask";
	
	/**
	 * 国内机票退票任务（NTC后台）
	 * */
	public static final String DOMAIR_AUTO_REFUND_TICKET_OFFLINE_TASK = "domAirAutoRefundTicketForNTCTask";

	/**
	 * 国内机票改签任务(AXO前台)
	 */
	public static final String DOMAIR_AUTO_REISSUE_TICKET_TASK ="domAirAutoReissueTicketTask ";

	/**
	 * 国内机票改签IUR任务
	 */
	public static final String DOMAIR_REISSUE_IUR_TASK="domAirReissueIURReceiveTask";

	/**
	 * 火车退票结果发送消息任务
	 */
	public static final String TRAIN_REFUND_NTC_PROCESS_MSG_TASK="trainRefundNtcProcessMsgTask";
	/**
	 * 火车改签结果发送消息任务
	 */
	public static final String TRAIN_CHANGE_NTC_PROCESS_MSG_TASK="trainChangeNtcProcessMsgTask";
	
	/**
	 * 订单状态消息推送重试接口
	 */
	public static final String TA_STATUS_MESSAGE_SEND_TASK="taStatusMessageSendTask";

	/**
	 * 流程之间消息发送
	 */
	public static final String PROCESS_MESSAGE_SEND_TASK="processMessageSendTask";

	/**
	 * 假退票数据抓取
	 */
	public static final String INTLAIR_REFUND_INVENTED_DATA_TASK="intlAirRefundInventedDataTask";

	/**
	 * 国际机票递送单号数据抓取
	 */
	public static final String INTLAIR_EXPRESS_DATA_TASK="intlAirExpressDataTask";

	/**
	 * 国际机票PNR最晚出票时限
	 */
	public static final String INTL_PNR_INFO_TASK="intlPnrInfoTask";

	public static final String DOMAIR_UATP_CODE_TASK="domAirUatpCodeTask";

	/**
	 * 将国际机票uatp卡录入pnr任务
	 */
	public static final String INTLAIR_UATP_UPDATE_TASK="intlAirUatpUpdateTask";

	/**
	 * HRFEED--profile处理任务
	 */
	public static final String PROFILE_HANDLER_TASK="profileHandlerTask";

	/**
	 * 订单提交后，未审订单的创建人提醒
	 */
	public static final String REMINDER_CREATOR_ORDER_APPROVALTASK="reminderCreatorOrderApprovalTask";
}
