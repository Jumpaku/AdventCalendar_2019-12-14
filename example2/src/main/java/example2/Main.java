package example2;

import org.jfree.graphics2d.svg.SVGGraphics2D;

import java.awt.*;
import java.awt.geom.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Main {
    /**
     * SVG画像に図を描画しファイルに書き出す
     * @param args
     */
    public static void main(String[] args) {
        // SVGGraphics2Dオブジェクトを用意する
        SVGGraphics2D g = new SVGGraphics2D(320, 240);
        // SVGGraphics2Dオブジェクトに図を描画する
        exampleDrawAndFill(g);
        examplePath(g);
        exampleArea(g);
        exampleTransform(g);
        // SVGGraphics2DオブジェクトからSVG画像の文字列を取得しファイルに書き出す
        String svg = g.getSVGDocument();
        try (PrintWriter writer = new PrintWriter(new File("./example.svg"))) {
            writer.print(svg);
        } catch (FileNotFoundException exp) {
            exp.printStackTrace();
        }
    }

    public static void exampleDrawAndFill(Graphics2D g) {
        Ellipse2D ellipse = new Ellipse2D.Double(0.0, 25.0, 100.0, 50.0);

        g.setColor(new Color(255, 75, 0));
        g.fill(ellipse);

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1f));
        g.draw(ellipse);
    }

    public static void examplePath(Graphics2D g) {
        Path2D curve = new Path2D.Double();
        curve.moveTo(125.0, 25.0);
        curve.curveTo(200.0, 25.0, 100.0, 75.0, 175.0, 75.0);

        g.setColor(new Color(3, 175, 122));
        g.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(curve);

        Path2D polyline = new Path2D.Double();
        polyline.moveTo(125.0, 25.0);
        polyline.lineTo(200.0, 25.0);
        polyline.lineTo(100.0, 75.0);
        polyline.lineTo(175.0, 75.0);

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(polyline);
    }

    public static void exampleArea(Graphics2D g) {
        Ellipse2D ellipse0 = new Ellipse2D.Double(0.0, 100.0, 100.0, 50.0);
        Ellipse2D ellipse1 = new Ellipse2D.Double(0.0, 100.0, 50.0, 100.0);
        Ellipse2D ellipse2 = new Ellipse2D.Double(0.0, 100.0, 50.0, 50.0);

        Area union = new Area(ellipse0);
        union.add(new Area(ellipse1));
        union.add(new Area(ellipse2));

        Area intersection = new Area(ellipse0);
        intersection.intersect(new Area(ellipse1));
        intersection.intersect(new Area(ellipse2));

        g.setColor(new Color(77, 196, 255));
        g.fill(union);
        g.setColor(new Color(0, 90, 255));
        g.fill(intersection);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(union);
        g.draw(intersection);
    }

    public static void exampleTransform(Graphics2D g) {
        Arc2D arc = new Arc2D.Double(-1.0, -1.0, 2.0, 2.0, -45.0, 270.0, Arc2D.PIE);
        AffineTransform t = new AffineTransform();
        t.translate(150.0, 150.0);
        t.rotate(Math.PI / 2);
        t.scale(50.0, 50.0);
        Shape transformed = t.createTransformedShape(arc);
        g.setColor(new Color(255, 241, 0));
        g.fill(transformed);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(transformed);
    }
}
