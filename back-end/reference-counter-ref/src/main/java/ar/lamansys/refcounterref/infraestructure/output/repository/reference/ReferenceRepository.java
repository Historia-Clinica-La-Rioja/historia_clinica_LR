package ar.lamansys.refcounterref.infraestructure.output.repository.reference;

import ar.lamansys.refcounterref.domain.reference.ReferenceGetBo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReferenceRepository extends JpaRepository<Reference, Integer> {

    @Transactional(readOnly = true)
    @Query(value = "SELECT new ar.lamansys.refcounterref.domain.reference.ReferenceGetBo(r.id, oc.startDate, " +
            "rn.id, rn.description, cl.id, cl.description, cs.id, cs.name, oc.doctorId, " +
            "p.firstName, p.lastName) " +
            "FROM Reference r " +
            "JOIN OutpatientConsultation oc ON (r.encounterId = oc.id) " +
            "JOIN ClinicalSpecialty cs ON (r.clinicalSpecialtyId = cs.id) " +
            "JOIN CareLine cl ON (cl.id = r.careLineId) " +
            "LEFT JOIN ReferenceNote rn ON (rn.id = r.referenceNoteId) " +
            "JOIN HealthcareProfessional hp ON (hp.id = oc.doctorId) " +
            "JOIN Person p ON (p.id = hp.personId) " +
            "WHERE oc.institutionId = :institutionId " +
            "AND oc.patientId = :patientId " +
            "AND r.clinicalSpecialtyId IN (:clinicalSpecialtyIds)")
    List<ReferenceGetBo> getReferences(@Param("institutionId") Integer institutionId, @Param("patientId") Integer patientId, @Param("clinicalSpecialtyIds") List<Integer> clinicalSpecialtyIds);

}