package com.mydrawer.filter;

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
* Servlet Filter implementation class CORSFilter
*/

@WebFilter(asyncSupported = true, urlPatterns = { "/*" })
public class CORSFilter implements Filter {

	public CORSFilter() {}

	public void destroy() {}

	/*
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
		throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;

		System.out.println("CORSFilter HTTP Request: " + request.getMethod());

		// Authorize (allow) all domains to consume the content
		((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Origin", "*");
		((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Headers", "Content-type");
		((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Headers", "x-fhir-starter");
		((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Headers", "Origin");
		((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Headers", "Accept");
		((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Headers", "X-Requested-With");
		((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS, HEAD");

		// config.addExposedHeader("Location");
		// config.addExposedHeader("Content-Location");

		HttpServletResponse resp = (HttpServletResponse) servletResponse;

		/*
		 * For HTTP OPTIONS verb/method reply with ACCEPTED status code --
		 * per CORS handshake
		 */
		if(request.getMethod().equals("OPTIONS")) {
			resp.setStatus(HttpServletResponse.SC_ACCEPTED);
			return;
		}

		// pass the request along the filter chain
		chain.doFilter(request, servletResponse);
	}

	/*
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// Init method stub
	}
}
