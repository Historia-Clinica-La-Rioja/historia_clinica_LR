import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TurnosRoutingModule } from './turnos-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { CoreModule } from "@core/core.module";
import { NewAgendaComponent } from './routes/new-agenda/new-agenda.component';
import { NewAttentionComponent } from './dialogs/new-attention/new-attention.component';
import { PresentationModule } from '@presentation/presentation.module';
import { CalendarModule } from 'angular-calendar';


@NgModule({
	declarations: [HomeComponent,
		 NewAgendaComponent,
		 NewAttentionComponent],
	imports: [
		CommonModule,
		CoreModule,
		PresentationModule,
		TurnosRoutingModule,
		CalendarModule
	]
})
export class TurnosModule {
}
