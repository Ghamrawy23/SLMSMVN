package org.example;
import java.io.*;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class Main {

    Student [] students ;
    Row [] courses ;
    public static void main(String[] args) {

        Main m = new Main () ;

        m.textToCsv("Data/student-data.txt", "Data/students.csv");
        m.xmlToCsv ("Data/coursedata.xml", "Data/coursedata.csv");
        m.printStudentDataConsole("Data/students.csv") ;
        m.printStudentCourseDetailsJson ("Data/Student course details.json", "122");
        m.enrollStudentToCourse("70", "12", "Data/Student course details.json") ;
    }

        public void enrollStudentToCourse (String studentId, String courseId, String jsonPath)
        {

        //validation on ids
        if (Integer.parseInt(studentId) > this.students.length )
        {
            System.out.println("Invalid student id");
            return ;
        }
        if (Integer.parseInt(courseId) > this.courses.length )
        {
            System.out.println("Invalid course id");
            return ;
        }

        //read json
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode object;
        File jsonFile = new File(jsonPath);
        try {
            object = mapper.readValue(jsonFile, ObjectNode.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //search students
        Iterator<String> keys = object.fieldNames();
        Boolean notFound = true ;
        int number = 0 ;
        Integer[] Existedcourses = new Integer[0];
        while(notFound && keys.hasNext()) {
            String id = keys.next();
            Integer coursesSize = object.get(id).size();

            if (id == studentId) {
                Existedcourses = new Integer[coursesSize+1];
                notFound = false;
                //max number is 6 to enroll in
                if (coursesSize >= 6) {
                    System.out.println("max number to enroll is 6");
                    return;
                }
                //search courses
                for (JsonNode coursesData : object.get(id)) {
                    Existedcourses[number++] = coursesData.asInt() ;
                    if (coursesData.asInt() == Integer.parseInt(courseId)) {
                        System.out.println("this student is already enrolled in this course");
                        return;
                    }

                }
            }
        }
                if (notFound) {
                    //write student if not exist here
                    System.out.println("This student hasn't enrolled in any courses");
                    System.out.println("------------------------------------------------------------------------------------");
                    try {
                        String jsonString = mapper.writeValueAsString(new Integer [] {Integer.parseInt(courseId)});
                        JsonNode node = mapper.readValue(jsonString, JsonNode.class);
                        object.put(studentId, node);
                        System.out.println("Course Added.");


                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                }

                else {
                    Existedcourses[number] = Integer.parseInt(courseId);

                    //write course to student if exists here (in the object that represent json)
                    try {
                        String jsonString = mapper.writeValueAsString(Existedcourses);
                        JsonNode node = mapper.readValue(jsonString, JsonNode.class);
                        object.put(studentId, node);
                        System.out.println("Course Added.");

                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    //write the json object to the file

                }

        try {
            mapper.writeValue(jsonFile, object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
            }

        
         public void printStudentDataConsole(String csvFilePath)  {

        // create a CsvMapper and CsvSchema
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();

// read from a file
        File csvFile;
        csvFile = new File(csvFilePath);
        MappingIterator<Student> it = null;

        // get the header
        CsvSchema headerSchema = mapper.schemaFor(Student.class).withHeader();
        List<String> header = headerSchema.getColumnNames();

        try {
            it = mapper.readerFor(Student.class).with(schema).readValues(csvFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//print header
        System.out.println("------------------------------- \n Current Student List \n------------------------------- \n");
        System.out.println("id \t Name \t Grade \t Email \t Address \t Region \t Country");
// print the data to the console
        while (it.hasNext()) {
            System.out.println(it.next());

        }
    }
         public void printStudentCourseDetailsJson(String jsonPath, String studentId) {

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode object;
        File jsonFile = new File(jsonPath);
        try {
            object = mapper.readValue(jsonFile, ObjectNode.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Iterator<String> keys = object.fieldNames();
        //print header
        System.out.println("==================================================================================== \nStudent Details page \n==================================================================================== \n");

//check if it exists in all students (valid or invalid)
        if (Integer.parseInt(studentId) - 1 <= this.students.length)
        {System.out.println("Name: " + this.students[Integer.parseInt(studentId) - 1].Name + "\tGrade: " + this.students[Integer.parseInt(studentId) - 1].Grade + "\tEmail: " + this.students[Integer.parseInt(studentId) - 1].Email);
        System.out.println("------------------------------------------------------------------------------------ \nEnrolled courses.\n");
    }
        else {
            System.out.println("Invalid Student ID");
            return ;
        }
        Boolean notFound = true ;
        while(notFound && keys.hasNext())
        {

            String id = keys.next() ;
            if(id == studentId)
            {
                int number = 0 ;
                for ( JsonNode coursesData : object.get(id))
                {
                    System.out.println(++number + "- " + this.courses[coursesData.asInt()-1]) ;
                }
                notFound = false ;
            }

        }

        if (notFound)
            System.out.println("This student hasn't enrolled in any courses");
        System.out.println("------------------------------------------------------------------------------------");



    }
         public void xmlToCsv(String xmlFile, String csvFile) {

        XmlMapper xmlMapper = new XmlMapper();
        List<Row> rows = null;
        try {
            rows = xmlMapper.readValue(new File(xmlFile), new TypeReference<List<Row>>(){});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            FileWriter csvWriter = new FileWriter(csvFile);
            csvWriter.append("id,CourseName,Instructor,CourseDuration,CourseTime,Location");
            csvWriter.append("\n");
            this.courses = new Row [rows.size()] ;
            for (int i = 0 ; i<rows.size() ; i++)
            {
                csvWriter.append(rows.get(i).getId());
                csvWriter.append(",");
                csvWriter.append(rows.get(i).getCourseName());
                csvWriter.append(",");
                csvWriter.append('"' + rows.get(i).getInstructor() + '"');
                csvWriter.append(",");
                csvWriter.append(rows.get(i).getCourseDuration());
                csvWriter.append(",");
                csvWriter.append(rows.get(i).getCourseTime());
                csvWriter.append(",");
                csvWriter.append(rows.get(i).getLocation());
                csvWriter.append(",");
                csvWriter.append("\n");
                Row temp = new Row () ;
                temp.id = rows.get(i).getId() ;
                temp.courseName = rows.get(i).getCourseName() ;
                temp.instructor = rows.get(i).getInstructor() ;
                temp.courseDuration = rows.get(i).getCourseDuration() ;
                temp.courseTime = rows.get(i).getCourseTime() ;
                temp.location = rows.get(i).getLocation() ;

                this.courses[i] = temp ;

            }

            csvWriter.flush();
            csvWriter.close();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        }

         public void textToCsv (String textFile, String csvFile){
            try {
                //Read the txt file
                BufferedReader br = new BufferedReader(new FileReader(textFile));
                String line;
                int id = 0;
                //Open the csv file

                FileWriter csvWriter = new FileWriter(csvFile);
                //Add headers

                csvWriter.append("id,Name,Grade,Email,Address,Region,Country");
                csvWriter.append("\n");
                line = br.readLine();
                String[] lines = line.split("\\$");
                this.students = new Student [lines.length] ;
                for (String entry :lines) {
                    if (id == 0) {
                        id++;
                        continue;
                    }
                    String[] values = entry.split("#");

                    Student temp = new Student () ;
                    temp.id = values[0];
                    temp.Name = values[1];
                    temp.Grade = values[2];
                    temp.Email = values[3];
                    temp.Address = values[4];
                    temp.Region = values[5];
                    temp.Country = values[6];
                    this.students[id-1] = temp ;
                    int count = 0 ;

                    id ++ ;
                    for (String value : values) {

                        if (count == 6)
                            csvWriter.append('"' + value + '"');

                        else csvWriter.append(value);

                        csvWriter.append(",");
                        count ++ ;
                    }
                    csvWriter.append("\n");

                }

                //Close the file
                csvWriter.flush();
                csvWriter.close();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

}
