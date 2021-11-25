package in.stockpe.stocksocket.truedata;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.zip.DataFormatException;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import in.stockpe.stocksocket.truedata.dto.TickData;
import in.stockpe.stocksocket.truedata.http.exception.TrueDataException;
import in.stockpe.stocksocket.truedata.utils.NaiveSSLContext;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

@Slf4j
public class TrueDataWebSocket {
    private WebSocket ws;
    private Date lastHeartBeatTime;

    private TrueDataWSLiveFeed onTickerArrivalListener;
    //private TrueDataWSOnTicks onTickerArrivalListener;
    private TrueDataWSOnConnect onConnectedListener;
    private TrueDataWSOnDisconnect onDisconnectedListener;
    private TrueDataWSOnError onErrorListener;
    private int reconnectionTime = 600000;//10min
    private Timer mTimer = new Timer();

    private final String wsuri = "wss://push.truedata.in";
    private String encoding = "text";
    private String user;
    private String password;
    private int port = 8082;
    private String method;
    private String[] symbols;
    public static Map<String, String> tickerMap = new HashMap();

    public TrueDataWebSocket(String user, String password, Integer port,  String method, String[] symbols) {
        this.user = user;
        this.password = password;
        this.method = method;
        this.symbols = symbols;
        this.port = port!=null ? port:this.port;

        try {
            String swsuri = this.wsuri + ":" + this.port + "?user=" + this.user + "&password=" + this.password;
            SSLContext context = NaiveSSLContext.getInstance("TLS");
            this.ws = (new WebSocketFactory()).setSSLContext(context).setVerifyHostname(false).createSocket(swsuri);
            this.ws.addListener(this.getWebsocketAdapter());
            ws.connect();
        } catch (IOException var8) {
            if (this.onErrorListener != null) {
                this.onErrorListener.onError(var8);
            }
            return;
        } catch (NoSuchAlgorithmException var9) {
            var9.printStackTrace();
        } catch (Exception e) {
            mTimer.schedule(reConnectTimerTask(), reconnectionTime);
        }
    }

    public void setOnErrorListener(TrueDataWSOnError listener) {
        this.onErrorListener = listener;
    }

    public void setOnTickerArrivalListener(TrueDataWSLiveFeed onTickerArrivalListener) {
        this.onTickerArrivalListener = onTickerArrivalListener;
    }

    public void setOnConnectedListener(TrueDataWSOnConnect listener) {
        this.onConnectedListener = listener;
    }

    public void setOnDisconnectedListener(TrueDataWSOnDisconnect listener) {
        this.onDisconnectedListener = listener;
    }

    public WebSocketAdapter getWebsocketAdapter() {
        return new WebSocketAdapter() {
            public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws WebSocketException {
                log.info("True Data WebSocket Successfully Connected...");
                TrueDataWebSocket.this.onConnectedListener.onConnected();
                Runnable runnable = new Runnable() {
                    public void run() {
                        log.info("TRUEDATA: Runnable called...");
                    }
                };
                ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
                service.scheduleAtFixedRate(runnable, 0L, 1L, TimeUnit.MINUTES);
            }

            public void onTextMessage(WebSocket websocket, String message) throws IOException, DataFormatException {
                JSONObject messageJSONObject = new JSONObject(message);
                if (!messageJSONObject.isNull("success")){
                    switch (messageJSONObject.get("message").toString()) {
                        case "TrueData Real Time Data Service":
                            addSymbols();
                            break;
                        case "symbols added":
                            log.info("Successfully Symbols Added...");
                            this.putDataIntoTickerMap(messageJSONObject.getJSONArray("symbollist"));
                        case "symbols removed":
                            log.info("Removed Symbols: "+messageJSONObject);
                        break;

                        case "touchline":
                            messageJSONObject.getJSONArray("symbollist").forEach( ticker -> {
                                System.out.println("MAP TouchLine ->>>"+ticker);
                                //todo: action to touchline map
                            });
                            if (TrueDataWebSocket.this.onTickerArrivalListener != null) {
                                TrueDataWebSocket.this.onTickerArrivalListener.touchLineHandler(messageJSONObject.getJSONArray("symbollist"));
                            }
                            break;

                        case "HeartBeat":
                            log.info("Message " + messageJSONObject.get("message") + " Time: " + messageJSONObject.get("timestamp"));
                            lastHeartBeatTime = new Date();
                            break;

                        default:
                            log.info(messageJSONObject.toString());
                    }
                }
                if (messageJSONObject.getJSONArray("trade") != null) {
                    JSONArray tradeArray = messageJSONObject.getJSONArray("trade");
                    if (TrueDataWebSocket.this.onTickerArrivalListener != null) {
                        TrueDataWebSocket.this.onTickerArrivalListener.tickHandler(TickData.from(tradeArray));
                    }
                }
                if (messageJSONObject.getBoolean("success") == false) {
                    log.info("Not connected");
                    log.info(messageJSONObject.toString());
                }
//                if (messageJSONObject.getJSONArray("bidask") != null) {
//                    JSONArray bidaskArr = messageJSONObject.getJSONArray("bidask");
//                    //todo: craete listener for bidask data
//                    if (TrueDataWebSocket.this.onTickerArrivalListener != null) {
//                        TrueDataWebSocket.this.onTickerArrivalListener.bidAskHandler(bidaskArr);
//                    }
//                }
            }

            private void putDataIntoTickerMap(JSONArray symbollist) {
                symbollist.forEach( ticker -> {
                    tickerMap.put(((JSONArray) ticker).toList().get(1).toString(), ((JSONArray) ticker).toList().get(0).toString());
                });
            }

            public void onBinaryMessage(WebSocket websocket, byte[] binary) {
                log.info("Binary Message Received .......");
                try {
                    super.onBinaryMessage(websocket, binary);
                } catch (Exception var4) {
                    var4.printStackTrace();
                    if (TrueDataWebSocket.this.onErrorListener != null) {
                        TrueDataWebSocket.this.onErrorListener.onError(var4);
                    }
                }
            }

            public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) {
                log.info("TrueData WebSocket Disconnected...");
                if (TrueDataWebSocket.this.onDisconnectedListener != null) {
                    TrueDataWebSocket.this.onDisconnectedListener.onDisconnected();
                }
            }

            public void onError(WebSocket websocket, WebSocketException cause) {
                try {
                    super.onError(websocket, cause);
                } catch (Exception var4) {
                    var4.printStackTrace();
                    if (TrueDataWebSocket.this.onErrorListener != null) {
                        TrueDataWebSocket.this.onErrorListener.onError(var4);
                    }
                }
            }
        };
    }

    public void disconnect() {
        if (this.ws != null && this.ws.isOpen()) {
            this.ws.disconnect();
        }
    }

    public boolean isConnectionOpen() {
        return this.ws != null && this.ws.isOpen();
    }

    public void addSymbols() {
        if (this.ws != null) {
            if (this.ws.isOpen()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("method", this.method);
                jsonObject.put("symbols", this.symbols);
                this.ws.sendText(jsonObject.toString());
            } else if (this.onErrorListener != null) {
                this.onErrorListener.onError(new TrueDataException("ticker is not connected", "504"));
            }
        } else if (this.onErrorListener != null) {
            this.onErrorListener.onError(new TrueDataException("ticker is null not connected", "504"));
        }
    }

    public void reConnect() {
        try {
            this.ws.connect();
        } catch (WebSocketException var2) {
            var2.printStackTrace();
        }
    }

    public TimerTask reConnectTimerTask(){
        return new TimerTask() {
            @Override
            public void run(){
                log.info("ReConnecting TrueData Websocket...");
                reConnect();
            }
        };
    }

//    function handleTouchline(touchline) {
//        return {
//                Symbol: touchline[0],
//                LastUpdateTime: touchline[2],
//                LTP: +touchline[3],
//                TickVolume: +touchline[4],
//                ATP: +touchline[5],
//                TotalVolume: +touchline[6],
//                Open: +touchline[7],
//                High: +touchline[8],
//                Low: +touchline[9],
//                Previous_Close: +touchline[10],
//                Today_OI: +touchline[11],
//                Previous_Open_Interest_Close: +touchline[12],
//                Turnover: +touchline[13],
//                Bid: +touchline[14] || 0,
//                BidQty: +touchline[15] || 0,
//                Ask: +touchline[16] || 0,
//                AskQty: +touchline[17] || 0,
//  };
//    }
//
//    function handleRealTimeData(tradeArray) {
//        return {
//                Symbol: touchlineMap[tradeArray[0]],
//                Symbol_ID: +tradeArray[0],
//                Timestamp: tradeArray[1],
//                LTP: +tradeArray[2],
//                LTQ: +tradeArray[3],
//                ATP: +tradeArray[4],
//                Volume: +tradeArray[5],
//                Open: +tradeArray[6],
//                High: +tradeArray[7],
//                Low: +tradeArray[8],
//                Prev_Close: +tradeArray[9],
//                OI: +tradeArray[10],
//                Prev_Open_Int_Close: +tradeArray[11],
//                Day_Turnover: +tradeArray[12],
//                Special: tradeArray[13],
//                Tick_Sequence_No: +tradeArray[14],
//                Bid: tradeArray[15] !== undefined ? +tradeArray[15] : bidDeactivatedMessage,
//                Bid_Qty: tradeArray[16] !== undefined ? +tradeArray[16] : bidDeactivatedMessage,
//                Ask: tradeArray[17] !== undefined ? +tradeArray[17] : bidDeactivatedMessage,
//                Ask_Qty: tradeArray[18] !== undefined ? +tradeArray[18] : bidDeactivatedMessage,
//  };
//    }
//
//    public void handleBidaskData(bidaskArray) {
//        return {
//                Symbol: touchlineMap[bidaskArray[0]],
//                SymbolId: bidaskArray[0],
//                Time: bidaskArray[1],
//                Bid: +bidaskArray[2],
//                BidQty: +bidaskArray[3],
//                Ask: +bidaskArray[4],
//                AskQty: +bidaskArray[5],};
//    }
}