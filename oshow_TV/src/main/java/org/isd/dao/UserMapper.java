package org.isd.dao;

import org.apache.ibatis.annotations.Param;
import org.isd.pojo.User;
import org.springframework.stereotype.Repository;

/**
 * 用户相关功能接口
 */
@Repository
public interface UserMapper {

    /** 删除用户 **/
    boolean delete(@Param("id") String id);

    /** 添加用户 **/
    void insert(User user);

    /** 修改用户 **/
    void update(User user);

    /** 根据设备用户的设备id号提取用户 **/
    User findByDeviceId(@Param("deviceid") String deviceid);

    /** 通过用户id查找信息 **/
    User selectById(@Param("id") String id);

}
