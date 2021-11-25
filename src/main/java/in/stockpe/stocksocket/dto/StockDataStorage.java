package in.stockpe.stocksocket.dto;

import in.stockpe.stocksocket.truedata.dto.TickData;

import java.util.HashMap;
import java.util.Map;

public class StockDataStorage {
    public static Map<String, TickData> stockStorage = new HashMap<>();
}
