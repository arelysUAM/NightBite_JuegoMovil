package ni.edu.uam.nightbiteapi.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsernameAvailabilityResponse {

    private String username;
    private boolean available;
    private String message;

    public UsernameAvailabilityResponse() {
    }

    public UsernameAvailabilityResponse(
            String username,
            boolean available,
            String message
    ) {
        this.username = username;
        this.available = available;
        this.message = message;
    }

}