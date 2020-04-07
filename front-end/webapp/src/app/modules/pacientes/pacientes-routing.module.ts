import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './routes/home/home.component';
import { CoreModule } from "@core/core.module";

const routes: Routes = [{path: '', component: HomeComponent}];

@NgModule({
	imports: [
		CoreModule,
		RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class PacientesRoutingModule {
}
