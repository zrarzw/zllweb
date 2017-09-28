/**
 * Project:				easyweb-core
 * Author:				Green
 * Company: 			杭州中软
 * Created Date:		2014-10-9
 * Description:			请填写该类的功能描述
 * Copyright @ 2014 CS&S.COM – Confidential and Proprietary
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date			|time		|Author	|Change Description
 */
package com.phantom.plane.core.handler;

import java.util.Date;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import com.phantom.plane.core.base.BaseException;
import com.phantom.plane.core.common.EwSysLogError;
import com.phantom.plane.core.exception.ExceptionUtil;
import com.phantom.plane.core.exception.IAppResponse;
import com.phantom.plane.core.utils.SpringContext;



/**
 * 数据库值存储异常信息
 * @author Green
 *
 */
public class DbHandler implements IExceptionHandler {

	@Override
	public Object handler(BaseException e, IAppResponse response) {
		StringBuffer msg = new StringBuffer();
		msg.append("\n");
		ExceptionUtil.getExceptionUtil().getExceptionMsg(e, msg);
		
		Throwable base = ExceptionUtil.getExceptionUtil().getRootCause(e);
		String errorCode = String.valueOf(e.getErrorCode());
		String errorMessage = base.getMessage()==null ? base.getClass().toString() : base.getMessage();
		
		EwSysLogError errorBO = new EwSysLogError();
		errorBO.setErrorCode(errorCode);
		errorBO.setErrorMessage(errorMessage);
		errorBO.setErrorInfo(msg.toString());
		errorBO.setOccurTime(new Date());
		
		SessionFactory sessionFactory = (SessionFactory) SpringContext.getBean("sessionFactory");
		if (null != sessionFactory) {
			Session session = null;
			Transaction transaction = null;
			try {
				session = sessionFactory.openSession();
				transaction = session.beginTransaction();
				session.save(errorBO);
				transaction.commit();
			} catch (Exception ex) {
				if (transaction != null) {
					transaction.rollback();
				}
			} finally {
				if (null != session) {
					session.close();
				}
			}
		}
		return response;
	}

}
