import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { Position } from '@presentation/components/identifier/identifier.component';
import { IDENTIFIER_CASES } from '../../../hsi-components/identifier-cases/identifier-cases.component';
import { AppFeature, AppointmentOrderDetailImageDto } from '@api-rest/api-model';
import { IDENTIFIER_IMAGE_CASES } from '../image-order-identifier-cases/image-order-coloredIconText-cases.component';
import { FeatureFlagService } from '@core/services/feature-flag.service';

@Component({
  selector: 'app-order-image-detail',
  templateUrl: './order-image-detail.component.html',
  styleUrls: ['./order-image-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class OrderImageDetailComponent implements OnInit {

  @Input() detailOrder: DetailOrderImage;

  detail:DetailOrderImage
  position = Position.ROW
  identifierCases= IDENTIFIER_CASES
  typeOrder: IDENTIFIER_IMAGE_CASES
  currentTypeOrder: IDENTIFIER_IMAGE_CASES
  selfDeterminationName = false

  constructor(
    private featureFlagService: FeatureFlagService,
  ) { }

  ngOnInit(): void {
    this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn =>{
			this.selfDeterminationName = isOn;
		})
    this.currentTypeOrder = this.detailOrder.idServiceRequest && this.detailOrder.studyName  ? IDENTIFIER_IMAGE_CASES.TRANSCRIBED_ORDER : IDENTIFIER_IMAGE_CASES.WITHOUT_ORDER
  }

}

export interface DetailOrderImage extends AppointmentOrderDetailImageDto {
  studyName: string,
  hasOrder: boolean,
  patient?: string
  studiesNames?: string,
}
