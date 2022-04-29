package chess.dao;

import chess.entity.RoomEntity;
import java.util.List;

public interface RoomDao {

    void saveNewRoom(final String roomName, final String password,
                     final String gameState, final String turn);

    boolean hasDuplicatedName(final String roomName);

    void saveGameState(final int roomId, final String state);

    void deleteRoomByName(final int roomId);

    void saveTurn(final int roomId, final String turn);

    RoomEntity findByRoomId(final int roomId);

    List<RoomEntity> findAllRooms();
}
