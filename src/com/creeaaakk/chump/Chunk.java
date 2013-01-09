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

public class Chunk
{
  public static final int SIZE_BYTES = Short.SIZE / 8;
  public static final int SIZE_MASK = 0xFFFF;
  public static final int MAX_SIZE = 0xFFFF;

  public final int size;
  public final byte[] payload;

  public Chunk(byte[] payload)
  {
    if (payload == null)
    {
      throw new IllegalArgumentException("payload is null");
    }
    else if (payload.length > MAX_SIZE)
    {
      throw new IllegalArgumentException("payload length > " + MAX_SIZE + ": " + payload.length);
    }

    this.size = payload.length;
    this.payload = payload;
  }

  /**
   * Formats the Chunk for a message.
   */
  public byte[] toBytes()
  {
    byte[] chunk = new byte[SIZE_BYTES + size];
    ByteBuffer.wrap(chunk).putShort((short) size).put(payload);
    return chunk;
  }

  /**
   * Helper function to get the size of a payload.
   * @param chunk byte[] representing a chunk
   * @return value of the size of the payload
   */
  public static int parseSize(byte[] chunk)
  {
    if (chunk == null)
    {
      throw new IllegalArgumentException("chunk are null");
    }
    else if (chunk.length < SIZE_BYTES)
    {
      throw new IllegalArgumentException("chunk length < " + SIZE_BYTES + ": " + chunk.length);
    }

    return ByteBuffer.wrap(chunk).getShort() & SIZE_MASK;
  }
}
