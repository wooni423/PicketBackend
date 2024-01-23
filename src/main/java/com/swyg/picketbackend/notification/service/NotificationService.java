package com.swyg.picketbackend.notification.service;

import com.swyg.picketbackend.auth.domain.Member;

import com.swyg.picketbackend.board.Entity.Board;
import com.swyg.picketbackend.board.repository.BoardRepository;
import com.swyg.picketbackend.global.exception.CustomException;
import com.swyg.picketbackend.global.util.ErrorCode;
import com.swyg.picketbackend.notification.domain.Notification;
import com.swyg.picketbackend.notification.dto.NotificationResponseDTO;
import com.swyg.picketbackend.notification.repository.EmitterRepository;
import com.swyg.picketbackend.notification.repository.NotificationRepository;
import com.swyg.picketbackend.notification.util.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@Log4j2
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 120L * 1000 * 60;

    private final EmitterRepository emitterRepository;

    private final NotificationRepository notificationRepository;

    private final BoardRepository boardRepository;

    // 메시지 알림
    public SseEmitter subscribeMember(Long currentLoginId, String lastEventId) {

        String emitterId = currentLoginId + "_" + System.currentTimeMillis();

        // 클라이언트 sse 연결 요청에 응답하기 위한 SseEmitter 객체 생성
        // 유효시간 지정으로 시간이 지나면 클라이언트에서 자동으로 재연결 요청
        SseEmitter sseEmitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        // SseEmitter의 완료/시간초과/에러로 인한 전송 불가 시 sseEmitter 삭제
        sseEmitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        sseEmitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
        sseEmitter.onError((e) -> emitterRepository.deleteById(emitterId));

        // 연결 직후, 데이터 전송이 없을 시 503 에러 발생. 에러 방지 위한 더미데이터 전송
        sendToClient(sseEmitter, emitterId, "이벤트 스트림이 생성되었습니다. [emitterId=" + emitterId + "]");


        // 클라이언트가 미수신한 Event 유실 예방, 연결이 끊켰거나 미수신된 데이터를 다 찾아서 보내준다.
        if (!lastEventId.isEmpty()) {
            Map<String, Object> eventList = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(currentLoginId));
            eventList.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(sseEmitter, entry.getKey(), entry.getValue()));
        }
        
        // D-day 알림
        List<Board> myBoardList = boardRepository.findAllByMemberId(currentLoginId);





        /*// 댓글 알림
        String url = "/board/" + boardId;
        String content = target.getTitle() + "에 새로운 댓글이 달렸습니다.";

        notificationService.send(target.getMember(), NotificationType.COMMENT, content, url);*/
        

        return sseEmitter;
    }

    // Notification 전송 메서드
    public void send(Member receiver, NotificationType notificationType, String content, String url) {
        Notification notification = notificationRepository.save(Notification.craetNotification(receiver, notificationType, content, url));
        String memberId = String.valueOf(receiver.getId());

        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByMemberId(memberId);

        sseEmitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendToClient(emitter,key, NotificationResponseDTO.of(notification));
                }
        );
    }
    
    // 알림 전송 메서드
    private void sendToClient(SseEmitter emitter, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(emitterId)
                    .data(data));
        } catch (IOException e) {
            emitterRepository.deleteById(emitterId);
            throw new CustomException(ErrorCode.NOTIFICATION_ERROR);
        }
    }


}