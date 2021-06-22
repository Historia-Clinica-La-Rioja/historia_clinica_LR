package ar.lamansys.immunization.domain.vaccine.doses;

import lombok.Getter;

@Getter
public enum VaccineDoseBo {

    DOSE_0((short) 1, "Dosis 0", (short) 1),
    DOSE_1((short) 2, "1er Dosis", (short) 1),
    DOSE_1_REINFORCEMENT((short) 3, "1er Refuerzo", (short) 4),
    DOSE_1_RAD26_S((short) 4, "1ra Dosis - rAd26-s", (short) 1),
    DOSE_2((short) 5, "2da Dosis", (short) 2),
    DOSE_2_RAD5_S((short) 6, "2da Dosis - rAd5-s", (short) 2),
    DOSE_2_REINFORCEMENT((short) 7, "2do Refuerzo", (short) 5),
    DOSE_3((short) 8, "3er Dosis", (short) 3),
    DOSE_3_REINFORCEMENT((short) 9, "3er Refuerzo", (short) 6),
    DOSE_4((short) 10, "4ta Dosis", (short) 4),
    DOSE_5((short) 11, "5ta Dosis", (short) 5),
    DOSE_6((short) 12, "6ta Dosis", (short) 6),
    DOSE_7((short) 13, "7ma Dosis", (short) 7),
    DOSE_8((short) 14, "8va Dosis", (short) 8),
    DOSE_9((short) 15, "9na Dosis", (short) 9),
    DOSE_10((short) 16, "10ma Dosis", (short) 10),
    ADDITIONAL((short) 17, "Adicional", (short) 1),
    DOSE_ANNUAL((short) 18, "Dosis Anual", (short) 3),
    DOSE_UNIQUE((short) 19, "Dosis Única", (short) 1),
    REINFORCEMENT((short) 20, "Refuerzo", (short) 4),
    UNIQUE_DOSE((short) 21, "Única Dosis", (short) 1),
            ;

    private final Short id;

    private final String description;

    private final Short order;

    VaccineDoseBo(Short id, String description, Short order) {
        this.id = id;
        this.description = description;
        this.order = order;
    }

    public static VaccineDoseBo map(Short id) {
        for(VaccineDoseBo e : values()) {
            if(e.id.equals(id)) return e;
        }
        throw new VaccineDosisException(VaccineDosisExceptionEnum.INVALID_DOSE, String.format("La dosis %s no existe", id));
    }
}
