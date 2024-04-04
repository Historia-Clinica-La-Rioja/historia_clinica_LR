import { Component, OnInit } from '@angular/core';
import { EElectronicSignatureStatus } from '@api-rest/api-model';
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
export class InterveningProfessionalsStatusOfTheSignatureComponent implements OnInit {
  datas: any[] = [];
  constructor() { }

  ngOnInit(): void {
    this.datas = this.datas.map(data => this.getColoredIconText(data));
  }

  private getColoredIconText(data: any): SignatureData {
    let icon = "";
    let text = "";
    let color;
    switch (data.state) {
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
    return { name: data.name, signature: { icon: icon, text: text, color: color } }
  }
}

interface SignatureData {
  name: string,
  signature: ColoredIconText
}