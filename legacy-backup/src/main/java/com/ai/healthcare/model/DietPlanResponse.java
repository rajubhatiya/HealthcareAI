package com.ai.healthcare.model;

import lombok.Data;

import java.util.List;

public class DietPlanResponse {

    private String title;

    private List<String> breakfast;
    private List<String> lunch;
    private List<String> dinner;
    private List<String> snacks;

    private List<String> nutritionTips;

    private String disclaimer;

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(List<String> breakfast) {
        this.breakfast = breakfast;
    }

    public List<String> getLunch() {
        return lunch;
    }

    public void setLunch(List<String> lunch) {
        this.lunch = lunch;
    }

    public List<String> getDinner() {
        return dinner;
    }

    public void setDinner(List<String> dinner) {
        this.dinner = dinner;
    }

    public List<String> getSnacks() {
        return snacks;
    }

    public void setSnacks(List<String> snacks) {
        this.snacks = snacks;
    }

    public List<String> getNutritionTips() {
        return nutritionTips;
    }

    public void setNutritionTips(List<String> nutritionTips) {
        this.nutritionTips = nutritionTips;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }
}
