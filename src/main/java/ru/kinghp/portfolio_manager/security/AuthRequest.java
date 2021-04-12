package ru.kinghp.portfolio_manager.security;

import lombok.Data;

@Data
public class AuthRequest {
    private String name;
    private String password;
}
