package dao;

import database.DatabaseConnection;
import models.HeirsTable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for heirstable.
 * One member can have multiple heirs (one-to-many).
 */
public class HeirsDAO {

    // ─── CREATE ──────────────────────────────────────────────────────────────────

    public boolean insertHeir(HeirsTable heir) {
        // Heir_Code is AUTO_INCREMENT — do not include it in INSERT
        String sql = "INSERT INTO heirstable "
                + "(PagIbig_MID_No, Heirs_Name, Heirs_Relationship, Heirs_Birthdate) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, heir.getPagIbigMIDNo());
            ps.setString(2, heir.getHeirsName());
            ps.setString(3, heir.getHeirsRelationship());
            ps.setDate(4,   heir.getHeirsBirthdate());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[HeirsDAO] insertHeir error: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteHeirsByMID(String pagIbigMIDNo) {
        String sql = "DELETE FROM heirstable WHERE PagIbig_MID_No = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pagIbigMIDNo);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("[HeirsDAO] deleteHeirsByMID error: " + e.getMessage());
            return false;
        }
    }

    // ─── READ (all heirs of one member) ──────────────────────────────────────────

    public List<HeirsTable> getHeirsByMID(String pagIbigMIDNo) {
        List<HeirsTable> list = new ArrayList<>();
        String sql = "SELECT * FROM heirstable WHERE PagIbig_MID_No = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pagIbigMIDNo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.err.println("[HeirsDAO] getHeirsByMID error: " + e.getMessage());
        }
        return list;
    }

    // ─── READ (single heir by Heir_Code) ─────────────────────────────────────────

    public HeirsTable getHeirByCode(int heirCode) {
        String sql = "SELECT * FROM heirstable WHERE Heir_Code = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, heirCode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            System.err.println("[HeirsDAO] getHeirByCode error: " + e.getMessage());
        }
        return null;
    }

    // ─── READ (all) ───────────────────────────────────────────────────────────────

    public List<HeirsTable> getAllHeirs() {
        List<HeirsTable> list = new ArrayList<>();
        String sql = "SELECT * FROM heirstable";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.err.println("[HeirsDAO] getAllHeirs error: " + e.getMessage());
        }
        return list;
    }

    // ─── UPDATE ──────────────────────────────────────────────────────────────────

    public boolean updateHeir(HeirsTable heir) {
        String sql = "UPDATE heirstable SET "
                + "Heirs_Name = ?, Heirs_Relationship = ?, Heirs_Birthdate = ? "
                + "WHERE Heir_Code = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, heir.getHeirsName());
            ps.setString(2, heir.getHeirsRelationship());
            ps.setDate(3,   heir.getHeirsBirthdate());
            ps.setInt(4,    heir.getHeirCode());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[HeirsDAO] updateHeir error: " + e.getMessage());
            return false;
        }
    }

    // ─── DELETE (single heir) ────────────────────────────────────────────────────

    public boolean deleteHeir(int heirCode) {
        String sql = "DELETE FROM heirstable WHERE Heir_Code = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, heirCode);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[HeirsDAO] deleteHeir error: " + e.getMessage());
            return false;
        }
    }

    // ─── DELETE (all heirs of a member) ──────────────────────────────────────────

    public boolean deleteAllHeirsByMID(String pagIbigMIDNo) {
        String sql = "DELETE FROM heirstable WHERE PagIbig_MID_No = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pagIbigMIDNo);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[HeirsDAO] deleteAllHeirsByMID error: " + e.getMessage());
            return false;
        }
    }

    // ─── Helper ───────────────────────────────────────────────────────────────────

    private HeirsTable mapRow(ResultSet rs) throws SQLException {
        return new HeirsTable(
                rs.getString("PagIbig_MID_No"),
                rs.getInt("Heir_Code"),
                rs.getString("Heirs_Name"),
                rs.getString("Heirs_Relationship"),
                rs.getDate("Heirs_Birthdate")
        );
    }
}