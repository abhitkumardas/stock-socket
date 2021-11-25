package in.stockpe.stocksocket.truedata;

import in.stockpe.stocksocket.truedata.dto.TickData;
import org.json.JSONArray;
import org.json.JSONObject;

public interface TrueDataWSLiveFeed {
    void tickHandler(TickData tickData);
    void touchLineHandler(JSONArray touchLineDataArr);
    void bidAskHandler(JSONArray bidAskDataArr);
    //void getObj(Object o);
}
