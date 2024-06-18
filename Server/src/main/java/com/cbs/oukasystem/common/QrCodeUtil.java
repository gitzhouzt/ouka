package com.cbs.oukasystem.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

import com.cbs.oukasystem.config.GlobalExceptionHandler;
import com.cbs.oukasystem.entity.common.QrImage;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * QRコードツール
 * <p>
 * 説明：
 * QRコードはエンコードの形式であり、
 * コンテンツをエンコードしてQRコードを取得し、
 * コンテンツをデコードしてデータコンテンツを取得します。
 *
 */
@SuppressWarnings("unused")
public class QrCodeUtil {

    /**
     * 埋め込まれた画像の高さ（px）（プログラムによって処理されます）
     * <p>
     * 注：埋め込まれた画像がQRコードを占有しすぎないようにしてください。
     * 占有しないと、生成されたQRコードが認識されない場合があります。
     * 埋め込まれた画像のサイズは、QRコードのサイズの1/5または1/4に設定することをお勧めします
     */
    private static int embeddedImgDefaultWidth;

    /** 埋め込まれた画像の高さ（px）（プログラムによって処理されます） */
    private static int embeddedImgDefaultHeight;

    /** 埋め込み画像とQRコード画像の間のボックスの幅（px） */
    private static int frameWidth;

    /** 埋め込み画像とQRコード画像の間のボックスの色. */
    private static int frameWidthColor;

    /** QRコード コードの色 */
    private static int qrCodeColor;

    /** QRコードの背景色 */
    private static int qrCodeBackgroundColor;

    /** 画像が縮小されたら、「空」の下の領域の色を補完します */
    private static int fillColor;

    /** QRコードライター */
    private static MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

    /*
     * 基本的なパラメータを初期化します
     */
    static {
        QrCodeUtil.embeddedImgDefaultWidth = 80;
        QrCodeUtil.embeddedImgDefaultHeight = 80;
        QrCodeUtil.frameWidth = 0;
        QrCodeUtil.frameWidthColor = Color.BLUE.getRGB();
        QrCodeUtil.qrCodeColor = Color.BLACK.getRGB();
        QrCodeUtil.qrCodeBackgroundColor = Color.WHITE.getRGB();
        QrCodeUtil.fillColor = Color.RED.getRGB();
    }

    /**
     * 基本パラメータのパラメータ値を設定および変更します
     *
     * @param embeddedImgDefaultWidth  埋め込まれた画像の高さ（px）（プログラムによって処理されます）
     * @param embeddedImgDefaultHeight 埋め込まれた画像の高さ（px）（プログラムによって処理されます）
     * @param frameWidth               埋め込み画像とQRコード画像の間のボックスの幅(px)
     * @param frameWidthColor          埋め込み画像とQRコード画像の間のボックスの色
     * @param qrCodeColor              QRコード コードの色
     * @param qrCodeBackgroundColor    qrコードの背景色
     * @param fillColor                画像が縮小されたら、「空」の下の領域の色を補完します
     * @date 2022/6/19 20:47
     */
    public static void modifyBasicParamsValues(Integer embeddedImgDefaultWidth, Integer embeddedImgDefaultHeight,
            Integer frameWidth, Color frameWidthColor, Color qrCodeColor, Color qrCodeBackgroundColor,
            Color fillColor) {
        if (embeddedImgDefaultWidth != null) {
            QrCodeUtil.embeddedImgDefaultWidth = embeddedImgDefaultWidth;
        }
        if (embeddedImgDefaultHeight != null) {
            QrCodeUtil.embeddedImgDefaultHeight = embeddedImgDefaultHeight;
        }
        if (frameWidth != null) {
            QrCodeUtil.frameWidth = frameWidth;
        }
        if (frameWidthColor != null) {
            QrCodeUtil.frameWidthColor = frameWidthColor.getRGB();
        }
        if (qrCodeColor != null) {
            QrCodeUtil.qrCodeColor = qrCodeColor.getRGB();
        }
        if (qrCodeBackgroundColor != null) {
            QrCodeUtil.qrCodeBackgroundColor = qrCodeBackgroundColor.getRGB();
        }
        if (fillColor != null) {
            QrCodeUtil.fillColor = fillColor.getRGB();
        }
    }

    /**
     * QRコードを生成します（destImagePathが指すファイルへ）---方法1
     *
     * @param content       QRコードの内容
     * @param width         QRコード幅(px)
     * @param height        QRコードの高さ(px)
     * @param destImagePath QRコード画像のアドレスを生成します
     *
     * @return 生成されたQRコードファイルパス
     * @throws IOException     IOException
     * @throws WriterException WriterException
     *
     * @date 2022/6/19 20:47
     */
    public static String encodeQrCode(String content, int width, int height, String destImagePath)
            throws IOException, WriterException {

        File dest = getFile(destImagePath);
        // 画像タイプ
        String format = "jpg";
        Map<EncodeHintType, Object> hints = new HashMap<>(4);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        try (FileOutputStream output = new FileOutputStream(dest)) {
            // ジェネレータマトリックス
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            // 出力画像
            MatrixToImageWriter.writeToStream(bitMatrix, format, output);
        }
        return destImagePath;
    }

    /**
     * QRコードを生成します（destImagePathが指すファイルへ）---方法2
     *
     * @param content       QRコードの内容
     * @param width         QRコード幅(px)
     * @param height        QRコードの高さ(px)
     * @param destImagePath QRコード画像のアドレスを生成します
     *
     * @return 生成されたQRコードファイルパス
     * @throws IOException     IOException
     * @throws WriterException WriterException
     *
     * @date 2022/6/19 20:47
     */
    public static String encodeQrCodeAnotherWay(String content, int width, int height, String destImagePath)
            throws IOException, WriterException {
        boolean result = ImageIO.write(genBufferedImage(content, width, height), "jpg", getFile(destImagePath));
        return destImagePath;
    }

    /**
     * 画像でQRコードを生成(destImagePath File)
     *
     * @param content         QRコードの内容
     * @param width           QRコード幅(px)
     * @param height          QRコードの高さ(px)
     * @param embeddedImgPath モザイク画像のアドレス
     * @param destImagePath   QRコード画像のアドレスを生成します
     *
     * @return 生成されたQRコードファイルパス
     * @throws IOException     IOException
     * @throws WriterException WriterException
     *
     * @date 2022/6/19 20:47
     */
    public static String encodeQrCodeWithEmbeddedImg(String content, int width, int height, String embeddedImgPath,
            String destImagePath) throws IOException, WriterException {
        boolean result = ImageIO.write(genBufferedImageWithEmbeddedImg(content, width, height, embeddedImgPath), "jpg",
                getFile(destImagePath));
        return destImagePath;
    }

    /**
     * 画像でQRコードを生成(outputStream)
     *
     * @param content         QRコードの内容
     * @param width           QRコード幅(px)
     * @param height          QRコードの高さ(px)
     * @param embeddedImgPath モザイク画像のアドレス
     *
     * @throws IOException     IOException
     * @throws WriterException WriterException
     *
     * @date 2022/6/19 20:47
     */
    public static void encodeQrCodeWithEmbeddedImg(String content, int width, int height, String embeddedImgPath,
            OutputStream outputStream) throws IOException, WriterException {
        boolean result = ImageIO.write(genBufferedImageWithEmbeddedImg(content, width, height, embeddedImgPath), "jpg",
                outputStream);
    }

    /**
     * 上記の写真とテキストが埋め込まれたQRコードを生成する
     * <p>
     * 注：埋め込まれた画像のパラメーター値がnullまたは空の文字の場合、テキスト付きのQRコードのみが生成されます
     *
     * @param param パラメトリックモデル
     *
     * @return 生成されたQRコードファイルパス
     * @throws IOException     IOException
     * @throws WriterException WriterException
     *
     * @date 2022/6/19 20:47
     */
    public static String encodeQrCodeWithEmbeddedImgAndFonts(QrImage param) throws IOException, WriterException {
        // まずQRコード画像を生成します
        BufferedImage qrImage;
        String embeddedImgFilePath = param.getEmbeddedImgFilePath();
        String qrCodeContent = param.getQrCodeContent();
        int qrCodeWidth = param.getQrCodeWidth();
        int qrCodeHeight = param.getQrCodeHeight();
        if (embeddedImgFilePath == null || embeddedImgFilePath.trim().length() == 0) {
            qrImage = genBufferedImage(qrCodeContent, qrCodeWidth, qrCodeHeight);
        } else {
            qrImage = genBufferedImageWithEmbeddedImg(qrCodeContent, qrCodeWidth, qrCodeHeight, embeddedImgFilePath);
        }
        int qrCodeImageWidth = qrImage.getWidth();
        int qrCodeImageHeight = qrImage.getHeight();

        String[] splitStrLines;
        splitStrLines = splitStrLines(param.getWordContent(), qrCodeImageWidth / param.getWordSize());
        // テキスト画像の高さ=テキストの行数*各行の高さ（テキストの高さ）+ 10px;
        // テキスト画像の下部が不完全に表示されないようにするために、ここに特別に10ピクセルの高さが追加されています。
        int fontsImageHeight = splitStrLines.length * param.getWordSize() + 10;
        // トップテキストで画像を作成する
        BufferedImage imageWithFontsBufferedImage = new BufferedImage(qrCodeImageWidth, fontsImageHeight,
                BufferedImage.TYPE_4BYTE_ABGR);
        Graphics fontsImageGraphics = imageWithFontsBufferedImage.getGraphics();
        fontsImageGraphics.fillRect(0, 0, qrCodeImageWidth, fontsImageHeight);
        fontsImageGraphics.setColor(Color.black);
        fontsImageGraphics.setFont(new Font("v-sans", Font.PLAIN, param.getWordSize()));

        // テキストの長さが1行の長さよりも長い場合、行に分割されます
        // 1行あたりの文字数を制限します

        // Number param.getWordContent().length() /2
        // String param.getWordContent().length()
        if (param.getWordContent().length() / 2 > qrCodeImageWidth / param.getWordSize()) {
            for (int i = 0; i < splitStrLines.length; i++) {
                fontsImageGraphics.drawString(splitStrLines[i], 0, param.getWordSize() * (i + 1));
            }
        } else {
            fontsImageGraphics.drawString(param.getWordContent(),
                    // 全長から単語の長さを2で割ったものが正しいオフセットの長さです
                    /**
                     * String ((qrCodeImageWidth / param.getWordSize() -
                     * param.getWordContent().length()) / 2) * param.getWordSize(),
                     */
                    /*
                     * Number
                     */
                    20,
                    param.getWordSize());
        }

        // 画像からRGBを読み取る
        int[] imageArrayFonts = new int[qrCodeImageWidth * fontsImageHeight];
        imageArrayFonts = imageWithFontsBufferedImage.getRGB(0, 0, qrCodeImageWidth, fontsImageHeight, imageArrayFonts,
                0, qrCodeImageWidth);

        int[] imageArrayQr = new int[qrCodeImageWidth * qrCodeImageHeight];
        imageArrayQr = qrImage.getRGB(0, 0, qrCodeImageWidth, qrCodeImageHeight, imageArrayQr, 0, qrCodeImageWidth);

        // 新しい画像を生成します（setsetRGBの場合、画像のstartXとstartYを制御することで、さまざまなステッチ効果を実現できます）
        BufferedImage newBufferedImage = new BufferedImage(qrCodeImageWidth, qrCodeImageHeight + fontsImageHeight,
                BufferedImage.TYPE_INT_RGB);
        // 上の画像、下のテキスト
        // 上半分のRGBを設定します
        newBufferedImage.setRGB(0, 0, qrCodeImageWidth, qrCodeImageHeight, imageArrayQr, 0, qrCodeImageWidth);
        // 下半分のRGBを設定する
        newBufferedImage.setRGB(0, qrCodeImageHeight, qrCodeImageWidth, fontsImageHeight, imageArrayFonts, 0,
                qrCodeImageWidth);
        // テキストを上に、画像を下に
        // 上半分のRGBを設定します
        /// newBufferedImage.setRGB(0, 0, qrCodeImageWidth, fontsImageHeight,
        // imageArrayFonts, 0, qrCodeImageWidth);
        // 半分のRGBを設定する
        /// newBufferedImage.setRGB(0, fontsImageHeight, qrCodeImageWidth,
        // qrCodeImageHeight, imageArrayQr, 0, qrCodeImageWidth);

        String qrCodeFileOutputPath = param.getQrCodeFileOutputPath();
        File outFile = getFile(qrCodeFileOutputPath);
        boolean result = ImageIO.write(newBufferedImage, "jpg", outFile);
        return qrCodeFileOutputPath;

    }

    /**
     * QRコードのコンテンツ情報を特定する
     *
     * @param file QRコード画像ファイル
     *
     * @return QRコードの内容
     * @throws NotFoundException NotFoundException
     * @throws IOException       IOException
     * @date 2022/6/19 20:47
     */
    public static String decodeQrCode(File file) throws NotFoundException, IOException {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        String data = decodeQrCode(image);
        return data;
    }

    /**
     * QRコードのコンテンツ情報を特定する
     *
     * @param is QRコード画像ファイルストリーム
     *
     * @return QRコードの内容
     * @throws NotFoundException NotFoundException
     * @throws IOException       IOException
     * @date 2022/6/19 20:47
     */
    public static String decodeQrCode(InputStream is) throws NotFoundException, IOException {
        BufferedImage image;
        image = ImageIO.read(is);
        if (image == null) {
            return null;
        }
        String data = decodeQrCode(image);
        return data;
    }

    /// --------------------------------------------------------以下はヘルパーメソッドとヘルパークラスです

    /**
     * ファイルを取得します（必要に応じて、フォルダーを作成します）
     *
     * @param filePath path
     * @return ファイルオブジェクト
     * @date 2019/9/10 10:48
     */
    private static File getFile(String filePath) {
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            boolean result = file.getParentFile().mkdirs();
        }
        return file;
    }

    /**
     * QRコードのコンテンツ情報を特定する
     *
     * @param image QRコード画像情報BufferedImage
     *
     * @return QRコードの内容
     * @throws NotFoundException NotFoundException
     * @date 2022/6/19 20:47
     */
    private static String decodeQrCode(BufferedImage image) throws NotFoundException {
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        HashMap<DecodeHintType, Object> hints = new HashMap<>(4);
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        result = new MultiFormatReader().decode(bitmap, hints);
        return result.getText();
    }

    /**
     * QRコード画像情報を生成するBufferedImage
     * <p>
     * 注：BufferedImageはQRコード画像のデータコンテナであり、QRコード画像をさらに生成するために使用できます。
     *
     * @param content QRコードデータコンテンツ
     * @param width   QRコード幅(px)
     * @param height  QRコードの高さ(px)
     *
     * @return QRコード画像情報BufferedImage
     * @throws WriterException WriterException
     * @date 2022/6/19 20:47
     */
    private static BufferedImage genBufferedImage(String content, int width, int height) throws WriterException {
        Map<EncodeHintType, Object> hints = getHints();
        // マトリックスを生成します（つまり、2次元配列を生成します）
        BitMatrix matrix;
        matrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        int[] pixels = new int[width * height];
        for (int y = 0; y < matrix.getHeight(); y++) {
            for (int x = 0; x < matrix.getWidth(); x++) {
                // QRコードの色を制御します。前面はQRコードの色、背面は背景色です。
                pixels[y * width + x] = matrix.get(x, y) ? qrCodeColor : qrCodeBackgroundColor;
            }
        }
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.getRaster().setDataElements(0, 0, width, height, pixels);
        return image;
    }

    /**
     * （埋め込み画像を使用して）QRコード画像情報を生成するBufferedImage
     * <p>
     * 注：BufferedImageはQRコード画像のデータコンテナであり、QRコード画像をさらに生成するために使用できます。
     *
     * @param content           QRコードデータコンテンツ
     * @param width             QRコードの幅（px）
     * @param height            QRコードの高さ（px）
     * @param EmbeddedImagePath QRコードにネストされるイメージパス
     *
     * @return QRコード画像情報 BufferedImage
     * @throws IOException     IOException
     * @throws WriterException WriterException
     * @date 2022/6/19 20:47
     */
    private static BufferedImage genBufferedImageWithEmbeddedImg(String content, int width, int height,
            String embeddedImagePath) throws WriterException, IOException {
        // ネストする画像を読み取り、指定した幅と高さに圧縮します
        BufferedImage scaleImage = scale(embeddedImagePath, embeddedImgDefaultWidth, embeddedImgDefaultHeight, false);
        int embeddedImgFinalWidth = scaleImage.getWidth();
        int embeddedImgFinalHalfWidth = embeddedImgFinalWidth / 2;
        int embeddedImgFinalHeight = scaleImage.getHeight();
        int embeddedImgFinalHalfHeight = embeddedImgFinalHeight / 2;
        int[][] srcPixels = new int[embeddedImgFinalWidth][embeddedImgFinalHeight];
        for (int i = 0; i < embeddedImgFinalWidth; i++) {
            for (int j = 0; j < embeddedImgFinalHeight; j++) {
                srcPixels[i][j] = scaleImage.getRGB(i, j);
            }
        }
        Map<EncodeHintType, Object> hint = getHints();
        // マトリックスを生成します（つまり、2次元配列を生成します）（注：以下はこのマトリックスを描画します）
        BitMatrix matrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hint);
        // QRコードマトリックスを1Dピクセルアレイに変換する
        int qrCodeHalfWidth = matrix.getWidth() / 2;
        int qrCodeHalfHeight = matrix.getHeight() / 2;
        int[] pixels = new int[width * height];
        // [埋め込み画像とQRコード画像の間のフレーム]にあるかどうか
        boolean atEmbeddedImgFrameArea;
        // [埋め込まれた画像の場所]にあるかどうか
        boolean atEmbeddedImgArea;
        // QRコード画像に埋め込まれた画像（左下隅）の座標X
        int embeddedImgCoordinatesX = qrCodeHalfWidth - embeddedImgFinalHalfWidth;
        // QRコード画像に埋め込まれた画像（左下隅）の座標y
        int embeddedImgCoordinatesY = qrCodeHalfHeight - embeddedImgFinalHalfHeight;
        for (int y = 0; y < matrix.getHeight(); y++) {
            for (int x = 0; x < matrix.getWidth(); x++) {

                atEmbeddedImgArea = x > qrCodeHalfWidth - embeddedImgFinalHalfWidth
                        && x < qrCodeHalfWidth + embeddedImgFinalHalfWidth
                        && y > qrCodeHalfHeight - embeddedImgFinalHalfHeight
                        && y < qrCodeHalfHeight + embeddedImgFinalHalfHeight;

                atEmbeddedImgFrameArea = (x > qrCodeHalfWidth - embeddedImgFinalHalfWidth - frameWidth
                        && x < qrCodeHalfWidth - embeddedImgFinalHalfWidth + frameWidth
                        && y > qrCodeHalfHeight - embeddedImgFinalHalfHeight - frameWidth
                        && y < qrCodeHalfHeight + embeddedImgFinalHalfHeight + frameWidth)

                        ||

                        (x > qrCodeHalfWidth + embeddedImgFinalHalfWidth - frameWidth
                                && x < qrCodeHalfWidth + embeddedImgFinalHalfWidth + frameWidth
                                && y > qrCodeHalfHeight - embeddedImgFinalHalfHeight - frameWidth
                                && y < qrCodeHalfHeight + embeddedImgFinalHalfHeight + frameWidth)

                        ||

                        (x > qrCodeHalfWidth - embeddedImgFinalHalfWidth - frameWidth
                                && x < qrCodeHalfWidth + embeddedImgFinalHalfWidth + frameWidth
                                && y > qrCodeHalfHeight - embeddedImgFinalHalfHeight - frameWidth
                                && y < qrCodeHalfHeight - embeddedImgFinalHalfHeight + frameWidth)

                        ||

                        (x > qrCodeHalfWidth - embeddedImgFinalHalfWidth - frameWidth
                                && x < qrCodeHalfWidth + embeddedImgFinalHalfWidth + frameWidth
                                && y > qrCodeHalfHeight + embeddedImgFinalHalfHeight - frameWidth
                                && y < qrCodeHalfHeight + embeddedImgFinalHalfHeight + frameWidth);

                // 埋め込まれた画像のピクセル配列をQRコードマトリックス番号ピクセルグループに埋め込む
                if (atEmbeddedImgArea) {
                    pixels[y * width + x] = srcPixels[x - embeddedImgCoordinatesX][y - embeddedImgCoordinatesY];
                }
                // 埋め込み画像とQRコード画像の間のボックス（色を設定）
                else if (atEmbeddedImgFrameArea) {
                    pixels[y * width + x] = frameWidthColor;
                    // QRコード画像エリア
                } else {
                    // QRコードの色を制御します。前面はQRコードの色、背面は背景色です。
                    pixels[y * width + x] = matrix.get(x, y) ? qrCodeColor : qrCodeBackgroundColor;
                }
            }
        }
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.getRaster().setDataElements(0, 0, width, height, pixels);
        return image;
    }

    /**
     * ヒント情報を取得する
     *
     * @return hint
     * @date 2022/6/19 20:47
     */
    private static Map<EncodeHintType, Object> getHints() {
        Map<EncodeHintType, Object> hint = new HashMap<>(8);
        hint.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // QRコード全体の白いフレーム
        hint.put(EncodeHintType.MARGIN, 1);
        return hint;
    }

    /**
     * 入力する元の画像を高さ（または幅）だけ減らして、要件を満たすアイコンのBufferedImage情報を生成します
     * <p>
     * 注：画像の元のサイズが指定された幅と高さよりも小さい場合、画像は拡大されませんが、元のサイズは維持されます。
     *
     * @param srcImageFile     ソースファイルアドレス
     * @param width（縮小）width
     * @param height（縮小）height
     * @param autoFill         指定された幅と高さに完全に達するように縮小された画像を塗りつぶすかどうか。
     *                         <p>
     *                         注：対応する幅（または高さ）に対する最長辺の比率に従って画像を縮小した後、最長辺の縮小サイズは、指定された幅（または高さ）と同じになります。
     *                         ただし、その高さ（または幅）は、短縮された最短辺と必ずしも同じではありません。この時点で、指定された色で塗りつぶすことを検討できます。お気に入り：
     *                         ※埋め込みたい画像の実際の幅は80、高さは100ですが、ここでは縮小サイズを幅10、高さ10と指定しています。
     *                         次の論理からわかります。縮小率は0.1、縮小後は画像の幅が8、高さが8になります。
     *                         は10になります。この時点で、高さは要件を満たしていますが、幅は2ピクセル悪くなっています。これで、充填する準備が整いました。
     *                         <p>
     *                         注：falseをお勧めします。
     *
     * @return 画像のBufferedImage情報
     * @throws IOException IOException
     * @date 2022/6/19 20:47
     */
    private static BufferedImage scale(String srcImageFile, int width, int height, boolean autoFill)
            throws IOException {
        // スケールダウン
        double ratio;
        File file = getFile(srcImageFile);
        BufferedImage srcImage = ImageIO.read(file);
        Image destImage = srcImage.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
        // 元の画像の高さまたは幅が指定された高さまたは幅よりも大きい場合（最も長い辺でスケーリング比を計算し、スケーリングします）
        if ((srcImage.getHeight() > height) || (srcImage.getWidth() > width)) {
            if (srcImage.getHeight() > srcImage.getWidth()) {
                ratio = Double.valueOf(height) / srcImage.getHeight();
            } else {
                ratio = Double.valueOf(width) / srcImage.getWidth();
            }
            AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
            destImage = op.filter(srcImage, null);
        }
        if (autoFill) {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphic = image.createGraphics();
            graphic.setColor(Color.red);
            graphic.fillRect(0, 0, width, height);
            if (width == destImage.getWidth(null)) {
                graphic.drawImage(destImage, 0, (height - destImage.getHeight(null)) / 2, destImage.getWidth(null),
                        destImage.getHeight(null), Color.white, null);
            } else {
                graphic.drawImage(destImage, (width - destImage.getWidth(null)) / 2, 0, destImage.getWidth(null),
                        destImage.getHeight(null), Color.white, null);
            }
            graphic.dispose();
            destImage = image;
        }
        return (BufferedImage) destImage;
    }

    /**
     * 文字列の行を複数の行に分割します
     */
    private static String[] splitStrLines(String str, int len) {
        int blocks = str.length() / len + 1;
        String[] strArray = new String[blocks];
        for (int i = 0; i < blocks; i++) {
            if ((i + 1) * len > str.length()) {
                strArray[i] = str.substring(i * len);
            } else {
                strArray[i] = str.substring(i * len, (i + 1) * len);
            }
        }
        return strArray;
    }

    /**
     * Writes a {@link BitMatrix} to {@link BufferedImage}, file or stream. Provided
     * here instead of core since it depends on Java SE libraries.
     */
    private static class MatrixToImageWriter {

        private static final MatrixToImageConfig DEFAULT_CONFIG = new MatrixToImageConfig();

        private MatrixToImageWriter() {
        }

        /**
         * Renders a {@link BitMatrix} as an image, where "false" bits are rendered as
         * white, and "true" bits are rendered as black.
         */
        private static BufferedImage toBufferedImage(BitMatrix matrix) {
            return toBufferedImage(matrix, DEFAULT_CONFIG);
        }

        /**
         * As {@link #toBufferedImage(BitMatrix)}, but allows customization of the
         * output.
         */
        private static BufferedImage toBufferedImage(BitMatrix matrix, MatrixToImageConfig config) {
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            BufferedImage image = new BufferedImage(width, height, config.getBufferedImageColorModel());
            int onColor = config.getPixelOnColor();
            int offColor = config.getPixelOffColor();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, matrix.get(x, y) ? onColor : offColor);
                }
            }
            return image;
        }

        /**
         * @deprecated use {@link #writeToPath(BitMatrix, String, Path)}
         */
        @Deprecated
        private static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
            writeToPath(matrix, format, file.toPath());
        }

        /**
         * Writes a {@link BitMatrix} to a file.
         *
         * @see #toBufferedImage(BitMatrix)
         */
        private static void writeToPath(BitMatrix matrix, String format, Path file) throws IOException {
            writeToPath(matrix, format, file, DEFAULT_CONFIG);
        }

        /**
         * @deprecated use
         *             {@link #writeToPath(BitMatrix, String, Path, MatrixToImageConfig)}
         */
        @Deprecated
        private static void writeToFile(BitMatrix matrix, String format, File file, MatrixToImageConfig config)
                throws IOException {
            writeToPath(matrix, format, file.toPath(), config);
        }

        /**
         * As {@link #writeToFile(BitMatrix, String, File)}, but allows customization of
         * the output.
         */
        @SuppressWarnings("all")
        private static void writeToPath(BitMatrix matrix, String format, Path file, MatrixToImageConfig config)
                throws IOException {
            BufferedImage image = toBufferedImage(matrix, config);
            if (!ImageIO.write(image, format, file.toFile())) {
                throw new IOException("Could not write an image of format " + format + " to " + file);
            }
        }

        /**
         * Writes a {@link BitMatrix} to a stream.
         *
         * @see #toBufferedImage(BitMatrix)
         */
        private static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
            writeToStream(matrix, format, stream, DEFAULT_CONFIG);
        }

        /**
         * As {@link #writeToStream(BitMatrix, String, OutputStream)}, but allows
         * customization of the output.
         */
        private static void writeToStream(BitMatrix matrix, String format, OutputStream stream,
                MatrixToImageConfig config) throws IOException {
            BufferedImage image = toBufferedImage(matrix, config);
            if (!ImageIO.write(image, format, stream)) {
                throw new IOException("Could not write an image of format 【" + format + "】");
            }
        }

        /**
         * MatrixToImageConfig
         * <p>
         * Encapsulates custom configuration used in methods of
         * {@link MatrixToImageWriter}.
         */
        private static class MatrixToImageConfig {

            private static final int BLACK = Color.BLACK.getRGB();

            private static final int WHITE = Color.WHITE.getRGB();

            private final int onColor;

            private final int offColor;

            /**
             * Creates a default config with on color {@link #BLACK} and off color
             * {@link #WHITE}, generating normal black-on-white barcodes.
             */
            private MatrixToImageConfig() {
                this(BLACK, WHITE);
            }

            /**
             * @param onColor  pixel on color, specified as an ARGB value as an int
             * @param offColor pixel off color, specified as an ARGB value as an int
             */
            private MatrixToImageConfig(int onColor, int offColor) {
                this.onColor = onColor;
                this.offColor = offColor;
            }

            private int getPixelOnColor() {
                return onColor;
            }

            private int getPixelOffColor() {
                return offColor;
            }

            private int getBufferedImageColorModel() {
                // Use faster BINARY if colors match default
                return onColor == BLACK && offColor == WHITE ? BufferedImage.TYPE_BYTE_BINARY
                        : BufferedImage.TYPE_INT_RGB;
            }
        }
    }

}