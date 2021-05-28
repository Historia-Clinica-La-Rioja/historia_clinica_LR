package ar.lamansys.sgx.shared.repositories;

public class QueryStringHelper {

	public static String escapeSql(String sql){
		return sql.replace("'", "''");
	}
}
