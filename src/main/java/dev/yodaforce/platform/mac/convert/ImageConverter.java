package dev.yodaforce.platform.mac.convert;

import com.sun.jna.Memory;
import de.jangassen.jfa.appkit.NSData;
import de.jangassen.jfa.appkit.NSImage;
import de.jangassen.jfa.cleanup.NSCleaner;
import dev.yodaforce.util.MemoryUtils;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

public class ImageConverter {
  public static Optional<NSImage> convert(Node graphic) {
    if (graphic instanceof ImageView) {
      Image image = ((ImageView) graphic).getImage();
      if (image != null && image.getUrl() != null) {
        try (InputStream stream = new URL(image.getUrl()).openStream()) {
          NSImage nsImage = getNsImage(stream.readAllBytes());
          NSCleaner.register(graphic, nsImage);
          return Optional.of(nsImage);
        } catch (IOException ignored) {
        }
      }
    }
    return Optional.empty();
  }

  private static NSImage getNsImage(byte[] imageData) {
    Memory memory = MemoryUtils.toMemory(imageData);
    NSData data = NSData.alloc().initWithBytes(memory, (int) memory.size());
    return NSImage.alloc().initWithData(data);
  }
}
