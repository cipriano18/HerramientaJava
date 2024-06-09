package com.proyect;

import com.proyect.MYSQL.CompilationMYSQL;
import java.sql.SQLException;

import com.proyect.MongoBD.CompilationMongoBD;
import com.proyect.Oracle.CompilationOracle;

public class Main {
    public static void main(String[] args) throws IllegalAccessException, SQLException {
        int option = 3;

        if (option == 1) {
            CompilationOracle compilationOracle = new CompilationOracle();
            compilationOracle.mainOracle();
        }
        if(option == 2){
            CompilationMongoBD compilationMongoBD = new CompilationMongoBD();
            compilationMongoBD.mainMongoBD();
        }
        if (option == 3) {
           CompilationMYSQL compilationMYSQL = new CompilationMYSQL();
           compilationMYSQL.mainMYSQL();
        }
    }
}

