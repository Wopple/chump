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

#import "ChumpHeaderTests.h"

@implementation ChumpHeaderTests

- (void)testParseVersion
{
    uint8_t bytes[] = {0, 1};
    uint8_t tooShortBytes[] = {0};
    NSData *header = [NSData dataWithBytes:bytes length:2];
    NSData *tooShortHeader = [NSData dataWithBytes:tooShortBytes length:1];
    STAssertEquals((short) 1, [ChumpHeader parseVersion:header], nil);
    STAssertThrows([ChumpHeader parseVersion:nil], nil);
    STAssertThrows([ChumpHeader parseVersion:tooShortHeader], nil);
}

- (void)testParseMessageType
{
    uint8_t bytes[] = {0, 0, 0, 3};
    uint8_t tooShortBytes[] = {0, 1, 2};
    NSData *header = [NSData dataWithBytes:bytes length:4];
    NSData *tooShortHeader = [NSData dataWithBytes:tooShortBytes length:3];
    STAssertEquals((short) 3, [ChumpHeader parseMessageType:header], nil);
    STAssertThrows([ChumpHeader parseMessageType:nil], nil);
    STAssertThrows([ChumpHeader parseMessageType:tooShortHeader], nil);
}

- (void)testParseTag
{
    uint8_t bytes[] = {0, 0, 0, 0, 0, 5};
    uint8_t tooShortBytes[] = {0, 1, 2, 3, 4};
    NSData *header = [NSData dataWithBytes:bytes length:6];
    NSData *tooShortHeader = [NSData dataWithBytes:tooShortBytes length:5];
    STAssertEquals((short) 5, [ChumpHeader parseTag:header], nil);
    STAssertThrows([ChumpHeader parseTag:nil], nil);
    STAssertThrows([ChumpHeader parseTag:tooShortHeader], nil);
}

- (void)testInit
{
    STAssertThrows([[[ChumpHeader alloc] init] class], nil);
}

- (void)testInitWithVersion
{
    STAssertNoThrow([[[ChumpHeader alloc] initWithVersion:0 messageType:1 tag:2] class], nil);
}

- (void)testToData
{
    ChumpHeader *header = [ChumpHeader headerWithVersion:0 messageType:1 tag:2];
    uint8_t bytes[] = {0, 0, 0, 1, 0, 2};
    NSData *data = [NSData dataWithBytes:bytes length:6];
    STAssertEqualObjects(data, [header toData], nil);
}

@end
