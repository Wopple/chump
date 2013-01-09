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
