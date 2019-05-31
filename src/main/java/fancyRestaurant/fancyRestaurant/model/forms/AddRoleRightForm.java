package fancyRestaurant.fancyRestaurant.model.forms;

import fancyRestaurant.fancyRestaurant.model.Rights;
import fancyRestaurant.fancyRestaurant.model.Role;

import javax.validation.constraints.NotNull;

public class AddRoleRightForm {

    @NotNull
    private int roleId;

    @NotNull
    private int rightId;

    private Iterable<Rights> rights;

    private Role role;

    public AddRoleRightForm(){

    }

    public AddRoleRightForm(Iterable<Rights> rights, Role role){

        this.rights = rights;
        this.role = role;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getRightId() {
        return rightId;
    }

    public void setRightId(int rightId) {
        this.rightId = rightId;
    }

    public Iterable<Rights> getRights() {
        return rights;
    }

    public Role getRole() {
        return role;
    }
}