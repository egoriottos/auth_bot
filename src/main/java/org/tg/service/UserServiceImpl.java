package org.tg.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import org.tg.entity.User;
import org.tg.repository.UserRepository;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveFromInitData(String initData, EgoriottosBotService botService) throws JsonProcessingException {
        if (!botService.validateTelegramData(initData)) {
            throw new IllegalArgumentException("Недопустимая подпись (hash)");
        }

        Map<String, Object> userMap = botService.parseInitData(initData);
        Long userId = ((Number) userMap.get("id")).longValue();

        User user = userRepository.findById(userId).orElse(new User());
        user.setId(userId);
        user.setFirstname((String) userMap.get("first_name"));
        user.setLastname((String) userMap.get("last_name"));
        user.setUsername((String) userMap.get("username"));

        return userRepository.save(user);
    }
}
