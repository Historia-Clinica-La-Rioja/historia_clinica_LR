package ar.lamansys.sgx.cubejs.application.dashboardinfo.excepciones;


public enum DashboardInfoExceptionEnum {
    API_URL_NOT_DEFINED(new DashboardInfoException("La url de la api no esta definida")),
    ;

    private final DashboardInfoException exception;

    DashboardInfoExceptionEnum(DashboardInfoException exception) {
        this.exception = exception;
    }

    public DashboardInfoException getException() {
        return exception;
    }
}
