export const MOCKS_TURNOS = [
	{
		path: 'solicitar',
		loads: [
			{
				name: 'BusquedaProfesional.search(nombre: string): ProfessionalDto[]',
				roles: 'Todo ADMINISTRATIVO sobre la institución',
				path: '/api/institucion/{institucionId}/profesional?nombre=...',
				method: 'GET',
				fetch: [
					{
						id: 66,
						nombre: 'Ricardo',
						apellido: 'Gutierrez',
						matricula: '19215/1',
						dni: '12345678',
					}
				],
			}
		],
		actions: [
			{
				name: 'Seleccionar profesional',
				navigate: './profesionalId',
			},
		],
	},
	{
		path: 'solicitar/profesionalId',
		loads: [
			{
				name: 'Profesional.getOne(profesionalId: number): ProfesionalDto',
				roles: 'Todo ADMINISTRATIVO sobre la institución',
				path: '/api/institucion/{institucionId}/profesional/{profesionalId}',
				method: 'GET',
				fetch: {
					id: 66,
					nombre: 'Ricardo',
					apellido: 'Gutierrez',
					cuil: '20351157988',
					email: 'martin.casco@gmail.com',
					telefono: '+5492494889965'
				},
			},
			{
				name: 'Diaries.getList(profesionalId: number): DiaryDto[]',
				roles: 'Todo ADMINISTRATIVO sobre la institución',
				path: '/api/institutions/{institutionId}/medicalConsultations/diary?professionalId=...',
				method: 'GET',
				fetch: [{
					id: 99,
					startDate: '2020-07-01',
					endDate: '2020-08-31',
					doctorsOfficeId: 5,
				}, {

					id: 100,
					startDate: '2020-07-01',
					endDate: '2020-08-31',
					doctorsOfficeId: 5,
				}],
			},
			{
				name: 'DiaryOpeningHours.getMany(diaryId: number[]): OpeningHoursDto[]',
				roles: 'Todo ADMINISTRATIVO sobre la institución',
				path: '/api/institutions/{institutionId}/medicalConsultations/diaryOpeningHours?diaryIds=99,100',
				method: 'GET',
				fetch: [
					{
						id: 66,
						dayWeekId: 1, // lunes
						from: '08:00',
						to: '12:00',
						maxOverTurn: 4,
						medicalAttentionTypeId: 0, // programada
					},
					{
						id: 67,
						dayWeekId: 1, // lunes
						from: '14:00',
						to: '18:00',
						maxOverTurn: 0,
						medicalAttentionTypeId: 1, // espontanea
					},
				],
			},
			{
				name: 'Appointments.getList(from: Date, to: Date): AppointmentListadoDto[]',
				roles: 'Todo ADMINISTRATIVO sobre la institución',
				path: '/api/institutions/{institutionId}/medicalConsultations/appointments?diaryIds=99,100',
				method: 'GET',
				fetch: [
					{
						id: 10,
						patient: { // basic patient dto
							id: 9,
							nombre: 'María',
							apellido: 'Gonzalez',
						},
						date: '2020-07-13',
						hour: '07:15',
						isOverTurn: false,
					},
					{
						id: 11,
						patient: { // basic patient dto
							id: 9,
							nombre: 'Jorge',
							apellido: 'Martines',
						},
						date: '2020-07-13',
						hour: '07:30',
						isOverTurn: false,
					}
				],
			}
		],
		actions: [
			{
				name: '[Modal] Nuevo turno en un horario',
				navigate: './nuevo-turno',
			},
		],
	},
	{
		path: 'solicitar/profesionalId/nuevo-turno',
		loads: [
			{
				name: 'MinimalSearchPatient.search(identificationTypeId: number, identificationNumber: string, genderId: number): PatientDto[]',
				roles: 'Todo ADMINISTRATIVO sobre la institución',
				path: 'ya existe: MinimalSearchPatientController',
				method: 'GET',
				fetch: [
					{
						id: 66,
					}
				],
			}
		],
		actions: [
			{
				name: '[Modal] Nuevo turno en un horario (step 2)',
				navigate: '../nuevo-turno2',
			},
		],
	},
	{
		path: 'solicitar/profesionalId/nuevo-turno2',
		loads: [
			{
				name: 'Patient.get(id: number): BasicPatientDto[]',
				roles: 'Todo ADMINISTRATIVO sobre la institución',
				path: '/api/patient/{id}',
				method: 'GET',
				fetch: { // basic patient dto
					id: 9,
					nombre: 'María',
					apellido: 'Gonzalez',
				},
			},
			{
				name: 'MedicalCoverage.list(): MedicalCoverageDto[]',
				roles: 'Todo ADMINISTRATIVO sobre la institución',
				path: '/api/medicalcoverage',
				method: 'GET',
				fetch: [
					{
						id: 66,
						nombre: 'Swiss Medical'
					},
					{
						id: 67,
						nombre: 'Osde'
					},
				],
			}
		],
	},
];


