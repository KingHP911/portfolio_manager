package ru.kinghp.portfolio_manager.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DBUserDto {
    private Long id;
    private String login;
}
