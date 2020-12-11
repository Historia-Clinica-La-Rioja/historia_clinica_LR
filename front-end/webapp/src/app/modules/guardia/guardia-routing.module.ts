import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { mockRouters } from "@presentation/utils/mock-routers.utils";
import { MOCKS_GUARDIA } from "./constants/mock-routers";
import { HomeComponent } from "./routes/home/home.component";


const routes: Routes = [{
	path: '',
	children: [
		{
			path: '',
			component: HomeComponent
		},
		...mockRouters(MOCKS_GUARDIA)
	]
}];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class GuardiaRoutingModule {
}
