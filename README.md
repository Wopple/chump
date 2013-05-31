CHUMP
=====

CHUMP is the CHUnked Message Protocol. It is a minimal message passing protocol.

## Protocol
-----------

version 0

    int16 -- version of the protocol (0x00 0x00)
    int16 -- type of the message (user defined)
    int16 -- tag (user defined, can be used to track responses)
    uint16 -- number of bytes in the payload
    byte[] -- the payload (arbitrary length array of bytes, may be empty)

## Implementations
------------------

CHUMP is designed to be a language independent protocol.

1. Java
2. Objective-C

## Origin
---------

I started this project while developing an Android app with a friend. We were
originally using HTTP which was far too heavy considering the small size and
simplistic nature of our client server messages. CHUMP provided us with a
protocol that is very lightweight in library size, data overhead, and speed
while still being sufficient for our needs. CHUMP may be useful to you for
another purpose.
