package com.phantom.plane.core.mybatis;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.phantom.plane.core.annotation.MapperClass;
import com.phantom.plane.core.base.BaseEntity;
import com.phantom.plane.core.dao.BaseDao;
import com.phantom.plane.core.dao.FlexiPage;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

@Repository("myBatisBaseDao")
@SuppressWarnings("unchecked")
public class MyBatisBaseDaoImpl<T extends BaseEntity> implements BaseDao<T> {

	@Resource
	@Qualifier("sqlSessionFactory")
	private SqlSessionFactory sqlSessionFactory;

	private SqlSession sqlSession;

	@SuppressWarnings("rawtypes")
	public <M extends Mapper<T>> M getMapper(Class cls) {
		MapperClass mapperClass = (MapperClass) cls.getAnnotation(MapperClass.class);
		if (null == mapperClass) {
			throw new RuntimeException("没有注解MapperClass");
		}
		return (M) getSqlSession().getMapper(mapperClass.value());
	}

	@Override
	public T getEntityById(Class<T> cls, Integer id) {
		return this.getMapper(cls).selectByPrimaryKey(id);
	}

	@Override
	public void addEntity(T entity) {
		this.getMapper(entity.getClass()).insert(entity);
	}

	@Override
	public void updateEntity(T entity) {
		this.getMapper(entity.getClass()).updateByPrimaryKey(entity);
	}

	@Override
	public void deleteEntityById(Class<T> cls, Integer id) {
		this.getMapper(cls).deleteByPrimaryKey(id);
	}

	@Override
	public List<T> selectAll(Class<T> cls) {
		return this.getMapper(cls).selectAll();
	}

	@Override
	public List<T> findByLike(Example example) {
		return this.getMapper(example.getEntityClass()).selectByExample(example);
	}

	@Override
	public List<T> findByPage(Example example, FlexiPage flexiPageDto) {

		RowBounds rowBounds = new RowBounds(flexiPageDto.getOffset(), flexiPageDto.getRp());
		return this.getMapper(example.getEntityClass()).selectByExampleAndRowBounds(example, rowBounds);
	}

	@Override
	public int findRowCount(Example example) {
		return this.getMapper(example.getEntityClass()).selectCountByExample(example);
	}

	public SqlSession getSqlSession() {
		if (null == sqlSession) {
			synchronized (MyBatisBaseDaoImpl.class) {
				this.sqlSession = new SqlSessionTemplate(sqlSessionFactory);
			}
		}
		return this.sqlSession;
	}

}
