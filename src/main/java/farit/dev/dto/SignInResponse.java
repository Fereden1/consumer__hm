package farit.dev.dto;

public record SignInResponse(
        boolean success,
        String token,
        String message
) {
}