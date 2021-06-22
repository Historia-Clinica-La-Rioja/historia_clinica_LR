package ar.lamansys.immunization.domain.vaccine;

import java.util.List;

public interface VaccineByFilterPort {

    List<VaccineBo> run(Integer days);
}
