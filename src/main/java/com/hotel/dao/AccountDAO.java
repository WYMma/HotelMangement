package com.hotel.dao;

import com.hotel.model.Account;
import java.sql.SQLException;
import java.util.List;

public interface AccountDAO {
    Account findById(int id) throws SQLException;
    Account findByUsername(String username) throws SQLException;
    Account findByEmail(String email) throws SQLException;
    List<Account> findAllAgents() throws SQLException;
    boolean authenticate(String username, String password) throws SQLException;
    void create(Account account) throws SQLException;
    void update(Account account) throws SQLException;
    void updatePassword(int id, String newPassword) throws SQLException;
    void delete(int id) throws SQLException;
    void setActive(int id, boolean active) throws SQLException;
}
