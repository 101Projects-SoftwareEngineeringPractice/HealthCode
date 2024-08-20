package org.software.code.service;

import org.software.code.model.dto.HealthCodeManagerDto;
import org.software.code.model.dto.NucleicAcidTestPersonnelDto;
import org.software.code.model.dto.UserInfoDto;
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

    void modifyUserInfo(long uid, String name, String phoneNumber, String identityCard, int district, int street, long community, String address);

    void statusNucleicAcidTestUser(long tid, boolean status);

    void statusManager(long mid, boolean status);

    void userModify(long uid, String name, String phoneNumber, int districtId, int streetId, long communityId, String address);

    void nucleicAcidOpposite(long tid);

    void manageOpposite(long mid);

    void addUserInfo(long uid, String name, String phoneNumber, String identityCard, int district, int street, long community, String address);

    String userLogin_test(String code);

    void deleteUserInfo(long uid);


}
