package net.pladema.clinichistory.requests.servicerequests.application;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.domain.IServiceRequestBo;
import net.pladema.reports.controller.dto.FormVDto;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateDeliveryOrderFormContext {

    public Map<String, Object> run(FormVDto formVDto, IServiceRequestBo serviceRequestBo) {
        log.trace("Input parameters -> formVDto {}, serviceRequestBo {}", formVDto, serviceRequestBo);
        Map<String, Object> ctx = new HashMap<>();
        ctx.put("completePatientName", formVDto.getCompletePatientName());
        ctx.put("address", formVDto.getAddress());
        ctx.put("reportDate", formVDto.getReportDate());
        ctx.put("hcnId", formVDto.getHcnId());
        ctx.put("medicalCoverage", formVDto.getMedicalCoverage());
        ctx.put("establishment", formVDto.getEstablishment());
        ctx.put("code", formVDto.getEstablishmentProvinceCode());
        ctx.put("ce", serviceRequestBo.getAssociatedSourceTypeId().equals(SourceType.OUTPATIENT));
        ctx.put("problems", serviceRequestBo.getProblemsPt());
        ctx.put("studies", serviceRequestBo.getStudies());
        ctx.put("cie10Codes", serviceRequestBo.getCie10Codes());
        ctx.put("completeProfessionalName", formVDto.getCompleteProfessionalName());
        ctx.put("licenses", formVDto.getLicenses());
        ctx.put("medicalCoverageCondition", formVDto.getMedicalCoverageCondition());
        ctx.put("room", formVDto.getRoomNumber());
        ctx.put("bed", formVDto.getBedNumber());
        ctx.put("affiliateNumber", formVDto.getAffiliateNumber());
        ctx.put("patientGender", formVDto.getPatientGender());
        ctx.put("documentType", formVDto.getDocumentType());
        ctx.put("documentNumber", formVDto.getDocumentNumber());
        ctx.put("patientAge", formVDto.getPatientAge());
        log.trace("Output -> {}", ctx);
        return ctx;
    }
}
