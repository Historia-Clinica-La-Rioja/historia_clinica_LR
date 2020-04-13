import { NgModule } from '@angular/core';

import { PacientesRoutingModule } from './pacientes-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { CoreModule } from "@core/core.module";
import { AppMaterialModule } from "../../app.material.module";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { PacientesTableComponent } from './component/pacientes-table/pacientes-table.component';
import { SearchCreateComponent } from './component/search-create/search-create.component';
import { FilterTableComponent } from './component/filter-table/filter-table.component';
import { PacientesLayoutComponent } from './pacientes-layout/pacientes-layout.component';


@NgModule({
	declarations: [
		FilterTableComponent,
		HomeComponent,
		PacientesLayoutComponent,
		PacientesTableComponent,
		SearchCreateComponent,
	],
	imports: [
		AppMaterialModule,
		CoreModule,
		FormsModule,
		PacientesRoutingModule,
		ReactiveFormsModule,
	]
})
export class PacientesModule {
}
