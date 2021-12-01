package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;


import ar.lamansys.sgx.shared.exceptions.NotFoundException;

public enum ESourceType {

    HOSPITALIZATION(0, "hospitalization"),
    OUTPATIENT(1, "outpatient"),
    RECIPE(2, "recipes"),
    ORDER(3, "orders"),
    EMERGENCY_CARE(4, "emergency_care"),
    IMMUNIZATION(5, "immunization"),
    ODONTOLOGY(6, "odontology"),
    NURSING(7, "nursing"),
    COUNTER_REFERENCE(8, "counter_reference");

    private Short id;
    private String value;

    ESourceType(Number id, String value) {
        this.id = id.shortValue();
        this.value = value;;
    }
 
    public String getValue() {
        return value;
    }
    public Short getId() {
        return id;
    }

    public static ESourceType map(Short id) {
        for(ESourceType e : values()) {
            if(e.id.equals(id)) return e;
        }
        throw new NotFoundException("source-not-exists", String.format("La fuente de datos %s no existe", id));
    }

}
