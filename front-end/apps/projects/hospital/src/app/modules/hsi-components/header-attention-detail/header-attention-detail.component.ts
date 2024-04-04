import { Component, Input } from '@angular/core';
import { Detail } from '@presentation/components/details-section-custom/details-section-custom.component';
import { PresentationModule } from '@presentation/presentation.module';
import { InterveningProfessionalsStatusOfTheSignatureComponent } from '../intervening-professionals-status-of-the-signature/intervening-professionals-status-of-the-signature.component';

@Component({
  selector: 'app-header-attention-detail',
  templateUrl: './header-attention-detail.component.html',
  styleUrls: ['./header-attention-detail.component.scss'],
  standalone: true,
  imports: [PresentationModule,InterveningProfessionalsStatusOfTheSignatureComponent]
})
export class HeaderAttentionDetailComponent {
  @Input() title: string;
  @Input() details: Detail[];

  constructor() { }

}