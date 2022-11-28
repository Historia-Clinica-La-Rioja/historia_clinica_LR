package net.pladema.clinichistory.hospitalization.service.indication.pharmaco;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.domain.ips.OtherPharmacoBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.NewDosageDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.OtherPharmacoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.PharmacoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.PharmacoSummaryDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.QuantityDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedIndicationPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.indication.pharmaco.domain.InternmentPharmacoBo;

import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class InternmentPharmacoServiceImpl implements InternmentPharmacoService {

	private final SharedIndicationPort sharedIndicationPort;

	private final DocumentFactory documentFactory;

	private final LocalDateMapper localDateMapper;

	private final InternmentEpisodeService internmentEpisodeService;


	@Override
	public Integer add(InternmentPharmacoBo pharmacoBo) {
		log.debug("Input parameter -> pharmacoBo {}", pharmacoBo);
		assertInternmentEpisodeCanCreateIndication(pharmacoBo.getEncounterId());
		Integer result = sharedIndicationPort.addPharmaco(toPharmacoDto(pharmacoBo));
		pharmacoBo.setId(documentFactory.run(pharmacoBo, false));
		sharedIndicationPort.saveDocument(pharmacoBo.getId(), result);
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public List<PharmacoSummaryDto> getInternmentEpisodePharmacos(Integer internmentEpisodeId) {
		log.debug("Input parameter -> internmentEpisodeId {}", internmentEpisodeId);
		List<PharmacoSummaryDto> result = sharedIndicationPort.getInternmentEpisodePharmacos(internmentEpisodeId);
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public PharmacoDto getInternmentEpisodePharmaco(Integer pharmacoId) {
		log.debug("Input parameter -> pharmacoId {}", pharmacoId);
		PharmacoDto result = sharedIndicationPort.getInternmentEpisodePharmaco(pharmacoId);
		log.debug("Output -> {}", result);
		return result;
	}

	private void assertInternmentEpisodeCanCreateIndication(Integer internmentEpisodeId) {
		if (internmentEpisodeService.haveEpicrisis(internmentEpisodeId)) {
			throw new ConstraintViolationException("No se puede crear una indicación debido a que existe una epicrisis", Collections.emptySet());
		}
	}

	private PharmacoDto toPharmacoDto(InternmentPharmacoBo bo) {
		NewDosageDto dosageDto = toDosageDto(bo.getDosage());

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
				bo.getSolvent() != null ? toOtherPharmacoDto(bo.getSolvent()) : null,
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