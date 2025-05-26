package org.tg.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.tg.entity.User;

public interface UserService {
    User saveFromInitData(String initData, EgoriottosBotService botService) throws JsonProcessingException;
}
