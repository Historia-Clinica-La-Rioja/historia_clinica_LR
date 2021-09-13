package ar.lamansys.odontology.domain.consultation;

import ar.lamansys.odontology.domain.OdontologySnomedBo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClinicalTermsValidatorUtils {

    public static boolean repeatedClinicalTerms(List<? extends ClinicalTermBo> clinicalTerms) {
        if (clinicalTerms == null || clinicalTerms.isEmpty())
            return false;
        final Set<OdontologySnomedBo> set = new HashSet<>();
        for (ClinicalTermBo ct : clinicalTerms)
            if (!set.add(ct.getSnomed()))
                return true;
        return false;
    }
}
