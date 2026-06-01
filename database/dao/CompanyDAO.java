package dao;

import database.DatabaseConnection;
import models.CompanyDetailsTable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAO {

    // ─── READ ALL ─────────────────────────────────────────────────────────────
    public List<CompanyDetailsTable> getAllCompanies() {
        List<CompanyDetailsTable> list = new ArrayList<>();
        String sql = "SELECT * FROM companydetailstable ORDER BY Company_Name";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[CompanyDAO] getAllCompanies error: " + e.getMessage());
        }
        return list;
    }

    // ─── CHECK CODE EXISTS ────────────────────────────────────────────────────
    public boolean companyCodeExists(String code) {
        String sql = "SELECT 1 FROM companydetailstable WHERE Company_Code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("[CompanyDAO] companyCodeExists error: " + e.getMessage());
            return false;
        }
    }

    // ─── INSERT ───────────────────────────────────────────────────────────────
    public boolean insertCompany(CompanyDetailsTable company) {
        String sql = "INSERT INTO companydetailstable "
                + "(Company_Code, Company_Name, Company_Address, Office_Assignment, Branch_Location) "
                + "VALUES (?,?,?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, company.getCompanyCode());
            ps.setString(2, company.getCompanyName());
            ps.setString(3, company.getCompanyAddress());
            ps.setString(4, company.getOfficeAssignment());
            ps.setString(5, company.getBranchLocation());   // nullable

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CompanyDAO] insertCompany error: " + e.getMessage());
            return false;
        }
    }

    // ─── MAPPER ───────────────────────────────────────────────────────────────
    private CompanyDetailsTable mapRow(ResultSet rs) throws SQLException {
        return new CompanyDetailsTable(
                rs.getString("Company_Code"),
                rs.getString("Company_Name"),
                rs.getString("Company_Address"),
                rs.getString("Office_Assignment"),
                rs.getString("Branch_Location")
        );
    }
}

