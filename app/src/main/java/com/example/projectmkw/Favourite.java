package com.example.projectmkw;

public class Favourite {

    private long pid;
    private String name;

    public Favourite(Member member){
        this.pid = member.getPid();
        this.name = member.getNames()[0];
    }

    public long getPid() {
        return pid;
    }

    public String getName() {
        return name;
    }
}
