import { Component, OnInit, Input } from '@angular/core';
import { DateDto } from '@api-rest/api-model';

@Component({
  selector: 'app-indices',
  templateUrl: './indices.component.html',
  styleUrls: ['./indices.component.scss']
})
export class IndicesComponent implements OnInit {

  constructor() { /*this is intentional*/ }

  @Input() consultations: OndontologyConsultationIndicesDto[] = [];
  selectedConsultation: OndontologyConsultationIndicesDto;


  ngOnInit() {
    this.selectedConsultation = this.consultations[0];
  }

}

export interface OndontologyConsultationIndicesDto {
  date: DateDto;
  permanentC: number;
  permanentP: number;
  permanentO: number;
  cpoIndex: number;
  permanentTeethPresent: number;
  temporaryC: number;
  temporaryE: number;
  temporaryO: number;
  ceoIndex: number;
  temporaryTeethPresent: number;
}
