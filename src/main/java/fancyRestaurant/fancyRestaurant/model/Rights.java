package fancyRestaurant.fancyRestaurant.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


@Entity
public class Rights {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min = 2)
    private String myRight;

    @ManyToMany(mappedBy = "rights")
    private List<Role> roles;

    public int getId() {
        return id;
    }

    public String getMyRight() {
        return myRight;
    }

    public void setMyRight(String myRight) {
        this.myRight = myRight;
    }
}
