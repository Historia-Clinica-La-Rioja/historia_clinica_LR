export const MOCKS_ORDERS = [
	{
		path: 'paciente/:idPaciente/ordenes',
		loads: [
			{
				name: 'MedicationRequest.medicationRequestList(patientId: number, statusId: string, medicationStatement: string, healthCondition: string): MedicationInfoDto []',
				roles: 'Todo ESPECIALISTA_MEDICO sobre la institución',
				comments: 'medicationRequestId puede ser null porque no siempre el medicamento esta asociado a una receta',
				path: '/api/institutions/{institutionId}/patient/{patientId}/medication-requests?status=active, medicationStatment=null, healthCondition=null',
				method: 'GET',
				fetch: [
					{
						snomed: {
							id: '11111',
							pt: 'IBUPROFENO comprimido 600mg',
						},
						statusId: '44444',
						healthCondition: {
							id: 6,
							snomed: {
								id: '2222',
								pt: 'ANGINAS'
							}
						},
						dosage: {
							frecuency: 4,
							periodUnit: 'horas',
							duration: 4,
							durationUnit: 'días',
							expired: false,
							chronic: false
						},
						startDate: {
							year: 2020,
							month: 5,
							day: 14
						},
						observations: 'Tomarlo durante las mañanas en ayuno',
						medicationRequestId: 1
					},
					{
						snomed: {
							id: '333',
							pt: 'TAFIROL comprimido 1g',
						},
						statusId: '44444',
						healthCondition: {
							id: 8,
							snomed: {
								id: '2222',
								pt: 'PAPERA'
							}
						},
						dosage: {
							frecuency: 1,
							periodUnit: 'horas',
							expired: false,
							chronic: true
						},
						startDate: {
							year: 2020,
							month: 5,
							day: 13
						},
						observations: 'Tomar antes de las comidas',
						medicationRequestId: 1
					},
					{
						snomed: {
							id: '123',
							pt: 'PARACETAMOL comprimido 1g',
						},
						statusId: '44444',
						healthCondition: {
							id: 8,
							snomed: {
								id: '2222',
								pt: 'PAPERA',
							}
						},
						startDate: {
							year: 2020,
							month: 5,
							day: 18
						},
						medicationRequestId: null
					},
					{
						snomed: {
							id: '777',
							pt: 'BAYASPIRINA comprimido 1g',
						},
						statusId: '44444',
						healthCondition: {
							id: 8,
							snomed: {
								id: '2222',
								pt: 'PAPERA'
							}
						},
						dosage: {
							duration: '8',
							durationUnit: 'días',
							expired: false,
							chronic: false
						},
						startDate: {
							year: 2020,
							month: 11,
							day: 17
						},
						medicationRequestId: 2
					}
				]
			},
			{
				name: 'ServiceRequest.getList(patientId: number): DiagnosticReportInfoDto[]',
				roles: 'Todo ESPECIALISTA_MEDICO sobre la institución',
				path: '/api/institutions/{institutionId}/patient/{patientId}/serviceRequests?statusId=null, diagnosticReport=null, healthCondition=null',
				method: 'GET',
				fetch: [
					{
						snomed: {
							sctid: '11111',
							pt: 'Radiografía de torax',
						},
						statusId: '44444',
						healthCondition: {
							sctid: '2223',
							pt: 'ANGINAS'
						},
						observations: 'Hacerlo en ayuno',
						doctor: {
							"id": 1,
							"firstName": "San",
							"lastName": "Martin"
						  }
					},
					{
						snomed: {
							sctid: '11112',
							pt: 'Radiografía de rodilla',
						},
						statusId: '44444',
						healthConditionSnomed: {
							sctid: '2222',
							pt: 'Dolor'
						},
						observations: 'Bien de costado (?',
						doctor: {
							"id": 1,
							"firstName": "San",
							"lastName": "Martin"
						  }
					}
				],
			},
			{
				name: 'MedicalRequests.medicalRequestsList(patientId: number): MedicalRequestsDto[]',
				roles: 'Todo ESPECIALISTA_MEDICO sobre la institución',
				comments: 'Falta definición de como cargar las indicaciones',
				path: '/api/institutions/{institutionId}/patient/{patientId}/medical-requests',
				method: 'GET',
				fetch: [
					{
						observations: 'Comer verduras',
						statusId: '44444',
						healthConditionSnomed: {
							id: '2222',
							pt: 'ANGINAS'
						},
						startDate: {
							year: 2020,
							month: 5,
							day: 14
						}
					}
				],
			}
		],
		actions: [
			{
				name: 'Nueva receta',
				navigate: './nueva-receta',
			},
			{
				name: 'Volver a recetar',
				navigate: './volver-recetar',
			},
			{
				name: 'Suspender',
				navigate: './suspender-receta',
			},
			{
				name: 'Reactivar',
				navigate: './reactivar-receta',
			},
			{
				name: 'Finalizar',
				navigate: './finalizar-receta',
			},
			{
				name: 'Descargar receta',
				navigate: './descargar-receta',
			},
			{
				name: 'Nueva orden',
				navigate: './nueva-orden',
			},
			{
				name: 'Completar orden',
				navigate: './completar-orden',
			},
			{
				name: 'Eliminar orden',
				navigate: './eliminar-orden',
			},
			{
				name: 'Ver resultados orden',
				navigate: './ver-orden',
			},
			{
				name: 'Nueva indicación',
				navigate: './nueva-indicacion',
			},
			{
				name: 'Finalizar indicación',
				navigate: './finalizar-indicacion',
			}
		]
	},
	{
		path: 'paciente/:idPaciente/ordenes/nueva-receta',
		loads: [
			{
				name: 'PatientMedicalCoverageController.getActivePatientMedicalCoverages(patientId: number): PatientMedicalCoverageDto []',
				roles: 'Cualquiera',
				path: '/api/patientMedicalCoverage/{patientId}/coverages',
				method: 'GET',
				comments: 'Ya esta hecho devuelve una lista de PatientMedicalCoverageDto. Esto es necesario solamente para las nuevas medicaciones con receta. Las sin receta no necesitan cobertura medica'
			}
		],
		actions: [
			{
				name: 'Agregar medicación',
				navigate: './agregar-medicacion',
			},
			{
				name: 'Editar medicación',
				navigate: './editar-medicacion',
			},
			{
				name: 'Eliminar medicación'
			},
			{
				name: 'Confirmar',
				navigate: './confirmar-receta',
			}
		]
	}, {
		path: 'paciente/:idPaciente/ordenes/volver-recetar',
		loads: [
			{
				comments: 'Es igual al flujo de nueva receta excepto que ya tiene un listado de medicamentos predefinidos'
			}
		]
	}, {
		path: 'paciente/:idPaciente/ordenes/descargar-receta',
		loads: [
			{
				name: 'MedicationRequest.downloadMedicationRequest(patientId: number, medicationRequestId: number): File',
				roles: 'Todo ESPECIALISTA_MEDICO sobre la institución',
				comments: 'Descarga el archivo pdf de la receta. Es el mismo endpoint que descarga la receta cuando se agrega una nueva',
				path: '/api/institutions/{institutionId}/patient/{patientId}/medicamentRequests/{medicationRequestId}/downloadFile',
				method: 'GET',
			}
		],
		actions: [
			{
				name: 'Volver',
				navigate: '../',
			}
		]
	},
	{
		path: 'paciente/:idPaciente/ordenes/nueva-receta/agregar-medicacion',
		loads: [
			{
				name: 'HCEGeneralStateController.getActiveProblems(patientId: number): HCEPersonalHistoryDto []',
				roles: 'Todo ESPECIALISTA_MEDICO, ENFERMERO_ADULTO_MAYOR, ENFERMERO, PROFESIONAL_DE_SALUD',
				path: '/institutions/{institutionId}/patient/{patientId}/hce/general-state',
				comments: 'Ya esta hecho',
				method: 'GET',
				fetch: [
					{
						id: 4,
						startDate: '2020-07-13',
						inactivationDate: '2020-07-13',
						statusId: '12312',
						snomed: {
							id: '2222',
							pt: 'ANGINAS'
						},
					},
					{
						id: 8,
						startDate: '2020-07-13',
						inactivationDate: '2020-07-13',
						statusId: '12312',
						snomed: {
							id: '2222',
							pt: 'PAPERA'
						},
					},
				]
			},
			{
				name: 'SnowstormController.getConcepts(term: String, ecl: String): HCEPersonalHistoryDto []',
				roles: 'Cualquiera',
				path: '/snowstorm/concepts?term=IBUPROFENO&ecl=123123',
				method: 'GET',
				comments: 'Ya esta hecho y devuelve la clase SnowstormSearchResponse'
			}
		],
		actions: [
			{
				name: 'Agregar',
				navigate: '../',
			}
		]
	},
	{
		path: 'paciente/:idPaciente/ordenes/nueva-receta/editar-medicacion',
		loads: [
			{
				name: 'HCEGeneralStateController.getActiveProblems(patientId: number): HCEPersonalHistoryDto []',
				roles: 'Todo ESPECIALISTA_MEDICO, ENFERMERO_ADULTO_MAYOR, ENFERMERO, PROFESIONAL_DE_SALUD',
				path: '/institutions/{institutionId}/patient/{patientId}/hce/general-state',
				comments: 'Ya esta hecho',
				method: 'GET',
				fetch: [
					{
						id: 4,
						startDate: '2020-07-13',
						inactivationDate: '2020-07-13',
						statusId: '12312',
						healthCondition: {
							id: 6,
							snomed: {
								id: '2222',
								pt: 'ANGINAS'
							}
						},
					},
					{
						id: 8,
						startDate: '2020-07-13',
						inactivationDate: '2020-07-13',
						statusId: '12312',
						healthCondition: {
							id: 6,
							snomed: {
								id: '2222',
								pt: 'ANGINAS'
							}
						},
					},
				]
			},
			{
				name: 'SnowstormController.getConcepts(term: String, ecl: String): HCEPersonalHistoryDto []',
				roles: 'Cualquiera',
				path: '/snowstorm/concepts?term=IBUPROFENO&ecl=123123',
				method: 'GET',
				comments: 'Ya esta hecho y devuelve la clase SnowstormSearchResponse'
			}
		],
		actions: [
			{
				name: 'Agregar',
				navigate: '../',
			}
		]
	},
	{
		path: 'paciente/:idPaciente/ordenes/nueva-receta/confirmar-receta',
		loads: [
			{
				name: 'MedicationRequest.create(patientId: number): PrescriptionDto',
				roles: 'Todo ESPECIALISTA_MEDICO, PROFESIONAL DE LA SALUD sobre la institución',
				comments: 'Los siguientes campos pueden ser nulos: observaciones, tiempo de administración, intervalo de administración',
				path: '/api/institutions/{institutionId}/patient/{patientId}/medication-requests',
				method: 'POST',
				body: {
					hasRecipe: true,
					medicalCoverageId: 1,
					medications: [
						{
							snomed: {
								id: '11111',
								pt: 'IBUPROFENO comprimido 600mg',
							},
							healthConditionId: 6,
							dosage: {
								diary: false,
								frecuency: 4,
								chronic: false,
								duration: 4,
							},
							observations: 'Tomarlo durante las mañanas en ayuno'
						}
					]
				},
				fetch: 12, // id del medication request creado
			}
		],
		actions: [
			{
				name: 'Descargar receta',
				navigate: './descargar-receta',
			}
		]
	}, {
		path: 'paciente/:idPaciente/ordenes/nueva-receta/confirmar-receta/descargar-receta',
		loads: [
			{
				name: 'MedicationRequest.downloadMedicationRequest(patientId: number, medicationRequestId: number): File',
				roles: 'Todo ESPECIALISTA_MEDICO sobre la institución',
				comments: 'Descarga el archivo pdf de la receta',
				path: '/api/institutions/{institutionId}/patient/{patientId}/medication-requests/{medicationRequestId}/download',
				method: 'GET',
			}
		],
		actions: [
			{
				name: 'Volver',
				navigate: '../../../',
			}
		]
	},
	{
		path: 'paciente/:idPaciente/ordenes/suspender-receta',
		loads: [
			{
				name: 'MedicationRequest.changeState(patientId: number): ChangeStateMedicationRequestDto',
				roles: 'Todo ESPECIALISTA_MEDICO, PROFESIONAL DE LA SALUD sobre la institución',
				comments: 'No se puede suspender un medicamento que esta finalizado',
				path: '/api/institutions/{institutionId}/patient/{patientId}/medication-requests/change-state',
				method: 'PUT',
				body: {
					dayQuantity: 14,
					medicationIds: [1, 2, 3, 4, 5]
				}
			}
		],
		actions: [
			{
				name: 'Volver',
				navigate: '../',
			}
		]
	},
	{
		path: 'paciente/:idPaciente/ordenes/reactivar-receta',
		loads: [
			{
				name: 'MedicationRequest.changeState(patientId: number): ChangeStateMedicationRequestDto',
				roles: 'Todo ESPECIALISTA_MEDICO, PROFESIONAL DE LA SALUD sobre la institución',
				comments: 'No se puede reactivar un medicamento que esta finalizado ni activo',
				path: '/api/institutions/{institutionId}/patient/{patientId}/medication-requests/change-state',
				method: 'PUT',
				body: {
					medicationIds: [1, 2, 3, 4, 5]
				}
			}
		],
		actions: [
			{
				name: 'Volver',
				navigate: '../',
			}
		]
	},
	{
		path: 'paciente/:idPaciente/ordenes/finalizar-receta',
		loads: [
			{
				name: 'MedicationRequest.changeState(patientId: number): ChangeStateMedicationRequestDto',
				roles: 'Todo ESPECIALISTA_MEDICO, PROFESIONAL DE LA SALUD sobre la institución',
				path: '/api/institutions/{institutionId}/patient/{patientId}/medication-requests/change-state',
				method: 'PUT',
				body: {
					medicationIds: [1, 2, 3, 4, 5]
				}
			}
		],
		actions: [
			{
				name: 'Volver',
				navigate: '../',
			}
		]
	},
	{
		path: 'paciente/:idPaciente/ordenes/nueva-orden',
		loads: [
			{
				name: 'PatientMedicalCoverageController.getActivePatientMedicalCoverages(patientId: number): PatientMedicalCoverageDto []',
				roles: 'Cualquiera',
				path: '/api/patientMedicalCoverage/{patientId}/coverages',
				method: 'GET',
				comments: 'Ya esta hecho devuelve una lista de PatientMedicalCoverageDto. Esto es necesario solamente para las nuevas medicaciones con receta. Las sin receta no necesitan cobertura medica'
			}
		],
		actions: [
			{
				name: 'Agregar estudio',
				navigate: './agregar-estudio',
			},
			{
				name: 'Editar estudio',
				navigate: './editar-estudio',
			},
			{
				name: 'Eliminar estudio'
			},
			{
				name: 'Confirmar',
				navigate: './confirmar-estudio',
			}
		]
	}, {
		path: 'paciente/:idPaciente/ordenes/nueva-orden/agregar-estudio',
		loads: [
			{
				name: 'HCEGeneralStateController.getActiveProblems(patientId: number): HCEPersonalHistoryDto []',
				roles: 'Todo ESPECIALISTA_MEDICO, ENFERMERO_ADULTO_MAYOR, ENFERMERO, PROFESIONAL_DE_SALUD',
				path: '/institutions/{institutionId}/patient/{patientId}/hce/general-state',
				comments: 'Ya esta hecho',
				method: 'GET',
				fetch: [
					{
						id: 4,
						startDate: '2020-07-13',
						inactivationDate: '2020-07-13',
						statusId: '12312',
						snomed: {
							id: '2222',
							pt: 'ANGINAS'
						},
					},
					{
						id: 8,
						startDate: '2020-07-13',
						inactivationDate: '2020-07-13',
						statusId: '12312',
						snomed: {
							id: '2222',
							pt: 'PAPERA'
						},
					},
				]
			},
			{
				name: 'SnowstormController.getConcepts(term: String, ecl: String): HCEPersonalHistoryDto []',
				roles: 'Cualquiera',
				path: '/snowstorm/concepts?term=RADIOLOGIA&ecl=123123',
				method: 'GET',
				comments: 'Ya esta hecho y devuelve la clase SnowstormSearchResponse. El campo ECL indica la categoría de termino que va a buscar (medicamentos, ordenes, diagnosticos, alergias)'
			}
		],
		actions: [
			{
				name: 'Agregar',
				navigate: '../',
			}
		]
	},
	{
		path: 'paciente/:idPaciente/ordenes/nueva-orden/editar-estudio',
		loads: [
			{
				name: 'HCEGeneralStateController.getActiveProblems(patientId: number): HCEPersonalHistoryDto []',
				roles: 'Todo ESPECIALISTA_MEDICO, ENFERMERO_ADULTO_MAYOR, ENFERMERO, PROFESIONAL_DE_SALUD',
				path: '/institutions/{institutionId}/patient/{patientId}/hce/general-state',
				comments: 'Ya esta hecho',
				method: 'GET',
				fetch: [
					{
						id: 4,
						startDate: '2020-07-13',
						inactivationDate: '2020-07-13',
						statusId: '12312',
						snomed: {
							id: '2222',
							pt: 'ANGINAS'
						},
					},
					{
						id: 8,
						startDate: '2020-07-13',
						inactivationDate: '2020-07-13',
						statusId: '12312',
						snomed: {
							id: '2222',
							pt: 'PAPERA'
						},
					},
				]
			},
			{
				name: 'SnowstormController.getConcepts(term: String, ecl: String): HCEPersonalHistoryDto []',
				roles: 'Cualquiera',
				path: '/snowstorm/concepts?term=IBUPROFENO&ecl=123123',
				method: 'GET',
				comments: 'Ya esta hecho y devuelve la clase SnowstormSearchResponse'
			}
		],
		actions: [
			{
				name: 'Agregar',
				navigate: '../',
			}
		]
	},
	{
		path: 'paciente/:idPaciente/ordenes/nueva-orden/confirmar-estudio',
		loads: [
			{
				name: 'ServiceRequest.create(patientId: number): ServiceRequestListDto',
				roles: 'Todo ESPECIALISTA_MEDICO sobre la institución',
				comments: 'Los siguientes campos pueden ser nulos: observaciones',
				path: '/api/institutions/{institutionId}/patient/{patientId}/serviceRequests',
				method: 'POST',
				body: {
					medicalCoverageId: 1,
					studies: [
						{
							snomed: {
								id: '11111',
								pt: 'Radiologia',
							},
							healthConditionId: 123123,
							observations: 'Tomarlo durante las mañanas en ayuno'
						}
					]
				},
				fetch: [1, 5, 7], // id de los service request creados
			}
		],
		actions: [
			{
				name: 'Descargar orden',
				navigate: './descargar-orden',
			}
		]
	}, {
		path: 'paciente/:idPaciente/ordenes/nueva-orden/confirmar-estudio/descargar-orden',
		loads: [
			{
				name: 'ServiceRequest.download(patientId: number, serviceRequestId: number): File',
				roles: 'Todo ESPECIALISTA_MEDICO sobre la institución',
				comments: 'Descarga el archivo pdf de la receta',
				path: '/api/institutions/{institutionId}/patient/{patientId}/serviceRequests/{serviceRequestId}/downloadFile',
				method: 'GET',
			}
		],
		actions: [
			{
				name: 'Volver',
				navigate: '../../../',
			}
		]
	},
	{
		path: 'paciente/:idPaciente/ordenes/completar-orden',
		loads: [
			{
				name: 'ServiceRequest.complete(patientId: number, serviceRequestId:number): CompleteRequestDto',
				roles: 'Todo ESPECIALISTA_MEDICO sobre la institución',
				path: '/api/institutions/{institutionId}/patient/{patientId}/serviceRequests/{serviceRequestId}',
				method: 'PUT',
				body: {
					observation: 'El paciente presenta la tiroide alta',
					link: 'http://www.google.com'
				}
			}
		],
		actions: [
			{
				name: 'Volver',
				navigate: '../',
			}
		]
	},
	{
		path: 'paciente/:idPaciente/ordenes/eliminar-orden',
		loads: [
			{
				name: 'ServiceRequest.delete(patientId: number, serviceRequestId:number)',
				roles: 'Todo ESPECIALISTA_MEDICO sobre la institución',
				comments: 'Borrado lógico cambiando el estado a ingresado por error',
				path: '/api/institutions/{institutionId}/patient/{patientId}/serviceRequests/{serviceRequestId}',
				method: 'DELETE',
			}
		],
		actions: [
			{
				name: 'Volver',
				navigate: '../',
			}
		]
	},
	{
		path: 'paciente/:idPaciente/ordenes/ver-orden',
		loads: [
			{
				name: 'ServiceRequest.get(patientId: number, serviceRequestId:number): DiagnosticReportInfoDto',
				roles: 'Todo ESPECIALISTA_MEDICO sobre la institución',
				comments: '',
				path: '/api/institutions/{institutionId}/patient/{patientId}/serviceRequests/{serviceRequestId}',
				method: 'GET',
				fetch: {
					snomed: {
						id: '11111',
						pt: 'Radiologia',
					},
					healthConditionSnomed: {
						id: '2222',
						pt: 'ANGINAS'
					},
					statusId: '213123',
					observation: 'El paciente presenta la tiroide alta',
					link: 'http://www.google.com'
				}
			}
		],
		actions: [
			{
				name: 'Volver',
				navigate: '../',
			}
		]
	}, {
		path: 'paciente/:idPaciente/ordenes/nueva-indicacion',
		loads: [
			{
				name: 'HCEGeneralStateController.getActiveProblems(patientId: number): HCEPersonalHistoryDto []',
				roles: 'Todo ESPECIALISTA_MEDICO, ENFERMERO_ADULTO_MAYOR, ENFERMERO, PROFESIONAL_DE_SALUD',
				path: '/institutions/{institutionId}/patient/{patientId}/hce/general-state',
				comments: 'Ya esta hecho',
				method: 'GET',
				fetch: [
					{
						id: 4,
						startDate: '2020-07-13',
						inactivationDate: '2020-07-13',
						statusId: '12312',
						snomed: {
							id: '2222',
							pt: 'ANGINAS'
						},
					},
					{
						id: 8,
						startDate: '2020-07-13',
						inactivationDate: '2020-07-13',
						statusId: '12312',
						snomed: {
							id: '2222',
							pt: 'PAPERA'
						},
					},
				]
			}
		],
		actions: [
			{
				name: 'Confirmar',
				navigate: '../',
			}
		]
	},
	{
		path: 'paciente/:idPaciente/ordenes/nueva-indicacion/confirmar-indicacion',
		loads: [
			{
				name: 'MedicalRequests.new(patientId: number): MedicalRequestsDto',
				roles: 'Todo ESPECIALISTA_MEDICO sobre la institución',
				path: '/api/institutions/{institutionId}/patient/{patientId}/medical-requests',
				method: 'POST',
				body: {
					healthConditionSnomed: {
						id: '2222',
						pt: 'ANGINAS'
					},
					observations: 'Tomarlo durante las mañanas en ayuno'
				},
				fetch: 8, // id del service request creado
			}
		],
		actions: [
			{
				name: 'Descargar orden',
				navigate: './descargar-orden',
			},
			{
				name: 'Volver',
				navigate: '../',
			}
		]
	},
	{
		path: 'paciente/:idPaciente/ordenes/finalizar-indicacion',
		loads: [
			{
				name: 'MedicalRequests.finish(patientId: number, medicalRequestId:number)',
				roles: 'Todo ESPECIALISTA_MEDICO sobre la institución',
				comments: 'Cargar la fecha fin',
				path: '/api/institutions/{institutionId}/patient/{patientId}/medical-requests/{medicalRequestId}',
				method: 'PUT',
			}
		],
		actions: [
			{
				name: 'Volver',
				navigate: '../',
			}
		]
	}
];


