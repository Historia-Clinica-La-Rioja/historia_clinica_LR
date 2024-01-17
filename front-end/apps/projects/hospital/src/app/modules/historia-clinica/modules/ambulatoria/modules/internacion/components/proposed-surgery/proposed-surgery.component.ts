import { Component, OnInit } from '@angular/core';
import { AnestheticReportProposedSurgeryService } from '../../services/anesthetic-report-proposed-surgery.service';
import { AnesthesicReportAddProposedSurgeryComponent } from '../../dialogs/anesthesic-report-add-proposed-surgery/anesthesic-report-add-proposed-surgery.component';
import { MatDialog } from '@angular/material/dialog';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { AppFeature } from '@api-rest/api-model';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
    selector: 'app-proposed-surgery',
    templateUrl: './proposed-surgery.component.html',
    styleUrls: ['./proposed-surgery.component.scss']
})
export class ProposedSurgeryComponent implements OnInit {

    anesthesicReportProposedSurgeryService: AnestheticReportProposedSurgeryService;
	searchConceptsLocallyFFIsOn = false;

    constructor(
		private readonly dialog: MatDialog,
		private readonly featureFlagService: FeatureFlagService,
        private readonly snomedService: SnomedService,
        private readonly snackBarService: SnackBarService,
    ) {
        this.anesthesicReportProposedSurgeryService = new AnestheticReportProposedSurgeryService(this.snomedService, this.snackBarService);
    }

    ngOnInit(): void {
        this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFFIsOn = isOn;
		});
    }

    addProposedSurgery(){
        this.dialog.open(AnesthesicReportAddProposedSurgeryComponent, {
            data: {
                proposedSurgeryService: this.anesthesicReportProposedSurgeryService,
                searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
            },
            autoFocus: false,
            width: '35%',
            disableClose: true,
        });
    }
}
