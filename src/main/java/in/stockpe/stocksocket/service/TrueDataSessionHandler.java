package in.stockpe.stocksocket.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.annotation.PreDestroy;
import java.lang.reflect.Type;

@Service
@Slf4j
public
class TrueDataSessionHandler extends StompSessionHandlerAdapter {

    //private WebSocketStompClient stompClient;

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("ssklmkmff");
        session.send("", "{method: \"addsymbol\", symbols: [\"NIFTY-I\", \"MINDTREE\", \"NIFTY 50\", \"CRUDEOIL-I\"]}\n");
        //session.subscribe("/topic/greetings", this);
        //session.send("/app/hello", "{\"name\":\"Client\"}".getBytes());

        log.info("New session: {}", session.getSessionId());
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        exception.printStackTrace();
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Greeting.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        log.info("PAYLOAD: "+ payload);
        log.info("Received: {}", ((Greeting) payload).getContent());
    }

//    @PreDestroy
//    public void preDestroy() {
//        stompClient.stop();
//    }
}
