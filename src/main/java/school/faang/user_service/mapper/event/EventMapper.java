package school.faang.user_service.mapper.event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import school.faang.user_service.dto.event.CreateEventDto;
import school.faang.user_service.dto.event.EventDto;
import school.faang.user_service.dto.event.UpdateEventDto;
import school.faang.user_service.entity.event.Event;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {
    Event toEntityEvent(CreateEventDto eventDto);

    @Mapping(target = "owner", source = "owner.id")
    EventDto toEventDto(Event event);

    List<EventDto> toListEventDto(List<Event> events);

    void toEntityUpdateEvent(UpdateEventDto updateEventDto, @MappingTarget Event event);


}
