package com.example.kea.repositories;

import com.example.kea.models.Course;
import com.example.kea.models.Student;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;

@Repository
public class StudentsArrayListRepository implements IStudentsRepository{

    private ArrayList<Student> students = new ArrayList<>();

    public StudentsArrayListRepository() {
        students.add(new Student(1, "Claus", "Bove", LocalDate.of(2017, 10, 5), "2210999999"));
        students.add(new Student(2, "Anna", "Bove", LocalDate.of(2017, 12, 12), "22199"));
        students.add(new Student(3, "Poul", "ffffff", LocalDate.of(2017, 2, 10), "221fadsda0999999"));
    }

    @Override
    public ArrayList<Student> readAll(){
        return students;
    }

    @Override
    public Student read(int id){
        for(Student student : students){
            if(student.getId() == id)
                return student;
        }

        return null;
    }

    @Override
    public void create(Student student){
        students.add(student);
    }

    @Override
    public void update(Student student){

    }

    @Override
    public void delete(int id){
        for(Student student : students){
            if(student.getId() == id) {
                students.remove(student);
                break;
            }
        }
    }

    @Override
    public ArrayList<Course> getCourses(int id) {
        return null;
    }
}
