package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgx.shared.exceptions.SelfValidating;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
public abstract class ClinicalTerm extends SelfValidating<ClinicalTerm> {

    private Integer id;

    private Integer patientId;

    @Valid
    @NotNull(message = "{value.mandatory}")
    private SnomedBo snomed;

    private String statusId;

    private String status;

    private String cie10codes;

	public String getSnomedSctid(){
		return getSnomed()!=null ? getSnomed().getSctid() : null;
	}

    protected ClinicalTerm(Integer id, Integer patientId, @Valid @NotNull(message = "{value.mandatory}") SnomedBo snomed,
                        String statusId, String status, String cie10codes) {
        this.id = id;
        this.patientId = patientId;
        this.snomed = snomed;
        this.statusId = statusId;
        this.status = status;
        this.cie10codes = cie10codes;
    }

	public boolean equals(ClinicalTerm c){
		return getSnomedSctid().equals(c.getSnomedSctid());
	}

    protected ClinicalTerm(@Valid @NotNull(message = "{value.mandatory}") SnomedBo snomed) {
        this.snomed = snomed;
    }
}
