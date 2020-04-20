package org.itstep.msk.app.service;

import org.itstep.msk.app.entity.User;

public interface UserService {
    User findByUsername(String username);

    boolean isUsernameExists(String username);

    User createUser(String username, String password, String email);

    void setPassword(User user, String password);

    void save(User user);

    String generateActivationCode();

    boolean activate(User user, String activationCode);
}
