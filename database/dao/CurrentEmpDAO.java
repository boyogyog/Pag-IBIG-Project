package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;
import models.CurrentEmpRecordTable;

public class CurrentEmpDAO {
    public void deleteCurrentEmpByMID(String mid) {
    deleteByMID(mid);
}
	
    // ─── INSERT ───────────────────────────────────────────────────────────────
    public boolean insertCurrentEmp(CurrentEmpRecordTable record) {
        String sql = "INSERT INTO currentemprecordtable "
                + "(PagIbig_MID_No, Company_Code, Occupation, Employment_Status, "
                + "TypeOfWork, Country_Of_Assignment, Date_Employed) "
                + "VALUES (?,?,?,?,?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, record.getPagIbigMIDNo());
            ps.setString(2, record.getCompanyCode());
            ps.setString(3, record.getOccupation());
            ps.setString(4, record.getEmploymentStatus());
            ps.setString(5, record.getTypeOfWork());        // nullable
            ps.setString(6, record.getCountryOfAssignment());
            ps.setDate(7,   record.getDateEmployed());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[CurrentEmpDAO] insertCurrentEmp error: " + e.getMessage());
            return false;
        }
    }
    
 // ── CHANGES TO SignInFrame.java ───────────────────────────────────────────
    //
    // 1. Fix CurrentEmpFormView — pass loggedInMID:
//        FIND:
//        frame.add(new CurrentEmpFormView());
//        REPLACE WITH:
//        frame.add(new CurrentEmpFormView(loggedInMID));
    //
    // 2. Fix PrevEmpFormView — pass loggedInMID:
//        FIND:
//        frame.add(new PrevEmpFormView());
//        REPLACE WITH:
//        frame.add(new PrevEmpFormView(loggedInMID));
    //
    // That's it for SignInFrame! HeirsFormView and MemberInfoFormView
    // already pass loggedInMID correctly.


    // ── ADD TO CurrentEmpDAO.java ─────────────────────────────────────────────
    //
    // Add this UPDATE method alongside insertCurrentEmp and getByMID:

        public boolean updateCurrentEmp(CurrentEmpRecordTable record) {
            String sql = "UPDATE currentemprecordtable SET "
                    + "Company_Code = ?, Occupation = ?, Employment_Status = ?, "
                    + "TypeOfWork = ?, Country_Of_Assignment = ?, Date_Employed = ? "
                    + "WHERE PagIbig_MID_No = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, record.getCompanyCode());
                ps.setString(2, record.getOccupation());
                ps.setString(3, record.getEmploymentStatus());
                ps.setString(4, record.getTypeOfWork());   // nullable
                ps.setString(5, record.getCountryOfAssignment());
                ps.setDate(6,   record.getDateEmployed());
                ps.setString(7, record.getPagIbigMIDNo()); // WHERE clause

                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                System.err.println("[CurrentEmpDAO] updateCurrentEmp error: " + e.getMessage());
                return false;
            }
        }
    // ─── READ ─────────────────────────────────────────────────────────────────
    public CurrentEmpRecordTable getCurrentEmpByMID(String pagIbigMIDNo) {
        String sql = "SELECT * FROM currentemprecordtable WHERE PagIbig_MID_No = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pagIbigMIDNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.err.println("[CurrentEmpDAO] getByMID error: " + e.getMessage());
        }
        return null;
    }

    // ─── MAPPER ───────────────────────────────────────────────────────────────
    private CurrentEmpRecordTable mapRow(ResultSet rs) throws SQLException {
        return new CurrentEmpRecordTable(
                rs.getString("PagIbig_MID_No"),
                rs.getString("Company_Code"),
                rs.getString("Occupation"),
                rs.getString("Employment_Status"),
                rs.getString("TypeOfWork"),
                rs.getString("Country_Of_Assignment"),
                rs.getDate("Date_Employed")
        );
    }
    
 // ─── UPDATE ───────────────────────────────────────────────────────────────
    // Alias so AdminDashboard can call getByMID()
    public CurrentEmpRecordTable getByMID(String pagIbigMIDNo) {
        return getCurrentEmpByMID(pagIbigMIDNo);
    }

    public boolean deleteByMID(String pagIbigMIDNo) {
        String sql = "DELETE FROM currentemprecordtable WHERE PagIbig_MID_No = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pagIbigMIDNo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CurrentEmpDAO] deleteByMID error: " + e.getMessage());
            return false;
        }
    }
}