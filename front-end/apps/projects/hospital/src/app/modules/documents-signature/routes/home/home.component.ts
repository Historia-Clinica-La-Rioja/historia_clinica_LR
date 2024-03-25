import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AppFeature } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';

const URL_DIGITAL_SIGNATURE = 'firma-digital/documentos';
const URL_JOINT_SIGNATURE = 'firma-conjunta';
export const URL_DOCUMENTS_SIGNATURE = 'firma-documentos';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  routePrefix: string;
  isEnabledDigitalSignature: boolean;
  isEnabledJointSignature = false ;
  constructor(private readonly featureFlagService: FeatureFlagService,
    private router: Router,
    private contextService: ContextService) {
    this.routePrefix = 'institucion/' + this.contextService.institutionId + '/'
    this.featureFlagService.isActive(AppFeature.HABILITAR_FIRMA_DIGITAL).subscribe(isEnabled => this.isEnabledDigitalSignature = isEnabled);
    this.featureFlagService.isActive(AppFeature.HABILITAR_FIRMA_CONJUNTA).subscribe(isEnabled => this.isEnabledJointSignature = isEnabled);
  }

  ngOnInit(): void {
    if (this.isEnabledDigitalSignature && !this.isEnabledJointSignature) {
      this.redirecToDigitalSignature();
    } else if (this.isEnabledJointSignature && !this.isEnabledDigitalSignature) {
      this.redirecToJointSignature();
    }
  }

  redirecToDigitalSignature() {
    this.router.navigate([`${this.routePrefix}${URL_DIGITAL_SIGNATURE}`]);
  }

  redirecToJointSignature() {
    this.router.navigate([`${this.routePrefix}${URL_JOINT_SIGNATURE}`]);
  }
}
