package net.pladema.establishment.infrastructure.output;

import java.util.Optional;

import net.pladema.establishment.repository.entity.Bed;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.pladema.establishment.application.port.BlockAttentionPlaceStorage;
import net.pladema.establishment.domain.AttentionPlaceBo;
import net.pladema.establishment.domain.EBlockAttentionPlaceReason;
import net.pladema.establishment.domain.FetchAttentionPlaceBlockStatusBo;
import net.pladema.establishment.repository.AttentionPlaceStatusRepository;
import net.pladema.establishment.repository.BedRepository;
import net.pladema.establishment.repository.entity.AttentionPlaceStatus;
import net.pladema.medicalconsultation.doctorsoffice.repository.DoctorsOfficeRepository;
import net.pladema.medicalconsultation.shockroom.infrastructure.repository.ShockroomRepository;
import net.pladema.staff.application.ports.HealthcareProfessionalStorage;

@Service
@RequiredArgsConstructor
public class BlockAttentionPlaceStorageImpl implements BlockAttentionPlaceStorage {

	private final BedRepository bedRepository;
	private final ShockroomRepository shockroomRepository;
	private final DoctorsOfficeRepository doctorsOfficeRepository;
	private final AttentionPlaceStatusRepository attentionPlaceStatusRepository;
	private final HealthcareProfessionalStorage healthcareProfessionalStorage;

	@Override
	public Optional<AttentionPlaceBo> findBedByIdAndInstitutionId(Integer bedId, Integer institutionId) {
		return bedRepository
			.findByIdAndInstitutionId(bedId, institutionId)
			.map(bed -> new AttentionPlaceBo(
				bed.getId(),
				Optional.ofNullable(bed.getStatusId()),
				bed.getFree()
			));
	}

	@Override
	public Optional<AttentionPlaceBo> findShockRoomByIdAndInstitutionId(Integer shockroomId, Integer institutionId) {
		return shockroomRepository
			.findById(shockroomId)
			.filter(shockroom -> institutionId.equals(shockroom.getInstitutionId()))
			.map(shockroom -> new AttentionPlaceBo(
					shockroom.getId(),
					Optional.ofNullable(shockroom.getStatusId()),
					true
			));
	}

	@Override
	public Optional<AttentionPlaceBo> findDoctorsOfficeByIdAndInstitutionId(Integer doctorsOfficeId, Integer institutionId) {
		return doctorsOfficeRepository
			.findById(doctorsOfficeId)
			.filter(doctorsOffice -> institutionId.equals(doctorsOffice.getInstitutionId()))
			.map(doctorsOffice -> new AttentionPlaceBo(
					doctorsOffice.getId(),
					Optional.ofNullable(doctorsOffice.getStatusId()),
					true
			));
	}

	@Override
	public Optional<FetchAttentionPlaceBlockStatusBo> fetchBedDetailedStatus(Integer institutionId, Integer bedId) {
		return findBedByIdAndInstitutionId(bedId, institutionId)
				.flatMap(sr -> sr.getStatusId())
				.flatMap(statusId -> fetchStatus(statusId));
	}

	@Override
	public Optional<FetchAttentionPlaceBlockStatusBo> fetchShockRoomDetailedStatus(Integer institutionId, Integer id) {
		return findShockRoomByIdAndInstitutionId(id, institutionId)
				.flatMap(sr -> sr.getStatusId())
				.flatMap(statusId -> fetchStatus(statusId));
	}

	@Override
	public Optional<FetchAttentionPlaceBlockStatusBo> fetchDoctorsOfficeDetailedStatus(Integer institutionId, Integer id) {
		return findDoctorsOfficeByIdAndInstitutionId(id, institutionId)
				.flatMap(office -> office.getStatusId())
				.flatMap(statusId -> fetchStatus(statusId));
	}

	private Optional<FetchAttentionPlaceBlockStatusBo> fetchStatus(Integer statusId) {
		return
		attentionPlaceStatusRepository.findById(statusId)
		.map(status -> {
			var professional = healthcareProfessionalStorage.fetchProfessionalByUserId(status.getUserId());
			var reasonEnum = EBlockAttentionPlaceReason.map(status.getReasonId());
			return new FetchAttentionPlaceBlockStatusBo(
				status.getId(),
				status.getIsBlocked(),
				status.getUserId(),
				status.getCreatedOn(),
				status.getReasonId(),
				reasonEnum,
				reasonEnum.getDescription(),
				status.getReason(),
				professional
			);
		});
	}

	@Override
	public Integer newBlockedStatus(EBlockAttentionPlaceReason reasonEnum, String reason, Integer userId) {
		var newAttention = AttentionPlaceStatus.blockedNow(userId, reasonEnum, reason);
		newAttention = attentionPlaceStatusRepository.save(newAttention);
		return newAttention.getId();
	}

	@Override
	public void updateBedStatusToBlocked(Integer bedId, Integer newStatusId) {
		bedRepository.findById(bedId).ifPresent(bed -> {
			bed.block(newStatusId);
			bedRepository.save(bed);
		});

	}

	@Override
	public void updateShockRoomStatus(Integer shockRoomId, Integer newStatusId) {
		shockroomRepository.updateStatus(shockRoomId, newStatusId);
	}

	@Override
	public void updateDoctorsOfficeStatus(Integer doctorsOfficeId, Integer newStatusId) {
		doctorsOfficeRepository.updateStatus(doctorsOfficeId, newStatusId);
	}

	@Override
	public void updateBedStatusToUnblocked(Integer bedId, Integer statusId) {
		bedRepository.findById(bedId).ifPresent(bed -> {
			bed.unBlock();
			bedRepository.save(bed);
		});
		this.attentionPlaceStatusRepository.deleteById(statusId);
	}

	@Override
	public void deleteShockRoomStatus(Integer id, Integer statusId) {
		this.updateShockRoomStatus(id, null);
		this.attentionPlaceStatusRepository.deleteById(statusId);
	}

	@Override
	public void deleteDoctorsOfficeStatus(Integer id, Integer statusId) {
		this.updateDoctorsOfficeStatus(id, null);
		this.attentionPlaceStatusRepository.deleteById(statusId);
	}
}
