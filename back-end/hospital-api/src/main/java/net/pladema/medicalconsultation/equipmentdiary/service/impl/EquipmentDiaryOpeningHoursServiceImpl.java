package net.pladema.medicalconsultation.equipmentdiary.service.impl;

import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.appointment.repository.AppointmentAssnRepository;
import net.pladema.medicalconsultation.diary.repository.OpeningHoursRepository;
import net.pladema.medicalconsultation.diary.repository.domain.DiaryOpeningHoursVo;
import net.pladema.medicalconsultation.diary.repository.entity.OpeningHours;

import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.equipmentdiary.repository.EquipmentDiaryOpeningHoursRepository;
import net.pladema.medicalconsultation.equipmentdiary.repository.entity.EquipmentDiaryOpeningHours;
import net.pladema.medicalconsultation.equipmentdiary.repository.entity.EquipmentDiaryOpeningHoursPK;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryBoMapper;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryOpeningHoursService;

import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryOpeningHoursBo;

import net.pladema.medicalconsultation.equipmentdiary.service.domain.OpeningHoursBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EquipmentDiaryOpeningHoursServiceImpl implements EquipmentDiaryOpeningHoursService {

    private static final Logger LOG = LoggerFactory.getLogger(EquipmentDiaryOpeningHoursServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final EquipmentDiaryOpeningHoursRepository diaryOpeningHoursRepository;

    private final OpeningHoursRepository openingHoursRepository;

	private final AppointmentAssnRepository appointmentAssnRepository;

    private final EquipmentDiaryBoMapper diaryBoMapper;

    @Override
    public void load(Integer diaryId, List<EquipmentDiaryOpeningHoursBo> diaryOpeningHours, List<EquipmentDiaryOpeningHoursBo>... oldOpeningHours) {
        Sort sort = Sort.by("dayWeekId", "from");
        List<OpeningHours> savedOpeningHours = openingHoursRepository.findAll(sort);

        diaryOpeningHours.forEach(doh -> {
            OpeningHoursBo openingHoursBo = doh.getOpeningHours();
            OpeningHours newOpeningHours = diaryBoMapper.toOpeningHours(openingHoursBo);
            Integer openingHoursId;

            //Si los horarios de atención definidos para la agenda ya existen en la BBDD
            // los registros son reutilizados. En caso contrario, son persistidos.
            Optional<OpeningHours> existingOpeningHours = savedOpeningHours.stream()
                    .filter(oh -> oh.equals(newOpeningHours)).findAny();
            if(existingOpeningHours.isPresent())
                openingHoursId = existingOpeningHours.get().getId();
            else {
				openingHoursId = openingHoursRepository.save(newOpeningHours).getId();
				if (oldOpeningHours.length > 0) {
					Optional<EquipmentDiaryOpeningHoursBo> recoveredOpeningHours = oldOpeningHours[0].stream()
							.filter(openingHours -> Objects.equals(openingHours.getOpeningHours().getDayWeekId(), newOpeningHours.getDayWeekId()) && openingHours.getOpeningHours().getFrom().equals(newOpeningHours.getFrom()))
							.findFirst();
					recoveredOpeningHours.ifPresent(diaryOpeningHoursBo -> appointmentAssnRepository.updateOldWithNewOpeningHoursId(diaryOpeningHoursBo.getOpeningHours().getId(), openingHoursId));
				}
			}

            openingHoursBo.setId(openingHoursId);
            diaryOpeningHoursRepository.saveAndFlush(createDiaryOpeningHoursInstance(diaryId, openingHoursId, doh));

        });
    }

	@Override
	public void update(Integer diaryId, List<EquipmentDiaryOpeningHoursBo> diaryOpeningHours) {
		List<EquipmentDiaryOpeningHoursBo> oldOpeningHours = diaryOpeningHoursRepository.getDiaryOpeningHours(diaryId).stream().map(this::createDiaryOpeningHoursBo).collect(Collectors.toList());
		diaryOpeningHoursRepository.deleteAll(diaryId);
		load(diaryId, diaryOpeningHours, oldOpeningHours);
	}
    
    private EquipmentDiaryOpeningHours createDiaryOpeningHoursInstance(Integer diaryId, Integer openingHoursId, EquipmentDiaryOpeningHoursBo doh){
		EquipmentDiaryOpeningHours diaryOpeningHours = new EquipmentDiaryOpeningHours();
        diaryOpeningHours.setPk(new EquipmentDiaryOpeningHoursPK(diaryId, openingHoursId));
        diaryOpeningHours.setMedicalAttentionTypeId(doh.getMedicalAttentionTypeId());
        diaryOpeningHours.setOverturnCount((doh.getOverturnCount() != null) ? doh.getOverturnCount() : 0);
        diaryOpeningHours.setExternalAppointmentsAllowed(doh.getExternalAppointmentsAllowed());
        return diaryOpeningHours;
    }

	private EquipmentDiaryOpeningHoursBo createDiaryOpeningHoursBo(DiaryOpeningHoursVo diaryOpeningHoursVo) {
		LOG.debug("Input parameters -> diaryOpeningHoursVo {} ", diaryOpeningHoursVo);
		EquipmentDiaryOpeningHoursBo result = new EquipmentDiaryOpeningHoursBo();
		result.setDiaryId(diaryOpeningHoursVo.getDiaryId());
		result.setMedicalAttentionTypeId(diaryOpeningHoursVo.getMedicalAttentionTypeId());
		result.setOverturnCount(diaryOpeningHoursVo.getOverturnCount());
		result.setOpeningHours(new net.pladema.medicalconsultation.equipmentdiary.service.domain.OpeningHoursBo(diaryOpeningHoursVo.getOpeningHours()));
		result.setExternalAppointmentsAllowed(diaryOpeningHoursVo.getExternalAppointmentsAllowed());
		LOG.debug(OUTPUT, result);
		return result;
	}





}
