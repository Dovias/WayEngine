package lt.wayout.minecraft.plugin.wayengine.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.annotation.Nullable;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonUtils {
	private static final Gson defaultGson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
	private JsonUtils(){}

	public static boolean saveObject(Path path, final Object object, Gson gson){
		try {
			if(!Files.exists(path)){
				Files.createDirectories(path.getParent());
			}
			Writer writer = new FileWriter(path.toFile());
			gson.toJson(object, writer);
			writer.close();
		} catch (IOException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean saveObject(Path path, final Object object){
		return saveObject(path, object, JsonUtils.defaultGson);
	}

	// Note: Suppress cast as workaround, cast is always checked
	public static @Nullable
	<T> T loadObject(Path path, Type type, Class<T> clazz, boolean deleteIfNull, Gson gson) {
		T object = null;
		try {
			Reader reader = new FileReader(path.toFile());
			if (type != null) {
				object = gson.fromJson(reader, type);
			} else if (clazz != null) {
				object = gson.fromJson(reader, clazz);
			}
			reader.close();
			if (object == null && deleteIfNull) {
				Files.delete(path);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return object;
	}

	public static <T> T loadObject(Path path, Class<T> clazz) {
		return loadObject(path, null, clazz, false, JsonUtils.defaultGson);
	}

	public static <T> T loadObject(Path path, Class<T> clazz, boolean deleteIfNull) {
		return loadObject(path, null, clazz, deleteIfNull, JsonUtils.defaultGson);
	}

	public static <T> T loadObject(Path path, Class<T> clazz, Gson gson) {
		return loadObject(path, null, clazz, false, gson);
	}

	public static <T> T loadObject(Path path, Class<T> clazz, boolean deleteIfNull, Gson gson) {
		return loadObject(path, null, clazz, deleteIfNull, gson);
	}

	public static <T> T loadObject(Path path, Type type) {
		return loadObject(path, type, null, false, JsonUtils.defaultGson);
	}

	public static <T> T loadObject(Path path, Type type, boolean deleteIfNull) {
		return loadObject(path, type, null, deleteIfNull, JsonUtils.defaultGson);
	}

	public static <T> T loadObject(Path path, Type type, Gson gson) {
		return loadObject(path, type, null, false, gson);
	}

	public static <T> T loadObject(Path path, Type type, boolean deleteIfNull, Gson gson) {
		return loadObject(path, type, null, deleteIfNull, gson);
	}
}
