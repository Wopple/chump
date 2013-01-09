package com.creeaaakk.chump;

import java.nio.ByteBuffer;

public class Chunk
{
  public static final int SIZE_BYTES = Short.SIZE / 8;
  public static final int SIZE_MASK = 0xFFFF;
  public static final int MAX_SIZE = 0xFFFF;

  public final int size;
  public final byte[] chunk;

  public Chunk(byte[] chunk)
  {
    if (chunk == null)
    {
      throw new IllegalArgumentException("chunk is null");
    }
    else if (chunk.length > MAX_SIZE)
    {
      throw new IllegalArgumentException("chunk length > " + MAX_SIZE + ": " + chunk.length);
    }

    this.size = chunk.length;
    this.chunk = chunk;
  }

  /**
   * Formats the Chunk for a message.
   */
  public byte[] toBytes()
  {
    byte[] bytes = new byte[SIZE_BYTES + size];
    ByteBuffer.wrap(bytes).putShort((short) size).put(chunk);
    return bytes;
  }

  /**
   * Helper function to get the size of a chunk formatted as bytes for a message.
   * @param bytes chunk formatted for a message
   * @return value of the size of the chunk
   */
  public static int parseSize(byte[] bytes)
  {
    if (bytes == null)
    {
      throw new IllegalArgumentException("bytes are null");
    }
    else if (bytes.length < SIZE_BYTES)
    {
      throw new IllegalArgumentException("bytes length < " + SIZE_BYTES + ": " + bytes.length);
    }

    return ByteBuffer.wrap(bytes).getShort() & SIZE_MASK;
  }
}
