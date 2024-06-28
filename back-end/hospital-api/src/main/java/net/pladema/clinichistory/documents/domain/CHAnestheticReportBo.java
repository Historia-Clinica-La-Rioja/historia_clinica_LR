package net.pladema.clinichistory.documents.domain;

import com.google.common.base.Joiner;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.infrastructure.output.repository.entity.CHDocumentSummary;
import net.pladema.clinichistory.documents.infrastructure.output.repository.entity.VClinicHistory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@ToString
public class CHAnestheticReportBo extends CHDocumentBo {

    public CHAnestheticReportBo(VClinicHistory entity, ECHEncounterType encounterType, ECHDocumentType documentType) {
        super(entity, encounterType, documentType);
        CHDocumentSummary summary = entity.getHealthConditionSummary();
    }

    @Override
    public List<ClinicalRecordBo> getClinicalRecords() {
        List<String> terms = Stream.of("")
                .filter(term -> term != null && !term.isBlank())
                .map(this::doCharReplacement)
                .collect(Collectors.toList());
        List<ClinicalRecordBo> result = new ArrayList<>();
        if (!terms.isEmpty()) {
            String evolution = Joiner.on(". <br />").join(terms);
            result.add(new ClinicalRecordBo("Parte anestésico", evolution.replace("|(", " (").replace('|', ',').replace("\\n", ".<br />")));
        }
        return result;
    }

    private String doCharReplacement(String term) {
        return term.replace("&", "&#38;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("'", "&#39;")
                .replace("\"", "&#34;");
    }
}
