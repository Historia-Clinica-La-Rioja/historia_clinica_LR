package ar.lamansys.sgh.publicapi.domain;

import lombok.Getter;

@Getter
public enum GenderEnum {
    FEMALE((short) 1),
    MALE((short) 2);

    private final Short genderId;

    GenderEnum(short i) {
        genderId = i;
    }
}
