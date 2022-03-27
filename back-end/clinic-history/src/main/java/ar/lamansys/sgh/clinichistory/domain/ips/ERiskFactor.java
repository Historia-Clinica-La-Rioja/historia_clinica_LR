package ar.lamansys.sgh.clinichistory.domain.ips;


import java.util.Arrays;

public enum ERiskFactor {

	SYSTOLIC_BLOOD_PRESSURE("271649006", "8480-6"),
    DIASTOLIC_BLOOD_PRESSURE("271650006", "8462-4"),
    MEAN_PRESSURE("6797001", "8478-0"),
    TEMPERATURE("703421000", "8310-5"),
    HEART_RATE("364075005", "8867-4"),
    RESPIRATORY_RATE("86290005","9279-1"),
    BLOOD_OXYGEN_SATURATION("103228002", "59408-5"),
    BLOOD_GLUCOSE("434912009", "15074-8"),
    GLYCOSYLATED_HEMOGLOBIN("259689004", "62854-5"),
    CARDIOVASCULAR_RISK("827181004", "65853-4"),
    HEIGHT("50373000", "8302-2"),
    WEIGHT("27113001", "29463-7"),
    HEAD_CIRCUMFERENCE("363812007", "11947-9"),
    BMI("60621009", "39156-5");

    private String loincCode;

    private String sctidCode;

    ERiskFactor(String sctidCode, String loincCode) {
        this.sctidCode = sctidCode;
        this.loincCode = loincCode;
    }

    public String getLoincCode() {
        return loincCode;
    }

    public String getSctidCode() {
        return sctidCode;
    }

    public static boolean isCodeAnthropometricData(String sctidCode) {
        return Arrays.asList(HEIGHT.sctidCode, WEIGHT.sctidCode, BMI.sctidCode, HEAD_CIRCUMFERENCE.sctidCode).contains(sctidCode);
    }

    public static boolean isCodeRiskFactor(String sctidCode) {
        return Arrays.asList(SYSTOLIC_BLOOD_PRESSURE.sctidCode, DIASTOLIC_BLOOD_PRESSURE.sctidCode,
                MEAN_PRESSURE.sctidCode, TEMPERATURE.sctidCode, HEART_RATE.sctidCode,
                RESPIRATORY_RATE.sctidCode, BLOOD_OXYGEN_SATURATION.sctidCode, BLOOD_GLUCOSE.sctidCode,
                GLYCOSYLATED_HEMOGLOBIN.sctidCode, CARDIOVASCULAR_RISK.sctidCode).contains(sctidCode);
    }

}
