package com.continuehub.server.dto;

import lombok.Data;

@Data
public class RefreshResponse {
    private String accessToken;
    private String refreshToken;
    private User user;

    public RefreshResponse(String accessToken, String refreshToken, User user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
    }

    @Data
    public static class User {
        private String firstName;
        private String lastName;
        private String email;

        public User(String firstName, String lastName, String email) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
        }
    }
}
