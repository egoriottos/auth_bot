package org.tg;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class TgConfig {

    public TgConfig(EgoriottosBotService botService) {
        this.botService = botService;
    }

    private final EgoriottosBotService botService;


    @PostConstruct
    public void registerBot() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botService.setBotToken(botService.getBotToken());
        botsApi.registerBot(botService);
    }
}
