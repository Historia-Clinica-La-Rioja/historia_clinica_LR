package net.pladema.establishment.repository;

import net.pladema.UnitRepository;
import net.pladema.establishment.repository.domain.BedSummaryVo;
import net.pladema.establishment.repository.entity.AgeGroup;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.establishment.repository.entity.BedCategory;
import net.pladema.establishment.repository.entity.CareType;
import net.pladema.establishment.repository.entity.ClinicalSpecialtySector;
import net.pladema.establishment.repository.entity.Room;
import net.pladema.establishment.repository.entity.Sector;
import net.pladema.establishment.repository.entity.SectorOrganization;
import net.pladema.staff.repository.entity.ClinicalSpecialty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BedSummaryRepositoryTest extends UnitRepository {

    @Autowired
    private EntityManager entityManager;

    private BedSummaryRepositoryImpl bedSummaryRepository;

    @BeforeEach
    void setUp(){
        this.bedSummaryRepository = new BedSummaryRepositoryImpl(entityManager);
    }

    @Test
    void test_getBedSummaries_success(){
        Integer institutionId = 1;

        CareType ct = new CareType();
        ct.setId((short) 1);
        ct.setDescription("Care type 1");
        save(ct);

        SectorOrganization so = new SectorOrganization();
        so.setId((short) 1);
        so.setDescription("Sector Organization 1");
        save(so);

        AgeGroup ag1 = new AgeGroup();
        ag1.setId((short) 1);
        ag1.setDescription("Age group 1");
        save(ag1);

        AgeGroup ag2 = new AgeGroup();
        ag2.setId((short) 2);
        ag2.setDescription("Age group 2");
        save(ag2);

        BedCategory bc = new BedCategory();
        bc.setDescription("Bed category 1");
        bc = save(bc);

        Sector s1 = mockSector(institutionId, ct, so, ag1);
        Sector s2 = mockSector(institutionId, ct, so, ag2);

        Room r1 = mockRoom(s1, "1", "Room description 1");
        Room r2 = mockRoom(s2, "2", "Room description 2");

        ClinicalSpecialty cs1 = getClinicalSpecialty("Clinical specialty 1", "Sctid code 1");
        ClinicalSpecialty cs2 = getClinicalSpecialty("Clinical specialty 2", "Sctid code 2");

        mockClinicalSpecialtySector(s1, cs1, "ClinicalSpecialty 1 Sector 1");
        mockClinicalSpecialtySector(s1, cs2, "ClinicalSpecialty 2 Sector 1");
        mockClinicalSpecialtySector(s2, cs2, "ClinicalSpecialty 2 Sector 2");

        // room 1 beds
        mockBed(bc, r1, "Bed 1");
        mockBed(bc, r1, "Bed 2");
        mockBed(bc, r1, "Bed 3");
        // room 2 beds
        mockBed(bc, r2, "Bed 4");
        mockBed(bc, r2, "Bed 5");

        List<BedSummaryVo> bedSummaries = bedSummaryRepository.execute(institutionId);

        assertThat(bedSummaries)
                .hasSize(5);

        assertThat(bedSummaries)
                .filteredOn(summary -> summary.getSector().getId().equals(s1.getId()))
                .hasSize(3)
                .allSatisfy(summary ->
                        assertThat(summary.getSector().getClinicalSpecialties().size() == 2))
                .allSatisfy(summary ->
                        assertThat(summary.getSector().getAgeGroup().equals(ag1.getDescription())));

        assertThat(bedSummaries)
                .filteredOn(summary -> summary.getSector().getId().equals(s2.getId()))
                .hasSize(2)
                .allSatisfy(summary ->
                        assertThat(summary.getSector().getClinicalSpecialties().size() == 1))
                .allSatisfy(summary ->
                        assertThat(summary.getSector().getAgeGroup().equals(ag2.getDescription())));

    }

    private Sector mockSector(Integer institutionId, CareType ct, SectorOrganization so, AgeGroup ag) {
        Sector s = new Sector();
        s.setInstitutionId(institutionId);
        s.setDescription("Sector 1 description");
        s.setCareTypeId(ct.getId());
        s.setSectorOrganizationId(so.getId());
        s.setAgeGroupId(ag.getId());
        s = save(s);
        return s;
    }

    private Room mockRoom(Sector s, String roomNumber, String description) {
        Room r = new Room();
        r.setRoomNumber(roomNumber);
        r.setType("Room type");
        r.setDescription(description);
        r.setSectorId(s.getId());
        r = save(r);
        return r;
    }
    
    private void mockClinicalSpecialtySector(Sector s, ClinicalSpecialty cs, String description) {
        ClinicalSpecialtySector css = new ClinicalSpecialtySector();
        css.setSectorId(s.getId());
        css.setClinicalSpecialtyId(cs.getId());
        css.setDescription(description);
        save(css);
    }

    private ClinicalSpecialty getClinicalSpecialty(String name, String sctidCode) {
        ClinicalSpecialty cs = new ClinicalSpecialty();
        cs.setName(name);
        cs.setClinicalSpecialtyTypeId((short) 1);
        cs.setSctidCode(sctidCode);
        cs = save(cs);
        return cs;
    }


    private void mockBed(BedCategory bc, Room r, String bedNumber) {
        Bed b = new Bed();
        b.setRoomId(r.getId());
        b.setBedCategoryId(bc.getId());
        b.setEnabled(true);
        b.setFree(true);
        b.setAvailable(true);
        b.setBedNumber(bedNumber);
        save(b);
    }

}
