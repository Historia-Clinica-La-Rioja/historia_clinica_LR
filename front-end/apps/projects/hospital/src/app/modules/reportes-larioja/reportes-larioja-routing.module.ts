import { RouterModule, Routes } from "@angular/router";
import { ReportesLariojaComponent } from "./reportes-larioja.component";
import { NgModule } from "@angular/core";
import { RoleGuard } from "@core/guards/RoleGuard";
import { ERole } from "@api-rest/api-model";

const routes: Routes = [
    {
        path: '',
        children: [
            {path: '', component: ReportesLariojaComponent}
        ],
        canActivate: [RoleGuard],
        data: {
            allowedRoles: [ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ERole.PERSONAL_DE_ESTADISTICA]
        }
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ReportesLariojaRoutingModule { }