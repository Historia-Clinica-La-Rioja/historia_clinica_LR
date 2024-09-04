package net.pladema.hl7.supporting.terminology.coding;

import lombok.experimental.UtilityClass;

import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;

@UtilityClass
public final class CodingValueSet {

    public static final String ABSENT_UNKNOWN="http://hl7.org/fhir/uv/ips/ValueSet/absent-unknown";
    public static final String DOC_TYPE="http://hl7.org/fhir/ValueSet/doc-typecodes";

    @UtilityClass
    public static class Condition {
        public static final String CATEGORY="http://hl7.org/fhir/uv/ips/ValueSet/problem-type-uv-ips";
        public static final String SEVERITY="http://hl7.org/fhir/uv/ips/ValueSet/condition-severity-uv-ips";

    }

	@UtilityClass
	public static class MedicationRequest {
		public enum CATEGORY {
			DISCHARGE("discharge","Discharge"),
			OUTPATIENT("outpatient","Outpatient"),
			INPATIENT("inpatient","Inpatient"),
			COMMUNITY("community","Community");
			private final String system;
			private final String code;
			private final String display;

			CATEGORY(String code, String display) {
				this.system = CodingSystem.MedicationRequest.CATEGORY;
				this.code = code;
				this.display = display;
			}
			public CodeableConcept getCodeableConcept() {
				return new CodeableConcept(new Coding(this.system,this.code,this.display));
			}
		}
	}

	@UtilityClass
	public static class ServiceRequest {
		public enum CATEGORY {
			LABORATORY_PROCEDURE("108252007","Laboratory procedure"),
			IMAGING("363679005","Imaging"),
			COUNSELLING("409063005","Counselling"),
			EDUCATION("409073007","Education"),
			SURGICAL_PROCEDURE("387713003","Surgical procedure");
			private final String system;
			private final String code;
			private final String display;

			CATEGORY(String code, String display) {
				this.system = CodingSystem.SNOMED;
				this.code = code;
				this.display = display;
			}
			public CodeableConcept getCodeableConcept() {
				return new CodeableConcept(new Coding(this.system,this.code,this.display));
			}
		}
	}
}
