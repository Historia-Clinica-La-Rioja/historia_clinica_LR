export const MOCKS_TURNOS = [
	{
		path: 'solicitar',
		loads: [
			{
				name: 'HealthcareProfessional.searchByName(nombre: string): ProfessionalDto[]', // dto ya existe
				roles: 'Todo ADMINISTRATIVO sobre la institución',
				path: '/api/institutions/{institutionId}/healthcareprofessional?nombre=...',
				method: 'GET',
				fetch: [
					{
						id: 66,
						licenceNumber: '19215/1',
						firstName: 'Ricardo',
						lastName: 'Gutierrez',
						identificationNumber: '12345678', 
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
				name: 'HealthcareProfessional.getOne(profesionalId: number): ProfessionalDto',
				roles: 'Todo ADMINISTRATIVO sobre la institución',
				path: '/api/institutions/{institutionId}/healthcareprofessional/{healthcareprofessionalId}',
				method: 'GET',
				fetch: {
					id: 66,
					licenceNumber: '19215/1',
					firstName: 'Ricardo',
					lastName: 'Gutierrez',
					identificationNumber: '12345678', 
				}				
			},
			{
				name: 'DiaryController.getDiaries(healthcareprofessionalId: number): DiaryListDto[]',
				roles: 'Todo ADMINISTRATIVO sobre la institución',
				path: '/api/institutions/{institutionId}/medicalConsultations/diary?healthcareprofessionalId=...',
				method: 'GET',
				fetch: [{
					id: 99,
					startDate: '2020-07-01',
					endDate: '2020-08-31',
					doctorsOfficeId: 5,
					appointmentDuration: 5,
					professionalAsignShift: true,
					includeHoliday: false
				}, {

					id: 100,
					startDate: '2020-07-01',
					endDate: '2020-08-31',
					doctorsOfficeId: 5,
					appointmentDuration: 8,
					professionalAsignShift: false,
					includeHoliday: true
				}],
			},
			{
				name: 'DiaryOpeningHours.getMany(diaryId: number[]): DiaryOpeningHoursDto[]',
				roles: 'Todo ADMINISTRATIVO sobre la institución',
				path: '/api/institutions/{institutionId}/medicalConsultations/diaryOpeningHours?diaryIds=99,100',
				method: 'GET',
				fetch: [
					{
						openingHours: {
							id: 66,
							dayWeekId: 1, // lunes
							from: '08:00',
							to: '12:00'
						},
						overturnCount: 4,
						medicalAttentionTypeId: 0, // programada
					},
					{
						openingHours: {
							id: 67,
							dayWeekId: 1, // lunes
							from: '14:00',
							to: '18:00'
						},
						overturnCount: 0,
						medicalAttentionTypeId: 1, // espontanea
					},
				],
			},
			{
				name: 'Appointments.getList(from: Date, to: Date): AppointmentListDto[]',
				roles: 'Todo ADMINISTRATIVO sobre la institución',
				path: '/api/institutions/{institutionId}/medicalConsultations/appointments?diaryIds=99,100',
				method: 'GET',
				fetch: [
					{
						id: 10,
						patient: { 
							id: 9,
							person: { //BasicPersonalDataDto
								firstName: 'María',
								lastName: 'Gonzalez',
								identificationNumber: '12345678', 
							}
						},
						date: '2020-07-13',
						hour: '07:15',
						isOverTurn: false,
					},
					{
						id: 11,
						patient: {  // nuevo dto
							id: 25,
							person: {//BasicPersonalDataDto
								firstName: 'Jorge',
								lastName: 'Martines',
								identificationNumber: '12345678', 
							}
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
				name: 'Patient.getPatientMinimal(identificationTypeId: number, identificationNumber: string, genderId: number): number[]',
				roles: 'Todo ADMINISTRATIVO sobre la institución',
				path: 'ya existe: net.pladema.patient.controller.PatientController#getPatientMinimal',
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
				name: 'Patient.getAppointmentPatientData(id: number): AppointmentPatientDto[]',
				roles: 'Todo ADMINISTRATIVO sobre la institución',
				path: '/api/patient/{id}/appointment-data',
				method: 'GET',
				fetch: { 
					id: 9,
					person: {//BasicPersonalDataDto
						firstName: 'María',
						lastName: 'Gonzalez',
						identificationNumber: '12345678'				
					},
					medicalCoverageName: 'OSDE',
					medicalCoverageAffiliateNumber: '3213211'
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
	{
		path: 'solicitar/profesionalId/nuevo-turno3',
		loads: [
			{
				name: 'Appointments.create(appointmentDto: AppointmentDto): number',
				roles: 'Todo ADMINISTRATIVO sobre la institución',
				path: '/api/institutions/{institutionId}/medicalConsultations/appointments',
				method: 'POST',
				fetch: { 
					patientId: 9,
					hour: '07:30',
					isOverturn: true, // aca no sabemos si esperar en dos endpoints o dejar uno  y que valide según sea o no sobreturno
					diaryId: 1,
					openingHoursId: 1,
				},
			},
		],
	},
];


