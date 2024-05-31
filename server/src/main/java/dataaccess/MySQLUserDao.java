package dataaccess;

import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.ResponseException;
import org.mindrot.jbcrypt.*;
import model.UserData;

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
        return null;
    }

    @Override
    public void clear() throws ResponseException {

    }

    private String encryptPassword(String password) {
        BCrypt encoder = new BCrypt();
        String hashedPassword = encoder.hashpw(password, BCrypt.gensalt());
        return hashedPassword;
    }

    //move it elsewhere?
    public boolean verifyUserPassword(String username, String providedPassword) throws ResponseException {
        UserData userData = getUser(username);
        BCrypt encoder = new BCrypt();
        var hashedPassword = userData.password();
        return encoder.checkpw(providedPassword, hashedPassword);
    }

}
