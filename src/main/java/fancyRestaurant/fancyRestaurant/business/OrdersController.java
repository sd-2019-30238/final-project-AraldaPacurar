package fancyRestaurant.fancyRestaurant.business;

import fancyRestaurant.fancyRestaurant.dao.*;
import fancyRestaurant.fancyRestaurant.model.*;
import fancyRestaurant.fancyRestaurant.model.builder.NotificationBuilder;
import fancyRestaurant.fancyRestaurant.model.builder.ShoppingCartBuilder;
import fancyRestaurant.fancyRestaurant.model.forms.FeedbackForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("order")
public class OrdersController {

    @Autowired
    private OrdersDAO ordersDao;

    @Autowired
    private UserDAO userDao;

    @Autowired
    private FoodDAO foodDao;

    @Autowired
    private RightsDAO rightsDao;

    @Autowired
    private NotificationDAO notificationDao;

    @Autowired
    private ShoppingCartDAO shoppingCartDao;

    @RequestMapping(value = "list/{userId}", method =  RequestMethod.GET)
    public String listOrders(Model model, @PathVariable int userId){

        Role myRole = null;
        User user = null;
        Optional<User> optionalUser = userDao.findById(userId);
        if(optionalUser.isPresent()){

            user = optionalUser.get();
            myRole = user.getRole();
        }

        List<Rights> rights = rightsDao.findByMyRight("view orders");
        Rights requiredRight = rights.get(0);

        if(!myRole.getRights().contains(requiredRight)){

            model.addAttribute("title", "Invalid request!");
            model.addAttribute("messages", "You do not have privileges to perform this action!");
            model.addAttribute("userId", userId);

            return "message";
        }

        model.addAttribute("title", "Pending Orders");
        model.addAttribute("orders", ordersDao.findAll());
        model.addAttribute("userId", userId);

        return "order/list";
    }

    @RequestMapping(value = "history/{userId}", method = RequestMethod.GET)
    public String listMyOrders(Model model, @PathVariable int userId){

        model.addAttribute("title", "Order History");
        model.addAttribute("orders", ordersDao.findByUserId(userId));
        model.addAttribute("userId", userId);
        model.addAttribute(new FeedbackForm());

        return "order/history";
    }

    @RequestMapping(value = "history/{userId}", method = RequestMethod.POST)
    public String processListMyOrders(@RequestParam int orderId, @PathVariable int userId, @ModelAttribute @Valid FeedbackForm feedback, Model model){

        Optional<Orders> optionalOrder = ordersDao.findById(orderId);
        Orders order = null;
        if(optionalOrder.isPresent()){
            order = optionalOrder.get();
        }

        if(!order.getStatus().equals("Payed")){
            model.addAttribute("title", "Error");
            model.addAttribute("messages", "Feedback can be submitted only for payed items.");
            model.addAttribute("userId", userId);

            return "message";
        }

        order.setFeedback(feedback.getFeedback());
        ordersDao.save(order);

        return "redirect:/order/history/" + userId;
    }

    @RequestMapping(value = "add/{userId}", method  = RequestMethod.GET)
    public String displayAddOrder(Model model, @PathVariable int userId){

        Role myRole = null;
        User user = null;
        Optional<User> optionalUser = userDao.findById(userId);
        if(optionalUser.isPresent()){

            user = optionalUser.get();
            myRole = user.getRole();
        }

        List<Rights> rights = rightsDao.findByMyRight("order");
        Rights requiredRight = rights.get(0);

        if(!myRole.getRights().contains(requiredRight)){

            model.addAttribute("title", "Invalid request!");
            model.addAttribute("messages", "You do not have privileges to perform this action!");
            model.addAttribute("userId", userId);

            return "message";
        }

        model.addAttribute("title", "Order Food");
        model.addAttribute("foods", foodDao.findAll());
        model.addAttribute("userId", userId);

        return "order/add";
    }

    @RequestMapping(value = "add/{userId}", method = RequestMethod.POST)
    public String processAddOrder(@RequestParam int[] foodIds, @PathVariable int userId, Model model){

        User user = null;

        for(int foodId : foodIds){

            Food food = null;
            Optional<Food> optionalFood = foodDao.findById(foodId);
            if(optionalFood.isPresent()){

                food = optionalFood.get();
            }

            ShoppingCart shoppingCart = new ShoppingCartBuilder()
                    .setFood(food)
                    .setUserId(userId)
                    .setPrice(food.getPrice())
                    .setDeals("")
                    .build();

            shoppingCartDao.save(shoppingCart);
        }

        model.addAttribute("title", "Items Added To Shopping Cart");
        model.addAttribute("messages", "Your items have been added successfully to the shopping cart!");
        model.addAttribute("userId", userId);

        return "message";
    }

    @RequestMapping(value = "remove/{userId}", method = RequestMethod.GET)
    public String displayRemoveOrder(Model model, @PathVariable int userId){

        Role myRole = null;
        User user = null;
        Optional<User> optionalUser = userDao.findById(userId);
        if(optionalUser.isPresent()){

            user = optionalUser.get();
            myRole = user.getRole();
        }

        List<Rights> rights = rightsDao.findByMyRight("send order");
        Rights requiredRight = rights.get(0);

        if(!myRole.getRights().contains(requiredRight)){

            model.addAttribute("title", "Invalid request!");
            model.addAttribute("messages", "You do not have privileges to perform this action!");
            model.addAttribute("userId", userId);

            return "message";
        }

        List<Orders> allORders = new ArrayList<>();
        List<Orders> ordersToDisplay = new ArrayList<>();

        for(Orders order : ordersDao.findAll()){
            if(!order.getStatus().equals("Payed")){
                ordersToDisplay.add(order);
            }
        }

        model.addAttribute("title", "Process Orders");
        model.addAttribute("orders", ordersToDisplay);
        model.addAttribute("userId", userId);

        return "order/remove";
    }

    @RequestMapping(value = "remove/{userId}", method = RequestMethod.POST)
    public String processRemoveOrder(@RequestParam int[] orderIds, @PathVariable int userId){

        for(int orderId : orderIds){

            Optional<Orders> optionalOrder = ordersDao.findById(orderId);
            Orders order = null;
            if(optionalOrder.isPresent()){
                order = optionalOrder.get();
            }

            User user = null;
            Optional<User> optionalUser = userDao.findById(order.getUser().getId());
            if(optionalUser.isPresent()) {

                user = optionalUser.get();
            }

            String notificationMessage = "No notifications";

            if(order.getStatus().equals("Pending")){
                order.setStatus("Shipping");
                notificationMessage = "Order number " + order.getId() + " has been sent!";
            }
            else if(order.getStatus().equals("Shipping")){
                order.setStatus("Payed");
                notificationMessage = "Order number " + order.getId() + " has arrived!";
            }

            Notification notification = new NotificationBuilder()
                    .setUser(user)
                    .setNotification(notificationMessage)
                    .build();

            order.notifyObserver(user, notification);

            ordersDao.save(order);
            notificationDao.save(notification);
        }

        return "redirect:/order/list/" + userId;
    }
}

