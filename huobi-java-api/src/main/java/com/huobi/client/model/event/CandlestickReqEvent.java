package com.huobi.client.model.event;

import java.util.List;

import com.huobi.client.model.Candlestick;
import com.huobi.client.model.enums.CandlestickInterval;

/**
 * The candlestick/kline data received by subscription of candlestick/kline.
 */
public class CandlestickReqEvent {

  private String symbol;
  private long timestamp;
  private CandlestickInterval interval;
  private List<Candlestick> data;

  /**
   * Get the symbol you subscribed.
   *
   * @return The symbol, like "btcusdt".
   */
  public String getSymbol() {
    return symbol;
  }

  /**
   * Get the UNIX formatted timestamp generated by server in UTC.
   *
   * @return The timestamp.
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Get candlestick/kline interval you subscribed.
   *
   * @return The interval, see {@link CandlestickInterval}
   */
  public CandlestickInterval getInterval() {
    return interval;
  }

  /**
   * Get the data of candlestick/kline
   *
   * @return The candlestick/kline data, see {@link Candlestick}
   */
  public List<Candlestick> getData() {
    return data;
  }

  public void setInterval(CandlestickInterval interval) {
    this.interval = interval;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public void setData(List<Candlestick>  data) {
    this.data = data;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }
}