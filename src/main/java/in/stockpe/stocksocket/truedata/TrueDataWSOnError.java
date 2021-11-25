package in.stockpe.stocksocket.truedata;

import in.stockpe.stocksocket.truedata.http.exception.TrueDataException;

public interface TrueDataWSOnError {
    void onError(Exception var1);

    void onError(TrueDataException var1);

    void onError(String var1);
}
