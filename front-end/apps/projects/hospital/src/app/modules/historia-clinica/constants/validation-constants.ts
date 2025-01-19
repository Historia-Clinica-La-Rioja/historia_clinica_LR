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
        heartRate: 999,
        respiratoryRate: 999,
        temperature: 99.9,
        bloodOxygenSaturation: 100,
        systolicBloodPressure: 999,
        diastolicBloodPressure: 999,
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

export const CLINICAL_EVALUATION = {
    MIN: {
        bloodPressure: 0,
        hematocrit: 0,
    },
    MAX: {
        bloodPressure: 999,
        hematocrit: 100,
    },
}

export const PREMEDICATION = {
    MIN: {
        dosis: 0,
    }
}

export const VITAL_SIGNS = {
    MIN: {
        bloodPressure: 20,
        pulse: 70,
        saturation: 0,
        endTidal: 0,
    },
    MAX: {
        bloodPressure: 140,
        pulse: 100,
        saturation: 100,
        endTidal: 240,
    },
}