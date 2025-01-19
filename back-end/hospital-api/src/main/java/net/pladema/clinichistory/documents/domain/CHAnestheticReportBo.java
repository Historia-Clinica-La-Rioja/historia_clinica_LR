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

    private String problems;
    private String medicines;
    private String bloodType;
    private String anthropometricData;
    private String riskFactors;
    private String surgeryProcedures;
    private String anestheticHistory;
    private String preMedications;
    private String histories;
    private String anestheticPlans;
    private String analgesicTechniques;
    private String anestheticTechniques;
    private String fluidAdministrations;
    private String anestheticAgents;
    private String nonAnestheticDrugs;
    private String intrasurgicalAnestheticProcedures;
    private String antibioticProphylaxis;
    private String vitalSignsAnesthesia;
    private String postAnesthesiaStatus;

    public CHAnestheticReportBo(VClinicHistory entity, ECHEncounterType encounterType, ECHDocumentType documentType) {
        super(entity, encounterType, documentType);
        CHDocumentSummary summary = entity.getHealthConditionSummary();
        this.problems = summary.getProblems();
        this.surgeryProcedures = summary.getSurgeryProcedures();
        this.bloodType = summary.getBloodType();
        this.anthropometricData = summary.getAnthropometricData();
        this.riskFactors = summary.getRiskFactors();
        this.anestheticHistory = summary.getAnestheticHistory();
        this.medicines = summary.getMedicines();
        this.preMedications = summary.getPreMedications();
        this.histories = summary.getHistories();
        this.anestheticPlans = summary.getAnestheticPlans();
        this.analgesicTechniques = summary.getAnalgesicTechniques();
        this.anestheticTechniques = summary.getAnestheticTechniques();
        this.fluidAdministrations = summary.getFluidAdministrations();
        this.anestheticAgents = summary.getAnestheticAgents();
        this.nonAnestheticDrugs = summary.getNonAnestheticDrugs();
        this.intrasurgicalAnestheticProcedures = summary.getIntrasurgicalAnestheticProcedures();
        this.antibioticProphylaxis = summary.getAntibioticProphylaxis();
        this.vitalSignsAnesthesia = summary.getVitalSignsAnesthesia();
        this.postAnesthesiaStatus = summary.getPostAnesthesiaStatus();
    }

    @Override
    public List<ClinicalRecordBo> getClinicalRecords() {
        List<String> terms = Stream.of(problems, surgeryProcedures, bloodType, anthropometricData, riskFactors,
                        anestheticHistory, medicines, preMedications, histories,
                        anestheticPlans, analgesicTechniques, anestheticTechniques, fluidAdministrations,
                        anestheticAgents, nonAnestheticDrugs, intrasurgicalAnestheticProcedures, antibioticProphylaxis,
                        vitalSignsAnesthesia, postAnesthesiaStatus)
                .filter(term -> term != null && !term.isBlank())
                .map(this::doCharReplacement)
                .collect(Collectors.toList());
        List<ClinicalRecordBo> result = new ArrayList<>();
        if (!terms.isEmpty()) {
            String joinedTerms = Joiner.on(". <br />").join(terms);
            result.add(new ClinicalRecordBo("Parte anestésico",
                    joinedTerms.replace("|(", " (")
                            .replace('|', ',')
                            .replace("\\n", ".<br />")
                            .replace("\\hr", "<hr />")
                            .replace(": .", ":")));
        }
        return result;
    }

    private String doCharReplacement(String term) {
        return term.replace("&", "&#38;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("'", "&#39;")
                .replace("\"", "&#34;")
                .replace("−", "&ndash;");
    }
}
