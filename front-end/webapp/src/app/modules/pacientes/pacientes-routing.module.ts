import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HomeComponent } from './routes/home/home.component';
import { CoreModule } from '@core/core.module';
import { SearchComponent } from './routes/search/search.component';

const routes: Routes = [
	{
		path: '',
		component: HomeComponent
	},
	{
		path: 'search',
		component: SearchComponent
	}
];

@NgModule({
	imports: [
		CoreModule,
		ReactiveFormsModule,
		FormsModule,
		RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class PacientesRoutingModule {
}
