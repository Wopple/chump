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

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import junit.framework.TestCase;

import org.junit.Test;

public class HeaderReaderTest extends TestCase
{
  @Test
  public void testBlocking() throws IOException, InterruptedException
  {
    PipedInputStream input = new PipedInputStream();
    PipedOutputStream output = new PipedOutputStream(input);
    final HeaderReader reader = new HeaderReader(input, true);

    output.write(new byte[]
      { 0, 0,
        0, 1,
        0, 2,

        0, 3,
        0, 4,
        0, 5,

        0, 6,
        0, 7 });

    Header header = reader.read();
    assertEquals((short) 0, header.version);
    assertEquals((short) 1, header.messageType);
    assertEquals((short) 2, header.tag);

    header = reader.read();
    assertEquals((short) 3, header.version);
    assertEquals((short) 4, header.messageType);
    assertEquals((short) 5, header.tag);

    final Thread tests = new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          reader.read();
          assertTrue(false);
        }
        catch (Exception exception)
        {
          assertTrue(false);
        }
      }
    });

    tests.start();
    tests.join(50);
    tests.interrupt();
  }

  @Test
  public void testNonblocking() throws IOException
  {
    PipedInputStream input = new PipedInputStream();
    PipedOutputStream output = new PipedOutputStream(input);
    HeaderReader reader = new HeaderReader(input, false);

    Header header = reader.read();
    assertNull(header);

    output.write(new byte[] { 0, 0, 0, 1, 0 });

    header = reader.read();
    assertNull(header);

    output.write(new byte[] { 2 });

    header = reader.read();
    assertNotNull(header);
    assertEquals((short) 0, header.version);
    assertEquals((short) 1, header.messageType);
    assertEquals((short) 2, header.tag);

    output.write(new byte[]
      { 0, 3,
        0, 4,
        0, 5,
        6, 7, 8, 9 });

    header = reader.read();
    assertNotNull(header);
    assertEquals((short) 3, header.version);
    assertEquals((short) 4, header.messageType);
    assertEquals((short) 5, header.tag);
  }
}
