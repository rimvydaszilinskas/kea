package com.example.kea.controller;

import com.example.kea.models.Course;
import com.example.kea.models.Student;
import com.example.kea.repositories.EnrollmentRepository;
import com.example.kea.repositories.ICoursesRepository;
import com.example.kea.repositories.CourseArrayRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
public class CoursesController {

    private ArrayList<Course> courses = new ArrayList<>();

    private ICoursesRepository courseRepo;
    private EnrollmentRepository eRepo;

    public CoursesController(){
        courseRepo = new CourseArrayRepository();
        eRepo = new EnrollmentRepository();
    }

    @GetMapping("/courses")
    public String index(Model model){
        courses = courseRepo.readAll();
        model.addAttribute("course", courses);
        return "courses/index";
    }

    @GetMapping("/courses/edit")
    public String edit(@RequestParam("id") int id, Model model){
        model.addAttribute("course", courseRepo.read(id));
        return "courses/edit";
    }

    @PostMapping("/courses/edit")
    public String edit(@ModelAttribute Course course){
        courseRepo.update(course);
        return "redirect:/courses";
    }

    @GetMapping("/courses/delete")
    public String delete(@RequestParam("id") int id, Model model){
        model.addAttribute("course", courseRepo.read(id));
        return "courses/delete";
    }

    @PostMapping("/courses/delete")
    public String delete(@ModelAttribute Course course){
        courseRepo.delete(course.getId());
        return "redirect:/courses";
    }

    @GetMapping("/courses/details")
    public String details(@RequestParam("id") int id, Model model){
        model.addAttribute("course", courseRepo.read(id));
        model.addAttribute("student", courseRepo.enrolledStudents(id));
        return "courses/details";
    }

    @GetMapping("/courses/create")
    public String create(){
        return "courses/create";
    }

    @PostMapping("/courses/create")
    public String create(@ModelAttribute Course course){
        courseRepo.create(course);
        return "redirect:/courses";
    }

    @GetMapping("courses/enroll")
    public String enrollStudents(@RequestParam("id")int id, Model model){
        ArrayList<Student> freeStudents = eRepo.getFreeStudents(id);
        model.addAttribute("students", freeStudents);
        model.addAttribute("course", courseRepo.read(id));
        return "courses/enroll";
    }
}
