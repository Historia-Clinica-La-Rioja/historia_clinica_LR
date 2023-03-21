package net.pladema.patient.service.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class AuditablePatientInfoBo {

	private String message;

	private String institutionName;

	private LocalDateTime createdOn;

	private String authorName;

	private String authorMiddleNames;

	private String authorLastName;

	private String authorOtherLastNames;

	private String authorNameSelfDetermination;

	private boolean includeNameSelfDetermination;

	public AuditablePatientInfoBo(String message, String institutionName, LocalDateTime createdOn, String authorName,
								  String authorMiddleNames, String authorLastName, String authorOtherLastNames,
								  String authorNameSelfDetermination) {
		this.message = message;
		this.institutionName = institutionName;
		this.createdOn = createdOn;
		this.authorName = authorName;
		this.authorMiddleNames = authorMiddleNames;
		this.authorLastName = authorLastName;
		this.authorOtherLastNames = authorOtherLastNames;
		this.authorNameSelfDetermination = authorNameSelfDetermination;
	}

	public String getAuthorFullName() {
		String name = null;
		if (includeNameSelfDetermination && !(authorNameSelfDetermination == null || authorNameSelfDetermination.isBlank())) {
			name = authorNameSelfDetermination;
			authorMiddleNames = null;
		} else name = authorName;
		return Stream.of(authorLastName, authorOtherLastNames, name, authorMiddleNames)
				.filter(Objects::nonNull)
				.collect(Collectors.joining(" "));
	}
}
