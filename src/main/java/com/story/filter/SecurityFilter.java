package com.story.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class SecurityFilter
*/

@WebFilter(asyncSupported = true, urlPatterns = { "/*" })

public class SecurityFilter implements Filter {

	public SecurityFilter() {}

	public void destroy() {}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
		throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;

		System.out.println("SecurityFilter HTTP Request: " + request.getMethod());

		HttpServletResponse resp = (HttpServletResponse) servletResponse;

		// pass the request along the filter chain
		chain.doFilter(request, servletResponse);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {

		// Init method stub
	}
}
