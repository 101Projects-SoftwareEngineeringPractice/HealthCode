package org.software.code.service;

import org.software.code.dto.HealthCodeManagerDto;
import org.software.code.dto.NucleicAcidTestPersonnelDto;
import org.software.code.dto.UserInfoDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {


    UserInfoDto getUserByUID(long uid);

    UserInfoDto getUserByID(String identity_card);

    String userLogin(String code);

    String nucleicAcidTestUserLogin(String identityCard, String password);

    List<NucleicAcidTestPersonnelDto> getNucleicAcidTestUser();

    List<HealthCodeManagerDto> getManagerUser();

    void newNucleicAcidTestUser(String identityCard, String password, String name);

    void newMangerUser(String identityCard, String password, String name);

    String managerLogin(String identityCard, String password);

    void modifyUserInfo(long uid, String name, String phoneNumber, String identityCard, int district, int street, int community, String address);

    void statusNucleicAcidTestUser(long tid, boolean status);

    void statusManager(long mid, boolean status);

    long extractUidValidateToken(String token);

    void userModify(long uid, String name, String phoneNumber, int districtId, int streetId, int communityId, String address);

    void nucleicAcidOpposite(long tid);

    void manageOpposite(long mid);

    void addUserInfo(long uid, String name, String phoneNumber, String identityCard, int district, int street, int community, String address);

    /***测试***/
    String testOthers();

//    UserInfoDto getInfo(Long uid);

    //    void updateInfo(Long uid, int district);
//
    void testhhh(String hhh);
}
