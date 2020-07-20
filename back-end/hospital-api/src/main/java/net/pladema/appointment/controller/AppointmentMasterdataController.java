package net.pladema.appointment.controller;

import io.swagger.annotations.Api;
import net.pladema.appointment.repository.entity.AppointmentState;
import net.pladema.appointment.repository.entity.MedicalAttentionType;
import net.pladema.appointment.service.AppointmentMasterDataService;
import net.pladema.clinichistory.hospitalization.controller.InternmentMasterdataController;
import net.pladema.sgx.masterdata.repository.MasterDataProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/institutions/{institutionId}/appointments/masterdata/")
@Api(value = "Appointments Master Data", tags = { "Appointments Master Data" })
public class AppointmentMasterdataController {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentMasterdataController.class);

    private AppointmentMasterDataService appointmentMasterDataService;

    public AppointmentMasterdataController(AppointmentMasterDataService appointmentMasterDataService){
        super();
        this.appointmentMasterDataService = appointmentMasterDataService;
    }

    @GetMapping(value = "/medicalAttention")
    public ResponseEntity<Collection<MasterDataProjection>> getMedicalAttention(){
        LOG.debug("{}", "All medical attention type");
        return ResponseEntity.ok().body(appointmentMasterDataService.findAll(MedicalAttentionType.class));
    }

    @GetMapping(value = "/appointmentState")
    public ResponseEntity<Collection<MasterDataProjection>> getAppointmentState(){
        LOG.debug("{}", "All appointment state");
        return ResponseEntity.ok().body(appointmentMasterDataService.findAll(AppointmentState.class));
    }
}
