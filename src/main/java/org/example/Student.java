package org.example;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Student {
    @JsonProperty("id")
    String id ;
    @JsonProperty("Name")
    String Name;
    @JsonProperty("Grade")
    String Grade ;
    @JsonProperty("Email")
    String Email;
    @JsonProperty("Address")
    String Address;
    @JsonProperty("Region")
    String Region;
    @JsonProperty("Country")
    String Country ;

    public String toString ()
    {
        return this.id + ", \t" + this.Name +  ", \t" + this.Grade + ", \t" + this.Email + ", \t" + this.Address + ", \t"+ this.Region + ", \t" + this.Country ;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String grade) {
        Grade = grade;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }
}
