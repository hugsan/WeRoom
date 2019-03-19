package com.itcom202.weroom.account.profiles;

public class Profile {

    private String name;
    private int age;
    private String gender;
    private String country;



    public Profile (String name, int age, String gender, String country){
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.country = country;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name= name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
