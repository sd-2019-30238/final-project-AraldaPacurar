package fancyRestaurant.fancyRestaurant.model;

import fancyRestaurant.fancyRestaurant.model.observer.MyObserver;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User implements MyObserver{

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min = 3, max = 15)
    private String username;

    @NotNull
    private String password;

    @NotNull
    @Size(min = 1, max = 40)
    private String email;

    @NotNull
    @Size(min = 1)
    private String address;

    @ManyToOne
    private Role role;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id")
    private List<Orders> orders = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id")
    private List<Notification> notifications = new ArrayList<>();

    public User(){

    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public void update(Notification notification) {

        notifications.add(notification);
    }
}
