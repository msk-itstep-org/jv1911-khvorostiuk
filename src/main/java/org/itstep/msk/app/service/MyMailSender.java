package org.itstep.msk.app.service;

import java.util.Map;

public interface MyMailSender {
    void send(String emailTo, String subject, String message, Map<String, Object> parameters) throws Exception;
}
