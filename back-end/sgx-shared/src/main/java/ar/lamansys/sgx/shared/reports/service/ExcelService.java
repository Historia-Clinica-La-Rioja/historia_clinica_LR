package ar.lamansys.sgx.shared.reports.service;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;

import javax.persistence.Query;

public interface ExcelService {

    IWorkbook buildExcelFromQuery(String title, String[] headers, Query query);
}
