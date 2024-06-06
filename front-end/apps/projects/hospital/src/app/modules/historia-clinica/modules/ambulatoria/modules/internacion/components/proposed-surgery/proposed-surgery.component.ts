import { Component, Input, OnInit } from '@angular/core';
import { AnestheticReportProposedSurgeryService } from '../../services/anesthetic-report-proposed-surgery.service';
import { AnesthesicReportAddProposedSurgeryComponent } from '../../dialogs/anesthesic-report-add-proposed-surgery/anesthesic-report-add-proposed-surgery.component';
import { MatDialog } from '@angular/material/dialog';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { AppFeature } from '@api-rest/api-model';

@Component({
    selector: 'app-proposed-surgery',
    templateUrl: './proposed-surgery.component.html',
    styleUrls: ['./proposed-surgery.component.scss']
})
export class ProposedSurgeryComponent implements OnInit {

    @Input() service: AnestheticReportProposedSurgeryService;
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

    addProposedSurgery(){
        this.dialog.open(AnesthesicReportAddProposedSurgeryComponent, {
            data: {
                proposedSurgeryService: this.service,
                searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
            },
            autoFocus: false,
            width: '35%',
            disableClose: true,
        });
    }
}
