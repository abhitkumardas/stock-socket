package in.stockpe.stocksocket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class StockDataController {

    public static int counter = 1;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
//
//    @MessageMapping("/news")
//    @SendTo("/topic/news")
//    public void broadcastNews(@Payload String message) {
//        return message;
//    }

    @MessageMapping("/counter")
    public void broadcastNews(@RequestHeader("message") String message) {
        log.info("Counter executed...");
        message = message + "No: " + counter++;
        this.simpMessagingTemplate.convertAndSend("/topic/counter", message);
    }

}
