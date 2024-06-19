package com.example.projectapp;

public class User {
public String fullName,age,email;
public User()
{ }
public User(String fullName,String age, String email){
    this.fullName=fullName;
    this.age=age;
    this.email=email;

}

    public String getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDisplayName() {
        return fullName;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
