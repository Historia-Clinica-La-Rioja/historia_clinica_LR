package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.domain.ips.FoodIntakeBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentFoodInTakeRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentFoodInTake;
import java.time.LocalTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoadFoodInTake {

    private final DocumentFoodInTakeRepository documentFoodInTakeRepository;

    public FoodIntakeBo run(Long documentId, Optional<FoodIntakeBo> foodInTake) {
        log.debug("Input parameters -> documentId {} foodInTake {}", documentId, foodInTake);

        foodInTake.ifPresent((foodInTakeBo -> {
            LocalTime clockTime = foodInTakeBo.getClockTime();
            DocumentFoodInTake saved = documentFoodInTakeRepository.save(new DocumentFoodInTake(documentId, clockTime));
            foodInTakeBo.setId(saved.getDocumentId());
        }));

        log.debug("Output -> {}", foodInTake);
        return foodInTake.orElse(null);
    }
}
