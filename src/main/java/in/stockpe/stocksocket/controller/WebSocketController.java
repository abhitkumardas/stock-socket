package in.stockpe.stocksocket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class WebSocketController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    WebSocketController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/send/message")
    public void onReceiveMessage(String message) {
        this.simpMessagingTemplate.convertAndSend("/chat", new SimpleDateFormat("HH:mm:ss").format(new Date()) + "- "+message);
    }
}
