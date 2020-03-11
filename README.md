# Secpod

A collaborative podcasting session recorder, over ssh.

*Still a WIP*

Hello there,

The goal here is to make a simple utility allowing us to stream audio to a server, which can record multiple tracks in a "session". The recording server is an ssh server, which takes an abritrary username that will be the name the session's name.

The project is splited in 2 parts:
- A *client*. A simple bash script that sends audio to the ssh server.
- The ssh server itself. It is responsible for storing and organizing all the tracks' blobs and authenticating the users.

## Track blob
A track blob is an opus audio file with a name looking like `1985-04-12T23:20:50.52Z_3a4d6027.opus`, where `1985-04-12T23:20:50.52Z` is the time which the recording of the track started and `c0a80066` is the IP address of the client with the `.` and in hexadecimal.
a
