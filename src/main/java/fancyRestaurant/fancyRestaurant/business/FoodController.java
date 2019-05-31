package fancyRestaurant.fancyRestaurant.business;

import fancyRestaurant.fancyRestaurant.business.factory.Discount;
import fancyRestaurant.fancyRestaurant.business.factory.DiscountCreator;
import fancyRestaurant.fancyRestaurant.dao.*;
import fancyRestaurant.fancyRestaurant.model.*;
import fancyRestaurant.fancyRestaurant.model.forms.FilterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("food")
public class FoodController {

    @Autowired
    private FoodDAO foodDao;

    @Autowired
    private RoleDAO roleDao;

    @Autowired
    private UserDAO userDao;

    @Autowired
    private RightsDAO rightsDao;

    @Autowired
    private FoodTypeDAO foodTypeDao;

    @RequestMapping(value = "list/{userId}", method = RequestMethod.GET)
    public String listFood(Model model, @PathVariable int userId){

        Role myRole = null;
        User user = null;
        Optional<User> optionalUser = userDao.findById(userId);
        if(optionalUser.isPresent()){

            user = optionalUser.get();
            myRole = user.getRole();
        }

        List<Rights> rights = rightsDao.findByMyRight("view food");
        Rights requiredRight = rights.get(0);

        if(!myRole.getRights().contains(requiredRight)){

            model.addAttribute("title", "Invalid request!");
            model.addAttribute("messages", "You do not have privileges to perform this action!");
            model.addAttribute("userId", userId);

            return "message";
        }

        model.addAttribute("title", "Available Food");
        model.addAttribute("userId", userId);
        model.addAttribute("foods", foodDao.findAll());
        model.addAttribute(new FilterForm());

        return "food/list";
    }

    @RequestMapping(value = "list/{userId}", method = RequestMethod.POST)
    public String processListFood(Model model, @PathVariable int userId, @ModelAttribute @Valid FilterForm filter){

        List<Food> filteredFood = new ArrayList<>();
        filteredFood = foodDao.findByName(filter.getFilter());

        if(filteredFood.isEmpty()){

            List<FoodType> foodType = foodTypeDao.findByName(filter.getFilter());
            if(!foodType.isEmpty()){

                filteredFood = foodDao.findByFoodTypeId(foodType.get(0).getId());
            }

            if(filteredFood.isEmpty()){

                try {
                    filteredFood = foodDao.findByPrice(Integer.parseInt(filter.getFilter()));
                }
                catch (NumberFormatException e){

                    model.addAttribute("title", "Available Food");
                    model.addAttribute("userId", userId);
                    model.addAttribute("foods", filteredFood);
                    model.addAttribute(new FilterForm());

                    return "food/list";
                }
            }
        }

        model.addAttribute("title", "Available Food");
        model.addAttribute("userId", userId);
        model.addAttribute("foods", filteredFood);
        model.addAttribute(new FilterForm());

        return "food/list";
    }

    @RequestMapping(value = "add/{userId}", method = RequestMethod.GET)
    public String displayAddFoodForm(Model model, @PathVariable int userId){

        Role myRole = null;
        User user = null;
        Optional<User> optionalUser = userDao.findById(userId);
        if(optionalUser.isPresent()){

            user = optionalUser.get();
            myRole = user.getRole();
        }

        List<Rights> rights = rightsDao.findByMyRight("add food");
        Rights requiredRight = rights.get(0);

        if(!myRole.getRights().contains(requiredRight)){

            model.addAttribute("title", "Invalid request!");
            model.addAttribute("messages", "You do not have privileges to perform this operation!");
            model.addAttribute("userId", userId);

            return "message";
        }

        model.addAttribute("title", "Add Food");
        model.addAttribute("userId", userId);
        model.addAttribute("foodTypes", foodTypeDao.findAll());
        model.addAttribute(new Food());

        return "food/add";
    }

    @RequestMapping(value = "add/{userId}", method = RequestMethod.POST)
    public String processAddFood(@ModelAttribute @Valid Food newFood, Errors errors, @PathVariable int userId, Model model){

        if(errors.hasErrors()){

            model.addAttribute("title", "Add Food");
            return "food/add";
        }

        newFood.setOriginalPrice(newFood.getPrice());

        foodDao.save(newFood);

        return "redirect:/food/list/" + userId;
    }

    @RequestMapping(value = "remove/{userId}", method = RequestMethod.GET)
    public String displayRemoveFood(Model model, @PathVariable int userId){

        Role myRole = null;
        User user = null;
        Optional<User> optionalUser = userDao.findById(userId);
        if(optionalUser.isPresent()){

            user = optionalUser.get();
            myRole = user.getRole();
        }

        List<Rights> rights = rightsDao.findByMyRight("remove food");
        Rights requiredRight = rights.get(0);

        if(!myRole.getRights().contains(requiredRight)){

            model.addAttribute("title", "Invalid request!");
            model.addAttribute("messages", "You do not have privileges to perform this action!");
            model.addAttribute("userId", userId);

            return "message";
        }

        model.addAttribute("title", "Remove Food");
        model.addAttribute("userId", userId);
        model.addAttribute("foods", foodDao.findAll());

        return "food/remove";
    }

    @RequestMapping(value = "remove/{userId}", method = RequestMethod.POST)
    public String processRemoveFood(@RequestParam int[] foodIds, @PathVariable int userId){

        for(int foodId : foodIds){

            foodDao.deleteById(foodId);
        }

        return "redirect:/food/list/" + userId;
    }

    @RequestMapping(value = "discount/{userId}", method = RequestMethod.GET)
    public String addDiscount(Model model, @PathVariable int userId){

        Role myRole = null;
        User user = null;
        Optional<User> optionalUser = userDao.findById(userId);
        if(optionalUser.isPresent()){

            user = optionalUser.get();
            myRole = user.getRole();
        }

        List<Rights> rights = rightsDao.findByMyRight("manage discounts");
        Rights requiredRight = rights.get(0);

        if(!myRole.getRights().contains(requiredRight)){

            model.addAttribute("title", "Invalid request!");
            model.addAttribute("messages", "You do not have privileges to perform this action!");
            model.addAttribute("userId", userId);

            return "message";
        }

        model.addAttribute("title", "Manage Discounts");
        model.addAttribute("foods", foodDao.findAll());
        model.addAttribute("userId", userId);

        return "food/discount";
    }

    @RequestMapping(value = "discount/{userId}", method = RequestMethod.POST)
    public String processAddDiscount(@RequestParam int[] foodIds, @RequestParam String action, @RequestParam String discount, @PathVariable int userId){

        for(int foodId: foodIds){

            Optional<Food> optionalFood = foodDao.findById(foodId);
            Food food = null;
            if(optionalFood.isPresent()){
                food = optionalFood.get();
            }

            Discount myDiscount = new DiscountCreator().createDiscount(discount);

            if(action.equals("Add Discount")) {

                food.setPrice(myDiscount.addDiscount(food.getPrice()));
                food.setDescription(myDiscount.toString());
            }
            else if(action.equals("Remove Discount")){

                food.setPrice(myDiscount.removeDiscount(food.getPrice(), food.getOriginalPrice()));
                food.setDescription("No discount");
            }

            foodDao.save(food);
        }

        return "redirect:/food/list/" + userId;
    }
}
