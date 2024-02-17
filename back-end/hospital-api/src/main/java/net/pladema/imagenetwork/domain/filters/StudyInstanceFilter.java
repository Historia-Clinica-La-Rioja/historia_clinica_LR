package net.pladema.imagenetwork.domain.filters;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StudyInstanceFilter {

    @JsonProperty("Level")
    private String level;

    @JsonProperty("Query")
    private StudyQueryFilter query;

    public StudyInstanceFilter(String studyInstanceUID) {
        level = "Study";
        this.setQuery(studyInstanceUID);
    }

    public void setQuery(String studyInstanceUID) {
        this.query = new StudyQueryFilter();
        this.query.setStudyInstanceUID(studyInstanceUID);
    }

}
