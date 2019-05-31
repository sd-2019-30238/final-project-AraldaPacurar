package fancyRestaurant.fancyRestaurant.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;


import javax.persistence.*;

@Entity
public class Notification {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private String notification;

    @ManyToOne(cascade = {CascadeType.ALL})
    private User user;

    @ManyToOne(cascade = {CascadeType.ALL})
    private Orders order;

    public Notification(){}

    public int getId() {
        return id;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }
}
