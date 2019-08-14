package org.isd.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拦截器 —— 处理跨域问题
 */
public class SimpleCORSFilter implements Filter {

    /**
     *
     *
     *@参数 [req, res, chain]
     *@返回值 void
     */
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;

        response.setHeader("Access-Control-Allow-Origin", "*");//"http://127.0.0.1:1841"
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "content-type, x-requested-with");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        chain.doFilter(req, res);

    }

    /**
     *实现方法
     *
     *@参数 [filterConfig]
     *@返回值 void
     */
    public void init(FilterConfig filterConfig) {}

    /**
     *实现方法
     *
     *@参数 []
     *@返回值 void
     */
    public void destroy() {}

}
