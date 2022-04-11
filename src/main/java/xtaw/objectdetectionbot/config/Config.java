package xtaw.objectdetectionbot.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import xtaw.objectdetectionbot.main.MainPlugin;
import xtaw.objectdetectionbot.util.JsonUtil;

public class Config {

	public static final Config INSTANCE = new Config();

	public static final File CONFIG_FILE = MainPlugin.INSTANCE.resolveConfigFile("ObjectDetection.json");

	public List<Long> bots = new ArrayList<>();
	public List<Long> groups = new ArrayList<>();

	public String format = "%AT%\n%IMAGE%\nyolo v5 测试成功 请不要回复\n 用时: %USED_TIME%ms";

	public boolean useGpu = false;

	public void save() {
		try {
			if (!Config.CONFIG_FILE.exists()) {
				Config.CONFIG_FILE.createNewFile();
			}
			JsonObject jsonObject = new JsonObject();
			JsonArray jsonArrayBots = new JsonArray();
			JsonArray jsonArrayGroups = new JsonArray();
			Iterator<Long> iteratorBots = this.bots.iterator();
			while (iteratorBots.hasNext()) {
				long id = iteratorBots.next();
				jsonArrayBots.add(id);
			}
			Iterator<Long> iteratorGroups = this.groups.iterator();
			while (iteratorGroups.hasNext()) {
				long id = iteratorGroups.next();
				jsonArrayGroups.add(id);
			}
			jsonObject.add("Bots", jsonArrayBots);
			jsonObject.add("Groups", jsonArrayGroups);
			jsonObject.addProperty("Format", this.format);
			jsonObject.addProperty("UseGpu", this.useGpu);
			PrintWriter pw = new PrintWriter(Config.CONFIG_FILE);
			pw.println(JsonUtil.toPrettyJson(jsonObject));
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void load() {
		try {
			if (!Config.CONFIG_FILE.exists()) {
				Config.CONFIG_FILE.createNewFile();
				return;
			}
			JsonElement jsonElement = JsonUtil.parse(new BufferedReader(new FileReader(Config.CONFIG_FILE)));
			if (jsonElement instanceof JsonNull) {
				return;
			}
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			JsonArray jsonArrayBots = jsonObject.get("Bots")
					.getAsJsonArray();
			JsonArray jsonArrayGroups = jsonObject.get("Groups")
					.getAsJsonArray();
			this.bots.clear();
			this.groups.clear();
			Iterator<JsonElement> iteratorBots = jsonArrayBots.iterator();
			while (iteratorBots.hasNext()) {
				long id = iteratorBots.next()
						.getAsLong();
				this.bots.add(id);
			}
			Iterator<JsonElement> iteratorGroups = jsonArrayGroups.iterator();
			while (iteratorGroups.hasNext()) {
				long id = iteratorGroups.next()
						.getAsLong();
				this.groups.add(id);
			}
			this.format = jsonObject.get("Format")
					.getAsString();
			this.useGpu = jsonObject.get("UseGpu")
					.getAsBoolean();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
