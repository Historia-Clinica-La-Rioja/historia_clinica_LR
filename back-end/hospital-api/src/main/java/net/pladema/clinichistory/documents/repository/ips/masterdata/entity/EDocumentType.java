package net.pladema.clinichistory.documents.repository.ips.masterdata.entity;

import net.pladema.sgx.exceptions.NotFoundException;

public enum EDocumentType {

    ANAMNESIS(1, "anamnesis", "anamnesis"),
    EVALUATION_NOTE(2, "evolutionNote", "evolutionnote"),
    EPICRISIS(3, "epicrisis", "epicrisis"),
    OUTPATIENT(4, "ambulatoria", "outpatient");

    private Short id;
    private String value;
    private String template;

    EDocumentType(Number id, String value, String template) {
        this.id = id.shortValue();
        this.value = value;
        this.template = template;
    }
 
    public String getValue() {
        return value;
    }
    public Short getId() {
        return id;
    }
    public String getTemplate(){
        return template;
    }

    public static EDocumentType map(Short id) {
        for(EDocumentType e : values()) {
            if(e.id.equals(id)) return e;
        }
        throw new NotFoundException("role-not-exists", String.format("El rol %s no existe", id));
    }

}
