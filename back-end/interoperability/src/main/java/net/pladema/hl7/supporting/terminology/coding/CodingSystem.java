package net.pladema.hl7.supporting.terminology.coding;

public class CodingSystem {

    public static final String LOINC="http://loinc.org";
    public static final String SNOMED="http://snomed.info/sct";
    public static final String FEDERADOR="https://federador.msal.gob.ar/uri";
    public static final String REFES="http://fhir.msal.gov.ar/refes";

    public static class SERVER {
        public static final String TESTAPP="https://testapp.hospitalitaliano.org.ar/masterfile-federacion-service/fhir/";
        public static final String BUS="http://mhd.sisa.msal.gov.ar/fhir/";
    }

    public static class DOMAIN {
        public static final String RENAPER="DOMINIOSINAUTORIZACIONDEALTA";
        public static final String TEST="http://www.msal.gov.ar";
    }

    //Absent and Unknown Data - IPS
    public static final String NODATA = "http://hl7.org/fhir/uv/ips/CodeSystem/absent-unknown-uv-ips";

    public static class Allergy {
        public static final String VERIFICATION_STATUS="http://terminology.hl7.org/CodeSystem/allergyintolerance-verification";
        public static final String CLINICAL_STATUS="http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical";
    }

    public static class Condition {
        public static final String STATUS="http://terminology.hl7.org/CodeSystem/condition-clinical";
        public static final String VERIFICATION="http://terminology.hl7.org/CodeSystem/condition-ver-status";
    }

    public static class Immunization {
        public static final String NOMIVAC="http://fhir.msal.gov.ar/CodeSystem/NOMIVAC-esquema";
    }

    public static class MedicationStatement {
        public static final String DOSE="http://terminology.hl7.org/CodeSystem/dose-rate-type";
    }

    public static class Patient {
        public static final String IDENTIFIER="http://www.renaper.gob.ar/dni";
    }

}
