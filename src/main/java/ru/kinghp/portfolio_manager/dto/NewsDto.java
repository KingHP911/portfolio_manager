package ru.kinghp.portfolio_manager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewsDto {

    private Long id;

    private String title;
    private String briefDis;
    private String fullDis;
    private String imgRef;
    private Boolean isViewed;
    private LocalDateTime validUntil;
    private String fButtonName;
    private String fButtonAction;

    @JsonProperty("dbUser")
    private DBUserDto dbUserDto;
}
