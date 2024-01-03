import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { EAppointmentModality } from '@api-rest/api-model';

@Component({
  selector: 'app-modality-radio-button-form',
  templateUrl: './modality-radio-button-form.component.html',
  styleUrls: ['./modality-radio-button-form.component.scss']
})
export class ModalityRadioButtonFormComponent implements OnInit {
  @Input() modalitys: any[];
  @Input() layout: string;
  @Output() selectedModality = new EventEmitter<EAppointmentModality>();
  readonly MODALITY_ON_SITE_ATTENTION = EAppointmentModality.ON_SITE_ATTENTION;

  constructor() { }

  ngOnInit(): void {
  }

  emitModalitySelected(event) {
    this.selectedModality.emit(event.value);
  }

}
