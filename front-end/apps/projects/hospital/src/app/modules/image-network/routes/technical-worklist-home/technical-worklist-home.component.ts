import { Component, OnInit } from '@angular/core';
import { AppFeature } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';

@Component({
  selector: 'app-technical-worklist-home',
  templateUrl: './technical-worklist-home.component.html',
  styleUrls: ['./technical-worklist-home.component.scss']
})
export class TechnicalWorklistHomeComponent implements OnInit {


  isEnableQueueImageList = false;

  constructor(
    private readonly featureFlagService: FeatureFlagService
  ) { }

  ngOnInit(): void {
    this.featureFlagService.isActive(AppFeature.HABILITAR_SOLAPA_COLA_IMAGENES)
    .subscribe(isEnabled => this.isEnableQueueImageList = isEnabled);

  }

}
