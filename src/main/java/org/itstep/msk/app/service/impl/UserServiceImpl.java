package org.itstep.msk.app.service.impl;

import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.enums.Role;
import org.itstep.msk.app.repository.UserRepository;
import org.itstep.msk.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Nullable
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean isUsernameExists(String username) {
        return findByUsername(username) != null;
    }

    @Override
    public User createUser(String username, String password, String email) {
        User user = new User();
        user.setUsername(username);
        setPassword(user, password);
        user.setEmail(email);
        user.getRoles().add(Role.ROLE_USER);
        user.setActive(false);
        user.setActivationCode(generateActivationCode());

        return user;
    }

    @Override
    public void setPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
        userRepository.flush();
    }

    @Override
    public String generateActivationCode() {
        return UUID.randomUUID().toString();
    }

    @Override
    public boolean activate(User user, String activationCode) {
        if (!user.getActivationCode().equalsIgnoreCase(activationCode)) {
            return false;
        }

        user.setActivationCode(generateActivationCode());
        user.setActive(true);
        save(user);

        return true;
    }
}
