package net.pladema.reports.util.struct;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CellStyle {

    public static class COLOR {

        private Short excelValue;
        private String odsValue;

        public COLOR(Short excelValue, String odsValue) {
            this.excelValue = excelValue;
            this.odsValue = odsValue;
        }

        public Short getExcelValue() {
            return excelValue;
        }
        public String getODSValue() {
            return odsValue;
        }
    }

    Short fontSize;
    Boolean bold;
    COLOR color;

}
