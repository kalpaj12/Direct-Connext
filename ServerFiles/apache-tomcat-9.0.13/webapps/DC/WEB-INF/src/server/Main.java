
package server;

import java.io.PrintWriter;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Main")
public class Main extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws java.io.IOException {
		PrintWriter pw = response.getWriter();
		pw.write("Hi!");
		pw.close();
	} 
}