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
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class MessageReader implements Closeable
{
  public static class Message
  {
    public final Header header;
    public final Chunk chunk;

    public Message(Header header, Chunk chunk)
    {
      this.header = header;
      this.chunk = chunk;
    }
  }

  private enum State
  {
    HEADER_NEXT,
    CHUNK_NEXT;
  }

  private final InputStream input;
  private final boolean isBlocking;
  private final HeaderReader headerReader;
  private final ChunkReader chunkReader;

  private State state = State.HEADER_NEXT;
  private Header header;
  private Chunk chunk;

  public MessageReader(InputStream input, boolean isBlocking)
  {
    this.input = input;
    this.isBlocking = isBlocking;
    headerReader = new HeaderReader(input, isBlocking);
    chunkReader = new ChunkReader(input, isBlocking);
  }

  @Override
  public void close() throws IOException
  {
    input.close();
  }

  public Message read() throws IOException, EOFException
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

  private Message readBlocking() throws IOException
  {
    header = headerReader.read();
    chunk = chunkReader.read();
    return new Message(header, chunk);
  }

  private Message readNonblocking() throws IOException
  {
    boolean done = false;

    while (!done)
    {
      switch (state)
      {
      case HEADER_NEXT:
        header = headerReader.read();

        if (header != null)
        {
          state = State.CHUNK_NEXT;
        }
        else
        {
          done = true;
        }

        break;
      case CHUNK_NEXT:
        chunk = chunkReader.read();

        if (chunk != null)
        {
          state = State.HEADER_NEXT;
          return new Message(header, chunk);
        }
        else
        {
          done = true;
        }

        break;
      }
    }

    return null;
  }
}
