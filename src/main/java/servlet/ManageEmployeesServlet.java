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
import model.Department;
import model.DepartmentCategory;
import model.Role;
import model.RoleCategory;
import model.User;

/**
 * Servlet implementation class ManageEmployeesServlet
 */
@WebServlet("/ManageEmployeesServlet")
public class ManageEmployeesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private HolidayBookingDTO hbDTO;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManageEmployeesServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		String param_action = request.getParameter("action");
		String tableStr = new String();
		
		switch(param_action) {
		case "allEmployees":
		{
			List<User> allEmployees = hbDTO.allEmployees();
			
			HttpSession session = request.getSession();
			session.setAttribute("allEmployees", allEmployees);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("employees.jsp");
			dispatcher.forward(request, response);
		}
		break;
		case "editProfile":
		{
			String username = request.getParameter("user");
			User employee = hbDTO.getEmployee(username);
			
//			get list of department and role
			List<DepartmentCategory> listDepartmentCategory = hbDTO.allDepartmentCategory();
			List<RoleCategory> listRoleCategory = hbDTO.allRolesCategory();
			
//			get employee department
			Department employeeDepartment =  hbDTO.getEmployeDepartment(username);
			DepartmentCategory employeeDepartmentCategory = employeeDepartment.getDepartmentCategory();
			
//			get employee role_category
			Role employeeRole = hbDTO.getEmployeRole(username);
			RoleCategory employeeRoleCategory = employeeRole.getRoleCategory();
			
			listDepartmentCategory.remove(employeeDepartmentCategory);
			listRoleCategory.remove(employeeRoleCategory);
			
			HttpSession session = request.getSession();
			session.setAttribute("employee", employee);
			session.setAttribute("listDepartmentCategory", listDepartmentCategory);
			session.setAttribute("listRoleCategory", listRoleCategory);
			session.setAttribute("employeeDepartmentCategory", employeeDepartmentCategory);
			session.setAttribute("employeeRoleCategory", employeeRoleCategory);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("profile.jsp");
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
		case "saveProfile":
		{

			String username = request.getParameter("user");
			
			User employee = hbDTO.getEmployee(username);
			int userId = employee.getId();
			
//			tableStr += "<br/><strong>"+username +"</strong>";
//			tableStr += "<br/><strong>"+employee +"</strong>";
			
			
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			int departmentCategoryId = Integer.parseInt(request.getParameter("department_category"));
			int roleCategoryId = Integer.parseInt(request.getParameter("role_category"));
			
			
//			update employee details	
			hbDTO.updateEmployee(userId, firstName, lastName, email, password);
			hbDTO.updateEmployeeDepartment(departmentCategoryId, username);
			hbDTO.updateEmployeeRole(roleCategoryId, username);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("employees.jsp");
			dispatcher.forward(request, response);
		
				

		}
		break;
		case "removeProfile":
		{

			String username = request.getParameter("user");
			User employee = hbDTO.getEmployee(username);
			int userId = employee.getId();
			String massage = "Employee Removed";
			
//			delete employee
//			hbDTO.deleteEmployee(userId);
			
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

}
