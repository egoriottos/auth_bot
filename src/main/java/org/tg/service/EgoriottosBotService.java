package org.tg.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EgoriottosBotService extends TelegramLongPollingBot {
    @Value("${telegram.username}")
    private String botUsername;

    @Value("${telegram.url}")
    private String url;

    @Value("${telegram.bot_token}")
    private String botToken;

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();

            if (text.equals("/start")) {
                sendWebAppButton(chatId);
            }
        }
        System.out.println("Получен апдейт: " + update);
    }

    private void sendWebAppButton(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Нажмите кнопку ниже, чтобы войти через Telegram WebApp:");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton webAppButton = new InlineKeyboardButton("Войти через Telegram");
        webAppButton.setWebApp(new WebAppInfo(url));

        markup.setKeyboard(List.of(List.of(webAppButton)));
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String getBotUsername() {
        return botUsername;
    }

    public boolean validateTelegramData(String initData) {
        try {
            String receivedHash = "";
            Map<String, String> params = new HashMap<>();

            for (String part : initData.split("&")) {
                String[] kv = part.split("=", 2);
                String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
                String value = kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "";

                if ("hash".equals(key)) {
                    receivedHash = value;
                } else {
                    params.put(key, value);
                }
            }

            String dataCheckString = params.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .collect(Collectors.joining("\n"));

            byte[] secretKey = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, "WebAppData".getBytes())
                    .hmac(botToken.getBytes());

            String computedHash = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secretKey)
                    .hmacHex(dataCheckString);

            return computedHash.equals(receivedHash);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public Map<String, Object> parseInitData(String initData) throws JsonProcessingException {
        String userJson = Arrays.stream(initData.split("&"))
                .filter(p -> p.startsWith("user="))
                .findFirst()
                .map(p -> URLDecoder.decode(p.substring(5), StandardCharsets.UTF_8))
                .orElse("{}");

        return new ObjectMapper().readValue(userJson, new TypeReference<>() {
        });
    }
}
