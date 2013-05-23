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

#import "CWChunk.h"

@implementation CWChunk

+ (int)sizeBytes
{
    return 2;
}

+ (int)sizeMask
{
    return 0xFFFF;
}

+ (int)maxSize
{
    return 0xFFFF;
}

@synthesize payload;

- (id)init
{
    return [self initWithPayload:[NSData dataWithBytes:nil length:0]];
}

// designated
- (id)initWithPayload:(NSData *)inPayload
{
    if (inPayload == nil)
    {
        [NSException raise:@"invalid nil" format:@"inPayload is nil"];
    }
    
    if (self = [super init])
    {
        payload = inPayload;
    }
    
    return self;
}

- (NSData *)toData
{
    unsigned short size = payload.length;
    char *sizeBytes = (char *) &size;
    char bytes[2];
    bytes[0] = sizeBytes[1];
    bytes[1] = sizeBytes[0];
    NSMutableData *data = [NSMutableData dataWithCapacity:payload.length + [CWChunk sizeBytes]];
    [data appendBytes:bytes length:2];
    [data appendData:payload];
    return [data copy];
}

+ (unsigned short)parseSize:(NSData *)chunk
{
    if (chunk == nil)
    {
        [NSException raise:@"invalid nil" format:@"chunk is nil"];
    }
    
    if (chunk.length < [CWChunk sizeBytes])
    {
        [NSException raise:@"invalid length" format:@"min length = %d :: chunk.length = %d", [CWChunk sizeBytes], (int) chunk.length];
    }
    
    char value[2];
    char bytes[2];
    [chunk getBytes:bytes length:2];
    value[0] = bytes[1];
    value[1] = bytes[0];
    return ((unsigned short *) value)[0];
}

@end
