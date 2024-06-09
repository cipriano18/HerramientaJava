package com.proyect.MYSQL;

public class SoccerPlayer {
   private int number;
   private String name;
   private int edad;
private AutomaticMappingMYSQL automaticMappingMYSQL;
private final String nameClass = this.getClass().getSimpleName();

    public SoccerPlayer(int number, String name, int edad) {
        this.number = number;
        this.name = name;
        this.edad = edad;
    }
    public SoccerPlayer() {
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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
    public void insert(String name, int number ,int edad){
        this.setEdad(edad);
        this.setName(name);
        this.setNumber(number);
        automaticMappingMYSQL.insertar(this);
    }
    public void setAutomaticMappingMYSQL(AutomaticMappingMYSQL automaticMappingMYSQL) {
        this.automaticMappingMYSQL = automaticMappingMYSQL;
    }
}
