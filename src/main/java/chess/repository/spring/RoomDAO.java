package chess.repository.spring;

import chess.domain.room.Room;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class RoomDAO {
    private static final RowMapper<Room> ROW_MAPPER = (resultSet, rowNumber) -> {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        return new Room(id, name);
    };

    private final JdbcTemplate jdbcTemplate;

    public RoomDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Room> findByLimit(int rowCounts, int offset) {
        String query = "SELECT * FROM ROOM LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, ROW_MAPPER, rowCounts, offset);
    }

    public int calculateRoomCounts() {
        String query = "SELECT COUNT(*) FROM ROOM";
        return jdbcTemplate.queryForObject(query, int.class).intValue();
    }

    public Optional<Room> findById(int id) {
        String query = "SELECT * FROM ROOM WHERE ID = ?";
        return jdbcTemplate.query(query, ROW_MAPPER, id)
                .stream()
                .findAny();
    }

    public int insertRoom(String name) {
        String query = "INSERT INTO ROOM (NAME) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = (connection) -> {
            PreparedStatement prepareStatement = connection.prepareStatement(query, new String[]{"id"});
            prepareStatement.setString(1, name);
            return prepareStatement;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public void deleteById(int id) {
        String query = "DELETE FROM ROOM WHERE ID = ?";
        jdbcTemplate.update(query, id);
    }
}