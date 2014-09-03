package com.common.mvc;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.common.conf.Configuration;


public class ActionManager {
	private static String suffix="Action";
	private static Map<String, Method> methods = new ConcurrentHashMap<String, Method>();
	private static String basePackage;
	private static Logger log=Logger.getLogger(ActionManager.class);
	static {
		basePackage = Configuration.getString("package");
		log.info("base :"+ basePackage);
	}

	public static void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		int length = req.getContextPath().length()+1;
		String url = req.getRequestURI();
		url = url.substring(length, url.length() - 3);
		String[] u = url.split("\\.");
		String methodName = "index";
		if (u.length == 2) {
			methodName = u[1];
		}
		String className = u[0].substring(0, 1).toUpperCase()
				+ u[0].substring(1);
		//杞垚鐩稿簲鐨勭被
		className = basePackage + "." + className+suffix;
		Class<?> actionClass=getActionClass(className);
		Action action=(Action) actionClass.newInstance();
		action.init(req, resp);
		Method method=getMethod(actionClass, methodName);
		if(method.getReturnType().getName().equals("void")){
			method.invoke(action);
			String renderUrl=action.getRenderUrl();
			if(renderUrl==null){
				renderUrl="/"+u[0]+"/"+methodName;
			}
			req.getRequestDispatcher(renderUrl+".jsp").forward(req, resp);
		}else{
			PrintWriter out=resp.getWriter();
			out.print("{success:true,data:");
			Object object=method.invoke(action).toString();
			if(object instanceof String){
				out.print("\"");
				out.print(object.toString());
				out.print("\"");
			}else{
				out.write(object.toString());
			}
		    out.print("}");
		    out.flush();
		}
		
	}

	
	
	private static Class<?> getActionClass(String className) throws Exception{
		return (Class<?>) Class.forName(className);
		
	}
	
	private static Method getMethod(Class<?> clz,String methodName){
		Method method=methods.get(clz.getName()+methodName);
		if(method==null){
			try {
				method=clz.getMethod(methodName);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		return method;
	}

}
