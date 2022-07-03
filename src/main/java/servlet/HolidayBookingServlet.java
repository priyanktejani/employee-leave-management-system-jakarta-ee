package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.HolidayBookingDTO;
import model.LeaveApplication;
import model.User;


/**
 * Servlet implementation class HolidayBookingServlet
 */
@WebServlet("/HolidayBookingServlet")
public class HolidayBookingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	@EJB
	private HolidayBookingDTO hbDTO;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HolidayBookingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String param_action = request.getParameter("action");
		String tableStr = new String();
		
		switch(param_action) {
		case "login":
		{
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			User user = hbDTO.validate(username, password);
			List<LeaveApplication> requests = null;
			
			HttpSession session = request.getSession();
			
			if (user != null) {
//				if user is admin than get all employees requests
				if (user.getIsAdmin() == 1) {
					requests = hbDTO.employeesRequests();
//				else get particular employee request
				} else {
					requests = hbDTO.employeeRequests(username);	
				}


//					get user
					session.setAttribute("user", user);
					session.setAttribute("requests", requests);
					
					RequestDispatcher dispatcher = request.getRequestDispatcher("dashboard.jsp");
					dispatcher.forward(request, response);
					
				
			} else {
				
				String massage = "Account Not Found";
				session.setAttribute("massage", massage);
				RequestDispatcher dispatcher = request.getRequestDispatcher("massage.jsp");
				dispatcher.forward(request, response);
			}
		
			
		}
		break;
		default:
		break;
		}
		
		response.setContentType("text/html;charset=UTF-8");
		
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head>");
		out.println("<title> Book Store App </title>");
		out.println("</head>");
		
		out.println("<body>");
		out.println(tableStr);
		out.println("</body>");
		out.println("</html>");
		out.close();
		
		
		
	}

}
