package com.example.b07group57.models;

import java.util.ArrayList;
import java.util.List;
public class BasicHabitList {

    public static List <HabitStructure> getBasicHabits(){
        List<HabitStructure> habits = new ArrayList<>();

        habits.add(new HabitStructure("Reduce car usage by carpooling or combining errands.", "Transportation", 7));
        habits.add(new HabitStructure("Switch to a fuel-efficient or electric vehicle.", "Transportation", 9));
        habits.add(new HabitStructure("Use public transit", "Transportation", 8));
        habits.add(new HabitStructure("Reduce meat consumption", "Food", 8));
        habits.add(new HabitStructure("Plan meals and compost food scraps", "Food", 6));
        habits.add(new HabitStructure("Switch to energy-efficient appliances and LED bulbs", "Energy", 9));
        habits.add(new HabitStructure("Switch to renewable energy", "Energy", 10));
        habits.add(new HabitStructure("Insulate housing to reduce energy loss.", "Energy", 8));
        habits.add(new HabitStructure("Recycle paper, plastics, and e-waste properly","Consumption", 9));
        habits.add(new HabitStructure("Reduce Reuse Recycle","Consumption", 9));
        habits.add(new HabitStructure("Use the Planetze App to track and reduce your carbon footprint", "Consumption", 10));




        return habits;
    }

}


