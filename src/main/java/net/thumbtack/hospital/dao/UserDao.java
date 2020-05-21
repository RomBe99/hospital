package net.thumbtack.hospital.dao;

public interface UserDao {
    int login(String sessionId, String login, String password);

    void logout(String sessionId);

    int hasPermissions(String sessionId);
}