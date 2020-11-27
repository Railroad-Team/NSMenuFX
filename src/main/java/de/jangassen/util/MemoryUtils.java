package de.jangassen.util;

import com.sun.jna.Memory;

import java.io.IOException;
import java.io.InputStream;

public class MemoryUtils {
  public static Memory toMemory(InputStream data) throws IOException {
    return toMemory(data.readAllBytes());
  }

  public static Memory toMemory(byte[] data) {
    Memory memory = new Memory(data.length);
    memory.write(0, data, 0, data.length);
    return memory;
  }

  public static Memory toMemory(int[] data) {
    Memory memory = new Memory(data.length);
    memory.write(0, data, 0, data.length);
    return memory;
  }
}
