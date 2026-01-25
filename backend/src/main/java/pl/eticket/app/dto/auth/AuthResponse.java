package pl.eticket.app.dto.auth;

public record AuthResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    Long expiresIn,
    UserInfo user
) {
    public AuthResponse(String accessToken, String refreshToken, Long expiresIn, UserInfo user) {
        this(accessToken, refreshToken, "Bearer", expiresIn, user);
    }
}
