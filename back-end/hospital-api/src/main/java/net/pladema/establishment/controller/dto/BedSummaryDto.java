package net.pladema.establishment.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;

import javax.annotation.Nullable;

@Getter
@Setter
@ToString
public class BedSummaryDto {

    private static final long serialVersionUID = 5649684681812631433L;

    private BedDto bed;

    private SectorSummaryDto sector;

    @Nullable
    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_TIME_FORMAT)
    private String probableDischargeDate;
}
