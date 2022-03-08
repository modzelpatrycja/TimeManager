package com.example.timemanager;

import java.util.List;

public class DayModel {

    private String name;
    private List<DayTasks> ChildItemList;

    public DayModel(String name, List<DayTasks> ChildItemList) {

        this.name = name;
        this.ChildItemList = ChildItemList;
    }

    public String getName() {

        return name;
    }


    public List<DayTasks> getChildItemList() {

        return ChildItemList;
    }


}