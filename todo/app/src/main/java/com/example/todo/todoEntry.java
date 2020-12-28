package com.example.todo;

public class todoEntry {

    String name,date,time;
    int id,check=0;

    public todoEntry(String name,String date, String time,int id,int check){
        this.name=name;
        this.date=date;
        this.time=time;
        this.id=id;
        this.check=check;
    }

    public todoEntry(String name,String date, String time){
        this.name=name;
        this.date=date;
        this.time=time;
    }

    public todoEntry(){
        this.name="";
        this.date="00/00/00";
        this.time="-1";
        this.id=-1;
        this.check=0;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public int getCheck() {
        return check;
    }

    public int getId() {
        return id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public void setId(int id) {
        this.id = id;
    }
}

