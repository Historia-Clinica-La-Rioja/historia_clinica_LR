package net.pladema.hl7.supporting.terminology.coding;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class CodingSystem {

    public static final String LOINC="http://loinc.org";
    public static final String SNOMED="http://snomed.info/sct";
    public static final String FEDERADOR="https://federador.msal.gob.ar/uri";
    public static final String REFES="http://fhir.msal.gov.ar/refes";
	public static final String REFES_RDI="http://refes.msal.gob.ar";
    public static final String REFES2="http://www.refesp.gob.ar/codigo";
    public static final String RENAPER="http://www.renaper.gob.ar/dni";

    //Absent and Unknown Data - IPS
    public static final String NODATA = "http://hl7.org/fhir/uv/ips/CodeSystem/absent-unknown-uv-ips";
	public static final String SNOMED_RDI_GENERIC="http://fhir.msal.gob.ar/RDI/CodeSystem/CSMedicamentosGenericosSCT";
	public static final String SNOMED_RDI_COMMERCIAL="http://fhir.msal.gob.ar/RDI/CodeSystem/CSMedicamentosComercialesSCT";
	public static final String SNOMED_RDI_REASON="http://fhir.msal.gob.ar/RDI/CodeSystem/csproblemas-salud";


    @UtilityClass
    public static class SERVER {
        public static final String PATIENT_SERVICE="/masterfile-federacion-service/fhir/";
    }

    @UtilityClass
    public static class Allergy {
        public static final String VERIFICATION_STATUS="http://terminology.hl7.org/CodeSystem/allergyintolerance-verification";
        public static final String CLINICAL_STATUS="http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical";
    }

    @UtilityClass
    public static class Condition {
        public static final String STATUS="http://terminology.hl7.org/CodeSystem/condition-clinical";
        public static final String VERIFICATION="http://terminology.hl7.org/CodeSystem/condition-ver-status";
    }

    @UtilityClass
    public static class Immunization {
        public static final String NOMIVACESCHEMA ="http://fhir.msal.gov.ar/CodeSystem/NOMIVAC-esquema";
        public static final String NOMIVACONDITIONCS="http://fhir.msal.gov.ar/Codesystem/nomivac-condicion-aplicacion";
    }

    @UtilityClass
    public static class MedicationStatement {
        public static final String DOSE="http://terminology.hl7.org/CodeSystem/dose-rate-type";
    }

    @UtilityClass
    public static class Patient {
        public static final String IDENTIFIER="http://www.renaper.gob.ar/dni";
    }

	@UtilityClass
	public static class Practitioner {
		public static final String IDENTIFIER_TYPE_SYSTEM="http://terminology.hl7.org/CodeSystem/v2-0203";
		public static final String IDENTIFIER_REFEPS="https://sisa.msal.gov.ar/REFEPS";
    }
	
    @UtilityClass
    public static class Coverage {
		public static final String PAYOR_IDENTIFIER_SYSTEM="http://ssalud.gob.ar/coberturas";
		public static final String CLASS_CODE="http://terminology.hl7.org/CodeSystem/coverage-class";
    }
    
	@UtilityClass
    public static class MedicationRequest {
		public static final String CATEGORY="http://terminology.hl7.org/CodeSystem/medicationrequest-category";
		public static final String DOSE_UNIT_SYSTEM="http://fhir.msal.gob.ar/RDI/CodeSystem/CSUnidadAsistencial";
	}
}
