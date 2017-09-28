package com.phantom.plane.core.base;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NameStyle(value = Style.camelhumpAndLowercase)
public class BaseEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("id")
	private Integer id;

	/**
	 *  创建时间1111
	 */
	@Column 
	private Date createTime;

	/**
	 * 创建人
	 */
	@Column
	private String createUser;

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the createUser
	 */
	public String getCreateUser() {
		return createUser;
	}

	/**
	 * @param createUser
	 *            the createUser to set
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

}
