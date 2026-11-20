package ie.atu.sample.dto;
import jakarta.validation.constraints.*;
import lombok.Data;
@Data
public class BookingRequest {
    @NotNull(message = "roomId is required")
    private Long roomId;
    @NotBlank(message = "organiserName is required")
    @Size(min = 2, max = 60, message = "organiserName must be 2-60 characters")
    private String organiserName;
    @NotNull(message = "attendeeCount is required")
    @Positive(message = "attendeeCount must be a positive number")
    private Integer attendeeCount;
}
