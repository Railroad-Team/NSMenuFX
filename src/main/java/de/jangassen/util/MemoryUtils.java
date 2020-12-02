package de.jangassen.util;

import com.sun.jna.Memory;

public final class MemoryUtils {
  private MemoryUtils() {
  }

  public static Memory toMemory(byte[] data) {
    Memory memory = new Memory(data.length);
    memory.write(0, data, 0, data.length);
    return memory;
  }
}
