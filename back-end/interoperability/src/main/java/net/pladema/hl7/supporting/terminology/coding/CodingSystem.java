package net.pladema.hl7.supporting.terminology.coding;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class CodingSystem {

    public static final String LOINC="http://loinc.org";
    public static final String SNOMED="http://snomed.info/sct";
    public static final String FEDERADOR="https://federador.msal.gob.ar/uri";
    public static final String REFES="http://fhir.msal.gov.ar/refes";

    //Absent and Unknown Data - IPS
    public static final String NODATA = "http://hl7.org/fhir/uv/ips/CodeSystem/absent-unknown-uv-ips";


    @UtilityClass
    public static class SERVER {
        public static final String TESTAPP="https://testapp.hospitalitaliano.org.ar/masterfile-federacion-service/fhir/";
        public static final String BUS="http://mhd.sisa.msal.gov.ar/fhir/";
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
        public static final String NOMIVAC="http://fhir.msal.gov.ar/CodeSystem/NOMIVAC-esquema";
    }

    @UtilityClass
    public static class MedicationStatement {
        public static final String DOSE="http://terminology.hl7.org/CodeSystem/dose-rate-type";
    }

    @UtilityClass
    public static class Patient {
        public static final String IDENTIFIER="http://www.renaper.gob.ar/dni";
    }
}
