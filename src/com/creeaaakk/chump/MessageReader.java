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
