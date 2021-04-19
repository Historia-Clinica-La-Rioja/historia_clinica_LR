import { MenuItem } from '@presentation/components/menu/menu.component';
import { Page } from '@presentation/components/page/page.component';
import { Observable, of } from 'rxjs';

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

const PATIENT_INFO_PAGE: Page = {
	"type": "components",
	"content": [
		{ "type": "typography", "args": { "value": "Resumen de historia clínica", "className": "title" } },
		{ "type": "typography", "args": { "value": "Información contenida en sistema legado", "className": "caption" } },
		{ "type": "typography", "args": { "value": "Resumen de historia clínica", "className": "title" } },
		{
			"type": "html",
			"args": {
				"value": "<div class=\"et_pb_section et_pb_section_2 et_section_regular\">\n\t\t\t\t\n\t\t\t\t\n\t\t\t\t\n\t\t\t\t\n\t\t\t\t\t<div class=\"et_pb_row et_pb_row_2\">\n\t\t\t\t<div class=\"et_pb_column et_pb_column_4_4 et_pb_column_2  et_pb_css_mix_blend_mode_passthrough et-last-child\">\n\t\t\t\t\n\t\t\t\t\n\t\t\t\t<div class=\"et_pb_module et_pb_image et_pb_image_1\">\n\t\t\t\t\n\t\t\t\t\n\t\t\t\t<span class=\"et_pb_image_wrap \"><img loading=\"lazy\" src=\"http://hsi.pladema.net/wp-content/uploads/2021/01/Frame-1.png\" alt=\"\" title=\"Frame (1)\" height=\"auto\" width=\"auto\" class=\"wp-image-120\"></span>\n\t\t\t</div><div class=\"et_pb_module et_pb_text et_pb_text_2  et_pb_text_align_center et_pb_bg_layout_light\">\n\t\t\t\t\n\t\t\t\t\n\t\t\t\t<div class=\"et_pb_text_inner\"><h1><strong>Desarrollo</strong></h1></div>\n\t\t\t</div> <!-- .et_pb_text --><div class=\"et_pb_module et_pb_image et_pb_image_2\">\n\t\t\t\t\n\t\t\t\t\n\t\t\t\t<span class=\"et_pb_image_wrap \"><img loading=\"lazy\" src=\"http://hsi.pladema.net/wp-content/uploads/2021/01/Rectangle-3.png\" alt=\"\" title=\"Rectangle 3\" height=\"auto\" width=\"auto\" class=\"wp-image-42\"></span>\n\t\t\t</div><div class=\"et_pb_module et_pb_text et_pb_text_3  et_pb_text_align_center et_pb_text_align_left-phone et_pb_bg_layout_light\">\n\t\t\t\t\n\t\t\t\t\n\t\t\t\t<div class=\"et_pb_text_inner\"><p><b data-stringify-type=\"bold\">HSI nace como un proyecto desarrollado por el grupo Lamansys del Instituto PLADEMA de la Fac. de Ciencias Exactas de la UNCPBA y cuenta con la cooperación del Ministerio de Salud de la República Argentina.</b></p>\n<p><span>A través del financiamiento del Ministerio de Desarrollo Productivo de la Nación, el Banco Interamericano de Desarrollo y la Agencia Nacional de Promoción de la Investigación, el Desarrollo Tecnológico y la Innovación, se desarrolló un sistema integral de gestión destinado a establecimientos de salud. </span></p>\n<p><span>La plataforma está diseñada para trabajar de manera integral con la Red Nacional de Salud Digital del Ministerio de Salud de la Nación. </span></p>\n<p>&nbsp;</p></div>\n\t\t\t</div> <!-- .et_pb_text -->\n\t\t\t</div> <!-- .et_pb_column -->\n\t\t\t\t\n\t\t\t\t\n\t\t\t</div> <!-- .et_pb_row -->\n\t\t\t\t\n\t\t\t\t\n\t\t\t</div>"
			}
		}
	]
};

export const PATIENT_TABS: { head: MenuItem, body$: Observable<Page> }[] = [
	{
		head: {
			label: { text: 'Información externa' },
			icon: 'content_paste',
			id: 'legacy',
			url: `extension/legacy`,
		},
		body$: of(PATIENT_INFO_PAGE),
	}
];
