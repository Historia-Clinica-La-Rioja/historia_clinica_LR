package net.pladema.snowstorm.services.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cie10RuleFeature {

    public static final Short FEMALE = 1;
    public static final Short MALE = 2;

    private Short gender;

    private Short age;
}

