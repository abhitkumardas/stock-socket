package in.stockpe.stocksocket.service;

import in.stockpe.stocksocket.domain.StockData;
import in.stockpe.stocksocket.domain.StockDataRepo;
import in.stockpe.stocksocket.dto.response.StockSocketRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@EnableScheduling
@Slf4j
public class StockService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private StockPriceFetcher stockPriceFetcher;

    @Autowired
    private TrueDataService trueDataService;

    //@Scheduled(fixedRate = 500)
    public void updateLivePrices() {
        stockPriceFetcher.getStockPrices();
    }

    //@Scheduled(fixedRate = 1000)
    public void updateLi() {
        trueDataService.webSocketClient();
    }

    //@Scheduled(fixedRate = 1000)
    public void publishLivePrices() {
        //log.info("response..."+new Date());
        if (StockDataRepo.stockDataList.stream().findFirst().isPresent()) {
            simpMessagingTemplate.convertAndSend("/topic/live-stock-price", StockDataRepo.stockDataList);
            //log.info(StockDataRepo.stockDataList.toString());
        }else {
            log.error("ERROR----");
        }
    }

    private List<StockSocketRes> buildLiveStockPriceRes() {
        return Arrays.asList(
                StockSocketRes.builder().symbol("TCS").price(1023l).build(),
                StockSocketRes.builder().symbol("INFY").price(1023l).build()
        );
    }
}

