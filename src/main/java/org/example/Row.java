package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Row {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getCourseDuration() {
        return courseDuration;
    }

    public void setCourseDuration(String courseDuration) {
        this.courseDuration = courseDuration;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String toString ()
    {
        return this.id + ", \t" + this.courseName + ", \t" + this.instructor + ", \t" + this.courseDuration + ", \t" + this.courseTime + ", \t" + this.location ;
    }

    @JsonProperty("id")

    String id ;
    @JsonProperty("CourseName")

    String courseName ;
    @JsonProperty("Instructor")

    String instructor ;
    @JsonProperty("CourseDuration")

    String courseDuration ;
    @JsonProperty("CourseTime")

    String courseTime ;
    @JsonProperty("Location")

    String location ;



}
