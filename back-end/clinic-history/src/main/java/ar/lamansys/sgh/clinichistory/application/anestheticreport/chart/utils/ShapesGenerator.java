package ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.utils;

import java.awt.BasicStroke;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ShapesGenerator {

    public static BasicStroke createDashedStroke() {
        return new BasicStroke(
                1.0f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10.0f,
                new float[]{5.0f},
                0.0f
        );
    }

    public static BasicStroke createDashedBoldedStroke() {
        return new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10.0f, new float[]{7.5f}, 0);
    }

    public static Shape createCaretUp(double x, double y, double width, double height) {
        Path2D.Double path = new Path2D.Double();
        path.moveTo(x, y);
        path.lineTo(x - width, y + height);
        path.moveTo(x, y);
        path.lineTo(x + width, y + height);
        return path;
    }

    public static Shape createCaretDown(double x, double y, double width, double height) {
        Path2D.Double path = new Path2D.Double();
        path.moveTo(x, y);
        path.lineTo(x - width, y - height);
        path.moveTo(x, y);
        path.lineTo(x + width, y - height);
        return path;
    }


    public static Shape createTriangle(double x, double y, double width, double height) {
        Path2D.Double path = new Path2D.Double();
        path.moveTo(x, y);
        path.lineTo(x + width, y);
        path.lineTo(x + width / 2, y + height);
        path.closePath();
        return path;
    }

    public static Shape createSquare(int x, int y, int w) {
        return new Rectangle(x, y, w, w);
    }


    public static Shape createCircle(double x, double y, int w) {
        return new Ellipse2D.Double(x, y, w, w);
    }
}
