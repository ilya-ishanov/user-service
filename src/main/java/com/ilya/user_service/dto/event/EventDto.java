package com.ilya.user_service.dto.event;

import lombok.Data;
import com.ilya.user_service.entity.event.EventStatus;
import com.ilya.user_service.entity.event.EventType;

import java.time.LocalDateTime;

@Data
public class EventDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private int maxAttendees;
    private Long owner;
    private EventType type;
    private EventStatus status;
    private LocalDateTime createdAt;
}
