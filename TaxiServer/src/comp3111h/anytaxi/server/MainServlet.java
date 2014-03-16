package comp3111h.anytaxi.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class MainServlet extends HttpServlet {
	public void doGet(HttpServletRequest req,
					  HttpServletResponse resp)
		throws IOException {	

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String navBar;
		
		if (user != null) {
			navBar = "Welcome, " + user.getNickname() + "! You can <a href=\"" +
					 userService.createLogoutURL("/") +
					 "\">sign out</a>.";
		} else {
			navBar = "Welcome! <a href=\"" + userService.createLoginURL("/") +
					"\">Sign in or register</a> to customize.";
		}
		
		
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		out.println(navBar);
		
		
	}
	
	public void doPost(HttpServletRequest req,
					   HttpServletResponse resp)
		throws IOException {
		
		doGet(req, resp);
	}
}