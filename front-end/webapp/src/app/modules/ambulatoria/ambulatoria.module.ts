import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './routes/home/home.component';
import { AmbulatoriaRoutingModule } from './ambulatoria-routing.module';



@NgModule({
  declarations: [HomeComponent],
  imports: [
	CommonModule,
	AmbulatoriaRoutingModule
  ]
})
export class AmbulatoriaModule { }
