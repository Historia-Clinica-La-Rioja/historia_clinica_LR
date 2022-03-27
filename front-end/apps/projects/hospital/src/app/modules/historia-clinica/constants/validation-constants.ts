export const FACTORES_DE_RIESGO = {
    MIN: {
        heartRate: 0,
        respiratoryRate: 0,
        temperature: 0,
        bloodOxygenSaturation: 0,
        systolicBloodPressure: 0,
        diastolicBloodPressure: 0,
        bloodGlucose: 1,
        glycosylatedHemoglobin: 1,
        cardiovascularRisk: 1,
    },
    MAX: {
        bloodGlucose: 500,
        glycosylatedHemoglobin: 20,
        cardiovascularRisk: 100,
    },
}

export const DATOS_ANTROPOMETRICOS = {
    MIN: {
        headCircumference: 1,
        height: 0,
        weight: 0,
    },
    MAX: {
        headCircumference: 100,
        height: 1000,
        weight: 1000,
    },
}