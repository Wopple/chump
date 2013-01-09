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
