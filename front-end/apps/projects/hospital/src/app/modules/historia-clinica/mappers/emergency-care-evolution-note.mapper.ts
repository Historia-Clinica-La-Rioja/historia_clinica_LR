import { OutpatientAnthropometricDataDto, OutpatientFamilyHistoryDto, OutpatientMedicationDto, OutpatientRiskFactorDto } from "@api-rest/api-model";
import { DateFormat, momentFormat } from "@core/utils/moment.utils";

export const toRiskFactors = (riskFactors: OutpatientRiskFactorDto): OutpatientRiskFactorDto =>  {
    if (riskFactors) {
        let result;
        Object.keys(riskFactors).forEach(
            key => {
                if (riskFactors[key]?.value) {
                    result = { ...result, [key]: riskFactors[key] }
                }
            }
        )
        return result;
    }
    return null;
}

export const mapFamilyHistories = (familyHistories: any[]): OutpatientFamilyHistoryDto[] => {
    return familyHistories?.map(f => {
        return {
            snomed: f.snomed,
            startDate: f.fecha ? momentFormat(f.fecha, DateFormat.API_DATE) : null
        }
    }) || []
}

export const mapAnthropometricData = (anthropometricData): OutpatientAnthropometricDataDto => {
    if (anthropometricData) {
        return {
            height: {
                value: anthropometricData.height?.toString()
            },
            weight: {
                value: anthropometricData.weight?.toString()
            },
            bloodType: anthropometricData.bloodType,
            headCircumference: {
                value: anthropometricData.headCircumference?.toString()
            },
            bmi: {
                value: null
            },
        }
    }
    return null;
}

export const mapMedications = (formData: any[]): OutpatientMedicationDto[] => {
    return formData?.map(r => {
        return {
            note: r.observaciones,
            snomed: r.snomed,
            suspended: r.suspendido
        }
    }) || []
}