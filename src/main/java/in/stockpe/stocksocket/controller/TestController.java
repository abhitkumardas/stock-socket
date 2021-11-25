package in.stockpe.stocksocket.controller;

import in.stockpe.stocksocket.dto.StockDataStorage;
import in.stockpe.stocksocket.truedata.*;
import in.stockpe.stocksocket.truedata.dto.TickData;
import in.stockpe.stocksocket.truedata.http.exception.TrueDataException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PreDestroy;

@RestController
@Slf4j
public class TestController {

    private TrueDataWebSocket trueDataWebSocket;

    @SneakyThrows
    @GetMapping("ws-test")
    public ResponseEntity testWS() {
        String [] symbols = {"MINDTREE", "TCS"};
        trueDataWebSocketUsage("tdws150", "shubham@150", "addsymbol", symbols);

        return ResponseEntity.ok("success");
    }


    public void trueDataWebSocketUsage(String user, String password, String method, String[] symbols) throws TrueDataException {
        trueDataWebSocket = new TrueDataWebSocket(user, password, null, method, symbols);

        trueDataWebSocket.setOnTickerArrivalListener(new TrueDataWSLiveFeed() {
            @Override
            public void tickHandler(TickData tickData) {
                StockDataStorage.stockStorage.put(tickData.getSymbol(), tickData);
                log.info("Tick Inserted: "+tickData);
            }

            @Override
            public void touchLineHandler(JSONArray touchLineDataArr) {
                log.info("Touch: "+touchLineDataArr);
            }

            @Override
            public void bidAskHandler(JSONArray bidAskDataArr) {
                log.info("BidAsk: "+bidAskDataArr);
            }
        });

//        avcd(){
//            if (messageJSONObject.isNull("success")){
//                switch (messageJSONObject.get("message").toString()) {
//                    case "HeartBeat":
//                        System.out.println("HB"+);
//                    case "TrueData Real Time Data Service":
//                        if (reconnectLogs.length === 0) console.log(jsonObj);
//
//                        subscribe(symbols);
//
//                        break;
//                }
//            }
//        }

        //trueDataWebSocket.reConnect();
        //trueDataWebSocket.connect();
        log.info("Connection Status: " + trueDataWebSocket.isConnectionOpen());
    }

    @PreDestroy
    public void preDestroy(){
        trueDataWebSocket.disconnect();
        log.info("Shutting Down, Is TrueData Still Connected: "+trueDataWebSocket.isConnectionOpen());
    }
}
