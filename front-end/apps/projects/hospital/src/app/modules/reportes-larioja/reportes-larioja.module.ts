import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReportesLariojaComponent } from './reportes-larioja.component';
import { MatCardModule } from '@angular/material/card';
import { MatTabsModule } from '@angular/material/tabs';
import { ReportesLariojaRoutingModule } from './reportes-larioja-routing.module';
import { ReportesProgramasComponent } from './reportes-programas/reportes-programas.component';
import { ReportesGeneralesComponent } from './reportes-generales/reportes-generales.component';
import { PresentationModule } from '@presentation/presentation.module';
import { ExtensionsModule } from '@extensions/extensions.module';
import { ReportesEnfermeriaComponent } from './reportes-enfermeria/reportes-enfermeria.component';
import { ReportesOdontologiaComponent } from './reportes-odontologia/reportes-odontologia.component';

@NgModule({
  declarations: [
    ReportesLariojaComponent,
    ReportesProgramasComponent,
    ReportesGeneralesComponent,
    ReportesEnfermeriaComponent,
    ReportesOdontologiaComponent
  ],
  imports: [
    CommonModule,
    // routing
    ReportesLariojaRoutingModule,
    // deps
    PresentationModule,
    ExtensionsModule,
    MatCardModule,
    MatTabsModule,
  ]
})
export class ReportesLariojaModule { }
