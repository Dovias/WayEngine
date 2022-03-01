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

	public static @Nullable Object loadObject(Path path, Object classOrType, boolean deleteIfNull, Gson gson){
		Object object = null;
		try {
			Reader reader = new FileReader(path.toFile());
			if(classOrType instanceof Type type){ // class implements type
				object = gson.fromJson(reader, type);
			}
			reader.close();
			if(object == null && deleteIfNull){
				Files.delete(path);
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		return object;
	}

	public static Object loadObject(Path path, Class<?> clazz){
		return loadObject(path, (Object) clazz, false, JsonUtils.defaultGson);
	}

	public static Object loadObject(Path path, Class<?> clazz, boolean deleteIfNull){
		return loadObject(path, (Object) clazz, deleteIfNull, JsonUtils.defaultGson);
	}

	public static Object loadObject(Path path, Class<?> clazz, Gson gson){
		return loadObject(path, (Object) clazz, false, gson);
	}

	public static Object loadObject(Path path, Class<?> clazz, boolean deleteIfNull, Gson gson){
		return loadObject(path, (Object) clazz, deleteIfNull, gson);
	}

	public static Object loadObject(Path path, Type type){
		return loadObject(path, (Object) type, false, JsonUtils.defaultGson);
	}

	public static Object loadObject(Path path, Type type, boolean deleteIfNull){
		return loadObject(path, (Object) type, deleteIfNull, JsonUtils.defaultGson);
	}

	public static Object loadObject(Path path, Type type, Gson gson){
		return loadObject(path, (Object) type, false, gson);
	}

	public static Object loadObject(Path path, Type type, boolean deleteIfNull, Gson gson){
		return loadObject(path, (Object) type, deleteIfNull, gson);
	}
}
