package net.pladema.establishment.repository;

import net.pladema.establishment.repository.domain.BedCategoriesDataFilterVo;
import net.pladema.establishment.repository.domain.BedCategoriesDataVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BedDataRepositoryImpl implements BedDataRepository {

    private static final Logger LOG = LoggerFactory.getLogger(net.pladema.establishment.repository.BedDataRepositoryImpl.class);

    private final EntityManager entityManager;

    public BedDataRepositoryImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }


    @Override
    @Transactional(readOnly = true)
    public List<BedCategoriesDataVo> execute(BedCategoriesDataFilterVo filter) {
        LOG.debug("Input parameters -> filter {}", filter);

        String sqlString2 = "SELECT bc.id, bc.description, COUNT(CASE WHEN b.free THEN 1 END) as libres, COUNT(CASE WHEN NOT b.free THEN 1 END) as ocupadas" +
                " FROM {h-schema}bed b " +
				"JOIN {h-schema}bed_category bc ON bc.id = b.bed_category_id " +
                (filter.getSectorDescription() != null ? "JOIN room r ON b.room_id = r.id " +
                " JOIN {h-schema}sector s ON r.sector_id = s.id " : "") +
                (filter.getSectorDescription() != null ?
                " WHERE UPPER(s.description) LIKE :sectorDescription " : "")  +
                " GROUP BY bc.id, bc.description ORDER BY bc.id";
        Query query = entityManager.createNativeQuery(sqlString2);

        if (filter.getSectorDescription() != null)
            query.setParameter("sectorDescription", "%"+filter.getSectorDescription().toUpperCase()+"%");
        List<Object[]> queryResult = query.getResultList();
        List<BedCategoriesDataVo> result = new ArrayList<>();
        queryResult.forEach( bedData -> result.add(new BedCategoriesDataVo( (Short)bedData[0], (String)bedData[1], (BigInteger)bedData[2], (BigInteger)bedData[3] )));
        return result;
    }
}
