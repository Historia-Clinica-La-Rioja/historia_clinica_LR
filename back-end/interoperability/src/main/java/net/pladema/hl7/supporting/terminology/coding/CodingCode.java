package net.pladema.hl7.supporting.terminology.coding;

import lombok.experimental.UtilityClass;
import net.pladema.hl7.dataexchange.model.adaptor.FhirCode;

@UtilityClass
public final class CodingCode {

    public static final FhirCode ABSENT_REASON = new FhirCode("unknown");
    public static final String NO_INFO = "No hay información para mostrar";

    @UtilityClass
    public static class Document {
        public static final String PATIENT_SUMMARY_DOC = "60591-5";
    }

    @UtilityClass
    public static class Allergy {
        public static final FhirCode KNOWN_ABSENT = new FhirCode("no-allergy-info",
                NO_INFO);
        public static final String NOT_KNOWN = "no-known-allergies";

        public static final FhirCode ENTRY = new FhirCode("48765-2", "Allergies and adverse reactions Document");
    }

    @UtilityClass
    public static class Condition {
        public static final FhirCode KNOWN_ABSENT = new FhirCode(
                "no-problem-info", NO_INFO);
        public static final FhirCode NOT_KNOWN = new FhirCode(
                "no-known-problems", "No known problems");

        public static final FhirCode ENTRY = new FhirCode("11450-4", "Problem list Reported");

        public static final FhirCode CATEGORY = new FhirCode("75326-9", "Problem");

        public static final String PROBLEM = "55607006";
        public static final String CHRONIC = "-55607006";
    }

    @UtilityClass
    public static class Device {
        public static final FhirCode TYPE = new FhirCode(
                "462894001",
                "Patient health record information system application software"
        );
    }

    @UtilityClass
    public static class Immunization {
        public static final FhirCode KNOWN_ABSENT = new FhirCode("no-immunization-info",
                NO_INFO);
        public static final String NOT_KNOWN = "no-known-immunization";

        public static final FhirCode ENTRY = new FhirCode("60484-3", "CDC Immunization panel");
    }

    @UtilityClass
    public static class Medication {
        public static final FhirCode KNOWN_ABSENT = new FhirCode(
                "no-medication-info", NO_INFO);
        public static final FhirCode NOT_KNOWN = new FhirCode(
                "no-known-medications", NO_INFO);

        public static final FhirCode ENTRY = new FhirCode("10160-0", "History of Medication use Narrative");

        public static final FhirCode DOSE = new FhirCode("ordered", "ordered");
    }

    @UtilityClass
    public static class DocumentReference {
        public static final String FINAL_STATUS = "445665009";
        public static final short OUTPATIENT_TYPE = (short)4;
        public static final Short RECIPE = (short)5;
    }
}
