package com.example.kea.repositories;

import com.example.kea.models.Course;
import com.example.kea.models.Student;
import com.example.kea.repositories.util.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EnrollmentRepository {
    private Connection conn;
    private PreparedStatement preparedStatement;
    private ResultSet result;

    public EnrollmentRepository(){
        try {
            this.conn = Database.getConnection();
        } catch (SQLException e) {
            System.out.println(e.getSQLState());
        }
    }

    //returns the students that are not enrolled in the particular course
    public ArrayList<Student> getFreeStudents(int courseID){
        ArrayList<Student> students = new ArrayList<>();
        try{
            preparedStatement = conn.prepareStatement("SELECT * FROM students WHERE id NOT IN (SELECT studentID FROM courseenrollment WHERE courseID=?)");
            preparedStatement.setInt(1, courseID);

            result = preparedStatement.executeQuery();

            while(result.next()){
                students.add(new Student(result.getInt("id"),
                        result.getString("name"),
                        result.getString("lastName"),
                        result.getDate("enrollmentDate").toLocalDate(),
                        result.getString("cpr")));
            }
            return students;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    //returns list of courses the student is not enrolled in
    public ArrayList<Course> getFreeCourses(int studentID) {
        ArrayList<Course> courses = new ArrayList<>();
        try{
            preparedStatement = conn.prepareStatement("SELECT * FROM courses WHERE id NOT IN (SELECT courseID FROM courseenrollment WHERE studentID=?)");
            preparedStatement.setInt(1, studentID);

            result = preparedStatement.executeQuery();

            while(result.next()){
                courses.add(new Course(result.getInt("id"),
                        result.getString("title"),
                        result.getDate("startDate").toLocalDate(),
                        result.getInt("ects")));
            }
            return courses;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public void enrollStudent(int studentID, int courseID){
        try{
            preparedStatement = conn.prepareStatement("INSERT INTO courseenrollment(studentID, courseID) VALUE (?, ?)");
            preparedStatement.setInt(1, studentID);
            preparedStatement.setInt(2, courseID);

            if(preparedStatement.execute()){
                System.out.println("Cannot insert into courseenrollment");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void unenroll(int studentID, int courseID){
        try{
            preparedStatement = conn.prepareStatement("DELETE FROM courseenrollment WHERE studentID=? AND courseID=?");
            preparedStatement.setInt(1, studentID);
            preparedStatement.setInt(2, courseID);

            if(preparedStatement.execute())
                System.out.println("Cannot delete");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
