package ar.lamansys.sgh.shared.infrastructure.input.service;


import java.util.List;
import java.util.Optional;

import ar.lamansys.sgh.shared.infrastructure.input.service.staff.LicenseNumberDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.MedicineDoctorCompleteDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionalCompleteDto;

public interface SharedStaffPort {

    Integer getProfessionalId(Integer userId);

    Optional<ClinicalSpecialtyDto> getClinicalSpecialty(Integer clinicalSpecialtyId);

    ProfessionalInfoDto getProfessionalCompleteInfo(Integer userId);

	ProfessionalCompleteDto getProfessionalComplete(Integer userId);

	ProfessionalCompleteDto getProfessionalCompleteById(Integer professionalId);

	List<ProfessionalCompleteDto> getProfessionalsCompleteByIds(List<Integer> professionalIds);

	List<MedicineDoctorCompleteDto> getProfessionalsCompleteByInstitutionId(Integer institutionId);

	Optional<String> getProfessionalCompleteNameByUserId(Integer professionalId);

	String getSectorName(Integer sectorId);

	String getRoomNumber(Integer sectorId);

	String getDoctorsOfficeDescription(Integer doctorsOfficeId);

	String getShockRoomDescription(Integer shockRoomId);

	Optional<List<LicenseNumberDto>> getLicenses(Integer healthcareProfessionalId);

}
