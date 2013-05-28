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

#import "ChumpChunkReader.h"

@implementation ChumpChunkReader

- (id)init
{
    NO_IMPLEMENTATION;
}

// designated
- (id)initWithInput:(NSInputStream *)inInput
{
    RAISE_IF_NIL(input);
    
    if (self = [super init])
    {
        input = inInput;
        state = SIZE_NEXT;
        dataReader = nil;
    }
    
    return self;
}

- (void)close
{
    [input close];
}

- (ChumpChunk *)read
{
    NSData *data;
    uint8_t sizeBuffer[2];
    
    while (TRUE)
    {
        switch (state) {
            case SIZE_NEXT:
                if (dataReader == nil)
                {
                    dataReader = [CWDataReader readerWithInput:input num:2];
                }
                
                data = [dataReader read];
                
                if (data != nil)
                {
                    [data getBytes:sizeBuffer];
                    ((uint8_t *) &chunkSize)[0] = sizeBuffer[1];
                    ((uint8_t *) &chunkSize)[1] = sizeBuffer[0];
                    
                    if (chunkSize == 0)
                    {
                        return [ChumpChunk chunkWithPayload:nil];
                    }
                    else
                    {
                        state = CHUNK_NEXT;
                        dataReader = nil;
                    }
                }
                else
                {
                    return nil;
                }
                
                break;
            case CHUNK_NEXT:
                if (dataReader == nil)
                {
                    dataReader = [CWDataReader readerWithInput:input num:chunkSize];
                }
                
                data = [dataReader read];
                
                if (data != nil)
                {
                    state = SIZE_NEXT;
                    dataReader = nil;
                    return [ChumpChunk chunkWithPayload:data];
                }
                else
                {
                    return nil;
                }
                
                break;
        }
    }
}

@end
