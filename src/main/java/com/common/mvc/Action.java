package com.common.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public abstract class Action {
	private HttpServletRequest req;
	private HttpServletResponse resp;
	private String renderUrl;
	
	
	public String getRenderUrl(){
		return renderUrl;
	}
	public void init(HttpServletRequest req, HttpServletResponse resp) {
		this.req = req;
		this.resp = resp;
	}

	protected Model getModel(Class<? extends Model> clz) {
		return Model.getModel(clz, req);
	}

	protected Model getModel() {
		return Model.getModel(Model.class, req);
	}

	protected void render(String path) {
		this.renderUrl = path;

	}

	protected void setAttr(String key, Object value) {
		req.setAttribute(key, value);
	}

	protected String getAttr(String key) {
		return req.getParameter(key);
	}

	protected void copyAttr(String keyStr) {
		String[] keys=keyStr.split(",");
		for (String key : keys) {
			req.setAttribute(key, req.getParameter(key));
		}
	}

	public abstract void index();



}
