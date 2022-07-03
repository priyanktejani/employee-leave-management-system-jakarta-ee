package servlet;

import java.io.IOException;
import java.sql.Date;
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
import model.DepartmentCategory;
import model.RoleCategory;

/**
 * Servlet implementation class AddEmployeeServlet
 */
@WebServlet("/AddEmployeeServlet")
public class AddEmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private HolidayBookingDTO hbDTO;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddEmployeeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
//		get list of department and role
		List<DepartmentCategory> listDepartmentCategory = hbDTO.allDepartmentCategory();
		List<RoleCategory> listRoleCategory = hbDTO.allRolesCategory();
		
//		get current date
		long millis = System.currentTimeMillis();  
	    Date date = new Date(millis);
		
		HttpSession session = request.getSession();
		session.setAttribute("listDepartmentCategory", listDepartmentCategory);
		session.setAttribute("listRoleCategory", listRoleCategory);
		session.setAttribute("date", date);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("addEmployee.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		// TODO Auto-generated method stub
		String username = request.getParameter("username");
		String firstName = request.getParameter("first_name");
		String lastName = request.getParameter("last_name");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		Date joinedDate = Date.valueOf(request.getParameter("joinedDate"));
		
		int departmentCategoryId = Integer.parseInt(request.getParameter("department_category"));
		int roleCategoryId = Integer.parseInt(request.getParameter("role_category"));
		int leaveBalance = 30;
		
//		insert employee general details
		hbDTO.insertEmployee(username, firstName, lastName, email, password, joinedDate);
		
//		insert employee department & role details
		hbDTO.insertEmployeeDepartment(departmentCategoryId, username);
		hbDTO.insertEmployeeRole(roleCategoryId, username);
		
//		employee entitlement details
		hbDTO.insertEmployeeEntitlementDetails(username, leaveBalance);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("dashboard.jsp");
		dispatcher.forward(request, response);

		
	}

}
