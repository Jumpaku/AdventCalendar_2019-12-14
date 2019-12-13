package example1;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.*;
import java.io.File;
import java.io.IOException;

public class Main {
    /**
     * JavaFXウィンドウに図を描画する
     * さらに描画した図をpng画像としてファイルに書き出す
     * @param args
     */
    public static void main(String[] args) {
        // Appクラスを指定してアプリケーションを起動する
        Application.launch(App.class, args);
    }
    public static class App extends Application {
        /**
         * アプリケーション起動時に呼び出される
         * @param primaryStage
         */
        @Override
        public void start(Stage primaryStage) throws IOException {
            // Canvasオブジェクトを用意する
            int width = 320;
            int height = 240;
            Canvas canvas = new Canvas(width, height);
            // CanvasオブジェクトのGraphicsContext2DオブジェクトをFXGraphics2Dクラスのコンストラクタに渡す
            GraphicsContext ctx = canvas.getGraphicsContext2D();
            Graphics2D g = new FXGraphics2D(ctx);
            // Graphics2Dクラスを通してCanvasオブジェクトに図を描画する
            exampleDrawAndFill(g);
            examplePath(g);
            exampleArea(g);
            exampleTransform(g);
            // Canvasオブジェクトをシーングラフに追加し，ウィンドウを表示する
            primaryStage.setScene(new Scene(new Pane(canvas)));
            primaryStage.show();
            // CanvasオブジェクトのPNG画像を書き出す
            SnapshotParameters params = new SnapshotParameters();
            WritableImage img = canvas.snapshot(params, new WritableImage(width, height));
            File file = new File("example.png");
            ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", file);
        }
    }

    public static void exampleDrawAndFill(Graphics2D g) {
        // 楕円を表現するEllipse2Dオブジェクトを生成する
        // 基本図形は他にLine2D(線分)，Rectangle2D(長方形)，Arc2D(弧)などもある
        Ellipse2D ellipse = new Ellipse2D.Double(0.0, 25.0, 100.0, 50.0);
        // Ellipseオブジェクトの内部の塗りつぶし色を設定する
        g.setColor(new Color(255, 75, 0));
        // Ellipseオブジェクトの内部を塗りつぶす
        g.fill(ellipse);
        // Ellipseオブジェクトの輪郭線の色を黒に設定する
        g.setColor(Color.BLACK);
        // Ellipseオブジェクトの輪郭線を太さ1の線に設定する
        g.setStroke(new BasicStroke(1f));
        g.draw(ellipse);
    }

    public static void examplePath(Graphics2D g) {
        // 複雑なパス図形を表現できるPath2Dオブジェクトを生成する
        Path2D curve = new Path2D.Double();
        // Path2Dオブジェクトの開始点を設定する
        curve.moveTo(125.0, 25.0);
        // Path2Dオブジェクトの開始点に，3次Bezier曲線を接続する
        curve.curveTo(200.0, 25.0, 100.0, 75.0, 175.0, 75.0);
        // Path2Dオブジェクトの輪郭線を描画する
        g.setColor(new Color(3, 175, 122));
        g.setStroke(new BasicStroke(1f));
        g.draw(curve);
        // Path2Dオブジェクトを生成する
        Path2D polyline = new Path2D.Double();
        // Path2Dオブジェクトの開始点を設定する
        polyline.moveTo(125.0, 25.0);
        // Path2Dオブジェクトの開始点に，線分を接続する
        polyline.lineTo(200.0, 25.0);
        // Path2Dオブジェクトに，さらに線分を接続する
        polyline.lineTo(100.0, 75.0);
        // Path2Dオブジェクトに，さらに線分を接続する
        polyline.lineTo(175.0, 75.0);
        // Path2Dオブジェクトの輪郭線を黒い太さ1線に設定し，描画する
        // BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUNDは線分の端点や接続部分を丸くするための設定
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(polyline);
    }

    public static void exampleArea(Graphics2D g) {
        // 3個のEllipse2Dオブジェクトを生成する
        Ellipse2D ellipse0 = new Ellipse2D.Double(0.0, 100.0, 100.0, 50.0);
        Ellipse2D ellipse1 = new Ellipse2D.Double(0.0, 100.0, 50.0, 100.0);
        Ellipse2D ellipse2 = new Ellipse2D.Double(0.0, 100.0, 50.0, 50.0);
        // 3個のEllipse2Dオブジェクトの和集合領域を表現するAreaオブジェクトを生成する
        Area union = new Area(ellipse0);
        union.add(new Area(ellipse1));
        union.add(new Area(ellipse2));
        // 3個のEllipse2Dオブジェクトの共通集合領域を表現するAreaオブジェクトを生成する
        Area intersection = new Area(ellipse0);
        intersection.intersect(new Area(ellipse1));
        intersection.intersect(new Area(ellipse2));
        // 和集合領域と共通集合領域を塗りつぶす
        g.setColor(new Color(77, 196, 255));
        g.fill(union);
        g.setColor(new Color(0, 90, 255));
        g.fill(intersection);
        // 和集合領域と共通集合領域の輪郭線を描画する
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(union);
        g.draw(intersection);
    }

    public static void exampleTransform(Graphics2D g) {
        // 弧を表現するArc2Dオブジェクトを生成する
        Arc2D arc = new Arc2D.Double(-1.0, -1.0, 2.0, 2.0, -45.0, 270.0, Arc2D.PIE);
        // アフィン変換を表現するAffineTransformオブジェクトを生成する
        AffineTransform t = new AffineTransform();
        t.translate(150.0, 150.0); // 平行移動
        t.rotate(Math.PI / 2); // 回転
        t.scale(50.0, 50.0); // 拡大
        // Arc2Dオブジェクトにアフィン変換した図形オブジェクトを生成する
        Shape transformed = t.createTransformedShape(arc);
        // アフィン変換した図形オブジェクトを塗りつぶす
        g.setColor(new Color(255, 241, 0));
        g.fill(transformed);
        // アフィン変換した図形オブジェクトの輪郭線を描く
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(transformed);
    }
}
