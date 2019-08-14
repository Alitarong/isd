package org.isd.pojo;

import java.util.Date;

/**
 * 用户表
 */
public class User {
    private String id;
    private String nickname;
    private String avatar;
    private String 	accountno;//账号 数据库里新添加的字段
    private String deviceid;
    private String location;
    private Integer subscribed;
    private Date member_expire;
    private Date create_time;
    private Date modify_time;
    private String access_token;
    private Date access_expire;
    private String refresh_token;
    private Date refresh_expire;
    private Date lastlogin;


    public String getAccountno() {
        return accountno;
    }

    public void setAccountno(String accountno) {
        this.accountno = accountno;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Integer subscribed) {
        this.subscribed = subscribed;
    }

    public Date getMember_expire() {
        return member_expire;
    }

    public void setMember_expire(Date member_expire) {
        this.member_expire = member_expire;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getModify_time() {
        return modify_time;
    }

    public void setModify_time(Date modify_time) {
        this.modify_time = modify_time;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Date getAccess_expire() {
        return access_expire;
    }

    public void setAccess_expire(Date access_expire) {
        this.access_expire = access_expire;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public Date getRefresh_expire() {
        return refresh_expire;
    }

    public void setRefresh_expire(Date refresh_expire) {
        this.refresh_expire = refresh_expire;
    }

    public Date getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(Date lastlogin) {
        this.lastlogin = lastlogin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", accountno='" + accountno + '\'' +
                ", deviceid='" + deviceid + '\'' +
                ", location='" + location + '\'' +
                ", member_expire=" + member_expire +
                ", create_time=" + create_time +
                ", modify_time=" + modify_time +
                ", access_token='" + access_token + '\'' +
                ", access_expire=" + access_expire +
                ", refresh_token='" + refresh_token + '\'' +
                ", refresh_expire=" + refresh_expire +
                ", lastlogin=" + lastlogin +
                '}';
    }
}
