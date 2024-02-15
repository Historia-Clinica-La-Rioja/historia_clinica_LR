import { Component, Input } from '@angular/core';
import { EAppointmentModality } from '@api-rest/api-model';
import { Color, ColoredLabel } from '@presentation/colored-label/colored-label.component';
import { MODALITYS } from '@turnos/constants/appointment';

@Component({
  selector: 'app-modality-label',
  templateUrl: './modality-label.component.html',
  styleUrls: ['./modality-label.component.scss']
})
export class ModalityLabelComponent {
  @Input() set  modality (modality:string){

    switch (modality){
      case EAppointmentModality.ON_SITE_ATTENTION: {
        this.modalityColorLabel = onSiteAttentionColoredLabel;
        break;
      }
      case EAppointmentModality.SECOND_OPINION_VIRTUAL_ATTENTION: {
        this.modalityColorLabel = secondOpinionAttentionColoredLabel;
        break;
      }
      case EAppointmentModality.PATIENT_VIRTUAL_ATTENTION: {
        this.modalityColorLabel = virtualAttentionColoredLabel;
        break;
      }
    }
  };
  modalityColorLabel: ColoredLabel;
  constructor() { }
}

export const onSiteAttentionColoredLabel: ColoredLabel = {
  description: MODALITYS[EAppointmentModality.ON_SITE_ATTENTION],
  color: Color.BLUE,
  icon: 'person',
}

export const virtualAttentionColoredLabel: ColoredLabel = {
  description: MODALITYS[EAppointmentModality.PATIENT_VIRTUAL_ATTENTION],
  color: Color.PURPLE,
  icon: 'video_call',
}

export const secondOpinionAttentionColoredLabel: ColoredLabel = {
  description: MODALITYS[EAppointmentModality.SECOND_OPINION_VIRTUAL_ATTENTION],
  color: Color.YELLOW,
  icon: 'switch_video',
}
