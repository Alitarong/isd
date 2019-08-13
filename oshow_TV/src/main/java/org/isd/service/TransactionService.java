package org.isd.service;


import org.isd.pojo.Transaction;

public interface TransactionService {

    /**支付接口**/
    //String placeOrder(User user, Integer v_id, Integer price);

    /** 查询支付结果、订单状态 **/
    boolean selectByNo(String out_trade_no);

}
