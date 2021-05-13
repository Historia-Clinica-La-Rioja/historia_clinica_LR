package ar.lamansys.odontology.application.fetchodontogram;

import ar.lamansys.odontology.domain.TeethGroupBo;

import java.util.List;

public interface GetOdontogramInfoService {
    List<TeethGroupBo> run();
}
