import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature, MasterDataDto } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { AnestheticDrugComponent } from '../../dialogs/anesthetic-drug/anesthetic-drug.component';
import { MedicationService } from '../../services/medicationService';
import { TranslateService } from '@ngx-translate/core';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { take } from 'rxjs';

@Component({
    selector: 'app-anesthetic-report-anesthetic-agents',
    templateUrl: './anesthetic-report-anesthetic-agents.component.html',
    styleUrls: ['./anesthetic-report-anesthetic-agents.component.scss']
})
export class AnestheticReportAnestheticAgentsComponent implements OnInit {

    @Input() service: MedicationService;
    searchConceptsLocallyFFIsOn = false;
    private viasArray: MasterDataDto[];
    private title: string;
    private label: string;

    constructor(
        private readonly dialog: MatDialog,
        private readonly translateService: TranslateService,
        private readonly featureFlagService: FeatureFlagService,
        readonly internacionMasterDataService: InternacionMasterDataService,
    ) { }

    ngOnInit(): void {
        this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
            this.searchConceptsLocallyFFIsOn = isOn;
        });
        this.internacionMasterDataService.getViasAnestheticAgent().pipe(take(1)).subscribe(vias => this.viasArray = vias);
        this.translateService.get(['internaciones.anesthesic-report.anesthetic-agents.TITLE',
            'internaciones.anesthesic-report.anesthetic-agents.LABEL']).subscribe(
                (msg) => {
                    const messagesValues: string[] = Object.values(msg);
                    this.title = messagesValues[0]
                    this.label = messagesValues[1]
                }
            );
    }

    addAnestheticAgent() {
        this.dialog.open(AnestheticDrugComponent, {
            data: {
                premedicationService: this.service,
                searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
                vias: this.viasArray,
                presentationConfig: {
                    title: this.title,
                    label: this.label
                }
            },
            autoFocus: false,
            width: '30%',
            disableClose: true,
        });
    }
}
