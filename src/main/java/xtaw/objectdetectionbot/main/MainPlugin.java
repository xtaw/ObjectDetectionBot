package xtaw.objectdetectionbot.main;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;
import xtaw.objectdetectionbot.command.CommandObjectDetection;
import xtaw.objectdetectionbot.config.Config;
import xtaw.objectdetectionbot.util.YoloV5Util;

public class MainPlugin extends JavaPlugin {

	public static final MainPlugin INSTANCE = new MainPlugin();

	private List<Listener<?>> registeredEvents = new ArrayList<>();

	private MainPlugin() {
<<<<<<< HEAD
		super(new JvmPluginDescriptionBuilder("xtaw.objectdetectionbot", "1.0.0").name("ObjectDetectionBot")
=======
		super(new JvmPluginDescriptionBuilder("xtaw.objectdetectionbot", "1.0.0").name("ObjectDetection")
>>>>>>> 4e10fda (new)
				.build());
	}

	@Override
	public void onLoad(PluginComponentStorage $this$onLoad) {
		if (!YoloV5Util.DIR_PATH.exists()) {
			YoloV5Util.DIR_PATH.mkdirs();
		}
		Config.INSTANCE.load();
	}

	@Override
	public void onEnable() {
		this.registerEvents();
		this.registerCommands();
	}

	@Override
	public void onDisable() {
		this.unregisterEvents();
		this.unregisterCommands();
	}

	public void registerEvents() {
		this.registeredEvents.add(GlobalEventChannel.INSTANCE.filter(e -> e instanceof GroupMessageEvent && Config.INSTANCE.bots.contains(((GroupMessageEvent) e).getBot()
				.getId()) && Config.INSTANCE.groups.contains(
						((GroupMessageEvent) e).getSubject()
								.getId()))
				.subscribeAlways(GroupMessageEvent.class, e -> {
					if (e.getMessage()
							.size() >= 2
							&& (e.getMessage()
									.get(1) instanceof At)
							&& ((At) e.getMessage()
									.get(1)).getTarget() == e.getBot()
											.getId()) {
						for (Message message : e.getMessage()) {
							if (message instanceof Image) {
								Image image = (Image) message;
								try {
									BufferedImage src = ImageIO.read(new URL(Image.Key.queryUrl(image)));
									long usedTime = YoloV5Util.detect(src);
									if (usedTime == -1L) {
										MainPlugin.this.getLogger()
												.error("Unable to run yolov5. Check if \"model.torchscript\" and \"coco.names\" exist and are valid.");
										src = null;
										break;
									}
									ByteArrayOutputStream baos = new ByteArrayOutputStream();
									ImageIO.write(src, "png", baos);
									ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
									Image imageMessage = ExternalResource.uploadAsImage(bais, e.getSubject());
									MessageChainBuilder builder = new MessageChainBuilder();
									String format = Config.INSTANCE.format.trim();
									while (format.startsWith("%IMAGE%")) {
										builder.append(imageMessage);
										format = format.substring(7)
												.trim();
									}
									String[] imagePieces = format.split("%IMAGE%");
									for (int i = 0; i < imagePieces.length; i++) {
										String imagePiece = imagePieces[i].trim();
										while (imagePiece.startsWith("%AT%")) {
											builder.append(new At(e.getSender()
													.getId()));
											imagePiece = imagePiece.substring(4)
													.trim();
										}
										String[] atPieces = imagePiece.split("%AT%");
										for (int i2 = 0; i2 < atPieces.length; i2++) {
											builder.append(atPieces[i2].replace("%USED_TIME%", String.valueOf(usedTime)));
											if (i + 1 < atPieces.length) {
												builder.append(new At(e.getSender()
														.getId()));
											}
										}
										while (imagePiece.endsWith("%AT%")) {
											builder.append(new At(e.getSender()
													.getId()));
											imagePiece = imagePiece.substring(0, imagePiece.length() - 4)
													.trim();
										}
										if (i + 1 < imagePieces.length) {
											builder.append(imageMessage);
										}
									}
									while (format.endsWith("%IMAGE%")) {
										builder.append(imageMessage);
										format = format.substring(0, format.length() - 7)
												.trim();
									}
									e.getSubject()
											.sendMessage(builder.build());
									src = null;
									baos = null;
									bais = null;
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
						}
					}
				}));
	}

	public void registerCommands() {
		CommandManager.INSTANCE.registerCommand(CommandObjectDetection.INSTANCE, true);
	}

	public void unregisterEvents() {
		for (Listener<?> listener : this.registeredEvents) {
			listener.complete();
		}
		this.registeredEvents.clear();
	}

	public void unregisterCommands() {
		CommandManager.INSTANCE.unregisterCommand(CommandObjectDetection.INSTANCE);
	}

}
