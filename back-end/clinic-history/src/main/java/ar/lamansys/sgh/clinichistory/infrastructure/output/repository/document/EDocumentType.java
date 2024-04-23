package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;


import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum EDocumentType {

    ANAMNESIS(1, "anamnesis", "anamnesis", "Evaluación de ingreso"),
    EVALUATION_NOTE(2, "evolutionNote", "evolutionnote", "Nota de evolución"),
    EPICRISIS(3, "epicrisis", "epicrisis", "Epicrisis"),
    OUTPATIENT(4, "ambulatoria", "outpatient", "Ambulatoria"),
    RECIPE(5, "receta", "recipe_order_table", "Receta"),
    ORDER(6, "orden", "recipe_order", "Orden"),
    EMERGENCY_CARE(7, "guardia", "emergency_care", "Guardia"),
    IMMUNIZATION(8, "inmunización", "immunization", "Inmunización"),
    ODONTOLOGY(9, "odontología", "odontology_consultation", "Odontología"),
    NURSING(10, "enfermería", "nursing_consultation", "Enfermería"),
    COUNTER_REFERENCE(11, "contrarreferencia", "counter_reference", "Contrarreferencia"),
    INDICATION(12,"indicación","indication", "Indicación"),
    NURSING_EVOLUTION_NOTE(13, "nursingEvolutionNote", "nursing_evolution_note", "Nota de evolución de enfermería"),
	DIGITAL_RECIPE(14, "digitalRecipe", "digital_recipe", "Receta digital"),
	TRIAGE(15, "triage","triage", "Triage"),
	EMERGENCY_CARE_EVOLUTION(16, "emergencyCareEvolutionNote", "emergency_care_evolution_note", "Nota de evolución de guardia"),
	MEDICAL_IMAGE_REPORT(17, "medicalImageReport", "report_image", "Reporte de imagen médica"),
	SURGICAL_HOSPITALIZATION_REPORT(18, "surgicalReport", "surgical_report", "Parte quirúrgico"),
	ANESTHETIC_REPORT(20, "anestheticReport", "anesthetic_report", "Parte anestésico"),
    ;

    private Short id;
    private String value;
    private String template;
	private String description;

    EDocumentType(Number id, String value, String template, String description) {
        this.id = id.shortValue();
        this.value = value;
        this.template = template;
		this.description = description;
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
	public String getDescription() { return description; }

    public static EDocumentType map(Short id) {
        for(EDocumentType e : values()) {
            if(e.id.equals(id)) return e;
        }
        throw new NotFoundException("document-type-not-exists", String.format("El tipo de documento %s no existe", id));
    }

	public static List<EDocumentType> getAllInternmentDocumentTypes(){
		return Stream.of(EDocumentType.ANAMNESIS, EDocumentType.EVALUATION_NOTE,
						EDocumentType.NURSING_EVOLUTION_NOTE, EDocumentType.EPICRISIS,
                        EDocumentType.ANESTHETIC_REPORT, EDocumentType.SURGICAL_HOSPITALIZATION_REPORT)
				.collect(Collectors.toList());
	}
}
