package farit.dev.dto;

public record SignInRequest(
        String username,
        String password
) {
}