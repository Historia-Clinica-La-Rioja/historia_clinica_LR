package ar.lamansys.refcounterref.infraestructure.input.rest;

import ar.lamansys.refcounterref.domain.enums.EReferenceClosureType;
import ar.lamansys.sgx.shared.masterdata.domain.EnumWriter;
import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collection;

@RestController
@RequestMapping("/reference/masterdata")
@Slf4j
@Tag(name = "Reference Master Data", description = "Reference Master Data")
public class ReferenceMasterDataController {
	@GetMapping(value = "/closure-types")
	public ResponseEntity<Collection<MasterDataDto>> getClosureTypes() {
		log.debug("{}", "All closure types");
		return ResponseEntity.ok().body(EnumWriter.writeList(EReferenceClosureType.getAll()));
	}
}
