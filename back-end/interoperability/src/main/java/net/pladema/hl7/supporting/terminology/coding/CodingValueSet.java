package net.pladema.hl7.supporting.terminology.coding;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class CodingValueSet {

    public static final String ABSENT_UNKNOWN="http://hl7.org/fhir/uv/ips/ValueSet/absent-unknown";
    public static final String DOC_TYPE="http://hl7.org/fhir/ValueSet/doc-typecodes";

    @UtilityClass
    public static class Condition {
        public static final String CATEGORY="http://hl7.org/fhir/uv/ips/ValueSet/problem-type-uv-ips";
        public static final String SEVERITY="http://hl7.org/fhir/uv/ips/ValueSet/condition-severity-uv-ips";

    }
}
