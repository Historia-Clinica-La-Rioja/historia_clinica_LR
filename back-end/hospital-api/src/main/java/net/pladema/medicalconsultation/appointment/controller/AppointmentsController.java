package net.pladema.medicalconsultation.appointment.controller;

import io.swagger.annotations.Api;
import net.pladema.medicalconsultation.appointment.controller.dto.CreateAppointmentDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentListDto;
import net.pladema.medicalconsultation.appointment.controller.mock.AppointmentMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/institutions/{institutionId}/medicalConsultations/appointments")
@Api(value = "Appointments ", tags = { "Appointments" })
@Validated
public class AppointmentsController {

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentsController.class);

    public static final String OUTPUT = "Output -> {}";

    public AppointmentsController() {
        super();
    }

    @PostMapping
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO')")
    @Transactional
    public ResponseEntity<Integer> create(
            @PathVariable(name = "institutionId") Integer institutionId,
            @RequestBody CreateAppointmentDto createAppointmentDto) {
        LOG.debug("Input parameters -> institutionId {}, appointmentDto {}", institutionId, createAppointmentDto);
        Random rand = new Random();
        Integer result = rand.nextInt(1000) + 0;;
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO')")
    public ResponseEntity<Collection<AppointmentListDto>> getList(@PathVariable(name = "institutionId")  Integer institutionId,
                                                                  @RequestParam(name = "diaryIds") @NotEmpty List<Integer> diaryIds){
        LOG.debug("Input parameters -> institutionId {}, diaryIds {}", institutionId, diaryIds);
        Collection<AppointmentListDto> result = AppointmentMock.mockListAppointmentListDto(diaryIds);
        LOG.debug("Output", result);
        return ResponseEntity.ok(result);
    }
}
