package net.pladema.violencereport.infrastructure.input.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Violence report", description = "Controller used to handle violence report related operations")
@RestController
@Validated
@PreAuthorize("hasPermission(#institutionId, 'ABORDAJE_VIOLENCIAS')")
@AllArgsConstructor
@RequestMapping(value = "/institution/{institutionId}/violence-report")
public class ViolenceReportController {

}
