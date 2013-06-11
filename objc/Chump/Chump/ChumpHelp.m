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

#import "ChumpHelp.h"

@implementation ChumpHelp

+ (short)flipShort:(short)value
{
    uint8_t *bytes = (uint8_t *) &value;
    uint8_t flipBytes[2];
    flipBytes[0] = bytes[1];
    flipBytes[1] = bytes[0];
    return ((short *) flipBytes)[0];
}

+ (NSData *)flipShortAsData:(short)value
{
    short bytes[1];
    bytes[0] = [ChumpHelp flipShort:value];
    return [NSData dataWithBytes:bytes length:2];
}

+ (short)parseNetworkShort:(NSData *)data range:(NSRange)range
{
    RAISE_IF_NIL(data);
    
    if (data.length < (range.location + range.length))
    {
        [NSException raise:@"invalid length" format:@"min length = %d :: data.length = %d", (int) (range.length + range.location), (int) data.length];
    }
    
    char value[2];
    char bytes[2];
    [data getBytes:bytes range:range];
    value[0] = bytes[1];
    value[1] = bytes[0];
    return ((short *) value)[0];
}

@end
