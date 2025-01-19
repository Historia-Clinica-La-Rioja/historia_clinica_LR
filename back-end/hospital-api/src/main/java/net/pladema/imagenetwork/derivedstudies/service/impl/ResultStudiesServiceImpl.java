package net.pladema.imagenetwork.derivedstudies.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.pladema.imagenetwork.application.exception.ResultStudiesException;
import net.pladema.imagenetwork.derivedstudies.repository.ResultStudiesRepository;
import net.pladema.imagenetwork.derivedstudies.repository.entity.ResultStudies;
import net.pladema.imagenetwork.derivedstudies.service.ResultStudiesService;
import net.pladema.imagenetwork.derivedstudies.service.domain.ResultStudiesBO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Date;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ResultStudiesServiceImpl implements ResultStudiesService {



	private final ResultStudiesRepository resultStudiesRepository;



	@SneakyThrows
	@Override
	public void insertPossibleStudy( Integer idMove, Integer appointmentId, String patientId, String patientName, String studyDate, String studyTime, String modality, String studyInstanceUid) {


		try {
			Date date= null;
			if(!studyDate.isEmpty()){
				String formattedDateString = studyDate.substring(0, 4) + "-" + studyDate.substring(4, 6) + "-" + studyDate.substring(6, 8);
				date = Date.valueOf(formattedDateString);
			}
			Time time=null;
			if (!studyTime.isEmpty()){
				String formattedTimeString = studyTime.substring(0, 2) + ":" + studyTime.substring(2, 4) + ":" + studyTime.substring(4, 6);
				time = Time.valueOf(formattedTimeString);
			}

			ResultStudiesBO resultStudiesBO = new ResultStudiesBO();
			resultStudiesBO.setAppointmentId(appointmentId);
			resultStudiesBO.setIdMove(idMove);
			resultStudiesBO.setPatientId(patientId);
			resultStudiesBO.setPatientName(patientName);
			resultStudiesBO.setStudyDate(date);
			resultStudiesBO.setStudyTime(time);
			resultStudiesBO.setModality(modality);
			resultStudiesBO.setStudyInstanceUid(studyInstanceUid);
			LocalDate localDate = LocalDate.now();
			Date now = Date.valueOf(localDate);
			resultStudiesBO.setAuditDate(now);
			save(resultStudiesBO);
		}  catch (Exception e){
			throw new ResultStudiesException(e.getMessage(),e);
		}

	}

	@Override
	public Integer save(ResultStudiesBO resultStudiesBO) {

		ResultStudies resultStudies = new ResultStudies(resultStudiesBO.getAppointmentId(),
				resultStudiesBO.getIdMove(),
				resultStudiesBO.getPatientId(),
				resultStudiesBO.getPatientName(),
				resultStudiesBO.getStudyDate(),
				resultStudiesBO.getStudyTime(),
				resultStudiesBO.getModality(),
				resultStudiesBO.getStudyInstanceUid(),
				resultStudiesBO.getAuditDate()
				);
		resultStudiesRepository.save(resultStudies);
		return null;
	}

	public  Boolean existsResult(Integer idMove){
		return ! resultStudiesRepository.findAllByIdMove(idMove).isEmpty();
	}

	@Transactional
	public void deleteResult(Integer idMove){
		resultStudiesRepository.deleteByIdMove(idMove);
	}
}
