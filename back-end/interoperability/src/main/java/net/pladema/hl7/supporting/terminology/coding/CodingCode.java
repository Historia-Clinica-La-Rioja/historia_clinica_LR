package net.pladema.hl7.supporting.terminology.coding;

import net.pladema.hl7.dataexchange.model.adaptor.FhirCode;

public class CodingCode {

    public static final FhirCode ABSENT_REASON = new FhirCode("unknown");

    public static class Allergy {
        public static final FhirCode KNOWN_ABSENT = new FhirCode("no-allergy-info");
        public static final String NOT_KNOWN = "no-known-allergies";
    }

    public static class Condition {
        public static final FhirCode KNOWN_ABSENT = new FhirCode(
                "no-problem-info", "No information about current problems");
        public static final FhirCode NOT_KNOWN = new FhirCode(
                "no-known-problems", "No known problems");
        public static final FhirCode CATEGORY = new FhirCode("75326-9", "Problem");

        public static final String DIAGNOSIS = "439401001";
    }

    public static class Device {
        public static final FhirCode TYPE = new FhirCode(
                "462894001",
                "software de aplicación de sistema de información de historias clínicas de pacientes (objeto físico)"
        );
    }

    public static class Immunization {
        public static final FhirCode KNOWN_ABSENT = new FhirCode("no-immunization-info");
        public static final String NOT_KNOWN = "no-known-immunization";
    }

    public static class Medication {
        public static final FhirCode KNOWN_ABSENT = new FhirCode(
                "no-medication-info", "No information about current medications");
        public static final FhirCode NOT_KNOWN = new FhirCode(
                "no-known-medications", "No known medications");

        public static final FhirCode DOSE = new FhirCode("ordered", "ordered");
    }

    public static class DocumentReference {
        public static final String FINAL_STATUS = "445665009";
        public static final short OUTPATIENT_TYPE = (short)4;

    }
}
