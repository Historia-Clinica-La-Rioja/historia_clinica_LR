package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;


import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum EDocumentType {

    ANAMNESIS(1, "anamnesis", "anamnesis"),
    EVALUATION_NOTE(2, "evolutionNote", "evolutionnote"),
    EPICRISIS(3, "epicrisis", "epicrisis"),
    OUTPATIENT(4, "ambulatoria", "outpatient"),
    RECIPE(5, "receta", "recipe_order_table"),
    ORDER(6, "orden", "recipe_order"),
    EMERGENCY_CARE(7, "guardia", "emergency_care"),
    IMMUNIZATION(8, "inmunización", "immunization"),
    ODONTOLOGY(9, "odontología", "odontology_consultation"),
    NURSING(10, "enfermería", "nursing_consultation"),
    COUNTER_REFERENCE(11, "contrarreferencia", "counter_reference"),
    INDICATION(12,"indicación","indication"),
    NURSING_EVOLUTION_NOTE(13, "nursingEvolutionNote", "nursing_evolution_note"),
	DIGITAL_RECIPE(14, "digitalRecipe", "digital_recipe"),
	TRIAGE(15, "triage","triage"),
	EMERGENCY_CARE_EVOLUTION(16, "emergencyCareEvolutionNote", "emergency_care_evolution_note"),
	MEDICAL_IMAGE_REPORT(17, "medicalImageReport", "report_image");

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
        throw new NotFoundException("document-type-not-exists", String.format("El tipo de documento %s no existe", id));
    }

	public static List<EDocumentType> getAllInternmentDocumentTypes(){
		return Stream.of(EDocumentType.ANAMNESIS, EDocumentType.EVALUATION_NOTE,
						EDocumentType.NURSING_EVOLUTION_NOTE, EDocumentType.EPICRISIS)
				.collect(Collectors.toList());
	}
}
