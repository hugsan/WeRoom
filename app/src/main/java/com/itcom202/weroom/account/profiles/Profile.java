package com.itcom202.weroom.account.profiles;

public class Profile {

    private String name;
    private int age;
    private String gender;
    private String country;
    private String role;



    public Profile (String name, int age, String gender, String country, String role){
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.country = country;
        this.role = role;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
