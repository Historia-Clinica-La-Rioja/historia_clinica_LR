package net.pladema.reports.service;

import net.pladema.reports.util.struct.IWorkbook;

import javax.persistence.Query;

public interface ExcelService {

    IWorkbook buildExcelFromQuery(String title, String[] headers, Query query);
}
