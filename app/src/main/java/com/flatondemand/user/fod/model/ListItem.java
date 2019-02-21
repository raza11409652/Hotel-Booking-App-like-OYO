package com.flatondemand.user.fod.model;

public class ListItem {
    private  String head , desc;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ListItem(String head, String desc) {

        this.head = head;
        this.desc = desc;
    }
}
