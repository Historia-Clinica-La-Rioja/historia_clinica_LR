import { NgModule } from '@angular/core';

import { PacientesRoutingModule } from './pacientes-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { CoreModule } from "@core/core.module";
import { AppMaterialModule } from "../../app.material.module";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { PacientesTableComponent } from './component/pacientes-table/pacientes-table.component';
import { SearchCreateComponent } from './component/search-create/search-create.component';


@NgModule({
	declarations: [
		HomeComponent,
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
