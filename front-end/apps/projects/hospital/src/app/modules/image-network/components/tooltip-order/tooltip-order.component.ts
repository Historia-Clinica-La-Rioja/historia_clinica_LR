import { Component, Input, OnInit } from '@angular/core';
import { E_TYPE_ORDER } from '@historia-clinica/modules/ambulatoria/modules/estudio/model/ImageModel';

@Component({
  selector: 'app-tooltip-order',
  templateUrl: './tooltip-order.component.html',
  styleUrls: ['./tooltip-order.component.scss']
})
export class TooltipOrderComponent implements OnInit {
  @Input() typeOrder: E_TYPE_ORDER;

	readonly orderTypes = E_TYPE_ORDER;
  
  constructor() { }

  ngOnInit(): void {
  }

}
