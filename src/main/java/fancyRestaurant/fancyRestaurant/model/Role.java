package fancyRestaurant.fancyRestaurant.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Role {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min = 2)
    private String role;

    @ManyToMany
    private List<Rights> rights;

    @OneToMany
    @JoinColumn(name = "role_id")
    private List<User> users = new ArrayList<>();

    public Role(int id, String role){
        this.id = id;
        this.role = role;
    }

    public Role(){

    }

    public void addRight(Rights right){

        rights.add(right);
    }

    public void removeRight(Rights right){

        rights.remove(right);
    }

    public int getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Rights> getRights() {
        return rights;
    }
}