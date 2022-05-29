package com.example.herokutestpostgresql.jdbc;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.example.herokutestpostgresql.entity.User;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UserJdbc {
    private final String host = "ec2-54-170-90-26.eu-west-1.compute.amazonaws.com";
    private final String database = "d85vtd6vldr07o";
    private final int port = 5432;
    private final String userName = "qvoltiklgrbbfl";
    private final String password = "0f187774cb9f544e53d392c85bd48582d6b491219b622563f313d1999798a8ae";
    private String url = "jdbc:postgresql://%s:%d/%s";

    private JdbcTemplate jdbcTemplate;

    public UserJdbc(Context context) {
        this.url = String.format(this.url, this.host, this.port, this.database);
        ConnectThread connectThread = new ConnectThread();
        connectThread.start();
        try {
            connectThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<User> select() {
        SelectThread selectThread = new SelectThread();
        selectThread.start();
        try {
            selectThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return selectThread.getUsers();
    }

    public User search(Integer id) {
        SearchThread searchThread = new SearchThread(id);
        searchThread.start();
        try {
            searchThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return searchThread.getUser();
    }

    public User search(String login, String password) {
        SearchThread searchThread = new SearchThread(login, password);
        searchThread.start();
        try {
            searchThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return searchThread.getUser();
    }

    public void insert(User user) {
        InsertThread insertThread = new InsertThread(user);
        insertThread.start();
        try {
            insertThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void insert(String email, String password) {
        InsertThread insertThread = new InsertThread(email, password);
        insertThread.start();
        try {
            insertThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Integer id, User user) {
        UpdateThread updateThread = new UpdateThread(id, user);
        updateThread.start();
        try {
            updateThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Integer id) {
        DeleteThread deleteThread = new DeleteThread(id);
        deleteThread.start();
        try {
            deleteThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
//--CLASS INSERT THREAD -------------------------------------------------------------------------------------
    class InsertThread extends Thread{
        private String login;
        private String password;
        private String email;

        InsertThread(String email, String password){
            this.login = email;
            this.password = password;
            this.email = email;
        }

        InsertThread(User user){
            this.login = user.getLogin();
            this.password = user.getPassword();
            this.email = user.getEmail();
        }

        @Override
        public void run() {
            try {
                jdbcTemplate.update("INSERT INTO \"USERS\" VALUES(DEFAULT, ?, ?, ?)", email, password, email);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("UserJdbc", e.getMessage() + "\ninsert");
            }
        }
    }
//--CLASS CONNECTION THREAD ---------------------------------------------------------------------------------
    class ConnectThread extends Thread{
        @Override
        public void run() {
            try {
                DriverManagerDataSource dataSource = new DriverManagerDataSource();
                dataSource.setDriverClassName("org.postgresql.Driver");
                dataSource.setUrl(url);
                dataSource.setUsername(userName);
                dataSource.setPassword(password);
                jdbcTemplate = new JdbcTemplate(dataSource);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("UserJdbc", e.getMessage() + "\nconnection");
            }
        }
    }
//--CLASS UPDATE THREAD ------------------------------------------------------------------------------------
    class UpdateThread extends Thread{
        private User user;
        private Integer id;

        UpdateThread(Integer id, User user){
            this.id = id;
            this.user = user;
        }

        @Override
        public void run() {
            try {
                jdbcTemplate.update("UPDATE \"USERS\" SET \"LOGIN\"=?, \"PASSWORD\"=?, \"EMAIL\"=? WHERE \"ID\"=?",
                        user.getLogin(), user.getPassword(), user.getEmail(), id);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("UserJdbc", e.getMessage() + "\nupdate");
            }
        }
    }
//--CLASS DELETE THREAD ------------------------------------------------------------------------------------
    class DeleteThread extends Thread{
        private Integer id;

        DeleteThread(Integer id){
            this.id = id;
        }

        @Override
        public void run() {
            try {
                jdbcTemplate.update("DELETE FROM \"USERS\" WHERE \"ID\"=?", id);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("UserJdbc", e.getMessage() + "\ndelete");
            }
        }
    }
//--CLASS SEARCH THREAD ------------------------------------------------------------------------------------
    class SearchThread extends Thread {
        private User user;
        private Integer id;
        private String login;
        private String password;
        private boolean logBool;

        SearchThread(Integer id){
            this.id = id;
            this.user = null;
        }

        SearchThread(String login, String password){
            this.logBool = true;
            this.login = login;
            this.password = password;
            this.user = null;
        }

        @Override
        public void run() {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    if (logBool)
                        user = jdbcTemplate.query("SELECT * FROM \"USERS\" WHERE (\"LOGIN\"=? AND \"PASSWORD\"=?)", new Object[]{login, password}, new UserMapper())
                                .stream().findAny().orElse(null);
                    else
                        user = jdbcTemplate.query("SELECT * FROM \"USERS\" WHERE \"ID\"=?", new Object[]{id}, new UserMapper())
                                .stream().findAny().orElse(null);
                else
                    user = null;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("UserJdbc", e.getMessage() + "\nsearch");
            }
        }

        public User getUser(){
            return user;
        }
    }
//--CLASS SELECT THREAD ------------------------------------------------------------------------------------
    class SelectThread extends Thread {
        private List<User> listUsers;

        @Override
        public void run(){
            try {
                listUsers = jdbcTemplate.query( "SELECT * FROM \"USERS\"", new UserMapper());
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("UserJdbc", e.getMessage() + "\nselect");
            }
        }

        public List<User> getUsers(){
            return listUsers;
        }
    }
//--CLASS USER MAPPER ------------------------------------------------------------------------------------
    private static final class UserMapper implements RowMapper<User> {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("ID"));
            user.setLogin(rs.getString("LOGIN"));
            user.setPassword(rs.getString("PASSWORD"));
            user.setEmail(rs.getString("EMAIL"));
            return user;
        }
    }
}
