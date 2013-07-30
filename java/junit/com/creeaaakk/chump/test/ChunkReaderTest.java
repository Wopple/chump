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

package com.creeaaakk.chump.test;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Arrays;

import junit.framework.TestCase;

import org.junit.Test;

import com.creeaaakk.chump.Chunk;
import com.creeaaakk.chump.ChunkReader;

public class ChunkReaderTest extends TestCase
{
  private boolean blocked = true;

  @Test
  public void testBlocking() throws IOException, InterruptedException
  {
    PipedInputStream input = new PipedInputStream();
    PipedOutputStream output = new PipedOutputStream(input);
    final ChunkReader reader = new ChunkReader(input, true);

    try
    {
      output.write(new byte[]
        { 0, 0,
          0, 4,
          1, 2, 3, 5,
          0, 3,
          6, 7 });

      Chunk chunk = reader.read();
      assertEquals(0, chunk.size);
      assertTrue(Arrays.equals(new byte[0], chunk.payload));

      chunk = reader.read();
      assertEquals(4, chunk.size);
      assertTrue(Arrays.equals(new byte[] { 1, 2, 3, 5 }, chunk.payload));

      final Thread tests = new Thread(new Runnable()
      {
        @Override
        public void run()
        {
          try
          {
            reader.read();
            blocked = false;
          }
          catch (IOException exception)
          {
            exception.printStackTrace();
          }
        }
      });

      tests.start();
      tests.join(50);
      tests.interrupt();
      assertEquals(true, blocked);
    }
    finally
    {
      output.close();
      reader.close();
    }
  }

  @Test
  public void testNonblocking() throws IOException
  {
    PipedInputStream input = new PipedInputStream();
    PipedOutputStream output = new PipedOutputStream(input);
    ChunkReader reader = new ChunkReader(input, false);

    try
    {
      Chunk chunk = reader.read();
      assertNull(chunk);

      output.write(new byte[] { 0 });

      chunk = reader.read();
      assertNull(chunk);

      output.write(new byte[] { 0 });

      chunk = reader.read();
      assertNotNull(chunk);
      assertEquals(0, chunk.size);
      assertTrue(Arrays.equals(new byte[0], chunk.payload));

      output.write(new byte[]
        { 0, 4,
          1, 2, 3, 5,
          6, 7, 8, 9 });

      chunk = reader.read();
      assertNotNull(chunk);
      assertEquals(4, chunk.size);
      assertTrue(Arrays.equals(new byte[] { 1, 2, 3, 5 }, chunk.payload));
    }
    finally
    {
      output.close();
      reader.close();
    }
  }
}
