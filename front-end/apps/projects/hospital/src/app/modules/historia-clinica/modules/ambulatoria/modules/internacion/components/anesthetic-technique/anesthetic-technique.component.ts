import { Component, OnInit } from '@angular/core';
import { AnestheticTechniqueData } from '../../services/anesthetic-technique.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature } from '@api-rest/api-model';
import { AnestheticTechniquePopupComponent } from '../../dialogs/anesthetic-technique-popup/anesthetic-technique-popup.component';
import { Observable } from 'rxjs';
import { AnestheticReportService } from '../../services/anesthetic-report.service';

@Component({
  selector: 'app-anesthetic-technique',
  templateUrl: './anesthetic-technique.component.html',
  styleUrls: ['./anesthetic-technique.component.scss']
})
export class AnestheticTechniqueComponent implements OnInit {

  searchConceptsLocallyFFIsOn = false;
  anestheticTechniqueList$: Observable<AnestheticTechniqueData[]>

  constructor(
    private readonly dialog: MatDialog,
		private readonly featureFlagService: FeatureFlagService,
    readonly service: AnestheticReportService,
  ) { }

  ngOnInit(): void {
    this.anestheticTechniqueList$ = this.service.anestheticTechniqueService.getAnestheticTechniqueList()
    this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFFIsOn = isOn;
		});
  }


  addAnestheticTechnique(): void {
    this.dialog.open(AnestheticTechniquePopupComponent, {
      data: {
          anestheticTechniqueService: this.service.anestheticTechniqueService,
          searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
      },
      autoFocus: false,
      width: '30%',
      disableClose: true,
  });
  }
}
