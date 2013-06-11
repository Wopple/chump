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

#import "ChumpDataReader.h"

@implementation ChumpDataReader

+ (id)readerWithInput:(NSInputStream *)input num:(NSUInteger)num
{
    return [[self alloc] initWithInput:input num:num];
}

- (id)init
{
    NO_IMPLEMENTATION;
}

// designated
- (id)initWithInput:(NSInputStream *)inInput num:(NSUInteger)inNum
{
    RAISE_IF_NIL(inInput);
    
    if (self = [super init])
    {
        input = inInput;
        num = inNum;
        index = 0;
        
        if (num > 0)
        {
            buffer = (uint8_t *) malloc(sizeof(uint8_t) * num);
        }
        else
        {
            buffer = NULL;
        }
    }
    
    return self;
}

- (void)dealloc
{
    FREE_IF_NOT_NULL(buffer);
}

- (NSData *)read
{
    if (num == 0)
    {
        return [NSData dataWithBytes:nil length:0];
    }
    
    if (![input hasBytesAvailable])
    {
        return nil;
    }
    
    index += [input read:buffer + index maxLength:num - index];
    
    if (index == num)
    {
        index = 0;
        return [NSData dataWithBytes:buffer length:num];
    }
    else if (index > num)
    {
        @throw [NSException exceptionWithName:@"fatal error" reason:@"ChumpDataReader read past the buffer size" userInfo:nil];
    }
    else
    {
        return nil;
    }
}

@end
