package net.pladema.imagenetwork.domain.filters;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class StudyQueryFilter {

    @JsonProperty("StudyInstanceUID")
    private String studyInstanceUID;
}
