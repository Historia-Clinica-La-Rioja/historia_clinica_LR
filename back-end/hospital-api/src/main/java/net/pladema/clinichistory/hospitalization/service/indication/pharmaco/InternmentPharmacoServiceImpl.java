package net.pladema.clinichistory.hospitalization.service.indication.pharmaco;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.domain.ips.OtherPharmacoBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.NewDosageDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.OtherPharmacoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.PharmacoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.QuantityDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedIndicationPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.hospitalization.service.indication.pharmaco.domain.InternmentPharmacoBo;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InternmentPharmacoServiceImpl implements InternmentPharmacoService {

	private final SharedIndicationPort sharedIndicationPort;

	private final DocumentFactory documentFactory;

	private final LocalDateMapper localDateMapper;

	@Override
	public Integer add(InternmentPharmacoBo pharmacoBo) {
		log.debug("Input parameter -> pharmacoBo {}", pharmacoBo);
		Integer result = sharedIndicationPort.addPharmaco(toPharmacoDto(pharmacoBo));
		pharmacoBo.setId(documentFactory.run(pharmacoBo, false));
		sharedIndicationPort.saveDocument(pharmacoBo.getId(), result);
		log.debug("Output -> {}", result);
		return result;
	}

	private PharmacoDto toPharmacoDto(InternmentPharmacoBo bo) {
		NewDosageDto dosageDto = toDosageDto(bo.getDosage());
		OtherPharmacoDto solventDto = toOtherPharmacoDto(bo.getSolvent());

		return new PharmacoDto(null,
				bo.getPatientId(),
				bo.getTypeId(),
				bo.getStatusId(),
				bo.getProfessionalId(),
				null,
				localDateMapper.toDateDto(bo.getIndicationDate()),
				localDateMapper.toDateTimeDto(bo.getCreatedOn()),
				bo.getSnomed() != null ? new SharedSnomedDto(bo.getSnomed().getSctid(), bo.getSnomed().getPt()) : null,
				dosageDto,
				solventDto,
				bo.getHealthConditionId(),
				bo.getFoodRelationId(),
				bo.getPatientProvided(),
				bo.getViaId(),
				bo.getNotes().getOtherNote());
	}

	private NewDosageDto toDosageDto(DosageBo bo) {
		NewDosageDto result = new NewDosageDto();
		result.setFrequency(bo.getFrequency());
		result.setPeriodUnit(bo.getPeriodUnit());
		result.setStartDateTime(localDateMapper.toDateTimeDto(bo.getStartDate()));
		result.setEvent(bo.getEvent());
		result.setQuantity(new QuantityDto(bo.getQuantity().getValue(), bo.getQuantity().getUnit()));
		return result;
	}

	private OtherPharmacoDto toOtherPharmacoDto(OtherPharmacoBo bo) {
		OtherPharmacoDto result = new OtherPharmacoDto();
		result.setSnomed(new SharedSnomedDto(bo.getSnomed().getSctid(),bo.getSnomed().getPt()));
		result.setDosage(toDosageDto(bo.getDosage()));
		return result;
	}

}