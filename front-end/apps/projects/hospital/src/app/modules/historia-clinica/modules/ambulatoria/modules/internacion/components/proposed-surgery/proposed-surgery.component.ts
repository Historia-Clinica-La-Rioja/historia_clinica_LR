import { Component, OnInit } from '@angular/core';
import { AnesthesicReportAddProposedSurgeryComponent } from '../../dialogs/anesthesic-report-add-proposed-surgery/anesthesic-report-add-proposed-surgery.component';
import { MatDialog } from '@angular/material/dialog';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { AppFeature } from '@api-rest/api-model';
import { AnestheticReportService } from '../../services/anesthetic-report.service';

@Component({
    selector: 'app-proposed-surgery',
    templateUrl: './proposed-surgery.component.html',
    styleUrls: ['./proposed-surgery.component.scss']
})
export class ProposedSurgeryComponent implements OnInit {

	searchConceptsLocallyFFIsOn = false;

    constructor(
		private readonly dialog: MatDialog,
		private readonly featureFlagService: FeatureFlagService,
        readonly service: AnestheticReportService,
    ) { }

    ngOnInit(): void {
        this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFFIsOn = isOn;
		});
    }

    addProposedSurgery(){
        this.dialog.open(AnesthesicReportAddProposedSurgeryComponent, {
            data: {
                proposedSurgeryService: this.service.anesthesicReportProposedSurgeryService,
                searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
            },
            autoFocus: false,
            width: '35%',
            disableClose: true,
        });
    }
}
