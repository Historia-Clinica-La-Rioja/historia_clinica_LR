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
            allowedRoles: [ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ERole.PERSONAL_DE_ESTADISTICA, ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_MEDICO,
            ]
        }
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class RutasRoutingModule { }
