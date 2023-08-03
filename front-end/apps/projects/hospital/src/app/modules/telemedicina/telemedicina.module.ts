import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './routes/home/home.component';
import { TelemedicinaRoutingModule } from './telemedicina-routing.module.';
import { PresentationModule } from '@presentation/presentation.module';
import { LazyMaterialModule } from '../lazy-material/lazy-material.module';
import { RequestsComponent } from './components/requests/requests.component';



@NgModule({
	declarations: [
		HomeComponent,
		RequestsComponent
	],
	imports: [
		CommonModule,
		TelemedicinaRoutingModule,
		LazyMaterialModule,
		PresentationModule,
	]
})
export class TelemedicinaModule { }
