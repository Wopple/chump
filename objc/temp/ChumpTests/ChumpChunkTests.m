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

#import "ChumpChunkTests.h"

@implementation ChumpChunkTests

- (void)testParseSize
{
    char bytes[] = {0, 2, 3, 4};
    char tooShortBytes[] = {0};
    NSData *chunk = [NSData dataWithBytes:bytes length:4];
    NSData *tooShortChunk = [NSData dataWithBytes:tooShortBytes length:1];
    STAssertEquals((unsigned short) 2, [ChumpChunk parseSize:chunk], @"");
    STAssertThrows([ChumpChunk parseSize:nil], @"");
    STAssertThrows([ChumpChunk parseSize:tooShortChunk], @"");
}

- (void)testInit
{
    ChumpChunk *chunk = [[ChumpChunk alloc] init];
    STAssertEquals((unsigned long) 0, chunk.payload.length, @"");
}

- (void)testInitWithPayload
{
    char bytes[] = {0, 1, 2, 3};
    NSData *payload = [NSData dataWithBytes:bytes length:4];
    ChumpChunk *chunk = [[ChumpChunk alloc] initWithPayload:payload];
    STAssertEquals(payload, chunk.payload, @"");
}

- (void)testToData
{
    char bytes[] = {0, 1, 2, 3};
    char expectedBytes[] = {0, 4, 0, 1, 2, 3};
    NSData *payload = [NSData dataWithBytes:bytes length:4];
    ChumpChunk *chunk = [[ChumpChunk alloc] initWithPayload:payload];
    STAssertEqualObjects([NSData dataWithBytes:expectedBytes length:6], [chunk toData], @"");
}

@end
