package xtaw.objectdetectionbot.util;

import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JsonUtil {

	public static final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting()
			.create();

	private JsonUtil() {
	}

	public static String toPrettyJson(JsonElement jsonElement) {
		return JsonUtil.PRETTY_GSON.toJson(jsonElement);
	}

	public static JsonElement parse(Reader reader) {
		return JsonParser.parseReader(reader);
	}

}
