import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AuditoriaRoutingModule } from './auditoria-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { PresentationModule } from '@presentation/presentation.module';
import { LazyMaterialModule } from '../lazy-material/lazy-material.module';
import { PatientsFusionComponent } from './routes/patients-fusion/patients-fusion.component';


@NgModule({
  declarations: [
    HomeComponent,
    PatientsFusionComponent
  ],
  imports: [
    CommonModule,
    AuditoriaRoutingModule,
	LazyMaterialModule,
	PresentationModule,
  ]
})
export class AuditoriaModule { }
