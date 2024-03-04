import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature, MasterDataDto } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { MedicationService } from '../../services/medicationService';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { take } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';
import { AnestheticDrugComponent } from '../../dialogs/anesthetic-drug/anesthetic-drug.component';

@Component({
  selector: 'app-anesthetic-plan',
  templateUrl: './anesthetic-plan.component.html',
  styleUrls: ['./anesthetic-plan.component.scss']
})
export class AnestheticPlanComponent implements OnInit {

  @Input() service: MedicationService;
	searchConceptsLocallyFFIsOn = false;
  viasArray: MasterDataDto[];
  private title: string
  private label: string

    constructor(
		private readonly dialog: MatDialog,
		private readonly featureFlagService: FeatureFlagService,
    readonly internacionMasterDataService: InternacionMasterDataService,
    private readonly translateService: TranslateService
    ) { }

    ngOnInit(): void {
      this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFFIsOn = isOn;
		});
    this.translateService.get(['internaciones.anesthesic-report.anesthetic-plan.ADD_PLAN_TITLE', 'internaciones.anesthesic-report.anesthetic-plan.MEDICATION' ]).subscribe(
      (msg) => {
        const messagesValues: string[] = Object.values(msg);
        this.title = messagesValues[0]
        this.label = messagesValues[1]
      }
  );
    this.internacionMasterDataService.getViasAnestheticPlan().pipe(take(1)).subscribe(vias => this.viasArray = vias)
    }

    addMedication(){
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


