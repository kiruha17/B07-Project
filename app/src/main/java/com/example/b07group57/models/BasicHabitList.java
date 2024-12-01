package com.example.b07group57.models;

import java.util.ArrayList;
import java.util.List;
public class BasicHabitList {

    public static List <HabitStructure> getBasicHabits(){
        List<HabitStructure> habits = new ArrayList<>();

        habits.add(new HabitStructure("Reduce car usage by carpooling or combining errands.", "Transportation", "Medium"));
        habits.add(new HabitStructure("Switch to a fuel-efficient or electric vehicle.", "Transportation", "High"));
        habits.add(new HabitStructure("Use public transit", "Transportation", "High"));
        habits.add(new HabitStructure("Reduce meat consumption", "Food", "High"));
        habits.add(new HabitStructure("Plan meals and compost food scraps", "Food", "Medium"));
        habits.add(new HabitStructure("Switch to energy-efficient appliances and LED bulbs", "Energy", "High"));
        habits.add(new HabitStructure("Switch to renewable energy", "Energy", "High"));
        habits.add(new HabitStructure("Insulate housing to reduce energy loss.", "Energy", "High"));
        habits.add(new HabitStructure("Recycle paper, plastics, and e-waste properly","Consumption", "High"));
        habits.add(new HabitStructure("Reduce Reuse Recycle","Consumption", "High"));
        habits.add(new HabitStructure("Use the Planetze App to track and reduce your carbon footprint", "Consumption", "High"));




        return habits;
    }

}


