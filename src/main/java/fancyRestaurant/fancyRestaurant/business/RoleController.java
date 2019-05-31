package fancyRestaurant.fancyRestaurant.business;

import fancyRestaurant.fancyRestaurant.model.Rights;
import fancyRestaurant.fancyRestaurant.model.Role;
import fancyRestaurant.fancyRestaurant.model.User;
import fancyRestaurant.fancyRestaurant.model.forms.AddRoleRightForm;
import fancyRestaurant.fancyRestaurant.dao.RightsDAO;
import fancyRestaurant.fancyRestaurant.dao.RoleDAO;
import fancyRestaurant.fancyRestaurant.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("role")
public class RoleController {

    @Autowired
    private RoleDAO roleDao;

    @Autowired
    private RightsDAO rightsDao;

    @Autowired
    private UserDAO userDao;

    @RequestMapping(value = "list/{userId}", method = RequestMethod.GET)
    public String listRoles(Model model, @PathVariable int userId){

        Role role = null;
        User user = null;
        Optional<User> optionalUser = userDao.findById(userId);
        if(optionalUser.isPresent()){

            user = optionalUser.get();
            role = user.getRole();
        }

        List<Rights> rights = rightsDao.findByMyRight("view roles");
        Rights requiredRight = rights.get(0);

        if(!role.getRights().contains(requiredRight)){

            model.addAttribute("title", "Invalid request!");
            model.addAttribute("messages", "You do not have permission to perform that action!");
            model.addAttribute("userId", userId);

            return "message";
        }

        model.addAttribute("title", "Roles");
        model.addAttribute("roles", roleDao.findAll());
        model.addAttribute("userId", userId);

        return "role/list";
    }

    @RequestMapping(value = "view/{userId}/{roleId}", method = RequestMethod.GET)
    public String viewRoleRights(Model model, @PathVariable int userId, @PathVariable int roleId){

        Role myRole = null;
        User myUser = null;
        Optional<User> optionalUser = userDao.findById(userId);
        if(optionalUser.isPresent()){

            myUser = optionalUser.get();
            myRole = myUser.getRole();
        }

        List<Rights> rights = rightsDao.findByMyRight("view roles");
        Rights requiredRight = rights.get(0);

        if(!myRole.getRights().contains(requiredRight)){

            model.addAttribute("title", "Invalid Request!");
            model.addAttribute("messages", "You do not have permission to perform that action!");
            model.addAttribute("userId", userId);

            return "message";
        }

        Role role = null;
        Optional<Role> optionalRole = roleDao.findById(roleId);
        if(optionalRole.isPresent()){

            role = optionalRole.get();
        }

        model.addAttribute("title", role.getRole() + " rights");
        model.addAttribute("rights", role.getRights());
        model.addAttribute("userId", userId);
        model.addAttribute("roleId", roleId);

        return "role/view";
    }

    @RequestMapping(value = "add-right/{userId}/{roleId}", method = RequestMethod.GET)
    public String displayAddRightForm(Model model, @PathVariable int userId, @PathVariable int roleId){

        Role myRole = null;
        User myUser = null;
        Optional<User> optionalUser = userDao.findById(userId);
        if(optionalUser.isPresent()){

            myUser = optionalUser.get();
            myRole = myUser.getRole();
        }

        List<Rights> rights = rightsDao.findByMyRight("add right to role");
        Rights requiredRight = rights.get(0);

        if(!myRole.getRights().contains(requiredRight)){

            model.addAttribute("title", "Invalid Request");
            model.addAttribute("messages", "You do not have permission to perform that action!");
            model.addAttribute("userId", userId);

            return "message";
        }

        Role role = null;
        Optional<Role> optionalRole = roleDao.findById(roleId);
        if(optionalRole.isPresent()){

            role = optionalRole.get();
        }

        AddRoleRightForm form = new AddRoleRightForm(rightsDao.findAll(), role);

        model.addAttribute("title", "Add Rights");
        model.addAttribute("form", form);
        model.addAttribute("userId", userId);
        model.addAttribute("roleId", roleId);

        return "role/add-right";
    }

    @RequestMapping(value = "add-right/{userId}/{roleId}", method = RequestMethod.POST)
    public String processAddRightForm(Model model, @ModelAttribute @Valid AddRoleRightForm form, Errors errors, @PathVariable int userId){

        if(errors.hasErrors()){

            model.addAttribute("title", "Add Rights");

            return "role/add-rights";
        }

        Rights newRight = null;
        Optional<Rights> optionalRights = rightsDao.findById(form.getRightId());
        if(optionalRights.isPresent()){

            newRight = optionalRights.get();
        }

        Role myRole = null;
        Optional<Role> optionalRole = roleDao.findById(form.getRoleId());
        if(optionalRole.isPresent()){

            myRole = optionalRole.get();
        }

        if(myRole.getRights().contains(newRight)){

            model.addAttribute("title", "Add Right");
            model.addAttribute("messages", "This role already has that right!");
            model.addAttribute("userId", userId);

            return "message";
        }

        myRole.addRight(newRight);
        roleDao.save(myRole);

        return "redirect:/role/view/" + userId + "/" + myRole.getId();
    }

    @RequestMapping(value = "remove-right/{userId}/{roleId}", method = RequestMethod.GET)
    public String displayRemoveRightForm(Model model, @PathVariable int userId, @PathVariable int roleId){

        Role myRole = null;
        User myUser = null;
        Optional<User> optionalUser = userDao.findById(userId);
        if(optionalUser.isPresent()){

            myUser = optionalUser.get();
            myRole = myUser.getRole();
        }

        List<Rights> rights = rightsDao.findByMyRight("remove right from role");
        Rights requiredRight = rights.get(0);

        if(!myRole.getRights().contains(requiredRight)){

            model.addAttribute("title", "Invalid Request");
            model.addAttribute("messages", "You do not have permission to perform that action!");
            model.addAttribute("userId", userId);

            return "message";
        }

        Role role = null;
        Optional<Role> optionalRole = roleDao.findById(roleId);
        if(optionalRole.isPresent()){

            role = optionalRole.get();
        }

        AddRoleRightForm form = new AddRoleRightForm(role.getRights(), role);

        model.addAttribute("title", "Remove Rights");
        model.addAttribute("form", form);
        model.addAttribute("userId", userId);
        model.addAttribute("roleId", roleId);

        return "role/remove-right";
    }

    @RequestMapping(value = "remove-right/{userId}/{roleId}", method = RequestMethod.POST)
    public String processRemoveRightForm(Model model, @ModelAttribute @Valid AddRoleRightForm form, Errors errors, @PathVariable int userId){

        if(errors.hasErrors()){

            model.addAttribute("title", "Remove Rights");

            return "role/remove-rights";
        }

        Rights rightToRemove = null;
        Optional<Rights> optionalRights = rightsDao.findById(form.getRightId());
        if(optionalRights.isPresent()){

            rightToRemove = optionalRights.get();
        }

        Role myRole = null;
        Optional<Role> optionalRole = roleDao.findById(form.getRoleId());
        if(optionalRole.isPresent()){

            myRole = optionalRole.get();
        }

        myRole.removeRight(rightToRemove);
        roleDao.save(myRole);

        return "redirect:/role/view/" + userId + "/" + myRole.getId();
    }
}
