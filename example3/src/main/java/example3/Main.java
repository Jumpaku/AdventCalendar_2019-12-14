package example3;


import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class Main {
    /**
     * JavaFXウィンドウ上にドラッグ軌跡を描画する
     * JavaFXウィンドウに描画されたものと同じドラッグ軌跡をSVG画像にも描画する
     */
    public static void main(String[] args) {
        // Appクラスを指定してアプリケーションを起動する
        Application.launch(App.class, args);
    }
    public static class App extends Application {
        /** 直前のドラッグ位置 */
        Point2D previousPoint;
        /**
         * アプリケーション起動時に呼び出される
         * @param primaryStage
         */
        @Override
        public void start(Stage primaryStage) {
            int width = 640;
            int height = 480;
            // Canvasオブジェクトを生成する
            Canvas canvas = new Canvas(width, height);
            // FXGraphics2Dオブジェクトを生成する
            FXGraphics2D fxGraphics2D = new FXGraphics2D(canvas.getGraphicsContext2D());
            // SVGGraphics2Dオブジェクトを生成する
            SVGGraphics2D svgGraphics2D = new SVGGraphics2D(width, height);
            // FXGraphics2DオブジェクトとSVGGraphics2Dオブジェクトをまとめたリストを生成する
            List<Graphics2D> graphics2DList = Arrays.asList(fxGraphics2D, svgGraphics2D);
            // Canvasオブジェクトにマウスプレス時のイベントハンドラを設定する
            canvas.setOnMousePressed(e -> {
                // マウスプレス時には図をクリアして現在のカーソル位置を保存する
                graphics2DList.forEach(g -> {
                    g.setBackground(Color.WHITE);
                    g.clearRect(0, 0, width, height);
                });
                previousPoint = new Point2D.Double(e.getX(), e.getY());
            });
            // Canvasオブジェクトにマウスドラッグ時のイベントハンドラを設定する
            canvas.setOnMouseDragged(e -> {
                // マウスドラッグ時には直前のカーソル位置と現在のカーソル位置の間に線分を描画して現在のカーソル位置を保存する
                Point2D.Double currentPoint = new Point2D.Double(e.getX(), e.getY());
                graphics2DList.forEach(g -> g.draw(new Line2D.Double(previousPoint, currentPoint)));
                previousPoint = currentPoint;
            });
            // Canvasオブジェクトにマウスリリース時のイベントハンドラを設定する
            canvas.setOnMouseReleased(e -> {
                // マウスリリース時にはSVG画像とPNG画像の書き出しを行う
                // PNG画像を書き出す
                SnapshotParameters params = new SnapshotParameters();
                WritableImage img = canvas.snapshot(params, new WritableImage(width, height));
                try {
                    File file = new File("example.png");
                    ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                // SVG画像を書き出す
                String svg = svgGraphics2D.getSVGDocument();
                try (PrintWriter writer = new PrintWriter(new File("./example.svg"))) {
                    writer.print(svg);
                } catch (FileNotFoundException exp) {
                    exp.printStackTrace();
                }
            });
            // Canvasオブジェクトをシーングラフに追加し，ウィンドウを表示する
            primaryStage.setScene(new Scene(new Pane(canvas)));
            primaryStage.show();
        }
    }
}

