package ar.lamansys.odontology.domain.consultation;

public interface OdontologyConsultationStorage {

    Integer save(ConsultationInfoBo consultationInfo);

    boolean hasPreviousConsultations(Integer patientId);

}
