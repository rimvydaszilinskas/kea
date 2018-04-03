package com.example.kea.repositories;

import com.example.kea.models.Course;
import com.example.kea.models.Student;
import com.example.kea.repositories.util.Database;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

@Repository
public class StudentsDBRepository implements IStudentsRepository{
    private ArrayList<Student> students = new ArrayList<>();
    private Connection conn;
    private PreparedStatement preparedStatement;
    private ResultSet result;

    public StudentsDBRepository(){
        try {
            this.conn = Database.getConnection();
        } catch (SQLException e) {
            System.out.println(e.getSQLState());
        }
    }

    @Override
    public ArrayList<Student> readAll() {
        try {
            students.clear();
            preparedStatement = conn.prepareStatement("SELECT * FROM students");
            result = preparedStatement.executeQuery();

            while(result.next()){
                students.add(new Student(result.getInt("id"),
                        result.getString("name"),
                        result.getString("lastname"),
                        result.getDate("enrollmentDate").toLocalDate(),
                        result.getString("cpr")));
            }
            return students;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Student read(int id) {
        try{
            preparedStatement = conn.prepareStatement("SELECT * FROM students WHERE id='" + id + "' LIMIT 1");
            result = preparedStatement.executeQuery();

            if(result.next()){
                return new Student(result.getInt("id"),
                        result.getString("name"),
                        result.getString("lastname"),
                        LocalDate.now(),
                        result.getString("cpr"));
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void create(Student student) {
        try{
            preparedStatement = conn.prepareStatement("INSERT INTO students (name, lastname, enrollmentDate, cpr) VALUES(?, ?, ?, ?)");
            preparedStatement.setString(1, student.getName());
            preparedStatement.setString(2, student.getLastName());
            preparedStatement.setDate(3, Date.valueOf(student.getEnrollmentDate()));
            preparedStatement.setString(4, student.getCpr());

            boolean result = preparedStatement.execute();

            if(result)
                System.out.println("Error creating database");

        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(Student student) {
        try{
            preparedStatement = conn.prepareStatement("UPDATE students SET name=?, lastname=?, enrollmentDate=?, cpr=? WHERE id=?");

            preparedStatement.setString(1, student.getName());
            preparedStatement.setString(2, student.getLastName());
            preparedStatement.setDate(3, Date.valueOf(student.getEnrollmentDate()));
            preparedStatement.setString(4, student.getCpr());
            preparedStatement.setInt(5, student.getId());

            boolean result = preparedStatement.execute();
            if(result)
                System.out.println("Error updating the table");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try{
            preparedStatement = conn.prepareStatement("DELETE FROM students WHERE id='" + id + "' LIMIT 1");

            boolean result = preparedStatement.execute();

            if(result)
                System.out.println("Error deleting the student");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Course> getCourses(int id){
        ArrayList<Course> courses = new ArrayList<>();
        try{
            preparedStatement = conn.prepareStatement("SELECT courseenrollment.courseID, courses.title, courses.startDate, courses.ects " +
                    "FROM courseenrollment " +
                    "INNER JOIN courses ON courseenrollment.courseID = courses.id " +
                    "WHERE courseenrollment.studentID = ?;");

            preparedStatement.setInt(1, id);
            result = preparedStatement.executeQuery();

            while(result.next()){
                courses.add(new Course(result.getInt("courseID"),
                        result.getString("title"),
                        result.getDate("startDate").toLocalDate(),
                        result.getInt("ects")));
            }
            return courses;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
