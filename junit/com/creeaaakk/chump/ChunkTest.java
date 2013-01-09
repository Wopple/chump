package com.creeaaakk.chump;

import java.nio.ByteBuffer;
import java.util.Arrays;

import junit.framework.TestCase;

import org.junit.Test;

public class ChunkTest extends TestCase
{
  @Test
  public void test()
  {
    try
    {
      assertNull(new Chunk(null));
    }
    catch (IllegalArgumentException exception)
    {
    }

    try
    {
      assertNull(new Chunk(new byte[Chunk.MAX_SIZE + 1]));
    }
    catch (IllegalArgumentException exception)
    {
    }

    byte[] bytes = new byte[Chunk.MAX_SIZE];
    Chunk chunk = new Chunk(bytes);
    assertEquals(Chunk.MAX_SIZE, chunk.size);
    assertEquals(bytes, chunk.chunk);

    byte[] messageBytes = new byte[Chunk.SIZE_BYTES + Chunk.MAX_SIZE];
    ByteBuffer.wrap(messageBytes).putShort((short) Chunk.MAX_SIZE);
    assertTrue(Arrays.equals(messageBytes, chunk.toBytes()));

    try
    {
      assertNull(Chunk.parseSize(null));
    }
    catch (IllegalArgumentException exception)
    {
    }

    try
    {
      assertNull(Chunk.parseSize(new byte[Chunk.SIZE_BYTES - 1]));
    }
    catch (IllegalArgumentException exception)
    {
    }

    assertEquals(Chunk.MAX_SIZE, Chunk.parseSize(messageBytes));
  }
}
