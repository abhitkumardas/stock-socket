package in.stockpe.stocksocket.controller;

import in.stockpe.stocksocket.dto.StockDataStorage;
import in.stockpe.stocksocket.truedata.dto.TickData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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

    @MessageMapping("/stock-data")
    public void broadcastNews(@RequestHeader("symbols") List<String> symbols, Session session) {

        final int[] i = {0};
        session.getOpenSessions().parallelStream().forEach( openSes -> {
            while (i[0] < 100) {
                System.out.println("sending ...");
                try {
                    openSes.getBasicRemote().sendObject(getLiveStockData(symbols));
                    Thread.sleep(1000);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (EncodeException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i[0]++;
            }

        });
        //this.simpMessagingTemplate.convertAndSend("/topic/counter", message);
    }

    private List<TickData> getLiveStockData(List<String> symbols) {
        return symbols.parallelStream().map(symbol -> {
            return StockDataStorage.stockStorage.get(symbol);
        }).collect(Collectors.toList());
    }

}
