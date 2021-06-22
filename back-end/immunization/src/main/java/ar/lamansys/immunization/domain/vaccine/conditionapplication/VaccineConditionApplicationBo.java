package ar.lamansys.immunization.domain.vaccine.conditionapplication;

import lombok.Getter;

@Getter
public enum VaccineConditionApplicationBo {

    NATIONAL_CALENDAR((short) 3, "Calendario Nacional"),
    RISK_FACTOR((short) 7, "Personas con factores de riesgo"),
    ASPLENIC((short) 13, "Asplénico"),
    PROPHYLAXIS_POST_DEAD_ANIMAL((short) 18, "Profilaxis Post Exposición - Contacto con Animal Muerto o Desaparecido"),
    PROPHYLAXIS_POST_OBSERVED_ANIMAL((short) 19, "Profilaxis Post Exposición - Contacto con Animal Observado"),
    PROPHYLAXIS_PRE_EXHIBITION((short) 20, "Profilaxis Preexposicion"),
    TRAVEL_ENDEMIC_AREA((short) 22, "Viaje a Zona Endémica"),
    ENDEMIC_AREA((short) 23, "Zona Endemica"),
    CAMPAIGN((short) 37, "Campaña"),
    GREATER_THAN_50((short) 38, "Mayor de 50 años"),
    MEDICAL_PRESCRIPTION((short) 39, "Prescripción médica"),
    COMPLEMENT_DEFICIT((short) 40, "Déficit de complemento"),
    STRATEGIC_STAFF((short) 41, "Personal Estratégico"),
    GREATER_THAN_60((short) 42, "Personas de 60 o más años"),
    BETWEEN_18_59_WITH_RISK_FACTOR((short) 43, "Personas de 18 a 59 años con Factores de Riesgo"),
    ;

    private final Short id;

    private final String description;

    VaccineConditionApplicationBo(Short id, String description) {
        this.id = id;
        this.description = description;
    }

    public static VaccineConditionApplicationBo map(Short id) {
        for(VaccineConditionApplicationBo e : values()) {
            if(e.id.equals(id)) return e;
        }
        throw new VaccineConditionApplicationException
                (VaccineConditionApplicationExceptionEnum.INVALID_CONDITION_APPLICATION,
                        String.format("La condición de aplicación %s no existe", id));
    }


}
