package pe.tiendavega.model.dto;

import java.util.Date;

public record LoginJWT(String accessToken, Date issuedAt, Date expiration) {
}
