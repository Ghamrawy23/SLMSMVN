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
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class Main {

    Student[] students;
    Course[] courses;
    Map<String, List<Integer>> studentCourses;

    Main ()
    {
        //those should run to load students and courses
        textToCsv("Data/student-data.txt", "Data/students.csv");
        xmlToCsv("Data/coursedata.xml", "Data/coursedata.csv");
    }
    public static void main(String[] args) {

        Main m = new Main();

        while (true)
        {
            //first page
            m.printStudentDataConsole("Data/students.csv") ;
            System.out.print("\nEnter student id: ");
            Scanner s = new Scanner (System.in) ;
            String idStudent = s.next() ;

            if (!isNumeric(idStudent))
            {
                System.out.println("Please Enter a valid choice.");
                pause() ;
                continue ;
            }
            else if (Integer.parseInt(idStudent)>m.students.length)
            {
                System.out.println("Please Enter a valid choice.");
                pause() ;
                continue ;
            }

            m.printStudentCourseDetailsJson ("Data/Student course details.json", idStudent) ;
            System.out.println("------------------------------------------------------------------------------------");
            Boolean loop = true ;
            while (loop) {

                System.out.print("\na - Enroll in a course\n" +
                        "d - Unenroll from an existing course\n" +
                        "r - Replacing an existing course\n" +
                        "b - Back to the main page\n" +"please select the required action: ");

                String choice =  s.next() ;



                switch (choice) {
                    case "a": {
                        System.out.println("====================================================================================\nEnrollment page\n====================================================================================================\n id, \tCourse Name, \tInstructor, \tCourse duration, \tCourse time, \tLocation\n----------------------------------------------------------------------------------------------------");
                        m.printAllCourses() ;
                        Boolean correct = false ;
                        String courseId = "" ;
                        System.out.println("Please make one of the following: ");
                        while (!correct) {
                            System.out.print("Enter the course id that you want to enroll the student to \nEnter b to go back to the home page\nPlease select the required action: ");
                            courseId = s.next();
                            if (courseId.equals("b")) {
                                correct = true;
                            }
                            else if (!isNumeric(courseId))
                            {
                                System.out.println("Wrong choice! please enter a valid choice");
                            }
                            else if (Integer.parseInt(courseId)>m.courses.length){
                                System.out.println("Wrong choice! please enter a valid choice");
                            }
                            else correct = true ;

                        }

                        if (courseId.equals("b"))
                            break ;

                        try {
                            m.enrollStudentToCourse("Data/Student course details.json", idStudent, courseId);
                            loop = false ;
                        } catch (Exception e)
                        {
                            System.out.println("Failed to enroll: " + e.getMessage());
                        }

                        pause() ;
                        break;
                    }
                    case "d": {
                        System.out.print("Enter course Id to unenroll: ");
                        String courseId = s.next();
                        try {
                            m.unenrollStudentFromCourse("Data/Student course details.json", idStudent, courseId);
                            loop = false ;
                        } catch (Exception e)
                        {
                            System.out.println("Failed to enroll: " + e.getMessage());
                        }
                        pause();
                        break;
                    }
                    case "r": {
                        System.out.println("====================================================================================================\n Available Courses\n ====================================================================================================\n id, \tCourse Name, \tInstructor, \tCourse duration, \tCourse time, \tLocation");
                        m.printAllCourses();
                        System.out.print("Enter course Id to unenroll: ");
                        String courseId1 = s.next();
                        System.out.print("Enter course Id to enroll: ");
                        String courseId2 = s.next();
                        try {
                            m.unenrollAndEnroll("Data/Student course details.json", idStudent, courseId1, courseId2);
                            loop = false ;
                        } catch (Exception e)
                        {
                            System.out.println("Failed to enroll: " + e.getMessage());
                        }
                       pause();
                        break;
                    }
                    case "b":
                        loop = false ;
                        break;
                    default:
                        loop = false ;
                        break;

                }
            }
        }
    }

    private static void pause() {
        System.out.print("Press Enter to continue...");
        new Scanner(System.in).nextLine();
    }

    public void unenrollStudentFromCourse(String jsonPath, String studentId, String courseId) throws Exception {
        readJson(jsonPath);
        if (Integer.parseInt(studentId) > this.students.length) {
            throw new Exception("Student id is invalid");
        }

        if (!this.studentCourses.containsKey(studentId)) {
            throw new Exception("Student doesn't have any courses");

        }
        if (Integer.parseInt(courseId) > this.courses.length) {
            throw new Exception("Course id is invalid");
        }

        if (!this.studentCourses.get(studentId).contains(Integer.parseInt(courseId))) {
            throw new Exception("Student isn't enrolled in this course");
        }

        List tempCourses = this.studentCourses.get(studentId);
        int index = tempCourses.indexOf(Integer.parseInt(courseId));
        tempCourses.remove(index);
        System.out.println("Unenrolled student from course.");

        //handle if all courses are unenrolled
        if (tempCourses.size() == 0) this.studentCourses.remove(studentId);
        writeJson(jsonPath);

    }

    public void unenrollAndEnroll(String jsonPath, String studentId, String courseId1, String courseId2) throws Exception {
        readJson(jsonPath);

        if (Integer.parseInt(studentId) > this.students.length) {
            throw new Exception("Student id is invalid");
        }

        if (!this.studentCourses.containsKey(studentId)) {
            throw new Exception("Student doesn't have any courses");
        }

        if (Integer.parseInt(courseId1) > this.courses.length) {
            throw new Exception("1st Course id is invalid");
        }

        if (!this.studentCourses.get(studentId).contains(Integer.parseInt(courseId1))) {
            throw new Exception("Student isn't enrolled in this course");
        }

        if (Integer.parseInt(courseId2) > this.courses.length) {
            throw new Exception("2nd Course id is invalid");
        }

        if (!this.studentCourses.get(studentId).contains(Integer.parseInt(courseId1))) {
            throw new Exception("Student already enrolled in this course");
        }

        List tempCourses = this.studentCourses.get(studentId);
        int index = tempCourses.indexOf(Integer.parseInt(courseId1));
        tempCourses.set(index, Integer.parseInt(courseId2));
        System.out.println("Unenrolled and enrolled successfuly");
        writeJson(jsonPath);

    }

    public void enrollStudentToCourse(String jsonPath, String studentId, String courseId) throws Exception {
        if (Integer.parseInt(studentId) > this.students.length) {
            //System.out.println("Invalid student id");
            throw new Exception("Invalid student id") ;
            //return;
        }
        if (Integer.parseInt(courseId) > this.courses.length) {
            throw new Exception("Invalid course id") ;
           // return;
        }

        readJson(jsonPath);

        if (this.studentCourses.containsKey(studentId))      //add to existing student
        {
            List tempCourses = this.studentCourses.get(studentId);

            if (tempCourses.size() >= 6) {
                //System.out.println("Student can't enroll in more than 6 courses");
                throw new Exception("Student can't enroll in more than 6 courses") ;
                //return;
            }
            if (tempCourses.contains(Integer.parseInt(courseId))) {
                //System.out.println("Student already enrolled in this course");
                throw new Exception("Student already enrolled in this course") ;
              //  return;

            } else {
                tempCourses.add(Integer.parseInt(courseId));
            }
        }               //add to new student
        else {

            this.studentCourses.put(studentId, new ArrayList<Integer>(Arrays.asList(Integer.valueOf(courseId))));
        }

        System.out.println("Student is Enrolled Successfully.");
        writeJson(jsonPath);

    }

    public void printStudentDataConsole(String csvFilePath) {

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
        System.out.println("Welcome to LMS\n created by Mohamed Ghamrawy_Januarycohort\n ====================================================================================------------------------------- \n Current Student List \n------------------------------- \n");
        System.out.println("id \t Name \t Grade \t Email \t Address \t Region \t Country");
    // print the data to the console
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    public void printStudentCourseDetailsJson(String jsonPath, String studentId) {

        this.readJson(jsonPath);

        //print header
        System.out.println("==================================================================================== \nStudent Details page \n==================================================================================== \n");

        //check if it exists in all students (valid or invalid)
        if (Integer.parseInt(studentId) - 1 <= this.students.length) {
            System.out.println("Name: " + this.students[Integer.parseInt(studentId) - 1].Name + "\tGrade: " + this.students[Integer.parseInt(studentId) - 1].Grade + "\tEmail: " + this.students[Integer.parseInt(studentId) - 1].Email);
            System.out.println("------------------------------------------------------------------------------------ \nEnrolled courses.\n");
        } else {
            System.out.println("Invalid Student ID");
            return;
        }


        List studentCourses = this.studentCourses.get(studentId) ;
        if (studentCourses == null) {
            System.out.println("This student hasn't enrolled in any courses");
            System.out.println("------------------------------------------------------------------------------------");
        return ;
        }

        for (Object course : studentCourses)
            System.out.println(this.courses[Integer.parseInt(course.toString())-1]);


    }

    public void xmlToCsv(String xmlFile, String csvFile) {

        XmlMapper xmlMapper = new XmlMapper();
        List<Course> rows = null;
        try {
            rows = xmlMapper.readValue(new File(xmlFile), new TypeReference<List<Course>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            FileWriter csvWriter = new FileWriter(csvFile);
            csvWriter.append("id,CourseName,Instructor,CourseDuration,CourseTime,Location");
            csvWriter.append("\n");
            this.courses = new Course[rows.size()];
            for (int i = 0; i < rows.size(); i++) {
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
                Course temp = new Course();
                temp.id = rows.get(i).getId();
                temp.courseName = rows.get(i).getCourseName();
                temp.instructor = rows.get(i).getInstructor();
                temp.courseDuration = rows.get(i).getCourseDuration();
                temp.courseTime = rows.get(i).getCourseTime();
                temp.location = rows.get(i).getLocation();

                this.courses[i] = temp;

            }

            csvWriter.flush();
            csvWriter.close();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void textToCsv(String textFile, String csvFile) {
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
            this.students = new Student[lines.length-1];
            for (String entry : lines) {
                if (id == 0) {
                    id++;
                    continue;
                }
                String[] values = entry.split("#");

                Student temp = new Student();
                temp.id = values[0];
                temp.Name = values[1];
                temp.Grade = values[2];
                temp.Email = values[3];
                temp.Address = values[4];
                temp.Region = values[5];
                temp.Country = values[6];
                this.students[id - 1] = temp;
                int count = 0;

                id++;
                for (String value : values) {

                    if (count == 6)
                        csvWriter.append('"' + value + '"');

                    else csvWriter.append(value);

                    csvWriter.append(",");
                    count++;
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

    public void readJson(String jsonPath) {
        File json = new File(jsonPath);
        ObjectMapper mapper = new ObjectMapper();

        try {
            this.studentCourses = mapper.readValue(json, new TypeReference<Map<String, List<Integer>>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void writeJson(String jsonPath) {
        File json = new File(jsonPath);
        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(json, this.studentCourses);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void printAllCourses ()
    {
        for (Course c : this.courses)
            System.out.println(c);
        System.out.println("\n----------------------------------------------------------------------------------------------------");
    }

    public static void clearScrean ()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
