package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the employee_entitlement database table.
 * 
 */
@Entity
@Table(name="employee_entitlement")
@NamedQuery(name="EmployeeEntitlement.findAll", query="SELECT e FROM EmployeeEntitlement e")
public class EmployeeEntitlement implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="leave_balance")
	private int leaveBalance;

	private byte status;

	@Column(name="user_username")
	private String userUsername;

	public EmployeeEntitlement() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLeaveBalance() {
		return this.leaveBalance;
	}

	public void setLeaveBalance(int leaveBalance) {
		this.leaveBalance = leaveBalance;
	}

	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public String getUserUsername() {
		return this.userUsername;
	}

	public void setUserUsername(String userUsername) {
		this.userUsername = userUsername;
	}

}