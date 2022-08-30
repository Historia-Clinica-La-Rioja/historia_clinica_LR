package net.pladema.clinichistory.hospitalization.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ResponsibleDoctorVo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.LicenseNumberDto;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.repository.domain.ResponsibleContact;
import net.pladema.clinichistory.hospitalization.repository.domain.summary.InternmentSummaryVo;

@Service
@Slf4j
public class InternmentEpisodeStorageImpl implements InternmentEpisodeStorage {
	private final EntityManager entityManager;

	private final SharedStaffPort sharedStaffPort;

	public InternmentEpisodeStorageImpl(EntityManager entityManager,
										SharedStaffPort sharedStaffPort) {
		this.entityManager = entityManager;
		this.sharedStaffPort = sharedStaffPort;
	}

	@Override
	public Optional<InternmentSummaryVo> getSummary(Integer internmentEpisodeId) {
		log.debug("getSummary -> internmentEpisodeId={}", internmentEpisodeId);
		String sqlString = "" +
				"SELECT " +
				"ie.id,  ie.entryDate, " +
				"ie.anamnesisDocId, da.statusId as anamnesisStatusId, " +
				"ie.epicrisisDocId, de.statusId as epicrisisStatusId, " +
				"b.id as bedId, b.bedNumber, " +
				"r.id as roomId, r.roomNumber, sector.description, " +
				"hpg.pk.healthcareProfessionalId," +
				"rc, ie.statusId, ie.probableDischargeDate, pd.administrativeDischargeDate, pd.physicalDischargeDate, pd.medicalDischargeDate " +
				"FROM InternmentEpisode ie " +
				"JOIN Bed b ON (b.id = ie.bedId) " +
				"JOIN Room r ON (r.id = b.roomId) " +
				"JOIN Sector sector ON (sector.id = r.sectorId) " +
				"LEFT JOIN Document da ON (da.id = ie.anamnesisDocId) " +
				"LEFT JOIN Document de ON (de.id = ie.epicrisisDocId) " +
				"LEFT JOIN HealthcareProfessionalGroup hpg ON (hpg.pk.internmentEpisodeId = ie.id and hpg.responsible = true) " +
				"LEFT JOIN ResponsibleContact rc ON (ie.id = rc.internmentEpisodeId) " +
				"LEFT JOIN PatientDischarge pd ON (ie.id = pd.internmentEpisodeId) " +
				"WHERE ie.id = :internmentEpisodeId" +
				"";
		Query query = entityManager.createQuery(sqlString);
		query.setParameter("internmentEpisodeId", internmentEpisodeId);

		return Optional.of((Object[]) query.getSingleResult())
				.map(this::mapResponse);
	}

	private InternmentSummaryVo mapResponse(Object[] row) {
		return  new InternmentSummaryVo((Integer) row[0],
				row[1] != null ? ((LocalDateTime) row[1]) : null,
				(Long) row[2],
				(String) row[3],
				(Long) row[4],
				(String) row[5],
				(Integer) row[6],
				(String) row[7],
				(Integer) row[8],
				(String) row[9],
				(String) row[10],
				row[11] != null ? buildResponsableDoctor((Integer) row[11]) : null,
				(ResponsibleContact) row[12],
				(Short) row[13],
				row[14] != null ? ((LocalDateTime) row[14]) : null,
				row[15] != null ? ((LocalDateTime) row[15]) : null,
				row[16] != null ? ((LocalDateTime) row[16]) : null,
				row[17] != null ? ((LocalDateTime) row[17]) : null);
	}

	private ResponsibleDoctorVo buildResponsableDoctor(Integer professionalId) {
		if (professionalId == null)
			return null;
		var professional = sharedStaffPort.getProfessionalCompleteById(professionalId);
		return new ResponsibleDoctorVo(professionalId,
				professional.getFirstName(),
				professional.getLastName(),
				professional.getAllLicenses().stream()
						.map(LicenseNumberDto::getInfo)
						.collect(Collectors.toList()),
				professional.getNameSelfDetermination());
	}
}
