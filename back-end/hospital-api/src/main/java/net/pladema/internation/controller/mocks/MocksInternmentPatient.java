package net.pladema.internation.controller.mocks;

import net.pladema.internation.controller.dto.*;

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
        return result;
    }

    private static PatientDto mockPatientDto(int i) {
        PatientDto result = new PatientDto();
        result.setPatientId(i);
        result.setName("Nombre " + i);
        result.setSurname("Apellido " +i);
        return result;
    }

    private static ResponsibleDoctorDto mockDoctorDto(int i) {
        ResponsibleDoctorDto result = new ResponsibleDoctorDto();
        result.setId(i);
        result.setName("Doctor name " + i);
        result.setSurname("Doctor surname " +i);
        return result;
    }

    private static BedDto mockBed(int i) {
        BedDto result = new BedDto();
        result.setBedNumber((short)i);
        result.setBedNumber((short)i);
        result.setRoom(mockRoomDto(i));
        return result;
    }

    private static RoomDto mockRoomDto(int i) {
        RoomDto result = new RoomDto();
        result.setId(i);
        result.setRoomNumber((short)i);
        result.setSector(mockSectorDto(i));
        result.setSpecialty(mockSpeciality(i));
        return result;
    }

    private static ClinicalSpecialtyDto mockSpeciality(int i) {
        ClinicalSpecialtyDto result = new ClinicalSpecialtyDto();
        result.setId((short)i);
        result.setName("Speciality " +i);
        return result;
    }

    private static SectorDto mockSectorDto(int i) {
        SectorDto result = new SectorDto();
        result.setId(i);
        result.setName("Sector " +i);
        return result;
    }
}
