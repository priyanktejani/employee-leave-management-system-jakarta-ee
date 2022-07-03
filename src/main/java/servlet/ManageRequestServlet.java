package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
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
import model.Department;
import model.DepartmentCategory;
import model.EmployeeEntitlement;
import model.LeaveApplication;
import model.Role;
import model.RoleCategory;
import model.User;

/**
 * Servlet implementation class ManageRequestServlet
 */
@WebServlet("/ManageRequestServlet")
public class ManageRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private HolidayBookingDTO hbDTO;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManageRequestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String param_action = request.getParameter("action");
		String tableStr = new String();
		
		switch(param_action) {
		case "requests":
		{
			
			String username = request.getParameter("username");
			String requestType = request.getParameter("requestType");
			User user = hbDTO.getEmployee(username);
			
			
			List<LeaveApplication> requests = null;
			List<User> allEmployees = null;
			
//			if user is admin than get all employees requests
			if (user.getIsAdmin() == 1) {
//				check for request type
				if (requestType.equals("approvedRequests")) {
					
					requests = hbDTO.employeesApprovedRequests();
					
				} else if (requestType.equals("rejectedRequests")) {
					
					requests = hbDTO.employeesRejectedRequests();
					
				} else {
					requests = hbDTO.employeesRequests();
				}
				allEmployees = hbDTO.allEmployees();
				
//			else get particular employee request
			} else {
//				check for request type
				if (requestType.equals("approvedRequests")) {
					
					requests = hbDTO.approvedEmployeeRequests(username);
					
				} else if (requestType.equals("rejectedRequests")) {
					
					requests = hbDTO.rejectedEmployeeRequests(username);
					
				} else {
					requests = hbDTO.employeeRequests(username);	
				}
			}
			
			HttpSession session = request.getSession();
			session.setAttribute("requests", requests);
			session.setAttribute("allEmployees", allEmployees);
			
//			pass request type to JSP in order to Change selected tab behaviour
			session.setAttribute("requestType", requestType);
			session.setAttribute("user", user);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("requests.jsp");
			dispatcher.forward(request, response);
			
		}
		break;
		case "requestDetails":
		{
			String username = request.getParameter("username");
			int requestId = Integer. parseInt(request.getParameter("requestId"));
			User employee = hbDTO.getEmployee(username);

//			get employee department
			Department employeeDepartment =  hbDTO.getEmployeDepartment(username);
			DepartmentCategory employeeDepartmentCategory = employeeDepartment.getDepartmentCategory();
			
//			get employee role_category
			Role employeeRole = hbDTO.getEmployeRole(username);
			RoleCategory employeeRoleCategory = employeeRole.getRoleCategory();
			
//			request details
			LeaveApplication employeeRequestDetails = hbDTO.employeeRequestDetails(requestId);
			
//			request details
			EmployeeEntitlement eployeeEntitlementDetails = hbDTO.eployeeEntitlement(username);
			
			
			HttpSession session = request.getSession();
			session.setAttribute("employee", employee);
			session.setAttribute("employeeDepartmentCategory", employeeDepartmentCategory);
			session.setAttribute("employeeRoleCategory", employeeRoleCategory);
			session.setAttribute("employeeRequestDetails", employeeRequestDetails);
			session.setAttribute("eployeeEntitlementDetails", eployeeEntitlementDetails);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("requestDetails.jsp");
			dispatcher.forward(request, response);
			
		}
		break;
		case "approvedRequest":
		{
			
			String username = request.getParameter("username");
			User employee = hbDTO.getEmployee(username);
			
			List<LeaveApplication> approvedEmployeeRequests = hbDTO.approvedEmployeeRequests(username);			
			HttpSession session = request.getSession();
			
			session.setAttribute("employee", employee);
			session.setAttribute("approvedEmployeeRequests", approvedEmployeeRequests);

			RequestDispatcher dispatcher = request.getRequestDispatcher("approvedRequests.jsp");
			dispatcher.forward(request, response);
			
		}
		break;
		case "approveRequest":
		{
			String username = request.getParameter("username");
			int approveId = Integer.parseInt(request.getParameter("approveId"));
			
			hbDTO.approveEmployeeRequest(username, approveId);
			
			String massage = "Request Approved";
			HttpSession session = request.getSession();
			session.setAttribute("massage", massage);
			

			RequestDispatcher dispatcher = request.getRequestDispatcher("massage.jsp");
			dispatcher.forward(request, response);
			
		}
		break;
		case "rejectRequest":
		{
			
//			String username = request.getParameter("username");
			int rejectId = Integer.parseInt(request.getParameter("rejectId"));

			hbDTO.rejectEmployeeRequest(rejectId);
			
			String massage = "Request Rejected";
			HttpSession session = request.getSession();
			session.setAttribute("massage", massage);
			

			RequestDispatcher dispatcher = request.getRequestDispatcher("massage.jsp");
			dispatcher.forward(request, response);
			
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String param_action = request.getParameter("action");
		String tableStr = new String();
		
		
		switch(param_action) {
		case "filterRequests":
		{
			
			String username = request.getParameter("username");
			User user = hbDTO.getEmployee(username);
			String employee = request.getParameter("filter_request");
			
			
			List<LeaveApplication> requests = null;
			List<User> allEmployees = hbDTO.allEmployees();

			if (employee.equals("allEmployee")) {
				requests = hbDTO.employeesRequests();
				
			} else {
				requests = hbDTO.employeeRequests(employee);	
			}
			
			HttpSession session = request.getSession();
			session.setAttribute("requests", requests);
			session.setAttribute("allEmployees", allEmployees);
			session.setAttribute("user", user);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("requests.jsp");
			dispatcher.forward(request, response);
			
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
