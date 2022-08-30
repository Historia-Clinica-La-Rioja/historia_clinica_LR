package net.pladema.medicalconsultation.controller;

import ar.lamansys.sgx.shared.masterdata.application.MasterDataService;
import ar.lamansys.sgx.shared.masterdata.infrastructure.output.repository.MasterDataProjection;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentBlockMotive;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.repository.entity.MedicalAttentionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/institutions/{institutionId}/medicalConsultations/masterdata/")
@Tag(name = "Medical consultation master data", description = "Medical consultation master data")
public class MedicalConsultationMasterdataController {

    private static final Logger LOG = LoggerFactory.getLogger(MedicalConsultationMasterdataController.class);

    private MasterDataService masterDataService;

    public MedicalConsultationMasterdataController(MasterDataService masterDataService){
        super();
        this.masterDataService = masterDataService;
    }

    @GetMapping(value = "/medicalAttention")
    public ResponseEntity<Collection<MasterDataProjection>> getMedicalAttention(){
        LOG.debug("{}", "All medical attention type");
        return ResponseEntity.ok().body(masterDataService.findAll(MedicalAttentionType.class));
    }

    @GetMapping(value = "/appointmentState")
    public ResponseEntity<Collection<MasterDataProjection>> getAppointmentState(){
        LOG.debug("{}", "All appointment state");
        return ResponseEntity.ok().body(masterDataService.findAll(AppointmentState.class));
    }

	@GetMapping(value= "/appointment-block-motive")
	public ResponseEntity<Collection<MasterDataProjection>> getAppointmentBlockMotive() {
		LOG.debug("{}", "All appointment block motive");
		return ResponseEntity.ok().body(masterDataService.findAll(AppointmentBlockMotive.class));
	}
}
