import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';
import { URL_DOCUMENTS_SIGNATURE } from '../../../../routes/home/home.component';
import { AppFeature } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { Detail } from '@presentation/components/details-section-custom/details-section-custom.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent{
  buttonBack = false;
  routePrefix: string;
  data: Detail[] = [];
  constructor(private readonly router: Router, private readonly contextService: ContextService, 
    private readonly featureFlagService: FeatureFlagService) { 
    this.routePrefix = 'institucion/' + this.contextService.institutionId + '/'
    this.featureFlagService.isActive(AppFeature.HABILITAR_FIRMA_DIGITAL).subscribe(isEnabledDigital =>{
      this.featureFlagService.isActive(AppFeature.HABILITAR_FIRMA_CONJUNTA).subscribe(isEnabledConjunta =>{
        if(isEnabledConjunta && isEnabledDigital){
          this.buttonBack = true;
        }
      }) 
    })
  }

  goToBackDocumentsSignature(){
    this.router.navigate([`${this.routePrefix}${URL_DOCUMENTS_SIGNATURE}`]);
  }

}
