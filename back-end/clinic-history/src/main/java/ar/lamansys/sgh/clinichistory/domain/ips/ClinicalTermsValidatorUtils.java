package ar.lamansys.sgh.clinichistory.domain.ips;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClinicalTermsValidatorUtils {

    public static boolean repeatedClinicalTerms(List<? extends ClinicalTerm> clinicalTerms) {
        if (clinicalTerms == null || clinicalTerms.isEmpty())
            return false;
        final Set<SnomedBo> set = new HashSet<>();
        for (ClinicalTerm ct : clinicalTerms)
            if (!set.add(ct.getSnomed()))
                return true;
        return false;
    }
}
