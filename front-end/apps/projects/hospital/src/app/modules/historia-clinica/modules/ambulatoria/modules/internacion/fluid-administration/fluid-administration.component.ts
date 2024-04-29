import { Component, OnInit } from '@angular/core';
import { FluidAdministrationData } from '../services/fluid-administration.service';
import { MatDialog } from '@angular/material/dialog';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { AppFeature } from '@api-rest/api-model';
import { FluidAdministrationPopupComponent } from '../fluid-administration-popup/fluid-administration-popup.component';
import { Observable } from 'rxjs';
import { AnestheticReportService } from '../services/anesthetic-report.service';

@Component({
  selector: 'app-fluid-administration',
  templateUrl: './fluid-administration.component.html',
  styleUrls: ['./fluid-administration.component.scss']
})
export class FluidAdministrationComponent implements OnInit {

  searchConceptsLocallyFFIsOn = false;
  fluidAdministrationList$: Observable<FluidAdministrationData[]>

  constructor(
    private readonly dialog: MatDialog,
		private readonly featureFlagService: FeatureFlagService,
    readonly service: AnestheticReportService,
  ) { }

  ngOnInit(): void {
    this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFFIsOn = isOn;
		});
    this.fluidAdministrationList$ = this.service.fluidAdministrationService.getFluidAdministrationList()
  }

  addFluidAdministration(): void {
    this.dialog.open(FluidAdministrationPopupComponent, {
      data: {
        fluidAdministrationService: this.service.fluidAdministrationService,
        searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
      },
      autoFocus: false,
      width: '30%',
      disableClose: true,
  });
  }

}
