import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ProgramReportsRoutingModule } from './program-reports-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { PresentationModule } from '@presentation/presentation.module';


@NgModule({
  declarations: [
    // routing
    HomeComponent,
  ],
  imports: [
    CommonModule,
    // routing
    ProgramReportsRoutingModule,
    // deps
    PresentationModule,
  ]
})
export class ProgramReportsModule { }