import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTabsModule } from '@angular/material/tabs';
import { PresentationModule } from '@presentation/presentation.module';
import { ExtensionsModule } from '@extensions/extensions.module';
import { RutasRoutingModule } from './rutas-routing.module';
import { RutasComponent } from './rutas.component';

@NgModule({
  declarations: [
    RutasComponent
  ],
  imports: [
    CommonModule,
    // routing
    RutasRoutingModule,
    // deps
    PresentationModule,
    ExtensionsModule,
    MatCardModule,
    MatTabsModule,
  ]
})
export class RutasModule { }
