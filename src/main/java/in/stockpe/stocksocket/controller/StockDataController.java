package in.stockpe.stocksocket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class StockDataController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
//
//    @MessageMapping("/news")
//    @SendTo("/topic/news")
//    public void broadcastNews(@Payload String message) {
//        return message;
//    }

    @MessageMapping("/news")
    public void broadcastNews(@Payload String message) {
        this.simpMessagingTemplate.convertAndSend("/topic/news", message);
    }

}
