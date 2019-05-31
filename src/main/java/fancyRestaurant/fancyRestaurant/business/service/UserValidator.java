package fancyRestaurant.fancyRestaurant.business.service;

import fancyRestaurant.fancyRestaurant.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {

    private static final String EMAIL_VALIDATION_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private static final int MIN_PASSWORD_LENGTH = 8;

    private final List<String> errors;
    private final User user;

    public UserValidator(User user){

        this.user = user;
        errors = new ArrayList<>();
    }

    public List<String> getErrors(){

        return errors;
    }

    public boolean validate(){

        validateEmail(user.getEmail());
        validatePassword(user.getPassword());

        return errors.isEmpty();
    }

    private void validateEmail(String email){

        if(!Pattern.compile(EMAIL_VALIDATION_REGEX).matcher(email).matches()){

            errors.add("Invalid email!");
        }
    }

    private void validatePassword(String password){

        if(password.length() < MIN_PASSWORD_LENGTH){

            errors.add("Password is too short!");
        }
        if(!containsSpecialCharacter(password)){

            errors.add("Password must contain at least one special character!");
        }
        if(!containsDigit(password)){

            errors.add("Password must contain at least one digit!");
        }
    }

    private boolean containsSpecialCharacter(String password){

        if(null == password || password.trim().isEmpty()){

            return false;
        }

        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(password);

        return m.find();
    }

    private boolean containsDigit(String password){

        if(null == password || password.trim().isEmpty()){

            return false;
        }

        for(char c: password.toCharArray()){

            if(Character.isDigit(c)){

                return true;
            }
        }

        return false;
    }
}
