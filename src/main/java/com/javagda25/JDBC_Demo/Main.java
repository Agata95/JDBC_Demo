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

    private static final String CREATE_TABLE_QUERY = "create table if not exists `students`(\n" +
            "`id` int auto_increment,\n" +
            "`name` varchar(255) not null,\n" +
            "`age` int not null,\n" +
            "`average` double not null,\n" +
            "`alive` tinyint not null,\n" +
            "primary key(id)\n" +
            ");";

    private static final String INSERT_QUERY = "insert into " +
            "students (`name`, `age`, `average`, `alive`) " +
            "values (?, ?, ?, ?);";

    private static final String REMOVE_QUERY = "delete from students " +
            "where `id` = ?;";

    private static final String SELECT_ALL_QUERY = "select * from students;";

    private static final String SELECT_ID_QUERY = "select * from students where `id` = ?;";

    private static final String SELECT_NAME_QUERY = "select * from students where `name` like ?;";

    private static final String SELECT_AGE_QUERY = "select * from students where `age` between ? and ?;";

    private static final String DB_HOST = "localhost"; // 127.0.0.1
    private static final String DB_PORT = "3306";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "root";
    private static final String DB_NAME = "jdbc_students";

    public static void main(String[] args) {
        MysqlDataSource dataSource = new MysqlDataSource();
//        dataSource - zbiór poleceń do bazy danycyh

        dataSource.setPort(Integer.parseInt(DB_PORT));
        dataSource.setUser(DB_USERNAME);
        dataSource.setServerName(DB_HOST);
        dataSource.setPassword(DB_PASSWORD);
        dataSource.setDatabaseName(DB_NAME);

        try {
            dataSource.setServerTimezone("Europe/Warsaw");
            dataSource.setUseSSL(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Connection connection = dataSource.getConnection();
            System.out.println("Hurra!");

            try (PreparedStatement statement = connection.prepareStatement(CREATE_TABLE_QUERY)) {
                statement.execute();
            }

            Scanner scanner = new Scanner(System.in);
            String flag;

            do {
                System.out.println("What do you want to do? add/remove/list/select/quit");
                flag = scanner.nextLine();

                switch (flag) {
                    case "add":
                        Student student = createStudent();
                        insertStudent(connection, student);
                        break;
                    case "remove":
                        removeStudent(connection);
                        break;
                    case "list":
                        listOfStudents(connection);
                        break;
                    case "select":
                        String choose = selectOne();
                        chooseQuery(choose, connection);
                        break;
                }

            } while (!flag.equalsIgnoreCase("quit"));

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void chooseQuery(String choose, Connection connection) throws SQLException {
        switch (choose){
            case "id":
                listById(connection);
                 break;
            case "name":
                listByName(connection);
                break;
            case "age":
                listByAge(connection);
                break;
        }
    }

    private static void listByAge(Connection connection) throws SQLException {
        List<Student> studentList = new ArrayList<>();
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
                Student student = new Student();

                student.setId(resultSet.getLong(1));
                student.setName(resultSet.getString(2));
                student.setAge(resultSet.getInt(3));
                student.setAverage(resultSet.getDouble(4));
                student.setAlive(resultSet.getBoolean(5));

                studentList.add(student);
            }
        }
        for (Student student : studentList) {
            System.out.println(student);
        }
    }

    private static void listByName(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_NAME_QUERY)) {
            System.out.println("Which student, you want to search? (name)");
            Scanner scanner = new Scanner(System.in);
            String name = scanner.nextLine();

            statement.setString(1, "%" + name + "%");
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Student student = new Student();

                student.setId(resultSet.getLong(1));
                student.setName(resultSet.getString(2));
                student.setAge(resultSet.getInt(3));
                student.setAverage(resultSet.getDouble(4));
                student.setAlive(resultSet.getBoolean(5));

                System.out.println(student);
            } else {
                System.out.println("No students with this name.");
            }
        }
    }

    private static void listById(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ID_QUERY)) {
            System.out.println("Which student, you want to search? (id number)");
            Scanner scanner = new Scanner(System.in);
            Long id = scanner.nextLong();

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Student student = new Student();

                student.setId(resultSet.getLong(1));
                student.setName(resultSet.getString(2));
                student.setAge(resultSet.getInt(3));
                student.setAverage(resultSet.getDouble(4));
                student.setAlive(resultSet.getBoolean(5));

                System.out.println(student);
            } else {
                System.out.println("No students with this id.");
            }
        }
    }

    private static String selectOne() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose how to you want search your students? id/name/age");
        return scanner.nextLine();
    }

    private static void listOfStudents(Connection connection) throws SQLException {
        List<Student> studentList = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY)) {

//          ResultSet szuka czy jest rekord, po wykonaniu next() przechodzi do pierwszego rekordu,
//          następny next() przechodzi do następnego rekordu

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Student student = new Student();

                student.setId(resultSet.getLong(1));
                student.setName(resultSet.getString(2));
                student.setAge(resultSet.getInt(3));
                student.setAverage(resultSet.getDouble(4));
                student.setAlive(resultSet.getBoolean(5));

                studentList.add(student);
            }
        }
        for (Student student : studentList) {
            System.out.println(student);
        }

    }

    private static void removeStudent(Connection connection) throws SQLException {
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

    private static Student createStudent() {
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

    private static void insertStudent(Connection connection, Student student) throws SQLException {
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
