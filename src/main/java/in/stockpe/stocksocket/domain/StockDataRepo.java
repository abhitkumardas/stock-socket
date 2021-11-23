package in.stockpe.stocksocket.domain;


import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class StockDataRepo {

    public static Set<StockData> stockDataList = new HashSet<>();

}
