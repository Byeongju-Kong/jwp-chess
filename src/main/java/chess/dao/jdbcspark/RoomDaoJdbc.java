package chess.dao.jdbcspark;

import chess.dao.RoomDao;
import chess.domain.exceptions.DatabaseException;
import chess.domain.piece.PieceColor;
import chess.dto.RoomNameDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static chess.dao.jdbcspark.DbConnection.getConnection;

public class RoomDaoJdbc implements RoomDao {

    @Override
    public List<RoomNameDto> findRoomNames() {
        String query = "SELECT * FROM room";
        List<RoomNameDto> roomNameDtos = new ArrayList<>();

        try (
            Connection con = getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                roomNameDtos.add(new RoomNameDto(rs.getString("name")));
            }
        } catch (SQLException e) {
            throw new DatabaseException();
        }

        return roomNameDtos;
    }

    @Override
    public String findRoomTurnColor(String roomName) {
        String query = "SELECT turn FROM room WHERE name = ?";

        try (
            Connection con = getConnection();
            PreparedStatement pstmt = createPreparedStatementWithOneParameter(
                con.prepareStatement(query), roomName);
            ResultSet rs = pstmt.executeQuery()) {
            if (!rs.next()) {
                return null;
            }

            System.out.println("!!!" + rs.getString("turn"));
            return rs.getString("turn");

        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    @Override
    public void addRoom(String name, PieceColor turnColor) {
        String query = "INSERT INTO room VALUES (?, ?)";

        try (
            Connection con = getConnection();
            PreparedStatement pstmt = createPreparedStatementWithTwoParameter(
                con.prepareStatement(query), name, turnColor.getName())) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    @Override
    public void deleteRoom(String roomName) {
        String query = "DELETE FROM room WHERE name = ?";

        try (
            Connection con = getConnection();
            PreparedStatement pstmt = createPreparedStatementWithOneParameter(
                con.prepareStatement(query), roomName)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    @Override
    public void updateTurn(String roomName, PieceColor turnColor) {
        String query = "UPDATE room SET turn = ? WHERE name = ?";

        try (
            Connection con = getConnection();
            PreparedStatement pstmt = createPreparedStatementWithTwoParameter(
                con.prepareStatement(query), turnColor.getName(), roomName)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    @Override
    public boolean existsRoom(String name) {
        return false;
    }


    private PreparedStatement createPreparedStatementWithOneParameter(
        PreparedStatement ps, String param) throws SQLException {
        ps.setString(1, param);
        return ps;
    }

    private PreparedStatement createPreparedStatementWithTwoParameter(
        PreparedStatement ps, String firstParam, String secondParam) throws SQLException {
        ps.setString(1, firstParam);
        ps.setString(2, secondParam);
        return ps;
    }
}
