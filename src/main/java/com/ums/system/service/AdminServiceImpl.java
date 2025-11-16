package com.ums.system.service;

import com.ums.system.dao.AdminDAOImpl;
import com.ums.system.model.Admin;
import com.ums.system.utils.ValidationUtil;

import java.sql.Connection;
import java.util.List;

public class AdminServiceImpl implements AdminService {
    private final AdminDAOImpl adminDAO;

    public AdminServiceImpl(Connection connection) {
        this.adminDAO = new AdminDAOImpl(connection);
    }

    @Override
    public boolean addAdmin(Admin admin) {
        if (!ValidationUtil.isValidEmail(admin.getEmail())) {
            System.out.println("Invalid email format! Please provide a valid email address (e.g., user@example.com)");
            return false;
        }

        if (!ValidationUtil.isValidPassword(admin.getPassword())) {
            System.out.println("Password does not meet security requirements!");
            System.out.println(ValidationUtil.getPasswordRequirements());
            return false;
        }

        Admin existing = adminDAO.getByEmail(admin.getEmail());
        if (existing != null) {
            System.out.println("Admin with email " + admin.getEmail() + " already exists.");
            return false;
        }

        adminDAO.insert(admin);
        Admin created = adminDAO.getByEmail(admin.getEmail());
        return created != null;
    }

    @Override
    public void updateAdmin(Admin admin) {
        adminDAO.update(admin);
    }

    @Override
    public void deleteAdmin(int id) {
        adminDAO.delete(id);
    }

    @Override
    public Admin getAdminById(int id) {
        return adminDAO.getById(id);
    }

    @Override
    public Admin getAdminByEmail(String email) {
        return adminDAO.getByEmail(email);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminDAO.getAll();
    }
}
