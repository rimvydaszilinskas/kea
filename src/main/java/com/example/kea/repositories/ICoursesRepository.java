package com.example.kea.repositories;

import com.example.kea.models.Course;
import com.example.kea.models.Student;

import java.util.ArrayList;

public interface ICoursesRepository {
    ArrayList<Course> readAll();
    Course read(int id);
    void create(Course course);
    void update(Course course);
    void delete(int id);
    ArrayList<Student> enrolledStudents(int id);
    ArrayList<Integer> enrolledStudentsID(int id);
}
