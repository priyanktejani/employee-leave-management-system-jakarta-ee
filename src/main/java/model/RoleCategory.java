package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the role_category database table.
 * 
 */
@Entity
@Table(name="role_category")
@NamedQuery(name="RoleCategory.findAll", query="SELECT r FROM RoleCategory r")
public class RoleCategory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="role_name")
	private String roleName;

	//bi-directional many-to-one association to Role
	@OneToMany(mappedBy="roleCategory")
	private List<Role> roles;

	public RoleCategory() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public Role addRole(Role role) {
		getRoles().add(role);
		role.setRoleCategory(this);

		return role;
	}

	public Role removeRole(Role role) {
		getRoles().remove(role);
		role.setRoleCategory(null);

		return role;
	}

}