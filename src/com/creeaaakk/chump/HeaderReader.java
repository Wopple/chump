package com.creeaaakk.chump;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class HeaderReader implements Closeable
{
  private final DataInputStream input;
  private final boolean isBlocking;

  public HeaderReader(InputStream input)
  {
    this(input, false);
  }

  public HeaderReader(InputStream input, boolean isBlocking)
  {
    if (input == null)
    {
      throw new RuntimeException("input is null");
    }

    this.input = new DataInputStream(input);
    this.isBlocking = isBlocking;
  }

  @Override
  public void close() throws IOException
  {
    input.close();
  }

  /**
   * @return complete header or null if more bytes are needed
   * @throws IOException
   * @throws EOFException
   */
  public Header read() throws IOException, EOFException
  {
    if (isBlocking)
    {
      return readBlocking();
    }
    else
    {
      return readNonblocking();
    }
  }

  private Header readBlocking() throws IOException, EOFException
  {
    short messageType = input.readShort();
    short tag = input.readShort();
    return new Header(messageType, tag);
  }

  private Header readNonblocking() throws IOException, EOFException
  {
    if (input.available() >= Header.HEADER_BYTES)
    {
      return readBlocking();
    }
    else
    {
      return null;
    }
  }

  public static short parseMessageType(byte[] message)
  {
    ByteBuffer buffer = ByteBuffer.wrap(message);
    return buffer.getShort(Short.SIZE / 8);
  }

  public static short parseTag(byte[] message)
  {
    ByteBuffer buffer = ByteBuffer.wrap(message);
    return buffer.getShort();
  }
}
