import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { AnalgesicTechniqueData } from '../../services/analgesic-technique.service';
import { AnalgesicTechniquePopupComponent } from '../../dialogs/analgesic-technique-popup/analgesic-technique-popup.component';
import { Observable } from 'rxjs';
import { AnestheticReportService } from '../../services/anesthetic-report.service';

@Component({
  selector: 'app-analgesic-technique',
  templateUrl: './analgesic-technique.component.html',
  styleUrls: ['./analgesic-technique.component.scss']
})
export class AnalgesicTechniqueComponent implements OnInit {

	searchConceptsLocallyFFIsOn = false;
  analgesicTechniqueList$: Observable<AnalgesicTechniqueData[]>

  constructor(
    private readonly dialog: MatDialog,
		private readonly featureFlagService: FeatureFlagService,
    readonly service: AnestheticReportService,
  ) { }

  ngOnInit(): void {
    this.analgesicTechniqueList$ = this.service.analgesicTechniqueService.getAnalgesicTechniqueList()
    this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFFIsOn = isOn;
		});
  }

  addAnalgesicTechnique(): void {
    this.dialog.open(AnalgesicTechniquePopupComponent, {
      data: {
        analgesicTechniqueService: this.service.analgesicTechniqueService,
        searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
      },
      autoFocus: false,
      width: '30%',
      disableClose: true,
    }  )
  }

}
