package com.proyect;

import java.sql.SQLException;

import com.proyect.MongoBD.CompilationMongoBD;
import com.proyect.Oracle.CompilationOracle;

public class Main {
    public static void main(String[] args) throws IllegalAccessException, SQLException {
        int option = 1;

        if (option == 1) {
            CompilationOracle compilationOracle = new CompilationOracle();
            compilationOracle.mainOracle();
        }
        if(option == 2){
            CompilationMongoBD compilationMongoBD = new CompilationMongoBD();
            compilationMongoBD.mainMongoBD();
        }
        if (option == 3) {
                
        }
    }
}

