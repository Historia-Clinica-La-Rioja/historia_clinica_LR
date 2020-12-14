
export const MOCKS_GUARDIA = [
	{
		path: 'mock',
		loads: [
			{
				name: 'EmergencyCareEpisode.getAll(): EmergencyCareDto[]',
				roles: 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD',
				path: '/api/institutions/{institutionId}/emergency-care/episodes',
				method: 'GET',
				fetch: [
					{
						id: 66,
						creationDate: {
							date: { day: 1, month: 1, year: 2020 },
							time: { hour: 10, minute: 30 }
						},
						patient: {
							id: 9,
							person: {
								firstName: 'Silvia',
								lastName: 'Martinez',
							},
							typeId: 1
						},
						triage: {
							id: 1,
							description: 'Nivel 1',
							color: '#fafafa',
						},
						type: {
							id: 1,
							description: 'Guardia adulto'
						},
						state: {
							id: 1,
							description: 'En atención'
						},
						doctorsOffice: {
							id: 1,
							description: 'Enfermeria',
						},
					},
					{
						id: 67,
						creationDate: {
							date: { day: 1, month: 3, year: 2020 },
							time: { hour: 15, minute: 10 }
						},
						patient: {
							id: 9,
							typeId: 3
						},
						triage: {
							id: 1,
							description: 'Nivel 1',
							color: '#fafafa',
						},
						type: {
							id: 1,
							description: 'Guardia adulto'
						},
						state: {
							id: 1,
							description: 'En atención'
						},
						doctorsOffice: {
							id: 1,
							description: 'Enfermeria',
						},
					},
					{
						id: 68,
						creationDate: {
							date: { day: 1, month: 3, year: 2020 },
							time: { hour: 12, minute: 29 }
						},
						triage: {
							id: 1,
							description: 'Nivel 1',
							color: '#fafafa',
						},
						type: {
							id: 1,
							description: 'Guardia adulto'
						},
						state: {
							id: 1,
							description: 'En atención'
						},
						doctorsOffice: {
							id: 1,
							description: 'Enfermeria',
						},
					}
				],
			}
		],
		actions: [
			{
				name: 'Nuevo episodio',
				navigate: './nuevo-episodio/administrativa',
			},
			{
				name: 'Ver episodio',
				navigate: './episodio/:id',
			},
			{
				name: '[Modal] Nuevo triage administrativo',
				navigate: './new-triage-administrativo',
			},
			{
				name: '[Modal] Nuevo triage medico adulto ginecologico',
				navigate: './new-triage-medico-adulto-ginecologico',
			},
			{
				name: '[Modal] Nuevo triage pediatrico',
				navigate: './new-triage-pediatrico',
			},
			{
				name: 'Atender',
				navigate: './attention',
			},
			{
				name: 'Finalizar por ausencia',
				navigate: './finalizar-ausencia',
			},
		],
	},
	{
		path: 'mock/nuevo-episodio/administrativa',
		loads: [
			{
				name: 'PatientMedicalCoverage.getActivePatientMedicalCoverages(patientId: number): PatientMedicalCoverageDto[]',
				roles: 'Cualquiera sobre guardia',
				path: '/api/institutions/{institutionId}/patientMedicalCoverage/{patientId}/coverages',
				method: 'GET',
				comments: 'Ya está hecho',
				fetch: [1, 2, 3, 4, 5]
			},
			{
				name: 'EmergencyCareMasterData.getType(): MasterDataInterface[]',
				roles: 'Cualquiera sobre guardia',
				path: '/api/emergency-care/masterdata/type',
				method: 'GET',
				fetch: [
					{
						id: 1,
						description: 'Adulto'
					},
					{
						id: 2,
						description: 'Pediátrico'
					},
					{
						id: 3,
						description: 'Ginecologico'
					}
				]
			},
			{
				name: 'EmergencyCareMasterData.getEntranceType(): MasterDataInterface[]',
				roles: 'Cualquiera sobre guardia',
				path: '/api/emergency-care/masterdata/entranceType',
				method: 'GET',
				fetch: [
					{
						id: 1,
						description: 'Caminando'
					},
					{
						id: 2,
						description: 'En silla de ruedas'
					},
					{
						id: 3,
						description: 'Ambulancia sin médico'
					},
					{
						id: 4,
						description: 'Ambulancia con médico'
					}
				]
			},
		],
		actions: [
			{
				name: 'Volver',
				navigate: '../',
			},
			{
				name: '[Modal] Buscar paciente',
				navigate: './persona-picker',
			},
			{
				name: '[Modal] Agregar cobertura',
				navigate: './modal-coberturas',
			},
			{
				name: 'Continuar',
				navigate: '../triage-administrativo',
			},
			{
				name: 'Continuar',
				navigate: '../triage-medico',
			},
			{
				name: 'Continuar',
				navigate: '../triage-medico-pediatrico',
			}
		],
	},
	{
		path: 'mock/nuevo-episodio/administrativa/modal-coberturas',
		loads: [{
			name: '',
			comments: 'Reutilizar el de turnos',
			path: ''
		}],
	},
	{
		path: 'mock/nuevo-episodio/administrativa/persona-picker',
		loads: [
			{
				name: 'PatientController.getPatientMinimal(identificationTypeId: number, identificationNumber: string, genderId: number): Integer[]',
				comments: 'Ya está hecho',
				roles: 'Cualquiera sobre guardia',
				path: '/api/patient/minimalsearch',
				method: 'GET',
			},
			{
				name: 'PatientController.getBasicPersonalData(patientId: number): ReducedPatientDto',
				comments: 'Ya está hecho evaluar la posibilidad de cambiarle el nombre. Tener en cuenta que a futuro se van a necesitar mas ' +
					'datos, por lo tanto quizas no es la mejor opcion cambiar el nombre y refactorizar. Pero puede quedar raro con este ' +
					'nombre si nunca se cambia',
				roles: 'Cualquiera sobre guardia',
				path: '/api/patient/{patientId}/appointment-patient-data',
				method: 'GET',
			},
		]
	},
	{
		path: 'mock/episodio/:id',
		loads: [
			{
				name: 'EmergencyCareAdministrative.get(): EmergencyCareAdministrativeDto',
				roles: 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD',
				path: '/api/institutions/{institutionId}/emergency-care/episodes/{episodeId}/administrative',
				method: 'GET',
				fetch: {
					id: 1,
					emergencyCareType: {
						id: 1,
						description: 'Guardia adultos'
					},
					creationDate: {
						date: { day: 1, month: 1, year: 2020 },
						time: { hour: 10, minute: 30 }
					},
					reasons: [
						{
							id: 1,
							description: 'motivo de consulta'
						}
					],
					entrance: {
						type: {
							id: 1,
							description: 'Caminando'
						},
						ambulanceCompanyId: '121212'
					},
					policeIntervention: {
						id: 1,
						callDate: {
							date: { day: 1, month: 1, year: 2020 },
							time: { hour: 9, minute: 30 }
						},
						plateNumber: 'ABC 111',
						firstName: 'Ricardo',
						lastName: 'Gutierrez'
					}
				},
			},
			{
				name: 'EmergencyCareEpisodeState.get(): EmergencyCareAdministrativeDto',
				roles: 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD',
				path: '/api/institutions/{institutionId}/emergency-care/episodes/{episodeId}/state',
				method: 'GET',
				fetch: {
					id: 2,
					description: 'En atención'
				}
			},
			{
				name: 'Triage.getAll(): TriageDto[]',
				roles: 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD',
				path: '/api/institutions/{institutionId}/emergency-care/episodes/{episodeId}/triage',
				method: 'GET',
				fetch: [
					{
						id: 1,
						category: {
							id: 1,
							name: 'Nivel I',
							description: 'Resucitación',
							color: {
								name: 'Rojo',
								code: '##FF0000'
							}
						},
						professional: {
							id: 1,
							firstName: 'Lucas',
							lastName: 'Bergottini'
						},
						doctorsOffice: {
							id: 1,
							description: 'Sala N'
						}
					},
					{
						id: 2,
						category: {
							id: 1,
							name: 'Nivel I',
							description: 'Resucitación',
							color: {
								name: 'Rojo',
								code: '##FF0000'
							}
						},
						professional: {
							id: 1,
							firstName: 'Julio',
							lastName: 'Rivera'
						},
						doctorsOffice: {
							id: 1,
							description: 'Sala N'
						},
						vitalSigns: {
							bloodOxygenSaturation: { value: 9, effectiveTime: '2020-11-21T12:00:12.000Z' },
							diastolicBloodPressure: { value: 12, effectiveTime: '2020-11-21T12:00:12.000Z' },
							heartRate: { value: 18, effectiveTime: '2020-11-21T12:00:12.000Z' },
							respiratoryRate: {value: 3, effectiveTime: '2020-11-21T12:00:12.000Z' },
							systolicBloodPressure: { value: 1, effectiveTime: '2020-11-21T12:00:12.000Z' },
							temperature: { value: 10, effectiveTime: '2020-11-21T12:00:12.000Z' }
						},
						notes: 'El paciente muestra signos de mejora'
					}
				],
			},
			{
				name: 'PersonController.getPersonalInformation(): PersonalInformationDto',
				roles: 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD',
				path: '/api/person/{personId}/personalInformation',
				method: 'GET',
				fetch: {
					id: 1,
					identificationNumber: '12321312',
					birthDate: { day: 1, month: 3, year: 2010 },
					email: 'person@example.com',
					cuil: '20391212412',
					phoneNumber: '249481211',
					identificationType: {
						id: 1,
						description: 'blabla'
					},
					address: {
						id: 1,
						street: 'Tierra del Fuego',
						number: '1154',
					}
				},
				comments: 'addressDto es mas largo pero se deja así en el mock por conveniencia'
			},
			{
				name: 'PatientMedicalCoverageController.getActivePatientMedicalCoverages(): PatientMedicalCoverageDto[]',
				roles: 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD',
				path: '/api/patientMedicalCoverage/{patientId}/coverages',
				method: 'GET',
				fetch: [{
					id: 1,
					vigencyDate: '2020-12-12',
					active: true,
					medicalCoverage: {
						id: 1,
						name: 'OSDE'
					}
				}],
			},
			{
				name: 'Patient.getPatientPhoto(): PersonPhotoDto[]',
				roles: 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD',
				path: '/api/patient/{patientId}/photo',
				method: 'GET',
				fetch: {
					imageData: 'some image data'
				},
			}

		],
		actions: [
			{
				name: '[Modal] Nuevo triage administrativo',
				navigate: '../../new-triage-administrativo',
			},
			{
				name: '[Modal] Nuevo triage medico adulto ginecologico',
				navigate: '../../new-triage-medico-adulto-ginecologico',
			},
			{
				name: '[Modal] Nuevo triage pediatrico',
				navigate: '../../new-triage-pediatrico',
			},
			{
				name: '[Modal] Buscar paciente',
				navigate: './persona-picker',
			},
			{
				name: 'Cancelar atención',
				navigate: './cancelar'
			},
			{
				name: 'Alta medica',
				navigate: './alta-medica',
			}
		],
	},
	{
		path: 'mock/nuevo-episodio/triage-administrativo',
		loads: [
			{
				name: 'TriageMasterData.getCategories(): TriageCategoryDto[]',
				roles: 'Todos los que tienen permisos sobre guardia',
				path: '/api/emergency-care/triage/masterdata/category',
				method: 'GET',
				fetch: [
					{
						id: 1,
						description: 'Nivel I',
						colour: {
							name: 'red',
							code: 'some'
						}
					},
					{
						id: 2,
						description: 'Nivel II',
						colour: {
							name: 'orange',
							code: 'some'
						}
					},
					{
						id: 3,
						description: 'Nivel III',
						colour: {
							name: 'yellow',
							code: 'some'
						}
					},
					{
						id: 4,
						description: 'Nivel IV',
						colour: {
							name: 'green',
							code: 'some'
						}
					},
					{
						id: 5,
						description: 'Nivel V',
						colour: {
							name: 'blue',
							code: 'some'
						}
					},

				],
			},
			{
				name: 'DoctorsOffice.getBySectorType(sectorTypeId: number): DoctorsOfficeDto[]',
				roles: 'Todos los que tienen permisos sobre guardia',
				path: '/api/institutions/{institutionId}/doctorsOffice/sectorType/:id',
				method: 'GET',
				fetch: [
					{
						id: 1,
						description: 'Consultorio 1',
						openingTime: '10:00',
						closingTime: '21:00'
					},
					{
						id: 2,
						description: 'Consultorio 2',
						openingTime: '10:00',
						closingTime: '21:00'
					},
				],
			},
			{
				name: 'EmergencyCareEpisode.new(): number',
				roles: 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD',
				path: '/api/institutions/{institutionId}/emergency-care/episodes',
				method: 'POST',
				body: {
					administrative: {
						patient: {
							id: 1,
							patientMedicalCoverageId: 1
						},
						reasons: [
							{
								id: 1,
								pt: 'some'
							},
							{
								id: 1,
								pt: 'some'
							}],
						typeId: 1,
						entranceTypeId: 1,
						policeIntervention: {
							dateCall: '20/10/2020',
							timeCall: '10:30',
							plateNumber: '91218',
							firstName: 'Ricardo',
							lastName: 'Gutierrez'
						}
					},
					triage: {
						categoryId: 1,
						doctorsOfficeId: 1
					}
				},
			}
		],
		actions: [
			{
				name: 'Confirmar episodio',
				navigate: '../../episodio/:id',
			},
		],
	},
	{
		path: 'mock/nuevo-episodio/triage-medico',
		comments: 'Este triage representa para el rol médico y tipo de guardia Ginécologico y Adulto',
		loads: [
			{
				name: 'TriageMasterData.getCategories(): TriageCategoryDto[]',
				roles: 'Todos los que tienen permisos sobre guardia',
				path: '/api/emergency-care/triage/masterdata/category',
				method: 'GET',
				fetch: [
					{
						id: 1,
						description: 'Nivel I',
						colour: {
							name: 'red',
							code: 'some'
						}
					},
					{
						id: 2,
						description: 'Nivel II',
						colour: {
							name: 'orange',
							code: 'some'
						}
					},
					{
						id: 3,
						description: 'Nivel III',
						colour: {
							name: 'yellow',
							code: 'some'
						}
					},
					{
						id: 4,
						description: 'Nivel IV',
						colour: {
							name: 'green',
							code: 'some'
						}
					},
					{
						id: 5,
						description: 'Nivel V',
						colour: {
							name: 'blue',
							code: 'some'
						}
					},

				],
			},
			{
				name: 'DoctorsOffice.getBySectorType(): DoctorsOfficeDto[]',
				comments: 'Ya existe',
				roles: 'Todos los que tienen permisos sobre guardia',
				path: '/api/institutions/{institutionId}/doctorsOffice/sectorType/:id',
				method: 'GET',
				fetch: [
					{
						id: 1,
						description: 'Consultorio 1',
						openingTime: '10:00',
						closingTime: '21:00'
					},
					{
						id: 2,
						description: 'Consultorio 2',
						openingTime: '10:00',
						closingTime: '21:00'
					},
				],
			},
			{
				name: 'EmergencyCareEpisode.new(): number',
				roles: 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD',
				path: '/api/institutions/{institutionId}/emergency-care/episodes/adult-gynecological',
				method: 'POST',
				body: {
					administrative: {
						patient: {
							id: 1,
							patientMedicalCoverageId: 1
						},
						reasons: [
							{
								id: 1,
								pt: 'some'
							},
							{
								id: 1,
								pt: 'some'
							}],
						typeId: 1,
						entranceTypeId: 1,
						policeIntervention: {
							dateCall: '20/10/2020',
							timeCall: '10:30',
							plateNumber: '91218',
							firstName: 'Ricardo',
							lastName: 'Gutierrez'
						}
					},
					triage: {
						categoryId: 1,
						doctorsOfficeId: 1,
						notes: 'triage',
						vitalSigns: {
							bloodOxygenSaturation: {
								effectiveTime: '2020-11-21T12:00:12.000Z',
								value: 10
							},
							diastolicBloodPressure: {
								effectiveTime: '2020-11-21T12:00:12.000Z',
								value: 10
							},
							heartRate: {
								effectiveTime: '2020-11-21T12:00:12.000Z',
								value: 10
							},
							respiratoryRate: {
								effectiveTime: '2020-11-21T12:00:12.000Z',
								value: 10
							},
							systolicBloodPressure: {
								effectiveTime: '2020-11-21T12:00:12.000Z',
								value: 10
							},
							temperature: {
								effectiveTime: '2020-11-21T12:00:12.000Z',
								value: 10
							}
						}
					}
				},
			}
		],
		actions: [
			{
				name: 'Confirmar episodio',
				navigate: '../../episodio/:id',
			},
		],
	},
	{
		path: 'mock/nuevo-episodio/triage-medico-pediatrico',
		loads: [
			{
				name: 'TriageMasterData.getCategories(): TriageCategoryDto[]',
				roles: 'Todos los que tienen permisos sobre guardia',
				path: '/api/emergency-care/triage/masterdata/category',
				method: 'GET',
				fetch: [
					{
						id: 1,
						description: 'Nivel I',
						colour: {
							name: 'red',
							code: 'some'
						}
					},
					{
						id: 2,
						description: 'Nivel II',
						colour: {
							name: 'orange',
							code: 'some'
						}
					},
					{
						id: 3,
						description: 'Nivel III',
						colour: {
							name: 'yellow',
							code: 'some'
						}
					},
					{
						id: 4,
						description: 'Nivel IV',
						colour: {
							name: 'green',
							code: 'some'
						}
					},
					{
						id: 5,
						description: 'Nivel V',
						colour: {
							name: 'blue',
							code: 'some'
						}
					},

				],
			},
			{
				name: 'DoctorsOffice.getBySectorType(): DoctorsOfficeDto[]',
				comments: 'Ya existe',
				roles: 'Todos los que tienen permisos sobre guardia',
				path: '/api/institutions/{institutionId}/doctorsOffice/sectorType/:id',
				method: 'GET',
				fetch: [
					{
						id: 1,
						description: 'Consultorio 1',
						openingTime: '10:00',
						closingTime: '21:00'
					},
					{
						id: 2,
						description: 'Consultorio 2',
						openingTime: '10:00',
						closingTime: '21:00'
					},
				],
			},
			{
				name: 'TriageMasterData.getBodyTemperature(): TriageDetailsMasterData[]', // todo analizar si es correcto el naming del dto
				roles: 'Todos los que tienen permisos sobre guardia',
				path: '/api/emergency-care/triage/masterdata/bodyTemperature',
				method: 'GET',
				fetch: [{
					id: 1,
					description: 'Fiebre',
					sctid_code: 1,
					},
					{
						id: 2,
						description: 'Hipotermia',
						sctid_code: 2,
					},
					{
						id: 3,
						description: 'Normal',
						sctid_code: 3,
					}],
				comments: 'verificar sctid_code',
			},
			{
				name: 'TriageMasterData.getMuscleHypertonia(): TriageDetailsMasterData[]',
				roles: 'Todos los que tienen permisos sobre guardia',
				path: '/api/emergency-care/triage/masterdata/muscleHypertonia',
				method: 'GET',
				fetch: [
					{
						id: 1,
						description: 'Hipertonía',
						sctid_code: 1,
					},
					{
						id: 2,
						description: 'Hipotonía',
						sctid_code: 2,
					},
					{
						id: 3,
						description: 'Normal',
						sctid_code: 3,
					}
				]
			},
			{
				name: 'TriageMasterData.getRespiratoryRetraction(): TriageDetailsMasterData[]',
				roles: 'Todos los que tienen permisos sobre guardia',
				path: '/api/emergency-care/triage/masterdata/respiratoryRetraction',
				method: 'GET',
				fetch: [
					{
						id: 1,
						description: 'Intercostal',
						sctid_code: 1,
					},
					{
						id: 2,
						description: 'Generalizado',
						sctid_code: 2,
					},
					{
						id: 3,
						description: 'No',
						sctid_code: 3,
					}
				]
			},
			{
				name: 'TriageMasterData.getPerfusion(): TriageDetailsMasterData[]',
				roles: 'Todos los que tienen permisos sobre guardia',
				path: '/api/emergency-care/triage/masterdata/perfusion',
				method: 'GET',
				fetch: [
					{
						id: 1,
						description: 'Normal',
						sctid_code: 1,
					},
					{
						id: 2,
						description: 'Alterada',
						sctid_code: 2,
					}
				]
			},
			{
				name: 'EmergencyCareEpisode.new(): number',
				roles: 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD',
				path: '/api/institutions/{institutionId}/emergency-care/episodes/pediatric',
				method: 'POST',
				body: {
					administrative: {
						patient: {
							id: 1,
							patientMedicalCoverageId: 1
						},
						reasons: [
							{
								id: 1,
								pt: 'some'
							},
							{
								id: 1,
								pt: 'some'
							}],
						typeId: 2,
						entranceTypeId: 1,
						policeIntervention: {
							dateCall: '20/10/2020',
							timeCall: '10:30',
							plateNumber: '91218',
							firstName: 'Ricardo',
							lastName: 'Gutierrez'
						}
					},
					triage: {
						categoryId: 1,
						doctorsOfficeId: 1,
						notes: 'triage',
						appearance: {
							bodyTemperatureId: 1,
							crying_excessive: true,
							muscleHypertoniaId: 1,
						},
						breathing: {
							respiratoryRetractionId: 1,
							stridor: true,
							respiratoryRate: {
								effectiveTime: '2020-11-21T12:00:12.000Z',
								value: 10
							},
							bloodOxygenSaturation: {
								effectiveTime: '2020-11-21T12:00:12.000Z',
								value: 10
							}
						},
						circulation: {
							perfusionId: 1,
							heartRate: {
								effectiveTime: '2020-11-21T12:00:12.000Z',
								value: 10
							}
						}
					}
				},
			}
		],
		actions: [
			{
				name: 'Confirmar episodio',
				navigate: '../../episodio/:id',
			},
		],
	},
	{
		path: 'mock/attention',
		loads: [
			{
				name: 'DoctorsOffice.getBySectorType(sectorTypeId: number): DoctorsOfficeDto[]',
				roles: 'Todos los que tienen permisos sobre guardia',
				path: '/api/institutions/{institutionId}/doctorsOffice/sectorType/:id',
				method: 'GET',
				fetch: [
					{
						id: 1,
						description: 'Consultorio 1',
						openingTime: '10:00',
						closingTime: '21:00'
					},
					{
						id: 2,
						description: 'Consultorio 2',
						openingTime: '10:00',
						closingTime: '21:00'
					},
				],
			},
			{
				name: 'EmergencyCareEpisodeState.changeState(emergencyCareEpisodeStateId: number, doctorsOfficeId: number): boolean',
				roles: 'Todos los que tienen permisos sobre guardia',
				path: '/api/institutions/{institutionId}/emergency-care/episodes/{episodeId}/state/change',
				method: 'POST',
				comments: 'Ambos campos son requeridos. Se pondra por defecto el ultimo consultorio en el que estuvo el paciente en el' +
					' select, en caso de tenerlo'
			},
		],
		actions: [
			{
				name: 'Confirmar',
				navigate: '../episodio/:id',
			}
		]
	},
	{
		path: 'mock/finalizar-ausencia',
		loads: [
			{
				name: 'EmergencyCareEpisodeState.changeState(emergencyCareEpisodeStateId: number): boolean',
				roles: 'Todos los que tienen permisos sobre guardia',
				path: '/api/institutions/{institutionId}/emergency-care/episodes/{episodeId}/state/change',
				method: 'POST'
			},
		],
	},
	{
		path: 'mock/new-triage-administrativo',
		loads: [
			{
				name: 'TriageMasterData.getCategories(): TriageCategoryDto[]',
				roles: 'Todos los que tienen permisos sobre guardia',
				path: '/api/emergency-care/triage/masterdata/category',
				method: 'GET',
				fetch: [
					{
						id: 1,
						description: 'Nivel I',
						colour: {
							name: 'red',
							code: 'some'
						}
					},
					{
						id: 2,
						description: 'Nivel II',
						colour: {
							name: 'orange',
							code: 'some'
						}
					},
					{
						id: 3,
						description: 'Nivel III',
						colour: {
							name: 'yellow',
							code: 'some'
						}
					},
					{
						id: 4,
						description: 'Nivel IV',
						colour: {
							name: 'green',
							code: 'some'
						}
					},
					{
						id: 5,
						description: 'Nivel V',
						colour: {
							name: 'blue',
							code: 'some'
						}
					},

				],
			},
			{
				name: 'DoctorsOffice.getBySectorType(sectorTypeId: number): DoctorsOfficeDto[]',
				roles: 'Todos los que tienen permisos sobre guardia',
				path: '/api/institutions/{institutionId}/doctorsOffice/sectorType/:id',
				method: 'GET',
				fetch: [
					{
						id: 1,
						description: 'Consultorio 1',
						openingTime: '10:00',
						closingTime: '21:00'
					},
					{
						id: 2,
						description: 'Consultorio 2',
						openingTime: '10:00',
						closingTime: '21:00'
					},
				],
			},
			{
				name: 'Triage.new(): number',
				roles: 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD',
				path: '/api/institutions/{institutionId}/emergency-care/episodes/{episodeId}/triage',
				method: 'POST',
				body: {
						categoryId: 1,
						doctorsOfficeId: 1
				}
			}
		],
	},
	{
		path: 'mock/new-triage-medico-adulto-ginecologico',
		loads: [
			{
				name: 'TriageMasterData.getCategories(): TriageCategoryDto[]',
				roles: 'Todos los que tienen permisos sobre guardia',
				path: '/api/emergency-care/triage/masterdata/category',
				method: 'GET',
				fetch: [
					{
						id: 1,
						description: 'Nivel I',
						colour: {
							name: 'red',
							code: 'some'
						}
					},
					{
						id: 2,
						description: 'Nivel II',
						colour: {
							name: 'orange',
							code: 'some'
						}
					},
					{
						id: 3,
						description: 'Nivel III',
						colour: {
							name: 'yellow',
							code: 'some'
						}
					},
					{
						id: 4,
						description: 'Nivel IV',
						colour: {
							name: 'green',
							code: 'some'
						}
					},
					{
						id: 5,
						description: 'Nivel V',
						colour: {
							name: 'blue',
							code: 'some'
						}
					},

				],
			},
			{
				name: 'DoctorsOffice.getBySectorType(sectorTypeId: number): DoctorsOfficeDto[]',
				roles: 'Todos los que tienen permisos sobre guardia',
				path: '/api/institutions/{institutionId}/doctorsOffice/sectorType/:id',
				method: 'GET',
				fetch: [
					{
						id: 1,
						description: 'Consultorio 1',
						openingTime: '10:00',
						closingTime: '21:00'
					},
					{
						id: 2,
						description: 'Consultorio 2',
						openingTime: '10:00',
						closingTime: '21:00'
					},
				],
			},
			{
				name: 'Triage.newAdultGynecological(): number',
				roles: 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD',
				path: '/api/institutions/{institutionId}/emergency-care/episodes/{episodeId}/triage/adult-gynecological',
				method: 'POST',
				body: {
						categoryId: 1,
						doctorsOfficeId: 1,
						notes: 'triage',
						vitalSigns: {
							bloodOxygenSaturation: {
								effectiveTime: '2020-11-21T12:00:12.000Z',
								value: 10
							},
							diastolicBloodPressure: {
								effectiveTime: '2020-11-21T12:00:12.000Z',
								value: 10
							},
							heartRate: {
								effectiveTime: '2020-11-21T12:00:12.000Z',
								value: 10
							},
							respiratoryRate: {
								effectiveTime: '2020-11-21T12:00:12.000Z',
								value: 10
							},
							systolicBloodPressure: {
								effectiveTime: '2020-11-21T12:00:12.000Z',
								value: 10
							},
							temperature: {
								effectiveTime: '2020-11-21T12:00:12.000Z',
								value: 10
							}
						}
				},
			}
		],
	},
	{
		path: 'mock/new-triage-medico-pediatrico',
		loads: [
			{
				name: 'TriageMasterData.getCategories(): TriageCategoryDto[]',
				roles: 'Todos los que tienen permisos sobre guardia',
				path: '/api/emergency-care/triage/masterdata/category',
				method: 'GET',
				fetch: [
					{
						id: 1,
						description: 'Nivel I',
						colour: {
							name: 'red',
							code: 'some'
						}
					},
					{
						id: 2,
						description: 'Nivel II',
						colour: {
							name: 'orange',
							code: 'some'
						}
					},
					{
						id: 3,
						description: 'Nivel III',
						colour: {
							name: 'yellow',
							code: 'some'
						}
					},
					{
						id: 4,
						description: 'Nivel IV',
						colour: {
							name: 'green',
							code: 'some'
						}
					},
					{
						id: 5,
						description: 'Nivel V',
						colour: {
							name: 'blue',
							code: 'some'
						}
					},

				],
			},
			{
				name: 'DoctorsOffice.getBySectorType(): DoctorsOfficeDto[]',
				comments: 'Ya existe',
				roles: 'Todos los que tienen permisos sobre guardia',
				path: '/api/institutions/{institutionId}/doctorsOffice/sectorType/:id',
				method: 'GET',
				fetch: [
					{
						id: 1,
						description: 'Consultorio 1',
						openingTime: '10:00',
						closingTime: '21:00'
					},
					{
						id: 2,
						description: 'Consultorio 2',
						openingTime: '10:00',
						closingTime: '21:00'
					},
				],
			},
			{
				name: 'TriageMasterData.getBodyTemperature(): TriageDetailsMasterData[]', // todo analizar si es correcto el naming del dto
				roles: 'Todos los que tienen permisos sobre guardia',
				path: '/api/emergency-care/triage/masterdata/bodyTemperature',
				method: 'GET',
				fetch: [{
					id: 1,
					description: 'Fiebre',
					sctid_code: 1,
				},
					{
						id: 2,
						description: 'Hipotermia',
						sctid_code: 2,
					},
					{
						id: 3,
						description: 'Normal',
						sctid_code: 3,
					}],
				comments: 'verificar sctid_code',
			},
			{
				name: 'TriageMasterData.getMuscleHypertonia(): TriageDetailsMasterData[]',
				roles: 'Todos los que tienen permisos sobre guardia',
				path: '/api/emergency-care/triage/masterdata/muscleHypertonia',
				method: 'GET',
				fetch: [
					{
						id: 1,
						description: 'Hipertonía',
						sctid_code: 1,
					},
					{
						id: 2,
						description: 'Hipotonía',
						sctid_code: 2,
					},
					{
						id: 3,
						description: 'Normal',
						sctid_code: 3,
					}
				]
			},
			{
				name: 'TriageMasterData.getRespiratoryRetraction(): TriageDetailsMasterData[]',
				roles: 'Todos los que tienen permisos sobre guardia',
				path: '/api/emergency-care/triage/masterdata/respiratoryRetraction',
				method: 'GET',
				fetch: [
					{
						id: 1,
						description: 'Intercostal',
						sctid_code: 1,
					},
					{
						id: 2,
						description: 'Generalizado',
						sctid_code: 2,
					},
					{
						id: 3,
						description: 'No',
						sctid_code: 3,
					}
				]
			},
			{
				name: 'TriageMasterData.getPerfusion(): TriageDetailsMasterData[]',
				roles: 'Todos los que tienen permisos sobre guardia',
				path: '/api/emergency-care/triage/masterdata/perfusion',
				method: 'GET',
				fetch: [
					{
						id: 1,
						description: 'Normal',
						sctid_code: 1,
					},
					{
						id: 2,
						description: 'Alterada',
						sctid_code: 2,
					}
				]
			},
			{
				name: 'Triage.newPediatric(): number',
				roles: 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD',
				path: '/api/institutions/{institutionId}/emergency-care/episodes/{episodeId}/triage/pediatric',
				method: 'POST',
				body:
					{
						categoryId: 1,
						doctorsOfficeId: 1,
						notes: 'triage',
						appearance: {
							bodyTemperatureId: 1,
							crying_excessive: true,
							muscleHypertoniaId: 1,
						},
						breathing: {
							respiratoryRetractionId: 1,
							stridor: true,
							respiratoryRate: {
								effectiveTime: '2020-11-21T12:00:12.000Z',
								value: 10
							},
							bloodOxygenSaturation: {
								effectiveTime: '2020-11-21T12:00:12.000Z',
								value: 10
							}
						},
						circulation: {
							perfusionId: 1,
							heartRate: {
								effectiveTime: '2020-11-21T12:00:12.000Z',
								value: 10
							}
						}
					},
			}
		],
	},
	{
		path: 'mock/episodio/:id/cancelar',
		loads: [
			{
				name: 'DoctorsOffice.getBySectorType(sectorTypeId: number): DoctorsOfficeDto[]',
				roles: 'Todos los que tienen permisos sobre guardia',
				path: '/api/institutions/{institutionId}/doctorsOffice/sectorType/:id',
				method: 'GET',
				fetch: [
					{
						id: 1,
						description: 'Consultorio 1',
						openingTime: '10:00',
						closingTime: '21:00'
					},
					{
						id: 2,
						description: 'Consultorio 2',
						openingTime: '10:00',
						closingTime: '21:00'
					},
				],
			},
			{
				name: 'EmergencyCareEpisodeState.changeState(emergencyCareEpisodeStateId: number, doctorsOfficeId: number): boolean',
				roles: 'Todos los que tienen permisos sobre guardia',
				path: '/api/institutions/{institutionId}/emergency-care/episodes/{episodeId}/state/change',
				method: 'POST',
				comments: 'Ambos campos son requeridos. Se pondra por defecto el ultimo consultorio en el que estuvo el paciente en el' +
					' select, en caso de tenerlo'
			},
		],
		actions: [
			{
				name: 'Confirmar',
				navigate: '../../../',
			}
		]
	},
	{
		path: 'mock/episodio/:id/alta-medica',
		loads: [
			{
				name: 'EmergencyCareEpisodeState.changeState(emergencyCareEpisodeStateId: number): boolean',
				roles: 'Todos los que tienen permisos sobre guardia',
				path: '/api/institutions/{institutionId}/emergency-care/episodes/{episodeId}/state/change',
				method: 'POST',
			},
		],
		actions: [
			{
				name: 'Confirmar',
				navigate: '../../../',
			}
		]
	},
	{
		path: 'mock/episodio/:id/persona-picker',
		loads: [
			{
				name: 'PatientController.getPatientMinimal(identificationTypeId: number, identificationNumber: string, genderId: number): Integer[]',
				comments: 'Ya está hecho',
				roles: 'Cualquiera sobre guardia',
				path: '/api/patient/minimalsearch',
				method: 'GET',
			},
			{
				name: 'PatientController.getBasicPersonalData(patientId: number): ReducedPatientDto',
				comments: 'Ya está hecho evaluar la posibilidad de cambiarle el nombre. Tener en cuenta que a futuro se van a necesitar mas ' +
					'datos, por lo tanto quizas no es la mejor opcion cambiar el nombre y refactorizar. Pero puede quedar raro con este ' +
					'nombre si nunca se cambia',
				roles: 'Cualquiera sobre guardia',
				path: '/api/patient/{patientId}/appointment-patient-data',
				method: 'GET',
			},
			{
				name: 'EmergencyCareAdministrative.setPatient(patientId: number): boolean',
				roles: 'Cualquiera sobre guardia',
				path: '/api/institutions/{institutionId}/emergency-care/episodes/{episodeId}/administrative/patient',
				method: 'PUT',
			}
		]
	}

];


