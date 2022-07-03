package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the leave_application database table.
 * 
 */
@Entity
@Table(name="leave_application")
@NamedQuery(name="LeaveApplication.findAll", query="SELECT l FROM LeaveApplication l")
public class LeaveApplication implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private byte approved;

	@Column(name="break_constraint")
	private String breakConstraint;

	@Temporal(TemporalType.DATE)
	@Column(name="from_date")
	private Date fromDate;

	private byte rejected;

	private String subject;

	@Temporal(TemporalType.DATE)
	@Column(name="to_date")
	private Date toDate;

	@Column(name="user_username")
	private String userUsername;

	public LeaveApplication() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getApproved() {
		return this.approved;
	}

	public void setApproved(byte approved) {
		this.approved = approved;
	}

	public String getBreakConstraint() {
		return this.breakConstraint;
	}

	public void setBreakConstraint(String breakConstraint) {
		this.breakConstraint = breakConstraint;
	}

	public Date getFromDate() {
		return this.fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public byte getRejected() {
		return this.rejected;
	}

	public void setRejected(byte rejected) {
		this.rejected = rejected;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getToDate() {
		return this.toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getUserUsername() {
		return this.userUsername;
	}

	public void setUserUsername(String userUsername) {
		this.userUsername = userUsername;
	}

}