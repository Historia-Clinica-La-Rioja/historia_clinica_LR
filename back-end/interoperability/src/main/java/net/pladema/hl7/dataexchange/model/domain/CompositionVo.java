//
// Clinical document used to represent the International Patient Summary (IPS) data set. The IPS dataset is a
// minimal and non-exhaustive patient summary dataset, specialty-agnostic, condition-independent, but readily usable by clinicians
// for the cross-border unscheduled care of a patient.
//

package net.pladema.hl7.dataexchange.model.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.hl7.dataexchange.model.adaptor.FhirAddress;
import net.pladema.hl7.dataexchange.model.adaptor.FhirDateMapper;
import org.apache.commons.text.WordUtils;
import org.hl7.fhir.r4.model.Coding;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class CompositionVo {

    private final Map<Short, String> CONFIDENTIALITY = new HashMap<>();

    private Date createdOn;
    private Short confidentiality;

    private Coding type;

    public CompositionVo(Coding type){
        this();
        this.type=type;
        this.createdOn = new Date();
    }

    public CompositionVo(){
        CONFIDENTIALITY.put((short)1, "U");
        CONFIDENTIALITY.put((short)2, "L");
        CONFIDENTIALITY.put((short)3, "M");
        CONFIDENTIALITY.put((short)4, "N");
        CONFIDENTIALITY.put((short)5, "R");
        CONFIDENTIALITY.put((short)6, "V");
    }

    public void setCreatedOn(LocalDate date){
        createdOn = FhirDateMapper.toDate(date);
    }

    public String getConfidentiality(){
        if(confidentiality == null)
            return "N";
        return CONFIDENTIALITY.get(confidentiality);
    }

    public String getTitle() {
        if (createdOn != null) {
            String formattedDate = WordUtils.capitalizeFully(
                    (new SimpleDateFormat("d 'de' MMMM 'de' yyyy',' HH:ss'hs'", new Locale("es", "AR")).format(createdOn)));
            return "Resumen del paciente al ".concat(formattedDate);
        }
        return "Resumen del paciente";
    }

}
