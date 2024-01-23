package com.swyg.picketbackend.notification.controller;


import com.swyg.picketbackend.auth.util.SecurityUtil;
import com.swyg.picketbackend.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    // 메시지 알림
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter memberSubscribe(
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        log.info("NotificationController....");
        Long currentLoginId = SecurityUtil.getCurrentMemberId();
        log.info("loginId = {}", currentLoginId);

        return notificationService.subscribeMember(currentLoginId, lastEventId);
    }
}
