package ar.lamansys.sgh.shared.infrastructure.input.service.imagenetwork;

public interface SharedStudyPermissionPort {
	String checkTokenStudyPermissions(String studyInstanceUID, String tokenStudy);
}
