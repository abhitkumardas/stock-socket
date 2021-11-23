package in.stockpe.stocksocket.domain;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Objects;

@Data
@Builder
@ToString
public class StockData {
    private String ticker;
    private Long ltp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockData stockData = (StockData) o;
        return ticker.equals(stockData.ticker) && ltp.equals(stockData.ltp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker, ltp);
    }
}
