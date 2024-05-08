package net.pladema.imagenetwork.imagequeue.domain;

public enum EImageMoveStatus {

    PENDING,
    MOVING,
    ERROR;

    static public EImageMoveStatus map(String status) {
        switch (status){
            case "PENDING":
                return PENDING;
            case "MOVING":
                return MOVING;
            default:
                return ERROR;
        }

    }
}
