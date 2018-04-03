package com.example.kea.repositories;

import com.example.kea.models.Course;
import com.example.kea.models.Student;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface IStudentsRepository {

    ArrayList<Student> readAll();
    Student read(int id);
    void create(Student student);
    void update(Student student);
    void delete(int id);
    ArrayList<Course> getCourses(int id);

}
