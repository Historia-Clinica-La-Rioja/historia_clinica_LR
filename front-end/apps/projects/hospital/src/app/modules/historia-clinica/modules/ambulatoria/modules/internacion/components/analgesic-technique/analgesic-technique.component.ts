import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { AnalgesicTechniqueService } from '../../services/analgesic-technique.service';
import { AnalgesicTechniquePopupComponent } from '../../dialogs/analgesic-technique-popup/analgesic-technique-popup.component';

@Component({
  selector: 'app-analgesic-technique',
  templateUrl: './analgesic-technique.component.html',
  styleUrls: ['./analgesic-technique.component.scss']
})
export class AnalgesicTechniqueComponent implements OnInit {

  @Input() service: AnalgesicTechniqueService;
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

  addAnalgesicTechnique(): void {
    this.dialog.open(AnalgesicTechniquePopupComponent, {
      data: {
        analgesicTechniqueService: this.service,
        searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
      },
      autoFocus: false,
      width: '30%',
      disableClose: true,
    }  )
  }

}
