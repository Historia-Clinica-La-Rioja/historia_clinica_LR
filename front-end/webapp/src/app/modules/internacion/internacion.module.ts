import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { InternacionRoutingModule } from './internacion-routing.module';
import { AppMaterialModule } from 'src/app/app.material.module';
import { CoreModule } from '@core/core.module';
import { InternacionHomeComponent } from './components/routes/home/internacion-home.component';
import { InternacionesTableComponent } from './components/internaciones-table/internaciones-table.component';


@NgModule({
	declarations: [
		InternacionesTableComponent,
		InternacionHomeComponent
	],
	imports: [
		CommonModule,
		AppMaterialModule,
		CoreModule,
		InternacionRoutingModule,
	]
})
export class InternacionModule { }
