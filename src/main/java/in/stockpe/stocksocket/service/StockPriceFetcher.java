package in.stockpe.stocksocket.service;

import in.stockpe.stocksocket.domain.StockData;
import in.stockpe.stocksocket.domain.StockDataRepo;
import in.stockpe.stocksocket.dto.response.StockSocketRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StockPriceFetcher {
    public void getStockPrices() {
        Random random = new Random();

        //todo: python socket for truedata or other data provider
        StockDataRepo.stockDataList = Arrays.asList(
                StockData.builder().ticker("TCS").ltp(random.nextInt(2000)+11l).build(),
                StockData.builder().ticker("INFY").ltp(random.nextInt(20)+90l).build()
        ).stream().collect(Collectors.toSet());

        //log.info(StockDataRepo.stockDataList.toString());
    }
}
