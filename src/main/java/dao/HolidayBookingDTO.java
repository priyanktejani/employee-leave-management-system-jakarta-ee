package dao;

import java.util.List;
import java.sql.Date;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import model.Department;
import model.DepartmentCategory;
import model.EmployeeEntitlement;
import model.LeaveApplication;
import model.RequestLog;
import model.Role;
import model.RoleCategory;
import model.User;

/**
 * Session Bean implementation class HolidayBookingDTO
 */
@Stateless
@LocalBean
@Remote(HolidayBookingDTORemote.class)
@Path("HolidayBookingREST")
public class HolidayBookingDTO implements HolidayBookingDTORemote {

	@PersistenceContext(unitName="HolidayBookingDAO")
	EntityManager em;
	
//	get current date
	long millis = System.currentTimeMillis();  
    Date date = new Date(millis); 

    /**
     * Default constructor. 
     */
    public HolidayBookingDTO() {
        // TODO Auto-generated constructor stub
    }
    
	public User validate(String username, String password) {
		User user = null;
		String getEmpQuery = "SELECT u FROM User u WHERE u.username = :username AND u.password = :password";
			
	//	if user found
		try {
			user = em.createQuery(getEmpQuery, User.class).setParameter("username", username)
			.setParameter("password", password).getSingleResult();	
			
		} catch (NoResultException e) {
	//		otherwise do nothing
		}
		
		return user;
		
	}
    
    public List<DepartmentCategory> allDepartmentCategory() {
    	List<DepartmentCategory> listDepartmentCategory = em.createNamedQuery("DepartmentCategory.findAll", DepartmentCategory.class).getResultList();
    	return listDepartmentCategory;
    }
    
    public List<RoleCategory> allRolesCategory() {
    	List<RoleCategory> listRoleCategory = em.createNamedQuery("RoleCategory.findAll", RoleCategory.class).getResultList();
    	return listRoleCategory;
    }
    
    public void insertEmployee(String username, String firstName, 
    		String lastName, String email, String password, Date joinedDate) {
		
		User employee = new User();
		employee.setUsername(username);
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		employee.setEmail(email);
		employee.setPassword(password);      
		employee.setDateJoined(joinedDate);
		em.persist(employee);
	}
    
    public void insertEmployeeDepartment(int departmentCategorytId, String userUsername) {
    	
    	DepartmentCategory departmentCategory = em.find(DepartmentCategory.class, departmentCategorytId);
		Department department = new Department();
		department.setDepartmentCategory(departmentCategory);
		department.setUserUsername(userUsername);
		department.setCreatedAt(date);
		em.persist(department);
	}
    
	public void insertEmployeeRole(int roleCategorytId, String userUsername) {
	    	
    	RoleCategory roleCategory = em.find(RoleCategory.class, roleCategorytId);
		Role role = new Role();
		role.setRoleCategory(roleCategory);
		role.setUserUsername(userUsername);
		em.persist(role);
	}
	
	public void insertEmployeeEntitlementDetails(String userUsername, int leaveBalance) {
		
//		set status default as 1(active)
		byte status = (byte) 1;
    	
    	EmployeeEntitlement employeeEntitlement = new EmployeeEntitlement();
    	employeeEntitlement.setUserUsername(userUsername);
    	employeeEntitlement.setLeaveBalance(leaveBalance);
    	employeeEntitlement.setStatus(status);
		em.persist(employeeEntitlement);
	}
	
	public List<User> allEmployees() {
		List<User> allEmployees = null;
		String getEmpQuery = "SELECT u FROM User u WHERE u.isAdmin = :isAdmin";
		
		byte isAdmin = (byte) 0;
		
		try {
			allEmployees = em.createQuery(getEmpQuery, User.class).setParameter("isAdmin", isAdmin).getResultList();	
    		
    	} catch (NoResultException e) {
//    		otherwise do nothing
    	}
		
    	return allEmployees;
	}
	
	public User getEmployee(String username) {
    	User user = null;
    	String getEmpQuery = "SELECT u FROM User u WHERE u.username = :username";
    	
//    	if user found
    	try {
    		user = em.createQuery(getEmpQuery, User.class).setParameter("username", username).getSingleResult();	
    		
    	} catch (NoResultException e) {
//    		otherwise do nothing
    	}
    	
    	return user;
    	
    }
	
	public Department getEmployeDepartment(String username) {
    	Department getEmployeDepartmet = null;
    	String getDepQuery = "Select d FROM Department d WHERE d.userUsername = :username";

//    	if Department found
    	try {
    		getEmployeDepartmet = em.createQuery(getDepQuery, Department.class).setParameter("username", username).getSingleResult();

    	} catch (NoResultException e) {
//    		otherwise do nothing
    	}
    	
    	return getEmployeDepartmet;
    	
    }
	
	public Role getEmployeRole(String username) {
    	Role getEmployeRole = null;
    	String getroleQuery = "Select r FROM Role r WHERE r.userUsername = :username";

//    	if Department found
    	try {
    		getEmployeRole = em.createQuery(getroleQuery, Role.class).setParameter("username", username).getSingleResult();

    	} catch (NoResultException e) {
//    		otherwise do nothing
    	}
    	
    	return getEmployeRole;
    	
    }
	
	public void updateEmployee(int UserId, String firstName, 
    		String lastName, String email, String password) {
		
		User employee = em.find(User.class, UserId);
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		employee.setEmail(email);
		employee.setPassword(password);      
		em.persist(employee);
	}
    
    public void updateEmployeeDepartment(int departmentCategorytId, String userUsername) {
    	Department department = null;
    	String getDepQuery = "Select d FROM Department d WHERE d.userUsername = :username";
    	
    	DepartmentCategory departmentCategory = em.find(DepartmentCategory.class, departmentCategorytId);
    	
//    	if Department found
    	try {
    		department = em.createQuery(getDepQuery, Department.class).setParameter("username", userUsername).getSingleResult();

    	} catch (NoResultException e) {
//    		otherwise do nothing
    	}
		department.setDepartmentCategory(departmentCategory);
		em.persist(department);
	}
    
	public void updateEmployeeRole(int roleCategorytId, String userUsername) {
		Role role = null;
		String getroleQuery = "Select r FROM Role r WHERE r.userUsername = :username";
	    	
    	RoleCategory roleCategory = em.find(RoleCategory.class, roleCategorytId);
//    	if Department found
    	try {
    		role = em.createQuery(getroleQuery, Role.class).setParameter("username", userUsername).getSingleResult();

    	} catch (NoResultException e) {
//    		otherwise do nothing
    	}
    	role.setRoleCategory(roleCategory);
		em.persist(role);
	}
	
	public void deleteEmployee(int userId) {
		
		
		User employee = null;
		String getroleQuery = "Select r FROM Role r WHERE r.userUsername = :username";
		String getDepQuery = "Select d FROM Department d WHERE d.userUsername = :username";
		String getLeaQuery = "Select l FROM LeaveApplication l WHERE l.userUsername = :username";
		String getEntQuery = "Select e FROM EmployeeEntitlement e WHERE e.userUsername = :username";
    	
//    	if user found
    	try {
    		employee = em.find(User.class, userId);
    		String userUsername = employee.getUsername();
    		Role role = em.createQuery(getroleQuery, Role.class).setParameter("username", userUsername).getSingleResult();
    		Department department = em.createQuery(getDepQuery, Department.class).setParameter("username", userUsername).getSingleResult();
    		LeaveApplication leaveApplication = em.createQuery(getLeaQuery, LeaveApplication.class).setParameter("username", userUsername).getSingleResult();
    		EmployeeEntitlement empEntitlement = em.createQuery(getEntQuery, EmployeeEntitlement.class).setParameter("username", userUsername).getSingleResult();
    		
    		
    		em.remove(role);
    		em.remove(department);
    		em.remove(leaveApplication);
    		em.remove(empEntitlement);
    	} catch (NoResultException e) {
//    		otherwise do nothing
    	}
    	

    	em.remove(employee);

	}
	
	public void insertEmployeeLeaveApplication(String userUsername, Date fromDate, Date toDate, String subject, String breakConstraint) {
		LeaveApplication leaveApplication = new LeaveApplication();
		leaveApplication.setUserUsername(userUsername);
		leaveApplication.setFromDate(fromDate);
		leaveApplication.setToDate(toDate);
		leaveApplication.setSubject(subject);
		leaveApplication.setBreakConstraint(breakConstraint);
		em.persist(leaveApplication);
	}
	
	
//  employees requests (single employees)
	public List<LeaveApplication> employeeRequests(String userUsername) {
		List<LeaveApplication> employeeRequests = null;
		String getReqQuery = "SELECT l FROM LeaveApplication l WHERE l.userUsername = :username AND l.approved = :approved AND l.rejected = :rejected";
		
		byte approved = (byte) 0;
		byte rejected = (byte) 0;
		
		try {
			employeeRequests = em.createQuery(getReqQuery, LeaveApplication.class)
					.setParameter("username", userUsername)
					.setParameter("approved", approved)
					.setParameter("rejected", rejected)
					.getResultList();	
    		
    	} catch (NoResultException e) {
//    		otherwise do nothing
    	}
		
    	return employeeRequests;
	}
	
	public List<LeaveApplication> approvedEmployeeRequests(String userUsername) {
		List<LeaveApplication> approvedEmployeeRequests = null;
		String getReqQuery = "SELECT l FROM LeaveApplication l WHERE l.userUsername = :username AND l.approved = :approved";
		
		byte approved = (byte) 1;
		
		try {
			approvedEmployeeRequests = em.createQuery(getReqQuery, LeaveApplication.class)
					.setParameter("username", userUsername).setParameter("approved", approved).getResultList();	
    		
    	} catch (NoResultException e) {
//    		otherwise do nothing
    	}
		
    	return approvedEmployeeRequests;
	}
	
	public List<LeaveApplication> rejectedEmployeeRequests(String userUsername) {
		List<LeaveApplication> rejectedEmployeeRequests = null;
		String getReqQuery = "SELECT l FROM LeaveApplication l WHERE l.userUsername = :username AND l.rejected = :rejected";
		
		byte rejected = (byte) 1;
		
		try {
			rejectedEmployeeRequests = em.createQuery(getReqQuery, LeaveApplication.class)
					.setParameter("username", userUsername).setParameter("rejected", rejected).getResultList();	
    		
    	} catch (NoResultException e) {
//    		otherwise do nothing
    	}
		
    	return rejectedEmployeeRequests;
	}
	
	public LeaveApplication employeeRequestDetails(int requestId) {
		LeaveApplication employeeRequestDetails = null;
		String getReqDetailsQuery = "SELECT l FROM LeaveApplication l WHERE l.id = :requestId";
		
		try {
			employeeRequestDetails = em.createQuery(getReqDetailsQuery, LeaveApplication.class).setParameter("requestId", requestId).getSingleResult();	
    		
    	} catch (NoResultException e) {
//    		otherwise do nothing
    	}
		
    	return employeeRequestDetails;
	}
	
	
//	employees requests (all employees)
	public List<LeaveApplication> employeesRequests() {
		List<LeaveApplication> employeesRequests = null;
		String getReqQuery = "SELECT l FROM LeaveApplication l WHERE l.approved = :approved AND l.rejected = :rejected";
		
		byte approved = (byte) 0;
		byte rejected = (byte) 0;
		
		try {
			employeesRequests = em.createQuery(getReqQuery, LeaveApplication.class)
					.setParameter("approved", approved)
					.setParameter("rejected", rejected)
					.getResultList();
    		
    	} catch (NoResultException e) {
//    		otherwise do nothing
    	}
		
    	return employeesRequests;
	}
	
	public List<LeaveApplication> employeesApprovedRequests() {
		List<LeaveApplication> employeesApprovedRequests = null;
		String getReqQuery = "SELECT l FROM LeaveApplication l WHERE l.approved = :approved";
		
		byte approved = (byte) 1;
		
		try {
			employeesApprovedRequests = em.createQuery(getReqQuery, LeaveApplication.class)
					.setParameter("approved", approved).getResultList();	
    		
    	} catch (NoResultException e) {
//    		otherwise do nothing
    	}
		
    	return employeesApprovedRequests;
	}
	
	public List<LeaveApplication> employeesRejectedRequests() {
		List<LeaveApplication> employeesRejectedRequests = null;
		String getReqQuery = "SELECT l FROM LeaveApplication l WHERE l.rejected = :rejected";
		
		byte rejected = (byte) 1;
		
		try {
			employeesRejectedRequests = em.createQuery(getReqQuery, LeaveApplication.class)
					.setParameter("rejected", rejected).getResultList();	
    		
    	} catch (NoResultException e) {
//    		otherwise do nothing
    	}
		
    	return employeesRejectedRequests;
	}
	
//	check for constrains break
	public Boolean isEntitled(String userUsername) {
		EmployeeEntitlement employeeEntitlement = null;
		String getEmpEntQuery = "SELECT e FROM EmployeeEntitlement e WHERE e.userUsername = :userUsername";
		
		try {
			employeeEntitlement = em.createQuery(getEmpEntQuery, EmployeeEntitlement.class)
					.setParameter("userUsername", userUsername)
					.getSingleResult();	
    		
    	} catch (NoResultException e) {
//    		otherwise do nothing
    	}
		
		int employeeLeaveBalance = employeeEntitlement.getLeaveBalance();
		if (employeeLeaveBalance > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public void approveEmployeeRequest(String userUsername, int approveId) {
		EmployeeEntitlement employeeEntitlement = null;
		String getEmpEntQuery = "SELECT e FROM EmployeeEntitlement e WHERE e.userUsername = :userUsername";
		
		try {
			employeeEntitlement = em.createQuery(getEmpEntQuery, EmployeeEntitlement.class)
					.setParameter("userUsername", userUsername)
					.getSingleResult();	
    		
    	} catch (NoResultException e) {
//    		otherwise do nothing
    	}
		
		int employeeLeaveBalance = employeeEntitlement.getLeaveBalance();
			employeeLeaveBalance -=1;
			
//			set updated leave balance
			employeeEntitlement.setLeaveBalance(employeeLeaveBalance);
			LeaveApplication approveEmployeeRequest = em.find(LeaveApplication.class, approveId);
			
//			set approved True
			byte approve = (byte) 1;
			approveEmployeeRequest.setApproved(approve);
			
//			set working status False
			byte active = (byte) 0;
			employeeEntitlement.setStatus(active);
			
			em.persist(approveEmployeeRequest);
			em.persist(employeeEntitlement);

	}
	
	public void rejectEmployeeRequest(int rejectId) {
		LeaveApplication rejectEmployeeRequest = em.find(LeaveApplication.class, rejectId);
		
//		set approved True
		byte reject = (byte) 1;
		rejectEmployeeRequest.setRejected(reject);

	}
	
//	check for constrains break
	public EmployeeEntitlement eployeeEntitlement(String userUsername) {
		EmployeeEntitlement eployeeEntitlement = null;
		String getEmpEntQuery = "SELECT e FROM EmployeeEntitlement e WHERE e.userUsername = :userUsername";
		
		try {
			eployeeEntitlement = em.createQuery(getEmpEntQuery, EmployeeEntitlement.class)
					.setParameter("userUsername", userUsername)
					.getSingleResult();	
    		
    	} catch (NoResultException e) {
//    		otherwise do nothing
    	}
		
		return eployeeEntitlement;
	}
	
//	check for constrains break
	public List<EmployeeEntitlement> allEmployeeEntitlement() {

		List<EmployeeEntitlement> allEmployeeEntitlement = em.createNamedQuery("EmployeeEntitlement.findAll", EmployeeEntitlement.class).getResultList();
    	return allEmployeeEntitlement;

	}

	@Override
	@GET
	@Path("/allDepartmentCategory")
	@Produces("text/plain")
	@Consumes("text/plain")
	public String allDepartmentCategoryREST() {
		List<DepartmentCategory> departmentCategories = allDepartmentCategory();
		String results;
		
		results = "<results>\n";
		for (DepartmentCategory departmentCategory: departmentCategories) {
			results += "<item>"+ departmentCategory.getDepartmentName() + "</item>\n";
		}
		results += "</results>";
    	return results;
	}
	
	@Override
	@GET
	@Path("/allRoleCategory")
	@Produces("text/plain")
	@Consumes("text/plain")
	public String allRoleCategoryREST() {
		List<RoleCategory> roleCategories = allRolesCategory();
		String results;
		
		results = "<results>\n";
		for (RoleCategory roleCategory: roleCategories) {
			results += "<item>"+ roleCategory.getRoleName() + "</item>\n";
		}
		results += "</results>";
    	return results;
	}

	
	@Override
	@GET
	@Path("/login/{username}/{password}")
	@Produces("text/plain")
	@Consumes("text/plain")
	public String validateREST(
			@PathParam("username") String username, 
			@PathParam("password") String password) {
		String result;
		result = "<result>\n";
		
		User user = validate(username, password);

		
		if (user != null) {
			if (user.getIsAdmin() == 1) {
				result += "<item>" + "user_type: "+ "Admin" + "</item>\n";
				result += "<item>" + "username: " + user.getUsername() + "</item>\n";
				result += "<item>" + "first_name: " + user.getFirstName() + "</item>\n";
				result += "<item>" + "last_name: " + user.getLastName() + "</item>\n";
				result += "<item>" + "email: " + user.getEmail() + "</item>\n";
			} else {
				
				
				Department department = getEmployeDepartment(username);
				Role role = getEmployeRole(username);
				DepartmentCategory departmentCategory = department.getDepartmentCategory();
				RoleCategory roleCategory = role.getRoleCategory();
				EmployeeEntitlement employeeEntitlement = eployeeEntitlement(username);
				
				result += "<item>" + "user_type: "+ "Employee" + "</item>\n";
				result += "<item>" + "username: " + user.getUsername() + "</item>\n";
				result += "<item>" + "first_name: " + user.getFirstName() + "</item>\n";
				result += "<item>" + "last_name: " + user.getLastName() + "</item>\n";
				result += "<item>" + "email: " + user.getEmail() + "</item>\n";
				result += "<item>" + "joined_date: " + user.getDateJoined() + "</item>\n";
				result += "<item>" + "department_name: " + departmentCategory.getDepartmentName() + "</item>\n";
				result += "<item>" + "role_name: " + roleCategory.getRoleName() + "</item>\n";
				result += "<item>" + "leave_balance: " + employeeEntitlement.getLeaveBalance() + "</item>\n";
				if (employeeEntitlement.getStatus() == 1) {
					result += "<item>" + "status: " + "true" + "</item>\n";
				} else {
					result += "<item>" + "status: " + "false" + "</item>\n";
				}
				
			}
		} else {
			result += "<item>" + "No Result"+ "</item>\n";
		}

		result += "</result>";
		
		return result;
	}

	@Override
	@GET
	@Path("/makerequest/{userUsername}/{fromDate}/{toDate}/{subject}")
	@Produces("text/plain")
	@Consumes("text/plain")
	public String insertEmployeeLeaveApplicationREST(
			@PathParam("userUsername") String userUsername, 
			@PathParam("fromDate") Date fromDate, 
			@PathParam("toDate") Date toDate, 
			@PathParam("subject") String subject) {
		
		String result;
		result = "<result>\n";
		try {
			insertEmployeeLeaveApplication(userUsername, fromDate, toDate, subject, null);
			RequestLog requestLog = new RequestLog();
			requestLog.setEmployeeName(userUsername);
			requestLog.setLog("request created from: " + fromDate + " to " + toDate + " for " + subject);
			em.persist(requestLog);
			result += "<item>" + "status: " + "request created" + "</item>\n";
    		
    	} catch (Exception e) {
//    		otherwise do nothing
    		result += "<item>" + "status: " + "request failed" + "</item>\n";
    	}
		result += "</result>";
		return result;
	}

}
