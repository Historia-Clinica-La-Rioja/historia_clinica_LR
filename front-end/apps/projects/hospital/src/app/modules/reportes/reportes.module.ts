import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReportesRoutingModule } from './reportes-routing.module';
import {HomeComponent} from './routes/home/home.component';
import {CoreModule} from '@core/core.module';


@NgModule({
  declarations: [HomeComponent],
	imports: [
		CommonModule,
		ReportesRoutingModule,
		CoreModule
	]
})
export class ReportesModule { }
