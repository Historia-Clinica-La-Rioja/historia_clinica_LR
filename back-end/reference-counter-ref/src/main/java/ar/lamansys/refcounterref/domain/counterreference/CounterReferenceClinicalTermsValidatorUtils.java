package ar.lamansys.refcounterref.domain.counterreference;

import ar.lamansys.refcounterref.domain.clinicalterm.ReferenceCounterReferenceClinicalTermBo;
import ar.lamansys.refcounterref.domain.snomed.SnomedBo;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CounterReferenceClinicalTermsValidatorUtils {

    public static boolean repeatedClinicalTerms(List<? extends ReferenceCounterReferenceClinicalTermBo> clinicalTerms) {
        if (clinicalTerms == null || clinicalTerms.isEmpty())
            return false;
        final Set<SnomedBo> set = new HashSet<>();
        for (ReferenceCounterReferenceClinicalTermBo ct : clinicalTerms)
            if (!set.add(ct.getSnomed()))
                return true;
        return false;
    }

}