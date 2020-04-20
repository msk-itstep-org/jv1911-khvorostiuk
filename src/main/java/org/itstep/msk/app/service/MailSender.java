package org.itstep.msk.app.service;

import java.util.Map;

public interface MailSender {
    void send(String emailTo, String subject, String message) throws Exception;

    void send(String emailTo, String subject, String view, Map<String, Object> parameters) throws Exception;
}
