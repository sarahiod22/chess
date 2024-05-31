package dataaccess;

import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.ResponseException;
import org.mindrot.jbcrypt.*;
import model.UserData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static dataaccess.DatabaseManager.configureDatabase;

public class MySQLUserDao implements UserDao{

    private static final String[] createTableStatements = {
            """
            CREATE TABLE IF NOT EXISTS  userData (
              `username` varchar(255) NOT NULL,
              `password` varchar(255) NOT NULL,
              `email` varchar(255) NOT NULL,
              PRIMARY KEY (`username`)
            )"""
    };

    public MySQLUserDao() {
        try {
            configureDatabase(createTableStatements);
        } catch (DataAccessException e){
            throw new RuntimeException("Unable to create user's table", e);
        }
    }

    @Override
    public void createUser(UserData newUser) throws ResponseException, DataAccessException {
        String insertStatement = "INSERT INTO userData (username, password, email) VALUES (?, ?, ?)";
        DatabaseManager.executeUpdate(insertStatement, newUser.username(), encryptPassword(newUser.password()), newUser.email());
    }

    @Override
    public UserData getUser(String username) throws ResponseException {
        String getStatement = "SELECT * FROM userData WHERE username = ?";
        try (var conn = DatabaseManager.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(getStatement);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
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
        String deleteStatement = "DELETE FROM userData";
        try (var conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(deleteStatement);
            stmt.executeUpdate();
        } catch (SQLException | DataAccessException e){
            throw new ResponseException(500, "Error: " + e.getMessage());
        }

    }

    private String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    //move it elsewhere?
    public boolean verifyUserPassword(String username, String providedPassword) throws ResponseException {
        var hashedPassword = getUser(username).password();
        return BCrypt.checkpw(providedPassword, hashedPassword);
    }

}
