package net.pladema.clinichistory.requests.servicerequests.application;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.domain.IServiceRequestBo;
import net.pladema.reports.service.domain.FormVBo;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CreateDeliveryOrderFormContext {

    public Map<String, Object> run(FormVBo formV, IServiceRequestBo serviceRequest) {
        log.trace("Input parameters -> formV {}, serviceRequest {}", formV, serviceRequest);
        Map<String, Object> ctx = new HashMap<>();
        ctx.put("formalPatientName", formV.getFormalPatientName());
        ctx.put("address", formV.getAddress());
        ctx.put("reportDate", formV.getReportDate());
        ctx.put("hcnId", formV.getHcnId());
        ctx.put("medicalCoverage", formV.getMedicalCoverage());
        ctx.put("establishment", formV.getEstablishment());
        ctx.put("code", formV.getEstablishmentProvinceCode());
        ctx.put("ce", serviceRequest.getAssociatedSourceTypeId().equals(SourceType.OUTPATIENT));
        ctx.put("problems", serviceRequest.getProblemsPt());
        ctx.put("studies", serviceRequest.getStudies());
        ctx.put("cie10Codes", serviceRequest.getCie10Codes());
        ctx.put("completeProfessionalName", formV.getCompleteProfessionalName());
        ctx.put("licenses", formV.getLicenses());
        ctx.put("medicalCoverageCondition", formV.getMedicalCoverageCondition());
        ctx.put("room", formV.getRoomNumber());
        ctx.put("bed", formV.getBedNumber());
        ctx.put("affiliateNumber", formV.getAffiliateNumber());
        ctx.put("patientGender", formV.getPatientGender());
        ctx.put("documentType", formV.getDocumentType());
        ctx.put("documentNumber", formV.getDocumentNumber());
        ctx.put("patientAge", formV.getPatientAge());
        log.trace("Output -> {}", ctx);
        return ctx;
    }
}
