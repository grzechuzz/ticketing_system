package pl.eticket.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.eticket.app.dto.auth.*;
import pl.eticket.app.entity.RefreshToken;
import pl.eticket.app.entity.Role;
import pl.eticket.app.entity.User;
import pl.eticket.app.exception.ApiException;
import pl.eticket.app.mapper.UserMapper;
import pl.eticket.app.repository.RefreshTokenRepository;
import pl.eticket.app.repository.RoleRepository;
import pl.eticket.app.repository.UserRepository;
import pl.eticket.app.security.JwtTokenProvider;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final UserMapper userMapper;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw ApiException.badRequest("Email already exists");
        }

        Role customerRole = roleRepository.findByName(Role.CUSTOMER)
                .orElseThrow(() -> ApiException.notFound("Role not found"));

        User newUser = User.create(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.firstName(),
                request.lastName(),
                request.phoneNumber(),
                customerRole
        );

        newUser = userRepository.save(newUser);
        return createAuthResponse(newUser);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailAndIsActiveTrue(request.email())
                .orElseThrow(() -> ApiException.unauthorized("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw ApiException.unauthorized("Invalid email or password");
        }

        return createAuthResponse(user);
    }

    @Transactional
    public AuthResponse refreshToken(RefreshRequest request) {
        String hash = hashToken(request.refreshToken());
        RefreshToken token = refreshTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> ApiException.unauthorized("Invalid or expired refresh token"));

        if (token.isRevoked()) {
            refreshTokenRepository.revokeAllByUserId(token.getUser().getId(), Instant.now());
            throw ApiException.unauthorized("Invalid token");
        }

        if (token.isExpired()) {
            throw ApiException.unauthorized("Refresh token expired");
        }

        token.revoke();
        token.setLastUsedAt(Instant.now());
        refreshTokenRepository.save(token);

        return createAuthResponse(token.getUser());
    }

    @Transactional
    public void logout(Long userId) {
        refreshTokenRepository.revokeAllByUserId(userId, Instant.now());
    }

    private AuthResponse createAuthResponse(User user) {
        String accessToken = tokenProvider.generateAccessToken(user.getId(), user.getEmail(), user.getRole().getName());
        String refreshToken = tokenProvider.generateRefreshToken(user.getId());

        RefreshToken rt = RefreshToken.create(user, hashToken(refreshToken), tokenProvider.getRefreshExpiration());
        refreshTokenRepository.save(rt);

        UserInfo userInfo = userMapper.userToUserInfo(user);

        return new AuthResponse(
                accessToken,
                refreshToken,
                tokenProvider.getAccessExpiration() / 1000,
                userInfo
        );
    }

    private String hashToken(String token) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Token hash error", e);
        }
    }
}
