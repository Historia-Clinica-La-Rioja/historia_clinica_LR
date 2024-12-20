package net.pladema.staff.infrastructure.input.shared;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ar.lamansys.sgh.shared.infrastructure.input.service.staff.LicenseNumberDto;

import net.pladema.staff.application.getallprofessionalregistrationnumbers.GetAllProfessionalRegistrationNumbers;

import net.pladema.staff.domain.ProfessionalRegistrationNumbersBo;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ProfessionalInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.MedicineDoctorCompleteDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionalCompleteDto;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.service.RoomService;
import net.pladema.establishment.service.SectorService;
import net.pladema.hl7.dataexchange.model.adaptor.FhirString;
import net.pladema.medicalconsultation.doctorsoffice.service.DoctorsOfficeService;
import net.pladema.medicalconsultation.shockroom.application.FetchShockRoomDescription;
import net.pladema.staff.controller.mapper.ClinicalSpecialtyMapper;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import net.pladema.staff.service.ClinicalSpecialtyService;
import net.pladema.staff.service.HealthcareProfessionalService;

@AllArgsConstructor
@Slf4j
@Service
public class SharedStaffImpl implements SharedStaffPort {

    private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;

	private final HealthcareProfessionalService healthcareProfessionalService;

    private final ClinicalSpecialtyService clinicalSpecialtyService;

    private final ClinicalSpecialtyMapper clinicalSpecialtyMapper;

	private final FeatureFlagsService featureFlagsService;
	
	private SectorService sectorService;

	private RoomService roomService;

	private DoctorsOfficeService doctorsOfficeService;

	private FetchShockRoomDescription fetchShockRoomDescription;

    @Override
    public Integer getProfessionalId(Integer userId) {
        return healthcareProfessionalExternalService.getProfessionalId(userId);
    }

    @Override
    public Optional<ClinicalSpecialtyDto> getClinicalSpecialty(Integer clinicalSpecialtyId) {
        return clinicalSpecialtyService.getClinicalSpecialty(clinicalSpecialtyId)
                .map(clinicalSpecialtyBo -> new ClinicalSpecialtyDto(clinicalSpecialtyBo.getId(), clinicalSpecialtyBo.getName()));
    }

    @Override
    public ProfessionalInfoDto getProfessionalCompleteInfo(Integer userId) {
        return Optional.ofNullable(healthcareProfessionalExternalService.findProfessionalByUserId(userId))
                .map(professional -> {
                    var specialties = clinicalSpecialtyService.getSpecialtiesByProfessional(professional.getId());
                    var ret = new ProfessionalInfoDto(
                    		professional.getId(),
                    		professional.getLicenceNumber(),
                    		professional.getFirstName(),
                            professional.getLastName(),
                            professional.getIdentificationNumber(),
                            professional.getPhoneNumber(),
                            clinicalSpecialtyMapper.fromListClinicalSpecialtyBo(specialties),
                            professional.getNameSelfDetermination(),
							professional.getMiddleNames(),
							professional.getOtherLastNames()
					);
					ret.setUseSelfDeterminedName(featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS));
					return ret;
                })
                .get();
    }

	@Override
	public ProfessionalCompleteDto getProfessionalComplete(Integer userId) {
		return healthcareProfessionalExternalService.getProfessionalCompleteInfoByUser(userId);
	}

	@Override
	public ProfessionalCompleteDto getProfessionalCompleteById(Integer professionalId) {
		return healthcareProfessionalExternalService.getProfessionalCompleteInfoById(professionalId);
	}

	@Override
	public List<ProfessionalCompleteDto> getProfessionalsCompleteByIds(List<Integer> professionalIds) {
		return healthcareProfessionalExternalService.getProfessionalsCompleteInfoByIds(professionalIds);
	}

	@Override
	public List<MedicineDoctorCompleteDto> getProfessionalsCompleteByInstitutionId(Integer institutionId) {
		return healthcareProfessionalService.getAllByInstitution(institutionId).stream()
				.map(prof -> healthcareProfessionalExternalService.getDoctorsCompleteInfo(prof.getId()))
				.collect(Collectors.toList());
	}

	@Override
	public Optional<String> getProfessionalCompleteNameByUserId(Integer userId){
		log.debug("Input parameteres -> userId {}", userId);
		ProfessionalCompleteDto professionalInfo = healthcareProfessionalExternalService.getProfessionalCompleteInfoByUser(userId);
		Optional<String> result = getCompleteName(professionalInfo);
		log.debug("Output -> result");
		return result;
	}
	
	@Override
	public String getSectorName(Integer sectorId) {
		return sectorService.getSectorName(sectorId);
	}

	@Override
	public String getRoomNumber(Integer roomId) {
		return roomService.getRoomNumber(roomId);
	}

	@Override
	public String getDoctorsOfficeDescription(Integer doctorsOfficeId) {
		return doctorsOfficeService.getDescription(doctorsOfficeId);
	}

	@Override
	public String getShockRoomDescription(Integer shockRoomId) {
		return fetchShockRoomDescription.execute(shockRoomId);
	}

	@Override
	public Optional<List<LicenseNumberDto>> getLicenses(Integer healthcareProfessionalId) {
		return Optional.of(
				healthcareProfessionalExternalService.getProfessionalCompleteInfoById(healthcareProfessionalId)
				.getAllLicenses()
				.stream()
				.map(l -> new LicenseNumberDto(
						l.getId(),
						l.getNumber(),
						l.getType()
				))
				.collect(Collectors.toList())
		);
	}

	private Optional<String> getCompleteName(ProfessionalCompleteDto professionalInfo) {
		if (professionalInfo == null) return Optional.empty();
		String name = featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS) ?
			professionalInfo.getNameSelfDetermination() :
			FhirString.joining(professionalInfo.getFirstName(), professionalInfo.getMiddleNames());
		String completeName = professionalInfo.getCompleteName(name);
		return Optional.of(completeName);
	}

}
