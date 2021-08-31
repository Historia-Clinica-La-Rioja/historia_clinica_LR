package ar.lamansys.sgx.shared.reports.util;

import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CellContent {

    private int row;
    private int column;
    private int numRows;
    private int numColumns;

    private boolean cellRange;

    private Object value;
    private boolean formula;

    private DATAFORMAT dataformat;

    public enum DATAFORMAT {
        STRING,
        DOUBLE,
        BOOLEAN,
        DATE,
        LOCALDATE,
        LOCALDATETIME,
        CALENDAR
    }

    private ICellStyle style;

    public int lastCol(){
        return column + numColumns -1;
    }

    public int lastRow(){
        return row + numRows -1;
    }

    public CellContent(int row, int column, int numRows, int numColumns, Object value, ICellStyle style){
        this(row, column, numRows, numColumns, value, style, DATAFORMAT.STRING);
    }

    public CellContent(int row, int column, int numRows, int numColumns, Object value, ICellStyle style, DATAFORMAT dataformat){
        this.row=row;
        this.column=column;
        this.numRows=numRows;
        this.numColumns=numColumns;
        this.cellRange = numRows > 1 || numColumns > 1;
        this.value=value;
        this.formula=isFormula(value);
        this.style=style;
        this.dataformat = dataformat;
    }

    private boolean isFormula(Object value) {
        return String.valueOf(value).startsWith("=");
    }
}
