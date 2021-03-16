package net.pladema.hl7.dataexchange.model.adaptor;

import lombok.experimental.UtilityClass;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Resource;

import java.util.List;

@UtilityClass
public class FhirNarrative {

    public static Narrative buildNarrative(List<Resource> resources){
        //=====================Narrative=====================
        Narrative humanText = new Narrative();
        humanText.setStatus(Narrative.NarrativeStatus.GENERATED);
        humanText.setDivAsString(getText(resources));
        return humanText;
    }

    private static StringBuilder header(){
        return new StringBuilder()
        .append("<div>")
        .append("<table>")
        .append("<tr>")
        .append("<th>System</th><th>Code</th><th>Display</th>")
        .append("</tr>");
    }

    private static StringBuilder row(CodeableConcept codeableConcept){
        if(codeableConcept.hasCoding()) {
            Coding coding = codeableConcept.getCoding().get(0);
            return new StringBuilder()
                    .append("<tr><td>")
                    .append(coding.getSystem())
                    .append("</td><td>")
                    .append(coding.getCode())
                    .append("</td><td>")
                    .append(coding.getDisplay())
                    .append("</td></tr>");
        }
        return null;
    }

    private static String getText(List<Resource> resources){
        StringBuilder content = new StringBuilder();

        resources.forEach(resource -> {
            switch (resource.getResourceType()){
                case Condition:
                    content.append(row(((Condition) resource).getCode()));
                    break;
                case MedicationStatement:
                    if(((MedicationStatement) resource).hasMedicationCodeableConcept())
                        content.append(row(((MedicationStatement) resource).getMedicationCodeableConcept()));
                    break;
                case AllergyIntolerance:
                    content.append(row(((AllergyIntolerance) resource).getCode()));
                    break;
                case Immunization:
                    content.append(row(((Immunization) resource).getVaccineCode()));
                    break;
                default:
            }
        });
        return header().append(content).append(footer()).toString();
    }

    private static StringBuilder footer(){
        return new StringBuilder().append("</table></div>");
    }
}
