package com.phantom.plane.login.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

/**
 * Created by  on 2016/9/2.
 */
@Entity
@NameStyle(value = Style.camelhumpAndLowercase)
@Table(name = "user")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)

public class UserBO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	 @Column
    private String account; 
	 @Column
	private String name;
	 @Column
    private Integer age;

 public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getAccount() {
 		return account;
 	}

 	public void setAccount(String account) {
 		this.account = account;
 	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}