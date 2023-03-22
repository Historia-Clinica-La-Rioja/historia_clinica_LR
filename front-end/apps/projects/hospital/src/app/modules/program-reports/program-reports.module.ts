import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProgramReportsRoutingModule } from './program-reports-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { PresentationModule } from '@presentation/presentation.module';
import { OdontoComponent } from './odonto/odonto.component';
import { GeneralesComponent } from './generales/generales.component';
import { EnfermeriaComponent } from './enfermeria/enfermeria.component';


@NgModule({
  declarations: [
    // routing
    HomeComponent,
    OdontoComponent,
    GeneralesComponent,
    EnfermeriaComponent,
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