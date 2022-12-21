package net.pladema.clinichistory.hospitalization.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.springframework.core.io.Resource;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StoredFileBo {

	private Resource resource;
}
