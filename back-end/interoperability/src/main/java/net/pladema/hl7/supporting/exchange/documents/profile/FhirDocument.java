package net.pladema.hl7.supporting.exchange.documents.profile;

import ca.uhn.fhir.rest.param.TokenParam;

import net.pladema.hl7.dataexchange.model.adaptor.FhirParam;
import net.pladema.hl7.supporting.exchange.documents.ips.PatientSummaryDocument;
import net.pladema.hl7.supporting.terminology.coding.CodingCode;
import net.pladema.hl7.supporting.terminology.coding.CodingSystem;
import org.hl7.fhir.r4.model.Coding;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class FhirDocument {

    private static final Map<Coding, Class<? extends IDocumentProfile>> supported = new HashMap<>();
    
    private final ApplicationContext applicationContext;


    public FhirDocument(ApplicationContext applicationContext){
        super();
        this.applicationContext=applicationContext;
    }

    static {
        //Add all fhir documents supported
        supported.put(PatientSummaryDocument.TYPE, PatientSummaryDocument.class);
    }

    public boolean isSupported(TokenParam type){
        return supported.keySet().stream()
                .map(c -> new TokenParam().setSystem(c.getSystem()).setValue(c.getCode()))
                .anyMatch(c -> c.equals(type));
    }

    public Coding getType(TokenParam type){
        Optional<Coding> coding = supported.keySet().stream()
                .filter(c -> c.getSystem().equals(type.getSystem()) && c.getCode().equals(type.getValue()))
                .findAny();
        return coding.orElse(null);
    }

    public Optional<IDocumentProfile> get(Coding code){
        try {
            return Optional.of(applicationContext.getBean(supported.get(code)));
        }
        catch(NullPointerException | BeansException ex){
            return Optional.empty();
        }
    }

    public static TokenParam defaultType(){
        return FhirParam.newTokenParam(CodingSystem.LOINC, CodingCode.Document.PATIENT_SUMMARY_DOC);
    }

    public static String defaultStringType(){
        return FhirParam.getParam(CodingSystem.LOINC,CodingCode.Document.PATIENT_SUMMARY_DOC);
    }
}
