import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RoleGuard } from '@core/guards/RoleGuard';
import { ERole } from '@api-rest/api-model';
import { HomeComponent } from './routes/home/home.component';
import { StudyDetailsComponent } from './routes/study-details/study-details.component';


const routes: Routes = [
	{
		path: '',
		children: [
			{
				path: '',
				component: HomeComponent,
				canActivate: [RoleGuard],
				data: { allowedRoles: [ERole.TECNICO, ERole.INFORMADOR, ERole.INDEXADOR] }
			},
			{
				path: 'detalle-estudio/:id',
				component: StudyDetailsComponent,
				canActivate: [RoleGuard],
				data: { allowedRoles: [ERole.INFORMADOR] }
			}
		]
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})

export class ImageNetworkRoutingModule { }
