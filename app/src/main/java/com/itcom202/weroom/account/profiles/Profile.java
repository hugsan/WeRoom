package com.itcom202.weroom.account.profiles;

import com.itcom202.weroom.account.profiles.TagDescription.TagModel;

import java.io.Serializable;
import java.util.List;

public class Profile implements Serializable {

    private String name;
    private int age;
    private String gender;
    private String country;
    private String role;
    private List<String> tags;



    public Profile (String name, int age, String gender, String country, String role, List<String> tags){
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.country = country;
        this.role = role;
        this.tags = tags;

    }
    //This constructor is needed to de-serialize the object from the FireBase database.
    public Profile(){}

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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
