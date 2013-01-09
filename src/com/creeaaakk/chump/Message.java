package com.creeaaakk.chump;

import java.nio.ByteBuffer;

public class Message
{
  public final Header header;
  public final Chunk chunk;

  public Message(Header header, Chunk chunk)
  {
    if (header == null)
    {
      throw new IllegalArgumentException("header is null");
    }

    if (chunk == null)
    {
      throw new IllegalArgumentException("chunk is null");
    }

    this.header = header;
    this.chunk = chunk;
  }

  /**
   * @return byte[] formatted message
   */
  public byte[] toBytes()
  {
    byte[] headerBytes = header.toBytes();
    byte[] chunkBytes = chunk.toBytes();
    byte[] bytes = new byte[headerBytes.length + chunkBytes.length];
    ByteBuffer.wrap(bytes).put(headerBytes).put(chunkBytes);
    return bytes;
  }
}
