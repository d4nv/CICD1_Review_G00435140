
package ie.atu.sample.client;

import ie.atu.sample.dto.RoomDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "room-service", url = "${room.service.url}")
public interface RoomClient {

    @GetMapping("/api/rooms/{id}")
    RoomDto getRoom(@PathVariable("id") Long id);
}
