package fancyRestaurant.fancyRestaurant.business;

import fancyRestaurant.fancyRestaurant.dao.FoodDAO;
import fancyRestaurant.fancyRestaurant.dao.OrdersDAO;
import fancyRestaurant.fancyRestaurant.dao.ShoppingCartDAO;
import fancyRestaurant.fancyRestaurant.dao.UserDAO;
import fancyRestaurant.fancyRestaurant.model.*;
import fancyRestaurant.fancyRestaurant.model.builder.OrderBuilder;
import fancyRestaurant.fancyRestaurant.model.decorator.FreeFriesCartDecorator;
import fancyRestaurant.fancyRestaurant.model.decorator.FreeBeerCartDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("cart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartDAO shoppingCartDao;

    @Autowired
    private FoodDAO foodDao;

    @Autowired
    private OrdersDAO ordersDao;

    @Autowired
    private UserDAO userDao;

    @RequestMapping(value = "list/{userId}", method = RequestMethod.GET)
    public String listItems(Model model, @PathVariable int userId){

        List<ShoppingCart> shoppingCarts = shoppingCartDao.findByUserId(userId);
        String deals = "No deals";

        if(shoppingCarts.size() > 0){
            deals = shoppingCarts.get(0).getDeals();
        }

        model.addAttribute("title", "Items");
        model.addAttribute("userId", userId);
        model.addAttribute("shoppingCart", shoppingCarts);
        model.addAttribute("deals", deals);

        return "cart/list";
    }

    @RequestMapping(value = "list/{userId}", method = RequestMethod.POST)
    public String processListItems(Model model, @RequestParam String deal, @PathVariable int userId){

        List<ShoppingCart> shoppingCarts = shoppingCartDao.findByUserId(userId);
        Cart cart = null;
        String deals = "No deals";

        for(ShoppingCart shoppingCart : shoppingCarts){

            if(deal.equals("beer")){
                cart = new FreeBeerCartDecorator(shoppingCart);
            }
            else{
                cart = new FreeFriesCartDecorator(shoppingCart);
            }

            cart.setDeals("");
            deals = cart.getDeals();

            shoppingCart.setDeals(cart.getDeals());

            shoppingCartDao.save(shoppingCart);
        }

        model.addAttribute("title", "Items");
        model.addAttribute("userId", userId);
        model.addAttribute("shoppingCart", shoppingCarts);
        model.addAttribute("deals", deals);

        return "cart/list";
    }

    @RequestMapping(value = "add/{userId}", method  = RequestMethod.GET)
    public String displayAddOrder(Model model, @PathVariable int userId){

        model.addAttribute("title", "Shopping Cart");
        model.addAttribute("userId", userId);
        model.addAttribute("shoppingCart", shoppingCartDao.findByUserId(userId));

        return "cart/add";
    }

    @RequestMapping(value = "add/{userId}", method = RequestMethod.POST)
    public String processAddOrder(@RequestParam int[] shoppingCartIds, @PathVariable int userId, Model model){

        User user = null;

        Optional<User> optionalUser = userDao.findById(userId);
        if(optionalUser.isPresent()){

            user = optionalUser.get();
        }

        for(int shoppingCartId : shoppingCartIds){

            Food food = null;
            ShoppingCart shoppingCart = null;
            Optional<ShoppingCart> optionalShoppingCart = shoppingCartDao.findById(shoppingCartId);
            if(optionalShoppingCart.isPresent()){

                shoppingCart = optionalShoppingCart.get();
                food = shoppingCart.getFood();
            }

            Orders order = new OrderBuilder()
                    .setUsername(user.getUsername())
                    .setFoodName(food.getName())
                    .setUser(user)
                    .setFood(food)
                    .setStatus("Pending")
                    .setFeedback("No Feedback")
                    .build();

            order.registerObserver(user);
            ordersDao.save(order);
            shoppingCartDao.delete(shoppingCart);
        }

        model.addAttribute("title", "Order placed successfully!");
        model.addAttribute("messages", "Your order has been placed successfully!");
        model.addAttribute("userId", userId);

        return "message";
    }

    @RequestMapping(value = "remove/{userId}", method  = RequestMethod.GET)
    public String displayRemoveItems(Model model, @PathVariable int userId){

        model.addAttribute("title", "Shopping Cart");
        model.addAttribute("userId", userId);
        model.addAttribute("shoppingCart", shoppingCartDao.findByUserId(userId));

        return "cart/remove";
    }

    @RequestMapping(value = "remove/{userId}", method = RequestMethod.POST)
    public String processRemoveItems(@RequestParam int[] shoppingCartIds, @PathVariable int userId, Model model){

        for(int shoppingCartId : shoppingCartIds){

            ShoppingCart shoppingCart = null;
            Optional<ShoppingCart> optionalShoppingCart = shoppingCartDao.findById(shoppingCartId);
            if(optionalShoppingCart.isPresent()){

                shoppingCart = optionalShoppingCart.get();
            }

            shoppingCartDao.delete(shoppingCart);
        }

        model.addAttribute("title", "Items successfully removed");
        model.addAttribute("messages", "The selected items have been successfully removed!");
        model.addAttribute("userId", userId);

        return "message";
    }
}
