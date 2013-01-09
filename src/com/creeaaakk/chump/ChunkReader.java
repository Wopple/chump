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

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class ChunkReader implements Closeable
{
  private enum State
  {
    SIZE_NEXT,
    CHUNK_NEXT;
  }

  private final DataInputStream input;
  private final boolean isBlocking;

  private State state = State.SIZE_NEXT;
  private int chunkSize = 0;
  private Chunk chunk;

  public ChunkReader(InputStream input)
  {
    this(input, false);
  }

  public ChunkReader(InputStream input, boolean isBlocking)
  {
    if (input == null)
    {
      throw new IllegalArgumentException("input is null");
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
   * @return complete chunk, can return null when non-blocking if more bytes are needed
   * @throws IOException
   * @throws EOFException
   */
  public Chunk read() throws IOException, EOFException
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

  private Chunk readBlocking() throws IOException
  {
    int size = readSize();

    if (size == 0)
    {
      return new Chunk(new byte[0]);
    }

    return readChunk(size);
  }

  private Chunk readNonblocking() throws IOException
  {
    boolean done = false;

    while (!done)
    {
      switch (state)
      {
      case SIZE_NEXT:
        if (attemptToReadSize())
        {
          if (chunkSize == 0)
          {
            return new Chunk(new byte[0]);
          }
          else
          {
            state = State.CHUNK_NEXT;
          }
        }
        else
        {
          done = true;
        }

        break;
      case CHUNK_NEXT:
        if (attemptToReadChunk())
        {
          state = State.SIZE_NEXT;
          return chunk;
        }
        else
        {
          done = true;
        }

        break;
      default:
        throw new Error("unknown state detected: " + state);
      }
    }

    return null;
  }

  private boolean attemptToReadSize() throws IOException
  {
    if (input.available() >= Chunk.SIZE_BYTES)
    {
      chunkSize = readSize();
      return true;
    }
    else
    {
      return false;
    }
  }

  private boolean attemptToReadChunk() throws IOException
  {
    if (input.available() >= chunkSize)
    {
      chunk = readChunk(chunkSize);
      return true;
    }
    else
    {
      return false;
    }
  }

  private int readSize() throws IOException
  {
    return input.readUnsignedShort();
  }

  private Chunk readChunk(int size) throws IOException
  {
    byte[] bytes = new byte[size];
    int actual = input.read(bytes);

    if (actual != size)
    {
      throw new Error("unexpected actual size: " + actual + " expected: " + size);
    }

    return new Chunk(bytes);
  }
}
