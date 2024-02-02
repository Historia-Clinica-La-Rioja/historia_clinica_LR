import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { AnestheticReportPemedicationComponent } from '../../dialogs/anesthetic-report-pemedication/anesthetic-report-pemedication.component';
import { AnestheticReportPremedicationAndFoodIntakeService } from '../../services/anesthetic-report-premedication-and-food-intake.service';

@Component({
    selector: 'app-anesthetic-report-antibiotic-prophylaxis',
    templateUrl: './anesthetic-report-antibiotic-prophylaxis.component.html',
    styleUrls: ['./anesthetic-report-antibiotic-prophylaxis.component.scss']
})
export class AnestheticReportAntibioticProphylaxisComponent implements OnInit {

    @Input() service: AnestheticReportPremedicationAndFoodIntakeService;
    searchConceptsLocallyFFIsOn = false;

    constructor(
        private readonly dialog: MatDialog,
        private readonly featureFlagService: FeatureFlagService,
    ) { }

    ngOnInit(): void {
        this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
            this.searchConceptsLocallyFFIsOn = isOn;
        });
    }

    addAntibioticProphylaxis() {
        this.dialog.open(AnestheticReportPemedicationComponent, {
            data: {
                premedicationService: this.service,
                searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
            },
            autoFocus: false,
            width: '30%',
            disableClose: true,
        });
    }

}
