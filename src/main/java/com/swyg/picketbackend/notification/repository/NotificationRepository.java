package com.swyg.picketbackend.notification.repository;

import com.swyg.picketbackend.auth.domain.Member;
import com.swyg.picketbackend.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByReceiver(Member member);

}
