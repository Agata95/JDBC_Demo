package com.javagda25.JDBC_Demo;

public interface StudentQueries {
    String CREATE_TABLE_QUERY = "create table if not exists `students`(\n" +
            "`id` int auto_increment,\n" +
            "`name` varchar(255) not null,\n" +
            "`age` int not null,\n" +
            "`average` double not null,\n" +
            "`alive` tinyint not null,\n" +
            "primary key(id)\n" +
            ");";

    String INSERT_QUERY = "insert into " +
            "students (`name`, `age`, `average`, `alive`) " +
            "values (?, ?, ?, ?);";

    String REMOVE_QUERY = "delete from students " +
            "where `id` = ?;";

    String SELECT_ALL_QUERY = "select * from students;";

    String SELECT_ID_QUERY = "select * from students where `id` = ?;";

    String SELECT_NAME_QUERY = "select * from students where `name` like ?;";

    String SELECT_AGE_QUERY = "select * from students where `age` between ? and ?;";


}
