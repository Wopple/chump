/*
 * Copyright (c) 2013, Creeaaakk Ware
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Creeaaakk Ware nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.creeaaakk.chump;

import java.util.Arrays;

import junit.framework.TestCase;

import org.junit.Test;

public class MessageBuilderTest extends TestCase
{
  @Test
  public void test()
  {
    Message message = null;

    try
    {
      // missing payload, should throw Error
      message = new MessageBuilder().setVersion((short) 0).setMessageType((short) 0).setTag((short) 0).build();
    }
    catch (Error error)
    {
    }

    assertNull(message);

    try
    {
      // missing tag, should throw Error
      message = new MessageBuilder().setVersion((short) 0).setMessageType((short) 0).setPayload(new byte[0]).build();
    }
    catch (Error error)
    {
    }

    assertNull(message);

    try
    {
      // missing messsage type, should throw Error
      message = new MessageBuilder().setVersion((short) 0).setTag((short) 0).setPayload(new byte[0]).build();
    }
    catch (Error error)
    {
    }

    assertNull(message);

    try
    {
      // missing version, should throw Error
      message = new MessageBuilder().setMessageType((short) 0).setTag((short) 0).setPayload(new byte[0]).build();
    }
    catch (Error error)
    {
    }

    assertNull(message);

    try
    {
      message = new MessageBuilder().setVersion((short) 0).setMessageType((short) 1).setTag((short) 2).setPayload(new byte[] { 4, 5, 6 }).build();
    }
    catch (Throwable throwable)
    {
      throwable.printStackTrace();
    }

    assertNotNull(message);
    assertEquals((short) 0, message.header.version);
    assertEquals((short) 1, message.header.messageType);
    assertEquals((short) 2, message.header.tag);
    assertEquals(3, message.chunk.size);
    assertTrue(Arrays.equals(new byte[] { 4, 5, 6 }, message.chunk.payload));
  }
}
