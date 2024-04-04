import { Component, Input } from '@angular/core';
import { DocumentElectronicSignatureProfessionalStatusDto, EElectronicSignatureStatus } from '@api-rest/api-model';
import { Color } from '@presentation/colored-label/colored-label.component';
import { ColoredIconText } from '@presentation/components/colored-icon-text/colored-icon-text.component';
import { PresentationModule } from '@presentation/presentation.module';

@Component({
  selector: 'app-intervening-professionals-status-of-the-signature',
  templateUrl: './intervening-professionals-status-of-the-signature.component.html',
  styleUrls: ['./intervening-professionals-status-of-the-signature.component.scss'],
  standalone: true,
  imports: [PresentationModule]
})
export class InterveningProfessionalsStatusOfTheSignatureComponent {
  @Input() set setInterveningProfessionals (interveningProfessionals: DocumentElectronicSignatureProfessionalStatusDto[]){
    if(interveningProfessionals){
      this.interveningProfessionals = interveningProfessionals.map(professional => this.getColoredIconText(professional));
    }
  };
  interveningProfessionals :ProfessionalSignatureData[];
  constructor() { }

  private getColoredIconText(professional: DocumentElectronicSignatureProfessionalStatusDto): ProfessionalSignatureData {
    let icon = "";
    let text = "";
    let color;
    switch (professional.status) {
      case EElectronicSignatureStatus.OUTDATED:
        text = 'firma-conjunta.STATE_SIGNATURE.OUTDATED';
        color = Color.GREY;
        icon = "cancel";
        break;
      case EElectronicSignatureStatus.PENDING:
        text = 'firma-conjunta.STATE_SIGNATURE.PENDING';
        color = Color.YELLOW;
        icon = "timer";
        break;
      case EElectronicSignatureStatus.REJECTED:
        text = 'firma-conjunta.STATE_SIGNATURE.REJECTED';
        color = Color.RED;
        icon = "cancel";
        break;
      case EElectronicSignatureStatus.SIGNED:
        text = 'firma-conjunta.STATE_SIGNATURE.SIGNED';
        color = Color.GREEN;
        icon = "check_circle";
        break;
    }
    return { professionalCompleteName: professional.professionalCompleteName, signature: { icon: icon, text: text, color: color } }
  }
}

interface ProfessionalSignatureData {
  professionalCompleteName: string,
  signature: ColoredIconText
}