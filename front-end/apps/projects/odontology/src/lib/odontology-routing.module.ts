import { RouterModule, Routes } from '@angular/router';
import { OdontologyComponent } from './routes/odontology.component';
import { NgModule } from '@angular/core';

const routes: Routes = [{
	path: '',
	component: OdontologyComponent
}];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})

export class OdontologyRoutingModule {

}
