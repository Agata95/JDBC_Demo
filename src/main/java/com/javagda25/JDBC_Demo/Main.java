package com.javagda25.JDBC_Demo;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

//  1. Stworzenie bazy danych (schema): jdbc_students
//  2. Tworzenie tabel i wypełnianie danymi

    public static void main(String[] args) {
        try {
            StudentDao studentDao = new StudentDao();
            questions(studentDao);

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private static void questions(StudentDao studentDao) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        String flag;

        do {
            System.out.println("What do you want to do? add/remove/list/select/quit");
            flag = scanner.nextLine();

            switch (flag) {
                case "add":
                    Student student = studentDao.createStudent();
                    studentDao.insertStudent(student);
                    break;
                case "remove":
                    studentDao.removeStudent();
                    break;
                case "list":
                    studentDao.listOfStudents();
                    break;
                case "select":
                    String choose = selectOne();
                    chooseQuery(studentDao, choose);
                    break;
            }

        } while (!flag.equalsIgnoreCase("quit"));
    }

    public static void chooseQuery(StudentDao studentDao, String choose) throws SQLException {
        switch (choose) {
            case "id":
                studentDao.listById();
                break;
            case "name":
                studentDao.listByName();
                break;
            case "age":
                studentDao.listByAge();
                break;
        }
    }

    public static String selectOne() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose how to you want search your students? id/name/age");
        return scanner.nextLine();
    }
}
