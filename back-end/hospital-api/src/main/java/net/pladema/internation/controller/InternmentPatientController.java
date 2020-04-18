package net.pladema.internation.controller;

import io.swagger.annotations.Api;
import net.pladema.address.repository.entity.Address;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.internation.controller.dto.*;
import net.pladema.person.controller.dto.APersonDto;
import net.pladema.person.controller.dto.BMPersonDto;
import net.pladema.person.controller.mapper.PersonMapper;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.repository.entity.PersonExtended;
import net.pladema.person.service.AddressExternalService;
import net.pladema.person.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/institution/{institutionId}/internment/patient")
@Api(value = "Internment Patient", tags = { "Internment Patient" })
public class InternmentPatientController {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public InternmentPatientController() {
        super();
    }
    
    @GetMapping
    public ResponseEntity<List<InternmentPatientDto>> getAllInternmentPatient(@PathVariable(name = "institutionId") Integer institutionId){
        List<InternmentPatientDto> result = mockInternmentPatients();
        LOG.debug("Internment patients -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

    private List<InternmentPatientDto> mockInternmentPatients(){
        List<InternmentPatientDto> result = new ArrayList<>();
        for (int i = 0; i<10;i++) {
            result.add(mockPatient(i));
        }
        return result;
    }

    private InternmentPatientDto mockPatient(int i) {
        InternmentPatientDto result = new InternmentPatientDto();
        result.setPatientId(i);
        result.setName("Nombre " + i);
        result.setSurname("Apellido " +i);
        result.setBed(mockBed(i));
        result.setDoctor(mockDoctorDto(i));
        return result;
    }

    private ResponsableDoctorDto mockDoctorDto(int i) {
        ResponsableDoctorDto result = new ResponsableDoctorDto();
        result.setId(i);
        result.setName("Doctor name " + i);
        result.setSurname("Doctor surname " +i);
        return result;
    }

    private BedDto mockBed(int i) {
        BedDto result = new BedDto();
        result.setBedNumber((short)i);
        result.setBedNumber((short)i);
        result.setRoom(mockRoomDto(i));
        return result;
    }

    private RoomDto mockRoomDto(int i) {
        RoomDto result = new RoomDto();
        result.setId(i);
        result.setRoomNumber((short)i);
        result.setSector(mockSectorDto(i));
        result.setSpeciality(mockSpeciality(i));
        return result;
    }

    private ClinicalSpecialityDto mockSpeciality(int i) {
        ClinicalSpecialityDto result = new ClinicalSpecialityDto();
        result.setId((short)i);
        result.setName("Speciality " +i);
        return result;
    }

    private SectorDto mockSectorDto(int i) {
        SectorDto result = new SectorDto();
        result.setId(i);
        result.setName("Sector " +i);
        return result;
    }


}