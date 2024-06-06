import { Component, Input, OnInit } from '@angular/core';
import { FluidAdministrationData, FluidAdministrationService } from '../services/fluid-administration.service';

@Component({
  selector: 'app-fluid-administration-list',
  templateUrl: './fluid-administration-list.component.html',
  styleUrls: ['./fluid-administration-list.component.scss']
})
export class FluidAdministrationListComponent implements OnInit {

  @Input() service: FluidAdministrationService ;
  fluidAdministrationList: FluidAdministrationData[]

  constructor() { }

  ngOnInit(): void {
    this.service.getFluidAdministrationList().subscribe(
      fluidAdministrations => this.fluidAdministrationList = fluidAdministrations
    )
  }

}
