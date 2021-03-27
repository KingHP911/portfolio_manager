package ru.kinghp.portfolio_manager.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
public class News {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String title;
    private String briefDis;
    private String fullDis;
    private String imgRef;
    private Boolean isViewed = false;
    private LocalDateTime validUntil;
    private String fButtonName;
    private String fButtonAction;

}
