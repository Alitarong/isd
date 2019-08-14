package org.isd.service.impl;

import org.isd.dao.ChapterMapper;
import org.isd.dao.SubscribeMapper;
import org.isd.dao.UserMapper;
import org.isd.dao.VideoMapper;
import org.isd.pojo.Subscribe;
import org.isd.pojo.User;
import org.isd.pojo.Video;
import org.isd.service.SubscribeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 购买业务实现
 */
@Service
@Transactional
public class SubscribeServiceImpl implements SubscribeService {

    /** 导入用户 dao **/
    @Resource
    private UserMapper userMapper;

    /** 导入视频 dao **/
    @Resource
    private VideoMapper videoMapper;

    /** 导入小节 dao **/
    @Resource
    private ChapterMapper chapterMapper;

    /** 导入购买 dao **/
    @Resource
    private SubscribeMapper subscribeMapper;

    /**
     *保存购买信息
     *
     *@参数 [userid, v_id]
     *@返回值 void
     */
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

            //存入购买信息
            subscribeMapper.save(subscribe);
        }

        //更新用户订单数量
        User user=userMapper.selectById(userid);
        user.setSubscribed(subscribeMapper.total(userid));

        userMapper.update(user);

    }

    /**
     *用户购买的视频 分页显示
     *
     *@参数 [userid, page, rows]
     *@返回值 java.util.List<org.isd.pojo.Subscribe>
     */
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

    /**
     *查询用户购买
     *
     *@参数 [userid]
     *@返回值 java.lang.Integer
     */
    @Override
    public Integer total(String userid) {

        return subscribeMapper.total(userid);

    }

    /**
     *判断用户是否购买
     *
     *@参数 [userid, v_id]
     *@返回值 boolean
     */
    @Override
    public boolean isPurchased(String userid, Integer v_id) {

        Subscribe subscribe =subscribeMapper.findByUserVid(userid,v_id);

        return (subscribe != null);

    }
}
