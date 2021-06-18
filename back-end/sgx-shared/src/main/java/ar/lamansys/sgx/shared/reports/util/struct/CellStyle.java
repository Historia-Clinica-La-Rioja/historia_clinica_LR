package ar.lamansys.sgx.shared.reports.util.struct;

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

    public enum HALIGNMENT {
        LEFT,
        CENTER,
        RIGHT
    }

    public enum VALIGNMENT {
        TOP,
        CENTER,
        BOTTOM
    }


    Short fontSize = 11;
    boolean bold = false;
    COLOR color;


    private boolean wrap = false;
    private VALIGNMENT vAlign = VALIGNMENT.BOTTOM;
    private HALIGNMENT halign = HALIGNMENT.LEFT;
    private boolean borders;
}
