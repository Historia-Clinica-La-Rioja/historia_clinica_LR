package net.pladema.medicalconsultation.diary.domain;

public interface IDiaryOpeningHoursBo {

    IOpeningHoursBo getIOpeningHours();

    Short getMedicalAttentionTypeId();

    void setDiaryId(Integer id);

    default boolean overlap(IDiaryOpeningHoursBo other) {
        return getIOpeningHours().overlap(other.getIOpeningHours());
    }
}
