package ar.lamansys.sgh.shared.infrastructure.input.service;

public interface SharedHospitalUserPort {

	HospitalUserPersonInfoDto getUserCompleteInfo(Integer userId);
}
