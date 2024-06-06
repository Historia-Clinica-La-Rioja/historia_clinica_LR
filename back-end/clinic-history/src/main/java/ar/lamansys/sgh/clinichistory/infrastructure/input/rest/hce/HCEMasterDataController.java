package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EAnesthesiaZone;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EPersonalHistoryType;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EPreviousAnesthesiaState;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.EProblemErrorReason;
import ar.lamansys.sgx.shared.masterdata.domain.EnumWriter;
import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/hce/masterdata")
@Tag(name = "HCE master data", description = "HCE master data")
public class HCEMasterDataController {

    @GetMapping(value = "/health/error-reasons")
    public ResponseEntity<Collection<MasterDataDto>> getProblemErrorReasons() {
        log.debug("Return all health condition error reasons");
        return ResponseEntity.ok().body(EnumWriter.writeList(EProblemErrorReason.getAll()));
    }

    @GetMapping(value = "/health/personal-history")
    public ResponseEntity<Collection<MasterDataDto>> getPersonalHistoryTypes() {
        log.debug("Return all personal history types");
        return ResponseEntity.ok().body(EnumWriter.writeList(EPersonalHistoryType.getAll()));
    }

    @GetMapping(value = "/health/anesthesia-state")
    public ResponseEntity<Collection<MasterDataDto>> getAnesthesiaStateOptions() {
        log.debug("Return all anesthesia state options");
        return ResponseEntity.ok().body(EnumWriter.writeList(EPreviousAnesthesiaState.getAll()));
    }

    @GetMapping(value = "/health/anesthesia-zone")
    public ResponseEntity<Collection<MasterDataDto>> getAnesthesiaZoneOptions() {
        log.debug("Return all anesthesia zone options");
        return ResponseEntity.ok().body(EnumWriter.writeList(EAnesthesiaZone.getAll()));
    }
}
