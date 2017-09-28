package com.phantom.plane.core.common;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "T_XT_LOG_ERROR")
public class EwSysLogError implements java.io.Serializable {

	private static final long serialVersionUID = 2606924176565994547L;
	private String logid;
	private String errorCode;
	private String errorMessage;
	private String errorInfo;
	private Date occurTime;

	public EwSysLogError() {
	}

	public EwSysLogError(String logid) {
		this.logid = logid;
	}

	@GenericGenerator(name = "generater", strategy = "uuid")
	@GeneratedValue(generator = "generater")
	@Id
	@Column(name = "LOGID", unique = true, nullable = false, length = 32)
	public String getLogid() {
		return this.logid;
	}

	public void setLogid(String logid) {
		this.logid = logid;
	}

	@Column(name = "ERROR_CODE", length = 50)
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	@Column(name = "ERROR_MESSAGE", length = 3000)
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Column(name = "ERROR_INFO")
	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OCCUR_TIME")
	public Date getOccurTime() {
		return occurTime;
	}

	public void setOccurTime(Date occurTime) {
		this.occurTime = occurTime;
	}

}
