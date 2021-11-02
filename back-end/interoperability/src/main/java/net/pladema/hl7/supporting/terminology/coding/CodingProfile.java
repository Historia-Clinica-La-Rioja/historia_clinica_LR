package net.pladema.hl7.supporting.terminology.coding;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class CodingProfile {

    public static final String DATA_ABSENT_REASON="http://hl7.org/fhir/StructureDefinition/data-absent-reason";

    @UtilityClass
    public static class Allergy {
        public static final String URL="http://hl7.org/fhir/uv/ips/StructureDefinition/AllergyIntolerance-uv-ips";
        public static final String BASEURL="http://hl7.org/fhir/StructureDefinition/AllergyIntolerance";
        public static final String TYPE="AllergyIntolerance";
    }

    @UtilityClass
    public static class Condition {
        public static final String URL="http://hl7.org/fhir/uv/ips/StructureDefinition/Condition-uv-ips";
        public static final String BASEURL="http://hl7.org/fhir/StructureDefinition/Condition";
        public static final String TYPE="Condition";

        @UtilityClass
        public static class PATH {
            public static final String CATEGORY="Condition.category";
            public static final String SEVERITY="Condition.severity";
        }
    }

    @UtilityClass
    public static class Device {
        public static final String URL="http://hl7.org/fhir/uv/ips/StructureDefinition/Device-uv-ips";
        public static final String BASEURL="http://hl7.org/fhir/StructureDefinition/Device";
        public static final String TYPE="Device";
    }

    @UtilityClass
    public static class DocumentReference {
        public static final String URL="http://hl7.org/fhir/us/core/StructureDefinition/us-core-documentreference";
        public static final String BASEURL="http://hl7.org/fhir/StructureDefinition/DocumentReference";
        public static final String TYPE="DocumentReference";
    }

    @UtilityClass
    public static class Immunization {
        public static final String URL="http://fhir.msal.gov.ar/StructureDefinition/NomivacImmunization";
        public static final String URL2="http://fhir.msal.gob.ar/core/StructureDefinition/Immunization-ar-core";
        public static final String BASEURL="http://hl7.org/fhir/StructureDefinition/Immunization";
        public static final String TYPE="Immunization";

        @UtilityClass
        public static class NOMIVAC {
            public static final String URL="http://fhir.msal.gov.ar/StructureDefinition/NomivacEsquema";
            public static final String PATH="Immunization.protocolApplied.series.extension";
            public static final String CODE="Extension";
        }
    }

    @UtilityClass
    public static class Medication {
        public static final String URL="";
        public static final String BASEURL="";
        public static final String TYPE="";
    }

    @UtilityClass
    public static class MedicationStatement {
        public static final String URL="http://hl7.org/fhir/uv/ips/StructureDefinition/MedicationStatement-uv-ips";
        public static final String BASEURL="http://hl7.org/fhir/StructureDefinition/MedicationStatement";
        public static final String TYPE="MedicationStatement";
    }

    @UtilityClass
    public static class Patient {
        public static final String URL="http://hl7.org/fhir/uv/ips/StructureDefinition/Patient-uv-ips";
        public static final String BASEURL="http://hl7.org/fhir/StructureDefinition/Patient";
        public static final String TYPE="Patient";

        public static final String EXT_FATHER="http://hl7.org/fhir/StructureDefinition/humanname-fathers-family";
        public static final String EXT_MATHER="http://hl7.org/fhir/StructureDefinition/humanname-mathers-family";

    }
}
