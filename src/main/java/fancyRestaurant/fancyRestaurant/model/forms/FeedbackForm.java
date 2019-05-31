package fancyRestaurant.fancyRestaurant.model.forms;

import javax.validation.constraints.NotNull;

public class FeedbackForm {

    @NotNull
    private String feedback;

    public FeedbackForm(String feedback){
        this.feedback = feedback;
    }

    public FeedbackForm(){

    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
