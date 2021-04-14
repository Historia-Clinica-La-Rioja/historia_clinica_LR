import { MenuItem } from '@presentation/components/menu/menu.component';
import { Page } from '@presentation/components/page/page.component';

/* tslint:disable */

export const SYSTEM_MENU: MenuItem[] = [
	{
		label: { text: 'Manuales de usuario' },
		icon: 'support',
		id: 'manuales',
		url: `extension/manuales`,
	}
];

export const SYSTEM_MANUALES_PAGE: Page = {
	"type": "components",
	"content": [
		{ "type": "typography", "args": { "value": "Este contenido se muestra a modo de ejemplo del potencial de los Puntos de extensión", "className": "caption" } },
		{ "type": "typography", "args": { "value": "Manuales de ayuda para la configuración del sistema", "className": "title" } },
		{ "type": "typography", "args": { "value": "MÓDULO 1 – Backoffice", "className": "headline" } },
		{ "type": "typography", "args": { "value": "Creación y configuración de instituciones, así como la estructura de las mismas (sectores, especialidades).", "className": "body-1" } },
		{ "type": "link", "args": { "value": "http://hsi.pladema.net/wp-content/uploads/2021/03/Manual-Backoffice-HSI.pdf", label: { text: "DESCARGAR" } } }
	]
};

export const INSTITUTION_MENU: MenuItem[] = [
	{
		label: { text: 'Información' },
		icon: 'lightbulb',
		id: 'informacion',
		url: `extension/informacion`,
	}
];

export const INSTITUTION_MANUALES_PAGE: Page = {
	"type": "components",
	"content": [
		{ "type": "html", "args": { "value": '<h1 style="text-align: center;"><strong>Una plataforma para modernizar e integrar</strong></h1><h1 style="text-align: center;">el sistema de salud p&uacute;blico</h1><div style="text-align: center;"><img title="Rectangle 3" src="http://hsi.pladema.net/wp-content/uploads/2021/01/Rectangle-3.png" alt="" width="auto" height="auto" /></div><div style="text-align: center;"><img title="imagen portada" src="http://hsi.pladema.net/wp-content/uploads/2021/01/imagen-portada.png" alt="" width="auto" height="auto" /></div><h1 style="text-align: center;"><strong>&iquest;Qu&eacute; es HSI?</strong></h1><div style="text-align: center;"><img title="Rectangle 3" src="http://hsi.pladema.net/wp-content/uploads/2021/01/Rectangle-3.png" alt="" width="auto" height="auto" /></div><p style="text-align: center;">Esta aplicaci&oacute;n ha sido desarrollada en cooperaci&oacute;n con el Ministerio de Salud de la Rep&uacute;blica Argentina con el fin de&nbsp;<strong>digitalizar la historia cl&iacute;nica de los pacientes, administrar turnos, camas y otros recursos de los centros atenci&oacute;n de salud.</strong></p><p style="text-align: center;">El sistema est&aacute; integrado con la Red Nacional de Interoperabilidad en Salud que conecta diferentes Sistemas de Informaci&oacute;n en Salud en Argentina,&nbsp;<strong>permitiendo el intercambio de informaci&oacute;n cl&iacute;nica de manera precisa, segura, puntual y de alta calidad, con fines asistenciales, epidemiol&oacute;gicos, estad&iacute;sticos y de salud.</strong></p><div>&nbsp;</div>' } },
		{ "type": "link", "args": { "value": "https://hsi.pladema.net/", label: { text: "A M P L I A R" } } }
	]
};
