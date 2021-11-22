package in.stockpe.stocksocket.service;

import in.stockpe.stocksocket.dto.response.StockSocketRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class StockService {


    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Scheduled(fixedRate = 1000)
    public void publishLivePrices() {
        List<StockSocketRes> stockSocketResList = buildLiveStockPriceRes();
        simpMessagingTemplate.convertAndSend("/topic/live-stock-price", stockSocketResList);
    }

    private List<StockSocketRes> buildLiveStockPriceRes() {
        return Arrays.asList(
                StockSocketRes.builder().symbol("TCS").price(1023l).build(),
                StockSocketRes.builder().symbol("TCS").price(1023l).build()
        );
    }
}

