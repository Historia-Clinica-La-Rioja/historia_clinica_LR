import { Component, Input } from '@angular/core';
import { Detail } from '@presentation/components/details-section-custom/details-section-custom.component';
import { PresentationModule } from '@presentation/presentation.module';
import { InterveningProfessionalsStatusOfTheSignatureComponent } from '../intervening-professionals-status-of-the-signature/intervening-professionals-status-of-the-signature.component';
import { DocumentElectronicSignatureProfessionalStatusService } from '@api-rest/services/document-electronic-signature-professional-status.service';

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
  professionalsList = [];
    constructor(private documentServices: DocumentElectronicSignatureProfessionalStatusService) { 
      this.documentServices.getDocumentElectronicSignatureProfessionalStatusController(10514).subscribe(professionals=>{
        this.professionalsList = professionals;
      })
  }

}