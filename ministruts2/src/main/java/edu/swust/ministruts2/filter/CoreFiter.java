package edu.swust.ministruts2.filter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.swust.ministruts2.config.ActionConfig;
import edu.swust.ministruts2.config.ResultView;
import edu.swust.ministruts2.context.ActionContext;



/**
 * Servlet Filter implementation class CoreFiter
 */
public class CoreFiter implements Filter {

	private static final String DEFAULT_CONFIG_FILE_PATH = "mini.xml";
	private static final String ACTION = "action";
	private static final String ACTION_NAME = "name";
	private static final String ACTION_CALSS = "class";
	private static final String ACTION_METHOD = "method";
	private static final String ACTION_METHOD_NULL = "";
	
	private static final String RESULT_ELEMENT = "result";
	private static final String RESULT_NAME = ACTION_NAME;
	private static final String RESULT_TYPE = "type";
	private static final String RESULT_DISPATCHER = "dispatcher";
	private static final String RESULT_REDIRECT = "redirect";
	private static final String RESULT_ERROR_INFO = "结果视图不存在。";
	
	private static final int URL_HEAD_LENGTH = 13;
	private Map<String, ActionConfig> configs = new HashMap<String, ActionConfig>();
	/**
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		/**
		 * 在此读取mini.xml，因为读取是IO操作，只需要读取一次，然后保存在内存中即可
		 */
		try {
			//拿到 document创建者
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            //拿到 document对象
			Document document = builder.parse(Thread.currentThread()
					                          .getContextClassLoader()
					                          .getResourceAsStream(DEFAULT_CONFIG_FILE_PATH));
		    NodeList nodeList = document.getElementsByTagName(ACTION);
			/**
		     * 通过document拿到Action中的所有元素
		     *   NodeList:所有Action元素的集合
		     */
		    for (int i = 0; i < nodeList.getLength(); i++) {
		    	/**
		    	 * 定义的Action是一个Element，强转之后有需要的方法
		    	 */
				Element element = (Element) nodeList.item(i);
				ActionConfig config = new ActionConfig();
				String name = element.getAttribute(ACTION_NAME);
				config.setName(name);
				config.setPath(element.getAttribute(ACTION_CALSS));
				String method = element.getAttribute(ACTION_METHOD);
				if (method != null && !method.equals(ACTION_METHOD_NULL)) {
					config.setMethod(method);
				}
				
				NodeList resultNodeList = element.getElementsByTagName(RESULT_ELEMENT);
				for (int j = 0; j < resultNodeList.getLength(); j++) {
					Element result = (Element) resultNodeList.item(j);
					String resultName = result.getAttribute(RESULT_NAME);
					String type = result.getAttribute(RESULT_TYPE);
					String url = result.getTextContent().trim(); // 去掉空格
					config.addResult(resultName, new ResultView(resultName, type, url));
				}
				
				configs.put(name, config);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(configs);
	}

	/**
	 * 每次请求都会执行
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		/**
		 * 强转为HttpServletResponse和HttpServletRequest，获得更多方法，便于操作
		 */
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpServletRequest request = (HttpServletRequest) req;
		
		ActionContext context = new ActionContext(request, response);
		ActionContext.setContext(context);
		/**
		 * 获取URI路径
		 */
		//  URI：  /minstruts2/xxxx    去掉前缀：/minstruts2/  得到：xxxx
		String uri = request.getRequestURI().substring(URL_HEAD_LENGTH);
		/**
		 * 根据不同的URI在Map中找到对应的config
		 * 		创建不同的对象，并且调用对应的方法
		 * 找不到直接放行
		 */
		
		ActionConfig config = configs.get(uri);
		if (config != null) {
			try {
				// 拿到对应的 class 类
				Class<?> clazz = Class.forName(config.getPath());
				// 创建对应的对象  每次创建 影响性能
				Object object = clazz.newInstance();
				//设置对应的参数
				Map<String, String[]> paraMap = request.getParameterMap();
				BeanUtils.copyProperties(object, paraMap); //利用org.apache.commons.beanutils.BeanUtils;
				// 拿到对应的方法
				Method method = clazz.getMethod(config.getMethod());
				// 执行方法
				Object obj = method.invoke(object);
				String result = null;
				try {
					result = (String)obj;
				} catch (Exception e) {
					// TODO: handle exception
				}
				if (result != null) {
				    ResultView resultView = config.getResult(result);
				    if (resultView != null) {
				        String resultUrl = resultView.getPath();
				        String resultType = resultView.getType();
				    	
				    	if (RESULT_DISPATCHER.equals(resultType)) {
				    		request.getRequestDispatcher(resultUrl).forward(request, response);
						} else if (RESULT_REDIRECT.equals(resultType)) {
							response.sendRedirect(resultUrl);
						}
					} else {
						throw new RuntimeException(RESULT_ERROR_INFO);
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

}
