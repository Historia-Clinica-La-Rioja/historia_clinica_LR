import { NgModule } from '@angular/core';

import { PacientesRoutingModule } from './pacientes-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { CoreModule } from "@core/core.module";
import { PacientesLayoutComponent } from './pacientes-layout/pacientes-layout.component';


@NgModule({
	declarations: [HomeComponent, PacientesLayoutComponent],
	imports: [
		CoreModule,
		PacientesRoutingModule
	]
})
export class PacientesModule {
}
