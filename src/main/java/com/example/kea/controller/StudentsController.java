package com.example.kea.controller;

import com.example.kea.models.Course;
import com.example.kea.models.Student;
import com.example.kea.repositories.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
public class StudentsController {

    private ArrayList<Student> students = new ArrayList<>();
    private EnrollmentRepository eRepo;

    private IStudentsRepository studentsRepository;

    public StudentsController() {

        // Change the "new object" to the repository you want to use
        // (e.g StudentsFileRepository() or StudentsArrayListRepository())
        studentsRepository = new StudentsDBRepository(); //StudentsArrayListRepository();
        eRepo = new EnrollmentRepository();
    }


    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("stu", studentsRepository.readAll());
        return "index";
    }

    @GetMapping("/create")
    public String create() {

        return "create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Student student) {

        studentsRepository.create(student);
        return "redirect:/";

    }

    @GetMapping("/details")
    public String details(@RequestParam("id") int id, Model model) {
        ArrayList<Course> courses = studentsRepository.getCourses(id);
        Student student = studentsRepository.read(id);
        model.addAttribute("student", student);
        model.addAttribute("courses", courses);
        return "details";
    }

    @GetMapping("/update")
    public String update(@RequestParam("id") int id, Model model) {

        Student student = studentsRepository.read(id);
        model.addAttribute("student", student);
        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Student student) {

        studentsRepository.update(student);
        return "redirect:/";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") int id, Model model) {

        Student student = studentsRepository.read(id);
        model.addAttribute("student", student);

        return "delete";
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute Student student) {

        studentsRepository.delete(student.getId());
        return "redirect:/";
    }

    @GetMapping("/enroll")
    public String enroll(@RequestParam("id") int id, Model model){
        ArrayList<Course> courses = eRepo.getFreeCourses(id);
        model.addAttribute("courses", courses);
        model.addAttribute("student", studentsRepository.read(id));
        return "enroll";
    }

    @RequestMapping(path="/enrollStud/{studentID}/{courseID}", method = RequestMethod.GET)
    public String enroll(@PathVariable int studentID, @PathVariable int courseID){
        eRepo.enrollStudent(studentID, courseID);
        return "redirect:/enroll?id=" + studentID;
    }

    @RequestMapping(path="/enrollStud/{studentID}/{courseID}/{courseIDRe}", method = RequestMethod.GET)
    public String enroll(@PathVariable int studentID, @PathVariable int courseID, @PathVariable int courseIDRe){
        eRepo.enrollStudent(studentID, courseID);
        return "redirect:/courses/enroll?id=" + courseIDRe;
    }

    @RequestMapping(path="/unenroll/{courseID}/{studentID}", method = RequestMethod.GET)
    public String unenroll(@PathVariable int studentID, @PathVariable int courseID){
        eRepo.unenroll(studentID, courseID);
        return "redirect:/details?id=" + studentID;
    }

    @RequestMapping(path="/unenroll/{courseID}/{studentID}/{courseIDredirect}", method = RequestMethod.GET)
    public String unenroll(@PathVariable int studentID, @PathVariable int courseID, @PathVariable int courseIDredirect){
        eRepo.unenroll(studentID, courseID);
        return "redirect:/courses/details?id=" + courseIDredirect;
    }

}