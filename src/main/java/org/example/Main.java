package org.example;
import java.io.*;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class Main {
    public static void main(String[] args) {

        textToCsv("Data/student-data.txt", "Data/students.csv");
        xmlToCsv ("Data/coursedata.xml", "Data/coursedata.csv");
    }

    public static void printStudentData(String jsonFile) {

        //read json here
        //print student data
        //print courses with its data

    }
        public static void xmlToCsv(String xmlFile, String csvFile) {

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

            }

            csvWriter.flush();
            csvWriter.close();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        }

        public static void textToCsv (String textFile, String csvFile){
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
                for (String entry : lines) {
                    if (id == 0) {
                        id++;
                        continue;
                    }
                    String[] values = entry.split("#");

                    //Write each value to its own cell
                    csvWriter.append(Integer.toString(id++));
                    csvWriter.append(",");

                    int count = 0 ;
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
