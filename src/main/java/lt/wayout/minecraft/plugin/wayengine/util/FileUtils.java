package lt.wayout.minecraft.plugin.wayengine.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.*;

public class FileUtils {

    private FileUtils() {}

    public static boolean saveObject(@NotNull Path path, @NotNull final Object object) {
        byte[] serializedData = Serialization.serializeObject(object);
        if (serializedData == null) return false;
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
            }
            Files.write(path, serializedData);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Nullable
    public static <T> T loadObject(@NotNull final Path path, @NotNull final Class<T> clazz, boolean deleteFile) {
        try {
            byte[] bytes = Files.readAllBytes(path);
            Object object = Serialization.deserializeObject(bytes);
            if (object != null && object.getClass() == clazz) {
                return clazz.cast(object);
            }
            if (deleteFile) Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean deleteFile(@NotNull final Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
