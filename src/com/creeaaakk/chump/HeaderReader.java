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
    short version = input.readShort();
    short messageType = input.readShort();
    short tag = input.readShort();
    return new Header(version, messageType, tag);
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
}
