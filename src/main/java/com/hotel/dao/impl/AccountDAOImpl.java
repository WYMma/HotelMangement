package com.hotel.dao.impl;

import com.hotel.dao.AccountDAO;
import com.hotel.model.Account;
import com.hotel.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAOImpl implements AccountDAO {
    @Override
    public Account findById(int id) throws SQLException {
        String sql = "SELECT * FROM account WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccount(rs);
                }
            }
        }
        return null;
    }

    @Override
    public Account findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM account WHERE username = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccount(rs);
                }
            }
        }
        return null;
    }

    @Override
    public Account findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM account WHERE email = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccount(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Account> findAllAgents() throws SQLException {
        List<Account> agents = new ArrayList<>();
        String sql = "SELECT * FROM account WHERE role = 'agent' AND active = true";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                agents.add(mapResultSetToAccount(rs));
            }
        }
        return agents;
    }

    @Override
    public boolean authenticate(String username, String password) throws SQLException {
        Account account = findByUsername(username);
        return account != null && account.isActive() && password.equals(account.getPassword());
    }

    @Override
    public void create(Account account) throws SQLException {
        String sql = "INSERT INTO account (username, password, email, first_name, last_name, phone, role, active) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword()); // Store password directly for now
            stmt.setString(3, account.getEmail());
            stmt.setString(4, account.getFirstName());
            stmt.setString(5, account.getLastName());
            stmt.setString(6, account.getPhone());
            stmt.setString(7, account.getRole());
            stmt.setBoolean(8, account.isActive());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    account.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(Account account) throws SQLException {
        String sql = "UPDATE account SET email = ?, first_name = ?, last_name = ?, phone = ?, active = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, account.getEmail());
            stmt.setString(2, account.getFirstName());
            stmt.setString(3, account.getLastName());
            stmt.setString(4, account.getPhone());
            stmt.setBoolean(5, account.isActive());
            stmt.setInt(6, account.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void updatePassword(int id, String newPassword) throws SQLException {
        String sql = "UPDATE accounts SET password = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM accounts WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public void setActive(int id, boolean active) throws SQLException {
        String sql = "UPDATE accounts SET is_active = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, active);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setId(rs.getInt("id"));
        account.setUsername(rs.getString("username"));
        account.setPassword(rs.getString("password"));
        account.setEmail(rs.getString("email"));
        account.setFirstName(rs.getString("first_name"));
        account.setLastName(rs.getString("last_name"));
        account.setPhone(rs.getString("phone"));
        account.setRole(rs.getString("role"));
        account.setActive(rs.getBoolean("active"));
        return account;
    }
}
