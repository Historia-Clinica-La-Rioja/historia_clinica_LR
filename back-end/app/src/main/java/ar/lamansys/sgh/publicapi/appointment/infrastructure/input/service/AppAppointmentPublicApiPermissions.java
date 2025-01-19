package ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service;

import ar.lamansys.sgh.publicapi.ApiConsumerCondition;
import lombok.AllArgsConstructor;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.session.application.port.UserSessionStorage;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AppAppointmentPublicApiPermissions implements AppointmentPublicApiPermissions{
	private final UserSessionStorage userSessionStorage;
	private final ApiConsumerCondition apiConsumerCondition;

	@Override
	public boolean canAccessAppointment(Integer institutionId) {
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isAssigment(ERole.API_TURNOS, institutionId)
						|| apiConsumerCondition.isRole(roleAssigment)
		);
	}

	@Override
	public boolean canCancelAppointment(Integer institutionId){
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isAssigment(ERole.API_TURNOS, institutionId)
						|| apiConsumerCondition.isRole(roleAssigment)
		);
	}

	@Override
	public boolean canAccessBookingByInstitution(Integer institutionId){
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isAssigment(ERole.API_TURNOS, institutionId)
						|| apiConsumerCondition.isRole(roleAssigment)
		);
	}

	@Override
	public boolean canAccessCancelBookingByInstitution(Integer institutionId){
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isAssigment(ERole.API_TURNOS, institutionId)
						|| apiConsumerCondition.isRole(roleAssigment)
		);
	}

	@Override
	public boolean canAccessMakeBooking(Integer institutionId){
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isAssigment(ERole.API_TURNOS, institutionId)
						|| apiConsumerCondition.isRole(roleAssigment)
		);
	}

	@Override
	public boolean canAccessFetchBookingProfessionals(Integer institutionId){
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isAssigment(ERole.API_TURNOS, institutionId)
						|| apiConsumerCondition.isRole(roleAssigment)
		);
	}

	@Override
	public boolean canAccessFetchAllBookingInstitutions(){
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isRole(ERole.API_TURNOS)
						|| apiConsumerCondition.isRole(roleAssigment)
		);
	}

	@Override
	public boolean canAccessFetchAllBookingInstitutionsExtended(){
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isRole(ERole.API_TURNOS)
						|| apiConsumerCondition.isRole(roleAssigment)
		);
	}

	@Override
	public boolean canAccessFetchMedicalCoverages(){
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isRole(ERole.API_TURNOS)
						|| apiConsumerCondition.isRole(roleAssigment)
		);
	}

	@Override
	public boolean canAccessFetchBookingSpecialties(){
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isRole(ERole.API_TURNOS)
						|| apiConsumerCondition.isRole(roleAssigment)
		);
	}

	@Override
	public boolean canAccessFetchBookingPracticesBySpecialtyAndHealthInsurance(){
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isRole(ERole.API_TURNOS)
						|| apiConsumerCondition.isRole(roleAssigment)
		);
	}

	@Override
	public boolean canAccessFetchBookingPracticesByProfessionalAndHealthInsurance(){
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isRole(ERole.API_TURNOS)
						|| apiConsumerCondition.isRole(roleAssigment)
		);
	}

	@Override
	public boolean canAccessFetchBookingSpecialtiesByProfessional(){
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isRole(ERole.API_TURNOS)
						|| apiConsumerCondition.isRole(roleAssigment)
		);
	}
}
