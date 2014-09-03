package com.common.mvc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.common.db.DbManger;

public class BaseDao {
	

	public List<Model> list(String sql, Class<? extends Model> clz,
			Object[] params) {
		System.out.println("sql=="+sql);
		List<Model> list = new ArrayList<Model>();
		try {
			Connection conn = getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;

			try {
				ps = conn.prepareStatement(sql);
				setParmas(ps, params);

				rs = ps.executeQuery();

				while (rs.next()) {

					list.add(Model.getModel(clz, rs));
				}
			} finally {
				close(conn, ps, rs);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public Model find(String sql, Class<? extends Model> clz, Object[] params) {
		Model module = null;
		try {
			Connection conn = getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;

			try {
				ps = conn.prepareStatement(sql);
				setParmas(ps, params);

				rs = ps.executeQuery();

				if (rs.next()) {
					module = clz.newInstance().init(rs);
				}
			} finally {
				close(conn, ps, rs);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return module;
	}

	public void execute(String sql, Object[] params) {
		try {
			Connection conn = getConnection();
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement(sql);

				setParmas(ps, params);

				ps.execute();
			} finally {
				close(conn, ps, null);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected Connection getConnection() throws SQLException {

		return DbManger.getConection();

	}

	private void setParmas(PreparedStatement ps, Object[] parms)
			throws SQLException {
		if (parms != null) {
			int index = 1;
			for (Object obj : parms) {
				if (obj instanceof String) {
					ps.setString(index, (String) obj);
				} else if (obj instanceof Long) {
					ps.setLong(index, (Long) obj);
				} else if (obj instanceof Integer) {
					ps.setInt(index, (Integer) obj);
				} else if (obj instanceof Double) {
					ps.setDouble(index, (Double) obj);
				} else if (obj instanceof Float) {
					ps.setDouble(index, (Float) obj);
				} else if (obj instanceof Date) {
					Date d = (Date) obj;
					java.sql.Timestamp sqlDate = new java.sql.Timestamp(
							d.getTime());
					ps.setTimestamp(index, sqlDate);
				} else {
					ps.setNull(index, Types.NULL);
				}
				index++;
			}
		}

	}

	private void close(Connection conn, PreparedStatement ps, ResultSet rs)
			throws SQLException {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {

			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (Exception e) {

			}
		}
		if (conn != null) {
			try {
				conn.commit();
			} catch (Exception e) {

			}
			try {
				conn.close();
			} catch (Exception e) {

			}
		}
	}
}
