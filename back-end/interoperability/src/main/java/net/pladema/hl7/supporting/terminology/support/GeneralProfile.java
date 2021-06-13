package net.pladema.hl7.supporting.terminology.support;

import lombok.experimental.UtilityClass;
import net.pladema.hl7.supporting.terminology.coding.CodingCode;
import net.pladema.hl7.supporting.terminology.coding.CodingSystem;
import net.pladema.hl7.supporting.terminology.coding.CodingValueSet;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public final class GeneralProfile {

    public static CodeSystem absentUnknown(){
        return TerminologySupport.loadCodeSystem(
                CodingSystem.NODATA, CodingValueSet.ABSENT_UNKNOWN,
                Arrays.asList(
                        CodingCode.ABSENT_REASON,
                        CodingCode.Allergy.KNOWN_ABSENT,
                        CodingCode.Condition.KNOWN_ABSENT,
                        CodingCode.Immunization.KNOWN_ABSENT,
                        CodingCode.Medication.KNOWN_ABSENT)
        );
    }

    public static List<ValueSet> allValueSet(){
        List<ValueSet> valueSets = new ArrayList<>();
        valueSets.add(TerminologySupport.loadValueSet(
                CodingValueSet.DOC_TYPE,
                CodingSystem.LOINC, CodingCode.Document.PATIENT_SUMMARY_DOC));
        return valueSets;
    }
}
