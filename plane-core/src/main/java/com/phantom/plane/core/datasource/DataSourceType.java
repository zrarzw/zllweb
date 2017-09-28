package com.phantom.plane.core.datasource;

import org.apache.log4j.Logger;

/**
 * 
* <p>project:plane-core</p>
* <p>Description:数据源枚举 </p>
* <p>Company: </p> 
* @author zw
* @date 2017年8月27日下午5:01:58
 */
public enum DataSourceType {
	

    WRITE("write","主库"), READ("read","从库");

    final private String code;

    final private String name;
    
    private static Logger logger = Logger.getLogger(DataSourceType.class);

    DataSourceType(String _code,String _name) {
        this.code = _code;
        this.name = _name;
    }

    public String getCode() {
        return code;
    }

    public String getName(){
        return name;
    }
/**
 * 
* <p>Title: </p>
* <p>Description: </p>
 */
    public static String getNameByCode(String _code){
        for(DataSourceType item : DataSourceType.values()){
            if(item.getCode().equals(_code))
            {
            	logger.info("springboot---------->根据_code返回datasourceName");
                return item.getName();
            }
        }
        return "";
    }
}
