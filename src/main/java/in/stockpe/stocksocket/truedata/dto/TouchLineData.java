package in.stockpe.stocksocket.truedata.dto;

import lombok.Builder;
import lombok.Data;
import org.json.JSONObject;

@Data
@Builder
public class TouchLineData {
    private String symbol;
    private String symbolId;
    private String lastUpdateTime;
    private String LTP;
    private String tickVolume;
    private String ATP;
    private String totalVolume;
    private String open;
    private String high;
    private String low;
    private String previousClose;
    private String todayOI;
    private String previousOpenInterestClose;
    private String turnover;
    private String bid;
    private String bidQty;
    private String ask;
    private String askQty;

    public TouchLineData from(JSONObject jsonObject) {
        return TouchLineData.builder()
                .build();
    }
}
