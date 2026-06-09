package pe.tiendavega.model.dto;

public record LoginJWT(String accessToken, long issuedAt, long expiration) {
}
