package net.pladema.internation.controller.mocks;

import net.pladema.internation.controller.dto.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class MocksInternmentPatient {


    public static List<InternmentEpisodeDto> mockInternmentPatients(Integer institutionId){
        List<InternmentEpisodeDto> result = new ArrayList<>();
        for (int i = 0; i<institutionId;i++) {
            result.add(mockPatient(i));
        }
        return result;
    }

    private static InternmentEpisodeDto mockPatient(int i) {
        InternmentEpisodeDto result = new InternmentEpisodeDto();
        result.setId(i);
        result.setPatient(mockPatientDto(i));
        result.setBed(mockBed(i));
        result.setDoctor(mockDoctorDto(i));
        result.setSpecialty(mockSpeciality(i));
        return result;
    }

    private static PatientDto mockPatientDto(int i) {
        PatientDto result = new PatientDto();
        result.setId(i);
        result.setFirstName("Nombre " + i);
        result.setLastName("Apellido " +i);
        return result;
    }

    private static ResponsibleDoctorDto mockDoctorDto(int i) {
        ResponsibleDoctorDto result = new ResponsibleDoctorDto();
        result.setId(i);
        result.setFirstName("Doctor name " + i);
        result.setLastName("Doctor surname " +i);
        result.setLicence("ABJ2132");
        return result;
    }

    private static BedDto mockBed(int i) {
        BedDto result = new BedDto();
        result.setBedNumber(i+"");
        result.setRoom(mockRoomDto(i));
        return result;
    }

    private static RoomDto mockRoomDto(int i) {
        RoomDto result = new RoomDto();
        result.setId(i);
        result.setRoomNumber(i+"");
        result.setSector(mockSectorDto(i));
        return result;
    }

    private static ClinicalSpecialtyDto mockSpeciality(int i) {
        ClinicalSpecialtyDto result = new ClinicalSpecialtyDto();
        result.setId(i);
        result.setName("Speciality " +i);
        return result;
    }

    private static SectorDto mockSectorDto(int i) {
        SectorDto result = new SectorDto();
        result.setId(i);
        result.setDescription("Sector " +i);
        return result;
    }

    public static InternmentSummaryDto mockInternmentSummary(Integer internmentEpisodeId) {
        InternmentSummaryDto result = new InternmentSummaryDto();
        result.setId(internmentEpisodeId);
        result.setBed(mockBed(internmentEpisodeId));
        result.setCreatedOn(LocalDateTime.now());
        result.setDoctor(mockDoctorDto(internmentEpisodeId));
        result.setSpecialty(mockSpeciality(internmentEpisodeId));
        result.setTotalInternmentDays(50);
        return result;
    }
}
