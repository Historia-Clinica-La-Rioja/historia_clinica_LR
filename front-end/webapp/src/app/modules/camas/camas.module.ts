import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CamasRoutingModule } from './camas-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { CoreModule } from '@core/core.module';

@NgModule({
	declarations: [
		HomeComponent,
	],
	imports: [
		CoreModule,
		FormsModule,
		CamasRoutingModule,
		ReactiveFormsModule,
	]
})
export class CamasModule {
}
