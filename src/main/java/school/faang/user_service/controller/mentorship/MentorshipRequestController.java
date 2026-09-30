package school.faang.user_service.controller.mentorship;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import school.faang.user_service.dto.RejectionDto;
import school.faang.user_service.dto.mentorship.CreateMentorshipRequestDto;
import school.faang.user_service.dto.mentorship.MentorshipRequestDto;
import school.faang.user_service.entity.MentorshipRequest;
import school.faang.user_service.service.mentorship.MentorshipRequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rec-mentorship")
public class MentorshipRequestController {
    private final MentorshipRequestService requestService;

    @PostMapping
    public MentorshipRequestDto createMentorshipRequest
            (@Valid @RequestBody CreateMentorshipRequestDto dto) {
        return requestService.createMentorshipRequest(dto);
    }

    @PostMapping("/{requestId}/accept")
    public void acceptRequestMentorship(@PathVariable Long requestId) {
        requestService.acceptRequestMentorship(requestId);
    }

    @PostMapping("/{requestId}/reject")
    public void rejectMentorshipRequest(@PathVariable Long requestId,
                                        @Valid @RequestBody RejectionDto rejectionDto) {
        requestService.rejectMentorshipRequest(requestId, rejectionDto);
    }

    @GetMapping("/{requesterId}/requester")
    public List<MentorshipRequestDto> getAllByRequesterId(@PathVariable Long requesterId) {
        return requestService.getAllByRequesterId(requesterId);
    }

    @GetMapping("/{receiverId}/receiver")
    public List<MentorshipRequestDto> getAllByReceiverId(@PathVariable Long receiverId) {
        return requestService.getAllByReceiverId(receiverId);
    }
}
