package ar.lamansys.sgh.clinichistory.application.anestheticreport;

import ar.lamansys.sgh.clinichistory.domain.document.impl.AnestheticReportBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.document.dto.AnestheticReportDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.mapper.AnestheticReportMapper;
import ar.lamansys.sgh.clinichistory.application.document.CommonContextBuilder;
import ar.lamansys.sgx.shared.files.pdf.GenerateDocumentContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class GenerateAnestheticReportDocumentContext implements GenerateDocumentContext<AnestheticReportBo> {

    private final AnestheticReportMapper anestheticReportMapper;
    private final CommonContextBuilder commonContextBuilder;

    @Override
    public Map<String, Object> run(AnestheticReportBo data) {
        log.debug("Input parameters -> anestheticReportBo {}", data);
        HashMap<String, Object> result = new HashMap<>();

        commonContextBuilder.run(data, result);

        AnestheticReportDto documentDto = anestheticReportMapper.fromAnestheticReportBo(data);
        this.completeValues(documentDto, result);

        log.debug("Output -> {}", result);
        return result;
    }
    
    private void completeValues(AnestheticReportDto document, Map<String, Object> contextMap) {

        contextMap.put("mainDiagnosis", document.getMainDiagnosis());

        contextMap.put("diagnosis", document.getDiagnosis());

        contextMap.put("surgeryProcedures", document.getSurgeryProcedures());

        contextMap.put("anthropometricData", document.getAnthropometricData());

        contextMap.put("riskFactors", document.getRiskFactors());

        contextMap.put("anestheticHistory", document.getAnestheticHistory());

        contextMap.put("medications", document.getMedications());

        contextMap.put("preMedications", document.getPreMedications());

        contextMap.put("foodIntake", document.getFoodIntake());

        contextMap.put("histories", document.getHistories());

        contextMap.put("procedureDescription", document.getProcedureDescription());

        contextMap.put("anestheticPlans", document.getAnestheticPlans());

        contextMap.put("analgesicTechniques", document.getAnalgesicTechniques());

        contextMap.put("anestheticTechniques", document.getAnestheticTechniques());

        contextMap.put("fluidAdministrations", document.getFluidAdministrations());

        contextMap.put("anestheticAgents", document.getAnestheticAgents());

        contextMap.put("nonAnestheticDrugs", document.getNonAnestheticDrugs());

        contextMap.put("antibioticProphylaxis", document.getAntibioticProphylaxis());

        contextMap.put("measuringPoints", document.getMeasuringPoints());

        contextMap.put("postAnesthesiaStatus", document.getPostAnesthesiaStatus());

        contextMap.put("anestheticChart", document.getAnestheticChart());
    }
    
}
