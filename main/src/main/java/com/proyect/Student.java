package com.proyect;
import java.util.List;
import java.util.Map;
public class Student {
    private String name;
    private int age;
    private final String nameClass = this.getClass().getSimpleName();
    private AutomaticMapping automaticMapping;

    public Student() {
    }

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student nombre " + name + " Edad " + age;
    }
    public void insert(String name, int age) {
        this.setName(name);
        this.setAge(age);
        automaticMapping.mapClassToTable(this);
    }
    public void delete(int id) {
        automaticMapping.deleteById(nameClass, id);
    }

    public void searchById(int id) {
        automaticMapping.searchById(nameClass, id);
    }

    public void update(int id) {
        automaticMapping.updateTable(nameClass, id, this);
    }

    public void retrieveAll() {
        List<Map<String, Object>> results = automaticMapping.recuperarDeTabla(nameClass);
        for (Map<String, Object> row : results) {
            System.out.println(row);
        }
    }

    public void setAutomaticMapping(AutomaticMapping automaticMapping) {
        this.automaticMapping = automaticMapping;
    }
}