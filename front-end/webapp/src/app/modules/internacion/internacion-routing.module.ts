import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { InternacionHomeComponent } from './components/routes/home/internacion-home.component';


const routes: Routes = [
	{
		path: '',
		component: InternacionHomeComponent
	}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class InternacionRoutingModule { }
