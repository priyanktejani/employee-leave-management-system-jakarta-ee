package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the department_category database table.
 * 
 */
@Entity
@Table(name="department_category")
@NamedQuery(name="DepartmentCategory.findAll", query="SELECT d FROM DepartmentCategory d")
public class DepartmentCategory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="department_name")
	private String departmentName;

	//bi-directional many-to-one association to Department
	@OneToMany(mappedBy="departmentCategory")
	private List<Department> departments;

	public DepartmentCategory() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDepartmentName() {
		return this.departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public List<Department> getDepartments() {
		return this.departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	public Department addDepartment(Department department) {
		getDepartments().add(department);
		department.setDepartmentCategory(this);

		return department;
	}

	public Department removeDepartment(Department department) {
		getDepartments().remove(department);
		department.setDepartmentCategory(null);

		return department;
	}

}