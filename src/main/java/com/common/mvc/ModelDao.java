package com.common.mvc;

import java.util.List;

public class ModelDao {
	private static BaseDao dao = new BaseDao();

	private Class<? extends Model> clz;

	public ModelDao(Class<? extends Model> clz) {
		this.clz = clz;
	}

	public Model get(String sql,Object id) {
		return dao.find(sql, clz, new Object[]{id});
	}
	
	public void execute(String sql,Object[] params){
		System.out.println("sql=="+sql);
		dao.execute(sql, params);
	}

	public List<Model> list(String sql, Object[] params) {
		return dao.list(sql, clz, params);
	}
	
	
}
