package ru.youngstanis.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Component
@Log4j
public class TelegramBotController extends TelegramLongPollingBot {
    private final String botName;
    private final String botToken;
    private final UpdateController updateController;

    public TelegramBotController(@Value("${telegram.bot.username}") String botName,
                                 @Value("${telegram.bot.token}") String botToken,
                                 UpdateController updateController) {
        this.botName = botName;
        this.botToken = botToken;
        this.updateController = updateController;
    }

    @PostConstruct
    public void init() {
        this.updateController.registerBot(this);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateController.processUpdate(update);
    }

    public void sendAnswerMessage(SendMessage message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(e);
            }
        }
    }
}
