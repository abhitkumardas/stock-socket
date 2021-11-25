package in.stockpe.stocksocket.truedata.dto;

import in.stockpe.stocksocket.truedata.TrueDataWebSocket;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.json.JSONArray;

@Data
@Builder
@ToString
public class TickData {
    private String symbol;
    private String symbolId;
    private String timeStamp;
    private String ltp;
//    private String LTQ;
//    private String tickVolume;
//    private String ATP;
//    private String totalVolume;
//    private String open;
//    private String high;
//    private String low;
//    private String previousClose;
//    private String todayOI;
//    private String previousOpenInterestClose;
//    private String turnover;
//    private String bid;
//    private String bidQty;
//    private String ask;
//    private String askQty;

    public static TickData from(JSONArray tradeArr) {
        return TickData.builder()
                .symbol(TrueDataWebSocket.tickerMap.get(tradeArr.get(0).toString()))
                .symbolId(tradeArr.get(0).toString())
                .timeStamp(tradeArr.get(1).toString())
                .ltp(tradeArr.get(2).toString())
                .build();
    }
}
