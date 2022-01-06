package net.pladema.snvs.domain.doctor;

import lombok.Getter;

@Getter
public class DoctorInfoBo {

    private final Integer id;

    public DoctorInfoBo(Integer id) {
        this.id = id;
    }
}
