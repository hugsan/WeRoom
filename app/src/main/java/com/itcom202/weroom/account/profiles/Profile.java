package com.itcom202.weroom.account.profiles;

public class Profile {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name= name;
    }

    private String name;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private int age;

    public Profile (String name, int age){
        this.name = name;
        this.age = age;
    }


}
