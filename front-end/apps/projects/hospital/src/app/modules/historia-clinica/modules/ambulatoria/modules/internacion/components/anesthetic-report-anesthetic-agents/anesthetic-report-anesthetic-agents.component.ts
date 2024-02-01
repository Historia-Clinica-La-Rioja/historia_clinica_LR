import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { AnestheticReportPremedicationAndFoodIntakeService } from '../../services/anesthetic-report-premedication-and-food-intake.service';
import { AnestheticReportPemedicationComponent } from '../../dialogs/anesthetic-report-pemedication/anesthetic-report-pemedication.component';

@Component({
    selector: 'app-anesthetic-report-anesthetic-agents',
    templateUrl: './anesthetic-report-anesthetic-agents.component.html',
    styleUrls: ['./anesthetic-report-anesthetic-agents.component.scss']
})
export class AnestheticReportAnestheticAgentsComponent implements OnInit {

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

    addAnestheticAgent(){
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
