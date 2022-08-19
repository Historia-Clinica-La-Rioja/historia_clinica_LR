import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DietDto, OtherIndicationDto, ParenteralPlanDto, PharmacoDto } from '@api-rest/api-model';import { DIET } from "@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications";
import { Status, Title } from "@presentation/components/indication/indication.component"

@Component({
  selector: 'app-internment-indication-detail',
  templateUrl: './internment-indication-detail.component.html',
  styleUrls: ['./internment-indication-detail.component.scss']
})
export class InternmentIndicationDetailComponent implements OnInit {

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: {
      indication: DietDto | PharmacoDto | OtherIndicationDto | ParenteralPlanDto, 
      header: Title,
      status: Status
    },
  ) { }

  ngOnInit(): void {
    console.log(this.data);
  }

}
