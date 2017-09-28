package com.phantom.plane.core.mybatis;

import java.util.List;

import javax.annotation.Resource;

import com.phantom.plane.core.base.BaseEntity;
import com.phantom.plane.core.base.BaseService;
import com.phantom.plane.core.dao.BaseDao;
import com.phantom.plane.core.utils.GenericeClassUtils;

import tk.mybatis.mapper.common.Mapper;

public class MyBatisBaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

	@SuppressWarnings("unchecked")
	protected Class<T> entityClass = (Class<T>) GenericeClassUtils.getSuperClassGenricType(this.getClass(), 0);

	protected <M extends Mapper<T>> M getMapper(Class cls) {

		return ((MyBatisBaseDaoImpl<T>) baseDao).getMapper(cls);
	}

	@Resource(name = "myBatisBaseDao")
	public BaseDao<T> baseDao;

	@Override
	public T getEntityById(Integer id) {
		return baseDao.getEntityById(entityClass, id);
	}

	@Override
	public void addEntity(T entity) {
		baseDao.addEntity(entity);
	}

	@Override
	public void updateEntity(T entity) {
		baseDao.updateEntity(entity);
	}

	@Override
	public void deleteEntityById(Integer id) {
		baseDao.deleteEntityById(entityClass, id);
	}

	@Override
	public List<T> selectAll() {
		return baseDao.selectAll(entityClass);
	}

}
