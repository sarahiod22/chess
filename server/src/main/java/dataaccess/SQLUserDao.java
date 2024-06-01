package dataaccess;

import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.ResponseException;
import org.mindrot.jbcrypt.*;
import model.UserData;
import java.sql.*;
import static dataaccess.DatabaseManager.*;

public class SQLUserDao implements UserDao{

    private static final String[] CREATE_TABLE_STMT = {
            """
            CREATE TABLE IF NOT EXISTS  userData (
              `username` varchar(255) NOT NULL,
              `password` varchar(255) NOT NULL,
              `email` varchar(255) NOT NULL,
              PRIMARY KEY (`username`)
            )"""
    };

    public SQLUserDao() {
        try {
            configureDatabase(CREATE_TABLE_STMT);
        } catch (DataAccessException e){
            throw new RuntimeException("Unable to create user's table", e);
        }
    }

    @Override
    public void createUser(UserData newUser) throws ResponseException {
        try {
            String insertStatement = "INSERT INTO userData (username, password, email) VALUES (?, ?, ?)";
            DatabaseManager.executeUpdate(insertStatement, newUser.username(), encryptPassword(newUser.password()), newUser.email());
        }catch (DataAccessException e){
            throw new ResponseException(500, "Error: " + e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws ResponseException {
        try (var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement( "SELECT * FROM userData WHERE username = ?")){
            stmt.setString(1, username);
            var rs = stmt.executeQuery();
            if(rs.next()){
                return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
            } else {
                return null;
            }
        }catch (SQLException | DataAccessException e){
            throw new ResponseException(500, "Error: " + e.getMessage());
        }
    }

    @Override
    public void clear() throws ResponseException {
        try (var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement("DELETE FROM userData")) {
            stmt.executeUpdate();
        } catch (SQLException | DataAccessException e){
            throw new ResponseException(500, "Error: " + e.getMessage());
        }

    }

    private String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

}
