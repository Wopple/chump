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

#import "ChumpHeader.h"

int const HEADER_VERSION_BYTES = 2;
int const HEADER_MESSAGE_TYPE_BYTES = 2;
int const HEADER_TAG_BYTES = 2;
int const HEADER_TOTAL_BYTES = HEADER_VERSION_BYTES + HEADER_MESSAGE_TYPE_BYTES + HEADER_TAG_BYTES;

@implementation ChumpHeader

+ (id)headerWithVersion:(short)version messageType:(short)messageType tag:(short)tag
{
    return [[self alloc] initWithVersion:version messageType:messageType tag:tag];
}

- (id)init
{
    NO_IMPLEMENTATION;
}

// designated
- (id)initWithVersion:(short)inVersion messageType:(short)inMessageType tag:(short)inTag
{
    if (self = [super init])
    {
        version = inVersion;
        messageType = inMessageType;
        tag = inTag;
    }
    
    return self;
}

- (NSData *)toData
{
    NSMutableData *data = [NSMutableData dataWithCapacity:[self calcBytes]];
    [data appendData:[ChumpHelp flipShortAsData:self.version]];
    [data appendData:[ChumpHelp flipShortAsData:self.messageType]];
    [data appendData:[ChumpHelp flipShortAsData:self.tag]];
    return [data copy];
}

- (NSUInteger)calcBytes
{
    return HEADER_TOTAL_BYTES;
}

- (short)version
{
    return version;
}

- (short)messageType
{
    return messageType;
}

- (short)tag
{
    return tag;
}

+ (short)parseVersion:(NSData *)header
{
    return [ChumpHelp parseNetworkShort:header range:NSMakeRange(0, HEADER_VERSION_BYTES)];
}

+ (short)parseMessageType:(NSData *)header
{
    return [ChumpHelp parseNetworkShort:header range:NSMakeRange(HEADER_VERSION_BYTES, HEADER_MESSAGE_TYPE_BYTES)];
}

+ (short)parseTag:(NSData *)header
{
    return [ChumpHelp parseNetworkShort:header range:NSMakeRange(HEADER_VERSION_BYTES + HEADER_MESSAGE_TYPE_BYTES, HEADER_TAG_BYTES)];
}

@end
