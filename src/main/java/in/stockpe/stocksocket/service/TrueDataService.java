package in.stockpe.stocksocket.service;

import com.angelbroking.smartapi.SmartConnect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.URI;
import java.util.Scanner;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class TrueDataService {


    public WebSocketClient webSocketClient() {
        final WebSocketClient client = new StandardWebSocketClient();

        final WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        final StompSessionHandler sessionHandler = new TrueDataSessionHandler();
        stompClient.connect("wss://push.truedata.in:8082?user=tdws150&password=shubham@150", sessionHandler);
        return client;
    }

    public void smartApi() {
        SmartConnect smartConnect = new SmartConnect("8PcJjrTP");

        log.info("smart api");
    }


//    @PostConstruct
//    public void postConstruct() {
//        WebSocketClient webSocketClient = new StandardWebSocketClient();
//        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
//        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
//        stompClient.setTaskScheduler(new ConcurrentTaskScheduler());
//
//        String url = "wss://push.truedata.in:8082/?user=tdws150&password=shubham@150&encoding=text";
//        StompSessionHandler sessionHandler = new TrueDataSessionHandler();
//        stompClient.connect(url, sessionHandler);
//
//        new Scanner(System.in).nextLine(); //Don't close immediately.
//    }
}
