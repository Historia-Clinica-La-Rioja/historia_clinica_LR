package net.pladema.clinichistory.requests.transcribed.infrastructure.output.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.clinichistory.requests.servicerequests.repository.domain.FileVo;
import net.pladema.clinichistory.requests.transcribed.infrastructure.output.repository.entity.OrderImageFile;

@Repository
public interface OrderImageFileRepository extends JpaRepository<OrderImageFile, Integer> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE OrderImageFile oif " +
            "SET oif.orderId = :oiId " +
            "WHERE oif.id IN :fileIds ")
    void updateOrderImageFile(@Param("oiId") Integer orderImageId, @Param("fileIds") List<Integer> fileIds);

    @Transactional(readOnly = true)
    @Query("SELECT oif.path " +
            "FROM OrderImageFile oif " +
            "WHERE oif.orderId IS NULL ")
    List<String> getDanglingFiles();

    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.clinichistory.requests.servicerequests.repository.domain.FileVo(oif.id, oif.name) " +
            "FROM OrderImageFile oif " +
            "WHERE oif.orderId = :oiId ")
    List<FileVo> getFilesByOrderId(@Param("oiId") Integer orderImageId);
}
