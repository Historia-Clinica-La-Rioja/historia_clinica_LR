package ar.lamansys.sgx.shared.restclient.services.domain;

@SuppressWarnings("serial")
public class WSResponseException extends Exception {
    public WSResponseException(String message) {
        super(message);
    }
}