package in.stockpe.stocksocket.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockSocketRes {
    private String symbol;
    private Long price;
}
