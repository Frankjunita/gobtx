package com.huobi.client.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.huobi.client.RequestOptions;
import com.huobi.client.exception.HuobiApiException;
import com.huobi.client.impl.utils.JsonWrapper;
import com.huobi.client.impl.utils.TimeService;
import com.huobi.client.model.Withdraw;
import com.huobi.client.model.enums.WithdrawState;
import java.math.BigDecimal;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestGetWithdrawHistory {

  private RestApiRequestImpl impl = null;
  private static final String data = "{\n" +
      "    \n" +
      "    \"status\": \"ok\",\n" +
      "    \"data\": [\n" +
      "      {\n" +
      "        \"id\": 1171,\n" +
      "        \"type\": \"withdraw\",\n" +
      "        \"currency\": \"ht\",\n" +
      "        \"tx-hash\": \"ed03094b84eafbe4bc16e7ef766ee959885ee5bcb265872baaa9c64e1cf86c2b\",\n"
      +
      "        \"amount\": 7.457467,\n" +
      "        \"address\": \"rae93V8d2mdoUQHwBDBdM4NHCMehRJAsbm\",\n" +
      "        \"address-tag\": \"100040\",\n" +
      "        \"fee\": 345,\n" +
      "        \"state\": \"confirmed\",\n" +
      "        \"created-at\": 1510912472199,\n" +
      "        \"updated-at\": 1511145876575\n" +
      "      }\n" +
      "    ]\n" +
      "}";
  private static final String Errordata = "{\n" +
      "    \n" +
      "    \"status\": \"ok\",\n" +
      "    \"data\": [\n" +
      "      {\n" +
      "        \"id\": 1171,\n" +
      "        \"type\": \"deposit\",\n" +
      "        \"currency\": \"xrp\",\n" +
      "        \"amount\": 7.457467,\n" +
      "        \"address\": \"rae93V8d2mdoUQHwBDBdM4NHCMehRJAsbm\",\n" +
      "        \"address-tag\": \"100040\",\n" +
      "        \"fee\": 0,\n" +
      "        \"state\": \"safe\",\n" +
      "        \"created-at\": 1510912472199,\n" +
      "        \"updated-at\": 1511145876575\n" +
      "      }\n" +
      "    ]\n" +
      "}";


  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void Initialize() {
    impl = new RestApiRequestImpl("12345", "67890", new RequestOptions());
  }

  @Test
  public void test() {
    RestApiRequest<List<Withdraw>> restApiRequest = impl.getWithdrawHistory("btc", 24966984923L, 1);
    assertTrue(restApiRequest.request.url().toString().contains("/v1/query/deposit-withdraw"));
    assertEquals("GET", restApiRequest.request.method());
    assertNotNull(restApiRequest.request.url().queryParameter("Signature"));
    assertEquals("btc", restApiRequest.request.url().queryParameter("currency"));
    assertEquals("24966984923", restApiRequest.request.url().queryParameter("from"));
    assertEquals("1", restApiRequest.request.url().queryParameter("size"));
    assertEquals("withdraw", restApiRequest.request.url().queryParameter("type"));


  }

  @Test
  public void testResult_Normal() {
    RestApiRequest<List<Withdraw>> restApiRequest =
        impl.getWithdrawHistory("ht", 24966984923L, 1);
    JsonWrapper jsonWrapper = JsonWrapper.parseFromString(data);
    List<Withdraw> withdrawList = restApiRequest.jsonParser.parseJson(jsonWrapper);
    assertEquals(new BigDecimal("345"), withdrawList.get(0).getFee());
    assertEquals(1171L, withdrawList.get(0).getId());
    assertEquals(TimeService.convertCSTInMillisecondToUTC(1510912472199L),
        withdrawList.get(0).getCreatedTimestamp());
    assertEquals(TimeService.convertCSTInMillisecondToUTC(1511145876575L),
        withdrawList.get(0).getUpdatedTimestamp());
    assertEquals(new BigDecimal("7.457467"), withdrawList.get(0).getAmount());
    assertEquals("rae93V8d2mdoUQHwBDBdM4NHCMehRJAsbm", withdrawList.get(0).getAddress());
    assertEquals("100040", withdrawList.get(0).getAddressTag());
    assertEquals("ht", withdrawList.get(0).getCurrency());
    assertEquals("ed03094b84eafbe4bc16e7ef766ee959885ee5bcb265872baaa9c64e1cf86c2b",
        withdrawList.get(0).getTxHash());
    assertEquals(WithdrawState.CONFIRMED, withdrawList.get(0).getWithdrawState());
  }

  @Test
  public void testResult_Unexpected() {

    RestApiRequest<List<Withdraw>> restApiRequest =
        impl.getWithdrawHistory("htbtc", 24966984923L, 1);
    JsonWrapper jsonWrapper = JsonWrapper.parseFromString(Errordata);
    thrown.expect(HuobiApiException.class);
    thrown.expectMessage("Get json item field");
    restApiRequest.jsonParser.parseJson(jsonWrapper);
  }

  @Test
  public void testInvalidSymbol() {

    thrown.expect(HuobiApiException.class);
    impl.getWithdrawHistory("?", 24966984923L, 1);

  }


}
