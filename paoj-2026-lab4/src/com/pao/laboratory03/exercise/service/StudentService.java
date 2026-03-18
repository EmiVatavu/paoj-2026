package com.pao.laboratory03.exercise.service;

import com.pao.laboratory03.exercise.exception.StudentNotFoundException;
import com.pao.laboratory03.exercise.model.Student;
import com.pao.laboratory03.exercise.model.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentService {
    private static StudentService instance;
    private List<Student> students;

    private StudentService() {
        this.students = new ArrayList<>();
    }

    public static StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }

    public void addStudent(String name, int age) {
        for (Student s : students) {
            if (s.getName().equals(name)) {
                throw new RuntimeException("Studentul există deja!");
            }
        }
        students.add(new Student(name, age));
    }

    public Student findByName(String name) {
        for (Student s : students) {
            if (s.getName().equals(name)) return s;
        }
        throw new StudentNotFoundException("Studentul " + name + " nu a fost găsit.");
    }

    public void addGrade(String studentName, Subject subject, double grade) {
        findByName(studentName).addGrade(subject, grade);
    }

    public void printAllStudents() {
        if (students.isEmpty()) {
            System.out.println("Nu există studenți.");
            return;
        }
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            System.out.println((i + 1) + ". " + s);
            for (Map.Entry<Subject, Double> entry : s.getGrades().entrySet()) {
                System.out.println("   " + entry.getKey().name() + " = " + entry.getValue());
            }
        }
    }

    public void printTopStudents() {
        List<Student> sorted = new ArrayList<>(students);
        sorted.sort((s1, s2) -> Double.compare(s2.getAverage(), s1.getAverage()));
        System.out.println("=== Top studenți ===");
        for (int i = 0; i < sorted.size(); i++) {
            System.out.printf("%d. %s — media: %.2f%n", (i + 1), sorted.get(i).getName(), sorted.get(i).getAverage());
        }
    }

    public Map<Subject, Double> getAveragePerSubject() {
        Map<Subject, Double> result = new HashMap<>();
        for (Subject sub : Subject.values()) {
            double sum = 0;
            int count = 0;
            for (Student s : students) {
                if (s.getGrades().containsKey(sub)) {
                    sum += s.getGrades().get(sub);
                    count++;
                }
            }
            if (count > 0) result.put(sub, sum / count);
        }
        return result;
    }
}