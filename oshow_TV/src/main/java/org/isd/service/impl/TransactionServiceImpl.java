package org.isd.service.impl;

import org.isd.dao.TransactionMapper;
import org.isd.pojo.Transaction;
import org.isd.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单业务实现
 */
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

   /** 导入订单 dao **/
   @Autowired
   private TransactionMapper transactionMapper;

   /**
    *通过商品订单查询
    *
    *@参数 [out_trade_no]
    *@返回值 boolean
    */
   @Override
   public boolean selectByNo(String out_trade_no) {

      Transaction transaction = transactionMapper.findByOrderid(out_trade_no);

      if (transaction == null && transaction.getId() == null){

         return false;

      }

      return true;

   }

/*    @Override
    public String placeOrder(User user, Integer v_id, Integer price) {
        String result="0";
        Transaction transaction = new Transaction();
        transaction.setUserid(user.getId());
        transaction.setV_id(v_id);
        transaction.setOrderid("oxygenshow-"+ C.getOrderid());
        //提取标题和图片
        Video video=videoMapper.selectById(v_id);
        if (video!=null){
            transaction.setName(video.getName());
            transaction.setCover(video.getCover());
            transaction.setPrice(video.getPrice());
        }else{
            result= "[{\"errno\":1,\"errmsg\":\"视频不存在\"}]";
            return result;
        }
        if(transaction.getPrice()==null || transaction.getPrice()==0){
            result= "[{\"errno\":1,\"errmsg\":\"价钱为空或为0\"}]";
            return result;
        }
        transaction.setStatus(0);
        transaction.setChannel(2);
        transaction.setCtime(new Date());
        transactionMapper.save(transaction);
        //将orderid存入cookie,轮询订单的状态
        C.setCookie(C.COOKIE_DEVICE,transaction.getOrderid(),24 * 3600);
        result=this.weixinpay(transaction);
        return result;
    }

    //微信支付接口
    private String weixinpay(Transaction tran){
        Map<String,Object> params=new HashMap<>();
        params.put("productid",tran.getV_id());
        params.put("productname",tran.getName());
        params.put("detail",tran.getUserid());
        params.put("extra","");
        params.put("orderid", tran.getOrderid());
        params.put("currency", "CNY");
        params.put("price", tran.getPrice() / 100.0);
        params.put("app", "oxygenshow");
        String ipaddr=C.getRequest().getRemoteAddr();
        if(ipaddr.contains("0:0:0:0:0:0:0:1")){
            ipaddr = "127.0.0.1";
        }
        params.put("ipaddr",ipaddr);
        String result=C.getPostResponse(C.WEIXINPAY,C.formatParameter(params));

        try{
            JSONArray array=JSONArray.fromObject(result);
            JSONObject object=array.getJSONObject(0);
            //保存支付的流水id
            if(object.getString(C.RESULT).equals(C.SUCCESS)){

            }else{
                String errmsg=object.getString(C.ERRMSG);
                object.replace(C.ERRMSG,C.decode(errmsg,"utf-8"));
                result=C.object2json(object);
            }
        }catch (Exception e){
        }

        return result;
    }*/

}
