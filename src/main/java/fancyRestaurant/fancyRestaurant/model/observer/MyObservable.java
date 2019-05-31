package fancyRestaurant.fancyRestaurant.model.observer;

import fancyRestaurant.fancyRestaurant.model.Notification;
import fancyRestaurant.fancyRestaurant.model.User;

import java.util.ArrayList;
import java.util.List;

public class MyObservable {

    public List<MyObserver> observers = new ArrayList<MyObserver>();

    public void registerObserver(MyObserver o){

        observers.add(o);
    }

    public void unregisterObserver(MyObserver o){

        observers.remove(o);
    }

    public void notifyObserver(User user, Notification notification){

        user.update(notification);
    }
}
