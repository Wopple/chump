package com.creeaaakk.chump;

import junit.framework.TestCase;

import org.junit.Test;

public class HeaderTest extends TestCase
{
  @Test
  public void test()
  {
    Header header = new Header((short) 0, (short) 1);
    byte[] bytes = header.toBytes();
    assertEquals(Header.HEADER_BYTES, bytes.length);
    assertEquals(header.messageType, Header.parseMessageType(bytes));
    assertEquals(header.tag, Header.parseTag(bytes));
  }
}
