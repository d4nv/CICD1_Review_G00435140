package ie.atu.sample.service;

import feign.FeignException;
import ie.atu.sample.client.RoomClient;
import ie.atu.sample.dto.BookingRequest;
import ie.atu.sample.dto.BookingResponse;
import ie.atu.sample.dto.RoomDto;
import ie.atu.sample.exception.BookingNotFoundException;
import ie.atu.sample.exception.CapacityExceededException;
import ie.atu.sample.exception.RoomNotFoundException;
import ie.atu.sample.model.Booking;
import ie.atu.sample.repository.BookingRepository;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomClient roomClient;

    public BookingService(BookingRepository bookingRepository, RoomClient roomClient) {
        this.bookingRepository = bookingRepository;
        this.roomClient = roomClient;
    }

    public BookingResponse createBooking(BookingRequest request) {
        RoomDto room;
        try {
            room = roomClient.getRoom(request.getRoomId());
        } catch (FeignException.NotFound e) {
            throw new RoomNotFoundException("Room " + request.getRoomId() + " not found");
        }

        if (request.getAttendeeCount() > room.getCapacity()) {
            throw new CapacityExceededException("Attendee count " + request.getAttendeeCount()
                    + " exceeds room capacity " + room.getCapacity());
        }

        Booking booking = new Booking(null, request.getOrganiserName(),
                request.getAttendeeCount(), request.getRoomId(), "ACTIVE");
        Booking saved = bookingRepository.save(booking);
        return toResponse(saved);
    }

    public BookingResponse getBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking " + id + " not found"));
        return toResponse(booking);
    }

    private BookingResponse toResponse(Booking b) {
        return new BookingResponse(b.getId(), b.getOrganiserName(),
                b.getAttendeeCount(), b.getRoomId(), b.getStatus());
    }
}