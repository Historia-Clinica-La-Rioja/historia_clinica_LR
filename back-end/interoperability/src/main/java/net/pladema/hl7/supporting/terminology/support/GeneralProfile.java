package net.pladema.hl7.supporting.terminology.support;

import net.pladema.hl7.supporting.terminology.coding.CodingCode;
import net.pladema.hl7.supporting.terminology.coding.CodingSystem;
import net.pladema.hl7.supporting.terminology.coding.CodingValueSet;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeneralProfile {

    public GeneralProfile(){
        super();
    }

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
        return new ArrayList<>(){{
            add(TerminologySupport.loadValueSet(
                    CodingValueSet.DOC_TYPE,
                    CodingSystem.LOINC, CodingCode.Document.PATIENT_SUMMARY_DOC));
        }};
    }
}
