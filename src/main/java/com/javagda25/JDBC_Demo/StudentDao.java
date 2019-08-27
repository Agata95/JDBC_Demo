package com.javagda25.JDBC_Demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.javagda25.JDBC_Demo.StudentQueries.*;

public class StudentDao {
    // data access object - dostęp do danych przez tę klasę

    private MysqlConnection mysqlConnection;

    public StudentDao() throws SQLException {
        mysqlConnection = new MysqlConnection();

        createTableIfNoExists();
    }

    private void createTableIfNoExists() throws SQLException {
        try (Connection connection = mysqlConnection.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(CREATE_TABLE_QUERY)) {
                statement.execute();
            }
        }
    }

    public Student createStudent() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Student's name:");
        String name = scanner.nextLine();

        System.out.println("Student's age:");
        int age = scanner.nextInt();

        System.out.println("Student's average:");
        Double average = scanner.nextDouble();

        System.out.println("Is alive? true/false");
        Boolean alive = scanner.hasNext();

        return new Student(name, age, average, alive);
    }

    public List<Student> listByAge() throws SQLException {
        List<Student> studentList = new ArrayList<>();
        try (Connection connection = mysqlConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SELECT_AGE_QUERY)) {
                System.out.println("Which student, you want to search? (between X and X)");
                Scanner scanner = new Scanner(System.in);
                System.out.println("Between:");
                int age1 = scanner.nextInt();
                System.out.println("And:");
                int age2 = scanner.nextInt();

                statement.setInt(1, age1);
                statement.setInt(2, age2);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    Student student = getStudents(resultSet);
                    studentList.add(student);
                }
            }
            for (Student student : studentList) {
                System.out.println(student);
            }
        }
        return studentList;
    }

    public void listByName() throws SQLException {
        try (Connection connection = mysqlConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SELECT_NAME_QUERY)) {
                System.out.println("Which student, you want to search? (name)");
                Scanner scanner = new Scanner(System.in);
                String name = scanner.nextLine();

                statement.setString(1, "%" + name + "%");
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    Student student = getStudents(resultSet);
                    System.out.println(student);
                } else {
                    System.out.println("No students with this name.");
                }
            }
        }
    }

    public void listById() throws SQLException {
        try (Connection connection = mysqlConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SELECT_ID_QUERY)) {
                System.out.println("Which student, you want to search? (id number)");
                Scanner scanner = new Scanner(System.in);
                Long id = scanner.nextLong();

                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    Student student = getStudents(resultSet);
                    System.out.println(student);
                } else {
                    System.out.println("No students with this id.");
                }
            }
        }
    }

    public List<Student> listOfStudents() throws SQLException {
        List<Student> studentList = new ArrayList<>();

        try (Connection connection = mysqlConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY)) {

//          ResultSet szuka czy jest rekord, po wykonaniu next() przechodzi do pierwszego rekordu,
//          następny next() przechodzi do następnego rekordu

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    Student student = getStudents(resultSet);
                    studentList.add(student);
                }
            }
            for (Student student : studentList) {
                System.out.println(student);
            }
        }
        return studentList;
    }

    private Student getStudents(ResultSet resultSet) throws SQLException {
        Student student = new Student();

        student.setId(resultSet.getLong(1));
        student.setName(resultSet.getString(2));
        student.setAge(resultSet.getInt(3));
        student.setAverage(resultSet.getDouble(4));
        student.setAlive(resultSet.getBoolean(5));
        return student;
    }

    public void removeStudent() throws SQLException {
//        polecenie try() musi tu być użyte, aby po wykonaniu dostęp się zamknął
//        connection is closable
        try (Connection connection = mysqlConnection.getConnection()) {

            System.out.println("Which student, you want to remove? (id number)");
            Scanner scanner = new Scanner(System.in);
            Long id = scanner.nextLong();

            try (PreparedStatement statement = connection.prepareStatement(REMOVE_QUERY)) {
                statement.setLong(1, id);

                boolean success = statement.execute();

                if (success) {
                    System.out.println("Sukces!");
                }
            }
        }
    }

    public void insertStudent(Student student) throws SQLException {
        try (Connection connection = mysqlConnection.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
                statement.setString(1, student.getName());
                statement.setInt(2, student.getAge());
                statement.setDouble(3, student.getAverage());
                statement.setBoolean(4, student.isAlive());

                boolean success = statement.execute();

                if (success) {
                    System.out.println("Sukces!");
                }

            }
        }
    }

}
