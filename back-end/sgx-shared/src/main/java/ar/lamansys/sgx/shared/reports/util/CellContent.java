package ar.lamansys.sgx.shared.reports.util;

import ar.lamansys.sgx.shared.reports.util.struct.CellStyle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CellContent {

    private int row;
    private int column;
    private int numRows;
    private int numColumns;

    private String value;
    private CellStyle style;

    public int lastCol(){
        return column + numColumns -1;
    }

    public int lastRow(){
        return row + numRows -1;
    }
}
