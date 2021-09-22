package ar.lamansys.sgh.publicapi.infrastructure.input;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public-api")
@Api(value = "Public Api", tags = {"Public Api"})
public class ActivitiesController {

    @GetMapping("/example")
    public Object activitiesExample() {
        return "Activities Example";
    }
}
