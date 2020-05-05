package net.pladema.establishment.controller.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import net.pladema.establishment.controller.dto.RoomDto;
import net.pladema.establishment.repository.entity.Room;

@Mapper
public interface RoomMapper {

    @Named("toRoomDto")
    RoomDto toRoomDto(Room bed);

    @Named("toListRoomDto")
    @IterableMapping(qualifiedByName = "toRoomDto")
    List<RoomDto> toListRoomDto(List<Room> roomList);

}
