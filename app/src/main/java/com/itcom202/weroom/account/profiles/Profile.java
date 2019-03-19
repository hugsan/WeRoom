package com.itcom202.weroom.account.profiles;

public class Profile {
    public String getPotato() {
        return potato;
    }

    public void setPotato(String potato) {
        this.potato = potato;
    }

    private String potato;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private int age;

    public Profile (String name, int age){
        this.potato = name;
        this.age = age;
    }


}
