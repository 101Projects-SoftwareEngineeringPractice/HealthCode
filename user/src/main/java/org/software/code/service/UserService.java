package org.software.code.service;

import org.software.code.model.dto.*;
import org.software.code.model.input.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserInfoDto getUserByUID(long uid);

    UserInfoDto getUserByID(String identity_card);

    String userLogin(String code);

    String nucleicAcidTestUserLogin(NucleicAcidsLoginRequest request);

    List<NucleicAcidTestPersonnelDto> getNucleicAcidTestUser();

    List<HealthCodeManagerDto> getManagerUser();

    void newNucleicAcidTestUser(CreateNucleicAcidRequest request);

    void newMangerUser(CreateManageRequest request);

    String managerLogin(ManagerLoginRequest request);

    void modifyUserInfo(UserInfoRequest request);

    void statusNucleicAcidTestUser(StatusNucleicAcidTestUserRequest request);

    void statusManager(StatusManagerRequest request);

    void userModify(UserModifyInput input);

    void nucleicAcidOpposite(long tid);

    void manageOpposite(long mid);

    void addUserInfo(UserInfoRequest request);

    String userLogin_test(String code);

    void deleteUserInfo(long uid);


}
