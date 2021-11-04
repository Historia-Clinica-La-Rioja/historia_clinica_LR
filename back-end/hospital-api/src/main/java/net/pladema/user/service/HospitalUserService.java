package net.pladema.user.service;

import net.pladema.user.controller.service.domain.UserPersonInfoBo;
import net.pladema.user.application.port.HospitalUserStorage;
import org.springframework.stereotype.Service;

@Service
public class HospitalUserService {

    private final HospitalUserStorage hospitalUserStorage;

    public HospitalUserService(HospitalUserStorage hospitalUserStorage) {
        this.hospitalUserStorage = hospitalUserStorage;
    }


    public UserPersonInfoBo getUserPersonInfo(Integer userId) {
        return hospitalUserStorage.getUserPersonInfo(userId)
                .get();
    }
}
