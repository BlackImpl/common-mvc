package com.common.mvc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.common.conf.Configuration;

public class Model extends HashMap<String, Object> implements
		Map<String, Object> {

	private static final long serialVersionUID = 1L;
	public static ModelDao dao;
	static {
		if (Configuration.containKey("dbName")) {
			dao = new ModelDao(getNowClass());
		}
	}
	
	public static Model getModel(Class<? extends Model> clz,
			HttpServletRequest req) {
		try {
			return clz.newInstance().init(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public String strV(String key){
		Object v=get(key);
		if(v!=null){
			return v.toString();
		}
		return null;
	}
	public static Model getModel(Class<? extends Model> clz, ResultSet rs) {
		try {
			return clz.newInstance().init(rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Class<? extends Model> getNowClass() {
		return new Object() {
			@SuppressWarnings("unchecked")
			public Class<? extends Model> getNowClass() {
				try {
					String clazzName = this.getClass().getName();
					return (Class<? extends Model>) Class.forName(clazzName
							.substring(0, clazzName.lastIndexOf('$')));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				return null;
			}
		}.getNowClass();
	}

	protected String name;

	@SuppressWarnings("unchecked")
	public Model init(HttpServletRequest request) {

		Enumeration<String> names = request.getParameterNames();

		while (names.hasMoreElements()) {

			String key = names.nextElement();

			String value = request.getParameter(key);

			if (value == null) {
				value = "";
			}

			this.put(key, value);

		}

		return this;
	}

	public Model init(ResultSet rs) {
		int col;
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			this.name = rsmd.getTableName(1);
			col = rsmd.getColumnCount();
			for (int i = 1; i <= col; i++) {
				this.put(rsmd.getColumnName(i), rs.getObject(i));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;

	}

	public Model set(String name, Object value) {
		this.put(name, value);
		return this;
	}

	public void save() {
		this.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
		StringBuilder sql = new StringBuilder();
		sql.append("insert into " + getTableName() + " (");
		Set<Entry<String, Object>> entries = this.entrySet();
		StringBuilder valueStrs = new StringBuilder();
		valueStrs.append("( ");
		Object[] values = new Object[entries.size()];
		Iterator<Entry<String, Object>> iterator = entries.iterator();
		for (int i = 0; i < entries.size(); i++) {
			if (i != 0) {
				sql.append(",");
				valueStrs.append(",");
			}
			Entry<String, Object> entry = iterator.next();
			sql.append(entry.getKey());
			valueStrs.append("?");
			values[i] = entry.getValue();
		}
		valueStrs.append(" )");
		sql.append(" ) values");
		sql.append(valueStrs);
		dao.execute(sql.toString(), values);
	}

	public void update() {
		StringBuilder sql = new StringBuilder();
		sql.append("update ");
		sql.append(getTableName());
		sql.append(" set ");
		Set<Entry<String, Object>> entries = this.entrySet();
		List<Object >values = new ArrayList<Object>();
		Iterator<Entry<String, Object>> iterator = entries.iterator();
		int start = 0;
		for (int i = 0; i < entries.size(); i++) {

			Entry<String, Object> entry = iterator.next();
			if (entry.getKey().equals("id")) {
				if (i == 0) {
					start = 1;
				}
				continue;
			}

			if (i != start) {
				sql.append(",");
			}
			sql.append(entry.getKey());
			sql.append("=?");
			values.add(entry.getValue());

		}
		sql.append(" where id=?");
		values.add(this.get("id"));
		dao.execute(sql.toString(), values.toArray());
	}

	public void delete() {
		String sql = "delete from " + getTableName() + " where id=?";
		dao.execute(sql, new Object[] { this.get("id") });
	}

	public Model get() {
		String sql = "select * from " + getTableName() + " where id=?";
		return dao.get(sql, this.get("id"));
	}

	public List<Model> list() {
		return list("", null);
	}

	public List<Model> list(String sql, String params) {
		if (sql.indexOf("select") == -1) {
			sql = "select * from " + getTableName() + " " + sql;
		}
		if (params != null) {
			String[] ps = params.split(",");
			Object[] v = new Object[ps.length];
			for (int i = 0; i < ps.length; i++) {
				v[i] = ps[i];
			}
			return dao.list(sql, v);
		} else {
			return dao.list(sql, null);
		}
	}

	protected String getTableName() {
		return this.getClass().getSimpleName().toLowerCase();
	}
	
	public String toString(){
		StringBuilder builder=new StringBuilder();
		builder.append("{");
		Iterator<Entry<String, Object>> it=this.entrySet().iterator();
		int i=0;
		while (it.hasNext()) {
			Entry<String, Object> entry=it.next();
			if(i!=0){
				builder.append(",");
			}else{
				i++;
			}
			builder.append("\"");
			builder.append(entry.getKey());
			builder.append("\":\"");
			builder.append(entry.getValue());
			builder.append("\"");
		}
		builder.append("}");
		return builder.toString();
	}

}
