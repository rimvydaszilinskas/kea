package com.example.kea.repositories;

import com.example.kea.models.Course;
import com.example.kea.models.Student;
import com.example.kea.repositories.util.Database;

import java.sql.*;
import java.util.ArrayList;

public class CourseArrayRepository implements ICoursesRepository {

    private ArrayList<Course> courses = new ArrayList<>();
    private Connection conn;
    private PreparedStatement preparedStatement;
    private ResultSet result;

    public CourseArrayRepository(){
        try{
            this.conn = Database.getConnection();
        } catch(SQLException e){
            System.out.println(e.getSQLState());
        }
    }

    @Override
    public ArrayList<Course> readAll() {
        try{
            courses.clear();
            preparedStatement = conn.prepareStatement("SELECT * FROM courses");
            result = preparedStatement.executeQuery();

            while(result.next()){
                courses.add(new Course(result.getInt("id"),
                        result.getString("title"),
                        result.getDate("startDate").toLocalDate(),
                        result.getInt("ects")));
            }
            return courses;
        } catch(SQLException e){
            System.out.println(e.getSQLState());
        }
        return null;
    }

    @Override
    public Course read(int id) {
        try{
            preparedStatement = conn.prepareStatement("SELECT * FROM courses WHERE id=?");
            preparedStatement.setInt(1, id);
            result = preparedStatement.executeQuery();

            if(result.next()){
                return new Course(result.getInt("id"),
                        result.getString("title"),
                        result.getDate("startDate").toLocalDate(),
                        result.getInt("ects"));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void create(Course course) {
        try{
            preparedStatement = conn.prepareStatement("INSERT INTO courses(title, startDate, ects) VALUES (?, ?, ?)");
            preparedStatement.setString(1, course.getTitle());
            preparedStatement.setDate(2, Date.valueOf(course.getStartDate()));
            preparedStatement.setInt(3, course.getEtcs());

            if(preparedStatement.execute())
                System.out.println("Cannot execute query");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(Course course) {
        try{
            preparedStatement = conn.prepareStatement("UPDATE courses SET title=?, startDate=?, ects=? WHERE id=?");
            preparedStatement.setString(1, course.getTitle());
            preparedStatement.setDate(2, Date.valueOf(course.getStartDate()));
            preparedStatement.setInt(3, course.getEtcs());
            preparedStatement.setInt(4, course.getId());

            boolean result = preparedStatement.execute();
            if(result)
                System.out.println("Error updating table");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try{
            preparedStatement = conn.prepareStatement("DELETE FROM courses WHERE id=?");
            preparedStatement.setInt(1, id);
            boolean result = preparedStatement.execute();

            if(result)
                System.out.println("Error deleting course");
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Student> enrolledStudents(int id){
        try{
            ArrayList<Student> students = new ArrayList<>();
            preparedStatement = conn.prepareStatement("SELECT courseenrollment.studentID, students.name, students.lastname," +
                    "students.enrollmentDate, students.cpr FROM courseenrollment " +
                    "INNER JOIN students ON courseenrollment.studentID = students.id " +
                    "WHERE courseenrollment.courseid = ?;");
            preparedStatement.setInt(1, id);
            result = preparedStatement.executeQuery();

            if(result.next()){
                students.add(new Student(result.getInt("studentID"),
                        result.getString("name"),
                        result.getString("lastname"),
                        result.getDate("enrollmentDate").toLocalDate(),
                        result.getString("cpr")));
                System.out.println(result.getInt("studentID"));
            }
            return students;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Integer> enrolledStudentsID(int id){
        ArrayList<Integer> studentIDs = new ArrayList<>();
        try{
            preparedStatement = conn.prepareStatement("SELECT studentID FROM courseenrollment WHERE courseID = ?");
            preparedStatement.setInt(1, id);

            result = preparedStatement.executeQuery();

            while (result.next()){
                studentIDs.add(result.getInt("studentID"));
            }
            return studentIDs;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
