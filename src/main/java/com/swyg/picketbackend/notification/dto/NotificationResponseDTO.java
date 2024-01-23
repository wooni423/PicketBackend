package com.swyg.picketbackend.notification.dto;


import com.swyg.picketbackend.notification.domain.Notification;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "알림 dto")
@Data
@Builder
public class NotificationResponseDTO {

    private Long id;

    private String content;

    private String url;

    private Boolean isRead;

    private LocalDateTime createAt;


    public static NotificationResponseDTO of(Notification notification) {
        return NotificationResponseDTO.builder()
                .id(notification.getId())
                .content(notification.getContent())
                .url(notification.getUrl())
                .isRead(notification.getIsRead())
                .createAt(notification.getCreateDate())
                .build();
    }


}
