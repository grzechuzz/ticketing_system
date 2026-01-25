package pl.eticket.app.dto.auth;

public record UserInfo(
    Long id,
    String email,
    String firstName,
    String lastName,
    String role
) { }
