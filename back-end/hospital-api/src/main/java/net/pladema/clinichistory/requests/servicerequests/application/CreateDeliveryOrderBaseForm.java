package net.pladema.clinichistory.requests.servicerequests.application;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EGender;
import ar.lamansys.sgh.shared.domain.general.ContactInfoBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import java.time.LocalDate;
import java.time.Period;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.domain.IServiceRequestBo;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.service.PersonService;
import net.pladema.reports.service.domain.FormVBo;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CreateDeliveryOrderBaseForm {

    private final PersonService personService;
    private final SharedPersonPort sharedPersonPort;

    public FormVBo run(Integer patientId, IServiceRequestBo serviceRequest, BasicPatientDto patientDto) {
        Person patientPerson = personService.findByPatientId(patientId).orElseThrow();
        String formalPatientPersonName = personService.getFormalPersonNameById(patientPerson.getId());
        var contactInfo = sharedPersonPort.getPersonContactInfoById(patientPerson.getId());

        LocalDate reportDate = serviceRequest.getReportDate();

        FormVBo formV = mapToBaseFormVBo(formalPatientPersonName, contactInfo, patientId, reportDate, patientDto);
        log.trace("Output -> {}", formV);
        return formV;
    }

    private FormVBo mapToBaseFormVBo(
            String formalPatientPersonName,
            ContactInfoBo contactInfo,
            Integer patientId,
            LocalDate reportDate,
            BasicPatientDto patientDto) {

        var address = contactInfo != null
                ? contactInfo.getAddress().getCompleteAddress()
                : null;

        var patientGender = patientDto != null && patientDto.getGender() != null
                ? EGender.map(patientDto.getGender().getId()).getValue()
                : null;
        var documentType = patientDto != null && patientDto.getIdentificationNumber() != null
                ? patientDto.getIdentificationType()
                : null;
        var documentNumber = patientDto != null
                ? patientDto.getIdentificationNumber()
                : null;

        var patientAge = patientDto != null && patientDto.getBirthDate() != null
                ? (short) Period.between(patientDto.getBirthDate(), reportDate).getYears()
                : null;

        return FormVBo.builder()
                .formalPatientName(formalPatientPersonName)
                .address(address)
                .reportDate(reportDate)
                .hcnId(patientId)
                .patientGender(patientGender)
                .documentType(documentType)
                .documentNumber(documentNumber)
                .patientAge(patientAge)
                .build();
    }
}
