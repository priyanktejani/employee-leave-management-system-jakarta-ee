package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the request_log database table.
 * 
 */
@Entity
@Table(name="request_log")
@NamedQuery(name="RequestLog.findAll", query="SELECT r FROM RequestLog r")
public class RequestLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="employee_name")
	private String employeeName;

	private String log;

	public RequestLog() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmployeeName() {
		return this.employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getLog() {
		return this.log;
	}

	public void setLog(String log) {
		this.log = log;
	}

}