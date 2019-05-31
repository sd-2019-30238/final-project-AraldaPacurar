package fancyRestaurant.fancyRestaurant.business;

import fancyRestaurant.fancyRestaurant.business.service.PasswordEncoder;
import fancyRestaurant.fancyRestaurant.business.service.UserValidator;
import fancyRestaurant.fancyRestaurant.dao.NotificationDAO;
import fancyRestaurant.fancyRestaurant.model.Rights;
import fancyRestaurant.fancyRestaurant.model.Role;
import fancyRestaurant.fancyRestaurant.model.User;
import fancyRestaurant.fancyRestaurant.dao.RightsDAO;
import fancyRestaurant.fancyRestaurant.dao.RoleDAO;
import fancyRestaurant.fancyRestaurant.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserDAO userDao;

    @Autowired
    private RoleDAO roleDao;

    @Autowired
    private RightsDAO rightsDao;

    @Autowired
    private NotificationDAO notificationDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String displayAuthenticationForm(Model model){

        model.addAttribute("title", "Authentication");
        model.addAttribute(new User());

        return "user/authentication";
    }

    @RequestMapping(value="", method = RequestMethod.POST)
    public String processAuthentication(@ModelAttribute User newUser, Errors errors, Model model){

        if(errors.hasErrors()){

            model.addAttribute("title", "Authentication");
            return "user/authentication";
        }

        List<User> user = userDao.findByUsername(newUser.getUsername());
        if(user.isEmpty()){

            model.addAttribute("title", "Login Error!");
            model.addAttribute("messages", "Invalid username or password!");

            return "user/message";
        }

        User myUser = user.get(0);
        String foundUserEncodedPassword = myUser.getPassword();
        PasswordEncoder passwordEncoder = new PasswordEncoder(newUser.getPassword());

        if (!foundUserEncodedPassword.equals(passwordEncoder.getEncodedPassword())) {

            model.addAttribute("title", "Login Error!");
            model.addAttribute("messages", "Invalid username or password!");

            return "user/message";
        }

        model.addAttribute("title", "Login Successful!");
        model.addAttribute("rights", myUser.getRole().getRights());
        model.addAttribute("userId", myUser.getId());

        return "user/menu";
    }

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String displayRegisterForm(Model model){

        model.addAttribute("title", "Registration");
        model.addAttribute(new User());

        return "user/registration";
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String processRegistrationForm(@ModelAttribute @Valid User newUser, Errors errors, Model model){

        if(errors.hasErrors()){

            model.addAttribute("title", "Registration");
            return "user/registration";
        }

        UserValidator userValidator = new UserValidator(newUser);
        if(!userValidator.validate()){

            model.addAttribute("title", "Registration Error!");
            model.addAttribute("messages", userValidator.getErrors());

            return "user/message";
        }

        if(!userDao.findByUsername(newUser.getUsername()).isEmpty()){

            model.addAttribute("title","Registration Error!");
            model.addAttribute("messages", "Username is already taken!");

            return "user/message";
        }

        PasswordEncoder passwordEncoder = new PasswordEncoder(newUser.getPassword());
        newUser.setPassword(passwordEncoder.getEncodedPassword());

        List<Role> roles = roleDao.findByRole("customer");
        newUser.setRole(roles.get(0));

        userDao.save(newUser);

        model.addAttribute("title", "Registration Completed!");

        return "user/message";
    }

    @RequestMapping(value = "/list/{userId}", method = RequestMethod.GET)
    public String listUsers(Model model, @PathVariable int userId){

        Role myRole = null;
        User user = null;
        Optional<User> optionalUser = userDao.findById(userId);
        if(optionalUser.isPresent()){

            user = optionalUser.get();
            myRole = user.getRole();
        }

        List<Rights> rights = rightsDao.findByMyRight("view users");
        Rights requiredRight = rights.get(0);

        if(!myRole.getRights().contains(requiredRight)){

            model.addAttribute("title", "Invalid request!");
            model.addAttribute("messages", "You do not have privileges to perform this action!");
            model.addAttribute("userId", userId);

            return "message";
        }

        model.addAttribute("title", "Registered Users");
        model.addAttribute("users", userDao.findAll());
        model.addAttribute("userId", userId);

        return "user/list";
    }

    @RequestMapping(value = "/listNotifications/{userId}", method = RequestMethod.GET)
    public String listNotifications(Model model, @PathVariable int userId){

        model.addAttribute("title", "View Notifications");
        model.addAttribute("notifications", notificationDao.findByUserId(userId));
        model.addAttribute("userId", userId);

        return "user/listNotifications";
    }

    @RequestMapping(value = "remove/{userId}", method = RequestMethod.GET)
    public String displayRemoveUser(Model model, @PathVariable int userId){

        Role myRole = null;
        User user = null;
        Optional<User> optionalUser = userDao.findById(userId);
        if(optionalUser.isPresent()){

            user = optionalUser.get();
            myRole = user.getRole();
        }

        List<Rights> rights = rightsDao.findByMyRight("fire");
        Rights requiredRight = rights.get(0);

        if(!myRole.getRights().contains(requiredRight)){

            model.addAttribute("title", "Invalid request!");
            model.addAttribute("messages", "You do not have privileges to perform this action!");
            model.addAttribute("userId", userId);

            return "message";
        }

        List<Role> roles = roleDao.findByRole("employee");

        model.addAttribute("title", "Fire Employee");
        model.addAttribute("employees", userDao.findByRoleId(roles.get(0).getId()));
        model.addAttribute("userId", userId);

        return "user/remove";
    }

    @RequestMapping(value = "remove/{userId}", method = RequestMethod.POST)
    public String processRemoveUser(@RequestParam int[] employeeIds, @PathVariable int userId){

        for(int employeeId : employeeIds){

            userDao.deleteById(employeeId);
        }

        return "redirect:/user/list/" + userId;
    }

    @RequestMapping(value = "hire/{userId}", method = RequestMethod.GET)
    public String displayHireEmployeeForm(Model model, @PathVariable int userId){

        Role myRole = null;
        User user = null;
        Optional<User> optionalUser = userDao.findById(userId);
        if(optionalUser.isPresent()){

            user = optionalUser.get();
            myRole = user.getRole();
        }

        List<Rights> rights = rightsDao.findByMyRight("hire");
        Rights requiredRight = rights.get(0);

        if(!myRole.getRights().contains(requiredRight)){

            model.addAttribute("title", "Invalid request!");
            model.addAttribute("messages", "You do not have privileges to perform this action!");
            model.addAttribute("userId", userId);

            return "message";
        }

        model.addAttribute("title", "Hire Employee");
        model.addAttribute(new User());
        model.addAttribute("userId", userId);

        return "user/hire";
    }

    @RequestMapping(value = "hire/{userId}", method = RequestMethod.POST)
    public String processHireEmployeeForm(@ModelAttribute @Valid User newUser, Errors errors, Model model, @PathVariable int userId){

        if(errors.hasErrors()){

            model.addAttribute("title", "Hire Employee");
            model.addAttribute("userId", userId);
            return "user/hire";
        }

        UserValidator userValidator = new UserValidator(newUser);
        if(!userValidator.validate()){

            model.addAttribute("title", "Registration Error!");
            model.addAttribute("messages", userValidator.getErrors());
            model.addAttribute("userId", userId);

            return "message";
        }

        if(!userDao.findByUsername(newUser.getUsername()).isEmpty()){

            model.addAttribute("title","Registration Error!");
            model.addAttribute("messages", "Username is already taken!");
            model.addAttribute("userId", userId);

            return "message";
        }

        PasswordEncoder passwordEncoder = new PasswordEncoder(newUser.getPassword());
        newUser.setPassword(passwordEncoder.getEncodedPassword());

        List<Role> roles = roleDao.findByRole("employee");
        newUser.setRole(roles.get(0));

        userDao.save(newUser);

        model.addAttribute("title", "Employee Hired!");
        model.addAttribute("userId", userId);

        return "message";
    }

}
