import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature, MasterDataDto, TimeDto } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { TranslateService } from '@ngx-translate/core';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { take } from 'rxjs';
import { AnestheticDrugComponent } from '../../dialogs/anesthetic-drug/anesthetic-drug.component';
import { AnestheticReportService } from '../../services/anesthetic-report.service';


@Component({
    selector: 'app-anesthetic-report-premedication-and-food-intake',
    templateUrl: './anesthetic-report-premedication-and-food-intake.component.html',
    styleUrls: ['./anesthetic-report-premedication-and-food-intake.component.scss']
})
export class AnestheticReportPremedicationAndFoodIntakeComponent implements OnInit {

    private viasArray: MasterDataDto[];
    private title: string
    private label: string

    @Output() timeSelected: EventEmitter<TimeDto> = new EventEmitter<TimeDto>();
	searchConceptsLocallyFFIsOn = false;

    constructor(
		private readonly dialog: MatDialog,
		private readonly featureFlagService: FeatureFlagService,
        private readonly translateService: TranslateService,
        readonly service: AnestheticReportService,
        readonly internacionMasterDataService: InternacionMasterDataService,
    ) { }

    ngOnInit(): void {
        this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
            this.searchConceptsLocallyFFIsOn = isOn;
        });
        this.internacionMasterDataService.getViasPremedication().pipe(take(1)).subscribe(vias => this.viasArray = vias);
        this.translateService.get(['internaciones.anesthesic-report.premedication-and-food-intake.ADD_PREMEDICATION_TITLE',
            'internaciones.anesthesic-report.premedication-and-food-intake.PREMEDICATION']).subscribe(
                (msg) => {
                    const messagesValues: string[] = Object.values(msg);
                    this.title = messagesValues[0]
                    this.label = messagesValues[1]
                }
            );
    }

    addPremedication(){
        this.dialog.open(AnestheticDrugComponent, {
            data: {
                premedicationService: this.service.anestheticReportPremedicationAndFoodIntakeService,
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

    onTimeSelected(newTimeValue: TimeDto) {
        this.timeSelected.emit(newTimeValue);
    }

}
