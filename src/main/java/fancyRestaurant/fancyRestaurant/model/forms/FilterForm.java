package fancyRestaurant.fancyRestaurant.model.forms;

import javax.validation.constraints.NotNull;

public class FilterForm {

    @NotNull
    private String filter;

    public FilterForm(String filter){
        this.filter = filter;
    }

    public FilterForm(){

    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
