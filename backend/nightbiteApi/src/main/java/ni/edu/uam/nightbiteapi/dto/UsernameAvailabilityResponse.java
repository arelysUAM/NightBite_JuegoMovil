package ni.edu.uam.nightbiteapi.dto;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}