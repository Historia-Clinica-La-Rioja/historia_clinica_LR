import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TurnosRoutingModule } from './turnos-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { CoreModule } from "@core/core.module";
import { NewAgendaComponent } from './routes/new-agenda/new-agenda.component';


@NgModule({
	declarations: [HomeComponent, NewAgendaComponent],
	imports: [
		CommonModule,
		CoreModule,
		TurnosRoutingModule
	]
})
export class TurnosModule {
}
