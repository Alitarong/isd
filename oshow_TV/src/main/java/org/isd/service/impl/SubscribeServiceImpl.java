package org.isd.service.impl;

import com.isd.oxygenshow.dao.ChapterMapper;
import com.isd.oxygenshow.dao.SubscribeMapper;
import com.isd.oxygenshow.dao.UserMapper;
import com.isd.oxygenshow.dao.VideoMapper;
import com.isd.oxygenshow.entity.Subscribe;
import com.isd.oxygenshow.entity.User;
import com.isd.oxygenshow.entity.Video;
import com.isd.oxygenshow.service.SubscribeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SubscribeServiceImpl implements SubscribeService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private VideoMapper videoMapper;
    @Resource
    private ChapterMapper chapterMapper;
    @Resource
    private SubscribeMapper subscribeMapper;

    //保存购买信息
    @Override
    public void save(String userid, Integer v_id) {
        Subscribe subscribe=subscribeMapper.findByUserVid(userid,v_id);
        Video video=videoMapper.selectById(v_id);
        if(subscribe==null){
            subscribe=new Subscribe();
            subscribe.setV_id(v_id);
            subscribe.setUserid(userid);
            subscribe.setName(video.getName());
            subscribe.setCover(video.getCover());
            subscribe.setText(video.getText());
            subscribe.setCtime(new Date());
            subscribe.setUtime(new Date());
            subscribe.setStatus(1);
            subscribeMapper.save(subscribe);
        }
        //更新用户订单数量
        User user=userMapper.selectById(userid);
        user.setSubscribed(subscribeMapper.total(userid));
        userMapper.update(user);
    }

    //用户购买的视频 分页显示
    @Override
    public List<Subscribe> listByPageUid(String userid, Integer page, Integer rows) {
       page =rows*(page-1);
       List<Subscribe> subscribes=subscribeMapper.listPageByUid(userid,page,rows);
       if(subscribes.size()>0){
            for(int i=0;i<subscribes.size();i++){
                Subscribe s=subscribes.get(i);
            }
       }else {

       }
       return subscribes;
    }

    @Override
    public Integer total(String userid) {
        return subscribeMapper.total(userid);
    }

    //判断用户是否购买
    @Override
    public boolean isPurchased(String userid, Integer v_id) {
        Subscribe subscribe =subscribeMapper.findByUserVid(userid,v_id);
        return (subscribe != null);
    }
}
