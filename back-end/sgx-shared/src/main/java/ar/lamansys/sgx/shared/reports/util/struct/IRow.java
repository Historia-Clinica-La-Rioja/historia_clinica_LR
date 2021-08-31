package ar.lamansys.sgx.shared.reports.util.struct;

import java.time.LocalDateTime;

public interface IRow extends Iterable<ICell> {

    String getCellContentAsString(int cellNumber);

    Number getCellContentAsNumber(int cellNumber);

    LocalDateTime getCellContentAsDate(int cellNumber);

    boolean isEmpty();

    ICell createCell(int cellNumber);

    int getCantCells();

    int getLastCellIndex();

}
