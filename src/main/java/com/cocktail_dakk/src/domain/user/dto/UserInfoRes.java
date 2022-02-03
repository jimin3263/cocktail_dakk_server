package com.cocktail_dakk.src.domain.user.dto;

import com.cocktail_dakk.src.domain.user.UserInfo;
import com.cocktail_dakk.src.domain.user.UserKeyword;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class UserInfoRes {
    private Long id;
    private String deviceNum;
    private String nickname;
    private Integer age;
    private String sex;
    private Integer alcoholLevel;
    private List<UserKeyword> userKeywords;

    public UserInfoRes(UserInfo userInfo){
        this.id = userInfo.getUserInfoId();
        this.deviceNum = userInfo.getDeviceNum();
        this.nickname = userInfo.getNickname();
        this.age = userInfo.getAge();
        this.sex = userInfo.getSex();
        this.alcoholLevel = userInfo.getAlcoholLevel();
    }
}
