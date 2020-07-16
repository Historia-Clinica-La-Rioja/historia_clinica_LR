import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from "../turnos/routes/home/home.component";
import { NewAgendaComponent } from "./routes/new-agenda/new-agenda.component";


const routes: Routes = [
	{
		path: '',
		children: [
			{
				path: '',
				component: HomeComponent
			},
			{
				path: 'new-agenda',
				component: NewAgendaComponent
			},
		]
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class TurnosRoutingModule {
}
