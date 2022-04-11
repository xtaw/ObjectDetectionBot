package xtaw.objectdetectionbot.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import ai.djl.Device;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.DetectedObjects.DetectedObject;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.modality.cv.translator.YoloV5Translator;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import xtaw.objectdetectionbot.config.Config;
import xtaw.objectdetectionbot.main.MainPlugin;

public class YoloV5Util {

	public static final File DIR_PATH = MainPlugin.INSTANCE.resolveDataFile("yolov5");
	public static final File MODEL_TORCHSCRIPT = new File(YoloV5Util.DIR_PATH, "model.torchscript");
	public static final File COCO_NAMES = new File(YoloV5Util.DIR_PATH, "coco.names");

	public static final Translator<Image, DetectedObjects> TRANSLATOR = YoloV5Translator.builder()
			.optSynsetArtifactName("coco.names")
			.build();

	private YoloV5Util() {
	}

	public static long detect(BufferedImage image) {
		if (!YoloV5Util.MODEL_TORCHSCRIPT.exists() || !YoloV5Util.COCO_NAMES.exists()) {
			return -1L;
		}
		Thread.currentThread()
				.setContextClassLoader(MainPlugin.class.getClassLoader());
		Criteria<Image, DetectedObjects> criteria = Criteria.builder()
				.setTypes(Image.class, DetectedObjects.class)
				.optDevice(Config.INSTANCE.useGpu ? Device.gpu() : Device.cpu())
				.optModelPath(YoloV5Util.DIR_PATH.toPath())
				.optModelName("model.torchscript")
				.optTranslator(YoloV5Util.TRANSLATOR)
				.optEngine("PyTorch")
				.build();
		BufferedImage letterBox = null;
		try (ZooModel<Image, DetectedObjects> model = ModelZoo.loadModel(criteria)) {
			int width = image.getWidth(null);
			int height = image.getHeight(null);
			double scale = Math.max(width, height) / 640D;
			letterBox = new BufferedImage(640, 640, BufferedImage.TYPE_3BYTE_BGR);
			Graphics g = letterBox.createGraphics();
			g.drawImage(image, 0, 0, (int) (width / scale), (int) (height / scale), null);
			long startTime = System.currentTimeMillis();
			try (Predictor<Image, DetectedObjects> predictor = model.newPredictor()) {
				DetectedObjects results = predictor.predict(ImageFactory.getInstance()
						.fromImage(letterBox));
				for (DetectedObject obj : results.<DetectedObject>items()) {
					BoundingBox bbox = obj.getBoundingBox();
					Rectangle rectangle = bbox.getBounds();
					String showText = obj.getClassName() + ": " + (int) (obj.getProbability() * 100D) + "%";
					Graphics g2 = image.createGraphics();
					g2.setColor(new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255)));
					g2.drawRect((int) (rectangle.getX() * scale), (int) (rectangle.getY() * scale), (int) (rectangle.getWidth() * scale), (int) (rectangle.getHeight() * scale));
					g2.drawString(showText, (int) (rectangle.getX() * scale), (int) (rectangle.getY() * scale - 5));
				}
			}
			letterBox = null;
			return System.currentTimeMillis() - startTime;
		} catch (RuntimeException | ModelException | TranslateException | IOException e) {
			e.printStackTrace();
		}
		letterBox = null;
		return -1L;
	}

}
