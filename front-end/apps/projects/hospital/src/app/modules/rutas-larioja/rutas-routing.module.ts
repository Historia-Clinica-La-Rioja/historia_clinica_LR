import { RouterModule, Routes } from "@angular/router";
import { RutasComponent } from "./rutas.component";
import { NgModule } from "@angular/core";
import { RoleGuard } from "@core/guards/RoleGuard";
import { ERole } from "@api-rest/api-model";

const routes: Routes = [
    {
        path: '',
        children: [
            {path: '', component: RutasComponent}
        ],
        canActivate: [RoleGuard],
        data: {
            allowedRoles: [
            ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE,
			ERole.ENFERMERO,
			ERole.ADMINISTRADOR,
			ERole.ESPECIALISTA_MEDICO,
			ERole.ADMINISTRATIVO,
			ERole.ADMINISTRADOR_AGENDA,
			ERole.ESPECIALISTA_EN_ODONTOLOGIA,
			ERole.PROFESIONAL_DE_SALUD,
			ERole.ENFERMERO_ADULTO_MAYOR,
			ERole.PERSONAL_DE_IMAGENES,
			ERole.PERSONAL_DE_LABORATORIO,
			ERole.PERSONAL_DE_FARMACIA,
			ERole.PERFIL_EPIDEMIO_INSTITUCION,
			ERole.PERFIL_EPIDEMIO_MESO,
			ERole.ADMINISTRATIVO_RED_DE_IMAGENES,
			ERole.PRESCRIPTOR,
			ERole.ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR,
			ERole.TECNICO,
			ERole.VIRTUAL_CONSULTATION_PROFESSIONAL,
			ERole.VIRTUAL_CONSULTATION_RESPONSIBLE,
			ERole.ABORDAJE_VIOLENCIAS
            ]
        }
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class RutasRoutingModule { }
