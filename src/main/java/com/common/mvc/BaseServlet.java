package com.common.mvc;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.common.conf.Configuration;


/**
 */
public class BaseServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	protected static String baseUrl = null;
	protected static String context=null;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			baseUrl=Configuration.getString("baseUrl");
		} catch (Exception e) {

		}
		if(baseUrl==null||baseUrl.trim().length()==0){
			baseUrl=config.getServletContext().getContextPath();
		}
		context=config.getServletContext().getContextPath();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setCharacterEncoding("GBK");
		resp.setContentType("text/html;charset=GBK");
		req.setAttribute("baseUrl", baseUrl);
		try {
			ActionManager.execute(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
			forward(req,resp, "/error.jsp");
		} 
		
	}
	
	protected void forward(HttpServletRequest req,HttpServletResponse resp,String path) {
		try {
			req.getRequestDispatcher(path+".jsp").forward(req, resp);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		};
	}
	
		
}
