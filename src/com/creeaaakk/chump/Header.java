package com.creeaaakk.chump;

import java.nio.ByteBuffer;

public class Header
{
  public static final int MESSAGE_TYPE_BYTES = Short.SIZE / 8;
  public static final int TAG_BYTES = Short.SIZE / 8;
  public static final int HEADER_BYTES = MESSAGE_TYPE_BYTES + TAG_BYTES;

  public final short messageType;
  public final short tag;

  public Header(short messageType)
  {
    this(messageType, (short) 0);
  }

  public Header(short messageType, short tag)
  {
    this.messageType = messageType;
    this.tag = tag;
  }

  /**
   * @return byte[] formatted for a message
   */
  public byte[] toBytes()
  {
    byte[] bytes = new byte[HEADER_BYTES];
    ByteBuffer.wrap(bytes).putShort(messageType).putShort(tag);
    return bytes;
  }

  public static short parseMessageType(byte[] bytes)
  {
    if (bytes == null)
    {
      throw new IllegalArgumentException("bytes are null");
    }
    else if (bytes.length < MESSAGE_TYPE_BYTES)
    {
      throw new IllegalArgumentException("bytes length < " + MESSAGE_TYPE_BYTES + ": " + bytes.length);
    }

    return ByteBuffer.wrap(bytes).getShort();
  }

  /**
   * Helper function to get the tag of a header formatted as bytes for a message.
   * @param bytes header formatted for a message
   * @return value of the tag of the header
   */
  public static short parseTag(byte[] bytes)
  {
    int minSize = MESSAGE_TYPE_BYTES + TAG_BYTES;

    if (bytes == null)
    {
      throw new IllegalArgumentException("bytes are null");
    }
    else if (bytes.length < minSize)
    {
      throw new IllegalArgumentException("bytes length < " + minSize + ": " + bytes.length);
    }

    return ByteBuffer.wrap(bytes).getShort(MESSAGE_TYPE_BYTES);
  }
}
