package ar.lamansys.sgh.clinichistory.application.calculatecie10;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cie10FacadeRuleFeature {

    public static final Short FEMALE = 1;
    public static final Short MALE = 2;

    private Short gender;

    private Short age;
}

