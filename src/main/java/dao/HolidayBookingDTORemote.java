package dao;

import java.sql.Date;

import javax.ejb.Remote;


@Remote
public interface HolidayBookingDTORemote {
	public String validateREST(String username, String password);
	public String allDepartmentCategoryREST();
	public String allRoleCategoryREST();
	public String insertEmployeeLeaveApplicationREST(String userUsername, Date fromDate, Date toDate, String subject);
}
