package org.isd.dao;

import org.apache.ibatis.annotations.Param;
import org.isd.pojo.Transaction;
import org.springframework.stereotype.Repository;

/**
 * 订单处理 -- DAO 层
 */
@Repository
public interface TransactionMapper {

    /** 用户点击支付，生成订单**/
    public int save(Transaction transaction);

    /** 更新用户 **/
    public void update(Transaction transaction);

    /** 加载交易过程 **/
    public Transaction findByOrderid(String orderid);

    /** 删除用户 **/
    public boolean delete(int transactionid);

    /** 通过商户订单号查询订单信息 **/
    Transaction selectByTranID(@Param(value = "outTradeNo") String outTradeNo);

    /** wx 回调，通过商户 id 修改订单状态 **/
    int updateByTranId(@Param("transaction1") Transaction transaction1);

}
