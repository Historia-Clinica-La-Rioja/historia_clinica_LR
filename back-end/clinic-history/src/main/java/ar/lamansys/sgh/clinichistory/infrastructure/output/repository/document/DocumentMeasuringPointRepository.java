package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.domain.ips.MeasuringPointBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentMeasuringPoint;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentMeasuringPointPK;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DocumentMeasuringPointRepository extends JpaRepository<DocumentMeasuringPoint, DocumentMeasuringPointPK> {

    @Transactional(readOnly = true)
    @Query(value = "SELECT new ar.lamansys.sgh.clinichistory.domain.ips.MeasuringPointBo(" +
            "mp.id, mp.date, mp.time, mp.bloodPressureMin, mp.bloodPressureMax, mp.bloodPulse, mp.o2Saturation, mp.co2EndTidal)" +
            "FROM DocumentMeasuringPoint dmp " +
            "JOIN MeasuringPoint mp ON (dmp.pk.measuringPointId = mp.id) " +
            "WHERE dmp.pk.documentId = :documentId")
    List<MeasuringPointBo> getMeasuringPointStateFromDocument(@Param("documentId") Long documentId);
}
