import { Component, Input, OnInit } from '@angular/core';
import { EAppointmentModality } from '@api-rest/api-model';
import { Color, ColoredLabel } from '@presentation/colored-label/colored-label.component';
import { MODALITYS } from '@turnos/constants/appointment';

@Component({
  selector: 'app-modality-label',
  templateUrl: './modality-label.component.html',
  styleUrls: ['./modality-label.component.scss']
})
export class ModalityLabelComponent implements OnInit {
  @Input() modalityColorLabel: ColoredLabel;

  constructor() { }

  ngOnInit(): void {
  }
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
