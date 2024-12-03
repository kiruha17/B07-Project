package com.example.b07group57.models;

import java.util.ArrayList;
import java.util.List;

public class BasicHabitList {

    public static List<HabitStructure> getBasicHabits(){
        List<HabitStructure> habits = new ArrayList<>();

        habits.add(new HabitStructure("Reduce car usage by carpooling or combining errands.", "Transportation", "High Impact"));
        habits.add(new HabitStructure("Switch to a fuel-efficient or electric vehicle.", "Transportation", "High Impact"));
        habits.add(new HabitStructure("Use public transit", "Transportation", "High Impact"));
        habits.add(new HabitStructure("Reduce meat consumption", "Food", "High Impact"));
        habits.add(new HabitStructure("Switch to energy-efficient appliances and LED bulbs", "Energy", "High Impact"));
        habits.add(new HabitStructure("Switch to renewable energy", "Energy", "High Impact"));
        habits.add(new HabitStructure("Insulate housing to reduce energy loss.", "Energy", "High Impact"));
        habits.add(new HabitStructure("Recycle paper, plastics, and e-waste properly", "Consumption", "High Impact"));
        habits.add(new HabitStructure("Reduce Reuse Recycle", "Consumption", "High Impact"));
        habits.add(new HabitStructure("Use the Planetze App to track and reduce your carbon footprint", "Consumption", "High Impact"));
        habits.add(new HabitStructure("Use public transport (bus, train, subway) or carpool instead of driving alone.", "Transportation", "High Impact"));
        habits.add(new HabitStructure("For long-distance travel, consider taking trains instead of flights.", "Transportation", "High Impact"));
        habits.add(new HabitStructure("Reduce meat consumption, particularly beef and pork, which have a high carbon footprint.", "Food", "High Impact"));
        habits.add(new HabitStructure("Avoid fast fashion, which contributes to high emissions due to mass production and waste.", "Clothing", "High Impact"));
        habits.add(new HabitStructure("Opt for clothing made from eco-friendly materials.", "Clothing", "High Impact"));
        habits.add(new HabitStructure("Reduce energy consumption by switching to LED lighting and energy-efficient appliances.", "Energy", "High Impact"));
        habits.add(new HabitStructure("Opt for renewable energy sources such as solar or wind, if available.", "Energy", "High Impact"));
        habits.add(new HabitStructure("Recycle old devices and avoid upgrading to newer models unnecessarily.", "Device", "High Impact"));
        habits.add(new HabitStructure("Dispose of electronics properly and avoid throwing them in landfills.", "Device", "High Impact"));
        habits.add(new HabitStructure("Reduce impulse buying and avoid unnecessary purchases that are not essential.", "Consumption", "High Impact"));
        habits.add(new HabitStructure("Support businesses that prioritize sustainability and eco-friendly practices.", "Consumption", "High Impact"));

        habits.add(new HabitStructure("Choose walking or cycling for short trips instead of driving.", "Transportation", "Medium Impact"));
        habits.add(new HabitStructure("Opt for electric or hybrid vehicles if possible.", "Transportation", "Medium Impact"));
        habits.add(new HabitStructure("Choose plant-based alternatives such as vegetables, legumes, and grains.", "Food", "Medium Impact"));
        habits.add(new HabitStructure("Reduce food waste by buying only what you need and storing food properly.", "Food", "Medium Impact"));
        habits.add(new HabitStructure("Buy second-hand or sustainable clothing.", "Clothing", "Medium Impact"));
        habits.add(new HabitStructure("Recycle or upcycle old clothes instead of throwing them away.", "Clothing", "Medium Impact"));
        habits.add(new HabitStructure("Unplug devices when not in use and use power strips to easily disconnect multiple devices.", "Energy", "Medium Impact"));
        habits.add(new HabitStructure("Use smart thermostats to optimize heating and cooling in your home.", "Energy", "Medium Impact"));
        habits.add(new HabitStructure("Choose energy-efficient electronics and appliances.", "Device", "Medium Impact"));
        habits.add(new HabitStructure("Switch to using devices that have lower energy consumption, such as energy-saving modes.", "Device", "Medium Impact"));
        habits.add(new HabitStructure("Buy only what you truly need, and consider the environmental impact of your purchases.", "Consumption", "Medium Impact"));
        habits.add(new HabitStructure("Avoid single-use items and opt for reusable alternatives.", "Consumption", "Medium Impact"));

        habits.add(new HabitStructure("Buy locally sourced food to reduce transportation emissions.", "Food", "Low Impact"));
        habits.add(new HabitStructure("Opt for products with minimal packaging or packaging made from recycled materials.", "Consumption", "Low Impact"));

        return habits;
    }

}
