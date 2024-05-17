package com.proyect;

public class Student {
private String name;
private int ID;

public Student(String name, int iD) {
    this.name = name;
    this.ID  = iD;
}
public String getName() {
    return name;
}
public void setName(String name) {
    this.name = name;
}
public int getID() {
    return ID;
}
public void setID(int iD) {
    ID = iD;
}
@Override
public String toString() {
    return "Student  nombre " + name + " Cedula " + ID ;
}
}