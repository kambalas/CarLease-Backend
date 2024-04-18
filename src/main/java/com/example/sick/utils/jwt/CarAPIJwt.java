package com.example.sick.utils.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@NoArgsConstructor
public class CarAPIJwt {
    @Getter
    @Setter
    private String jwt;
    @Getter
    @Setter
    private long expiresAt;

    public CarAPIJwt(String jwt, long expiresAt) {
        this.jwt = jwt;
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        long currentTime = Instant.now().getEpochSecond();
        return currentTime > expiresAt;
    }
}
