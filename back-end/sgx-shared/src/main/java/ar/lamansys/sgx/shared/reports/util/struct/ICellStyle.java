package ar.lamansys.sgx.shared.reports.util.struct;

public interface ICellStyle {

    enum HALIGNMENT {
        LEFT,
        CENTER,
        RIGHT
    }

    enum VALIGNMENT {
        TOP,
        CENTER,
        BOTTOM
    }

    void setFontColor(Object value);

    short getFontSize();

    void setFontSize(short size);

    boolean isBold();

    void setBold(boolean boldFont);

    boolean isWrap();

    void setWrap(boolean wrapText);

    boolean isBorders();

    void setBorders(boolean withBorders);

    HALIGNMENT getHAlign();

    void setHAlign(HALIGNMENT horizontalAlign);

    VALIGNMENT getVAlign();

    void setVAlign(VALIGNMENT verticalAlign);
}
