package fr.ropiteaux.rom.core.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.eclipse.jetty.server.Request;

import com.google.inject.Inject;

import fr.ropiteaux.rom.core.RomConst;

public class RomSecurityFilter implements Filter {

	private final static String USER_URI_PATH = "/api/user";

	RomSecurity security;

	@Inject
	public RomSecurityFilter(RomSecurity security) {
		this.security = security;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {

		String requestURI = ((Request) request).getRequestURI();

		if (requestURI.startsWith(USER_URI_PATH)) { // User request : must be
													// authenticated
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			String requestToken = httpRequest.getHeader("Auth");

			if (requestToken == null || requestToken.equals("")
					|| !requestToken.contains(RomConst.REQUEST_TOKEN_SEPARATOR)) {
				send401Error((HttpServletResponse) response,
						"Authentication Error");
			} else {
				String[] requestAuthValues = requestToken
						.split(RomConst.REQUEST_TOKEN_SEPARATOR);

				if (requestAuthValues.length != 2)
					send401Error((HttpServletResponse) response,
							"Authentication Error");

				try {
					security.validateAuthentication(requestAuthValues[0],
							requestAuthValues[1]);
					filterChain.doFilter(request, response); // validation ok
																// continue
																// treatment
				} catch (Exception e) {
					if (e instanceof RomSecurityException) {
						switch (((RomSecurityException) e).getErrorCode()) {
						case RomSecurityException.USER_TOKEN_EXPIRED:
							send401Error((HttpServletResponse) response,
									"Authentication Expired");
							break;
						case RomSecurityException.UNKNWON_USER:
						case RomSecurityException.INVALID_USER_TOKEN:
						default:
							send401Error((HttpServletResponse) response,
									"Authentication Error");
						}
					} else
						send401Error((HttpServletResponse) response,
								"Authentication Error");
				}
			}
		} else { // Other request do not need authentication
			filterChain.doFilter(request, response);
		}
	}

	private void send401Error(HttpServletResponse response, String message)
			throws IOException {
		response.setStatus(401);
		response.setCharacterEncoding("UTF-8");
		response.setContentType(MediaType.APPLICATION_JSON);
		response.getWriter().print(message);
	}

	@Override
	public void destroy() {
	}
}