package com.proyect;

public class person {
    private String nameClass = this.getClass().getSimpleName();
    private String name;
    private int edad;
    private AutomaticMappingMongo automaticMappingMongo;

    public person (){

    }
    
    public person(String name, int edad) {
        this.name = name;
        this.edad = edad;
    }
    public person(AutomaticMappingMongo automaticMappingMongo) {
        this.automaticMappingMongo = automaticMappingMongo;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getEdad() {
        return edad;
    }
    public void setEdad(int edad) {
        this.edad = edad;
    }
    public void insert() {
        automaticMappingMongo.insert(this, nameClass);
    }
}
