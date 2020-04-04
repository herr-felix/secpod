# Secpod

A collaborative podcasting session recorder, over ssh.

*Still a WIP*

Hello there,

The goal here is to make a simple utility allowing us to stream audio to a server, which can record multiple tracks in a "session". The recording server is an ssh server, which takes an abritrary username that will be the name the session's name.

The project is splited in 2 parts:
- A *client*. A simple bash script that sends audio to the ssh server.
- The ssh server itself. It is responsible for storing and organizing all the tracks' blobs and authenticating the users.

## Track blob
A track blob is an opus audio file with a name looking like `1985-04-12T23:20:50.52Z_3a4d6027.opus`, where `1985-04-12T23:20:50.52Z` is the time which the recording of the track started and `c0a80066` is the IP address of the client in hexadecimal.
a

## Commands

A call to a secpod server looks like `ssh -i ~/.ssh/id_rsa room@host "command"`

 - `claim` : **stdin**: the public key of a new admin. The room must not exists.
 - `apotheosize` : **stdin**: the public key of a new admin. Connection must be from an admin.
 - `introduce` : **stdin**: the public key authorized to connect to the room. Connection must be from an admin.
 - `keys` : **stdout** All the public keys known to the room. Connection must be from an admin.
 - `forget` : **stdin**: the public key to ban. Connection must be from an admin.
 - `record [label]` : **stdin**: An audio stream. Connection must known to the room. `label` is a string matching `[a-zA-z0-9-_]{1,32}` and will be added to track name. Usefule for post production. If `label` doesn't match `[a-zA-z0-9-_]{1,32}`, it will be simply be ignored.
 - `harvest` **stdout** A gzipped tar archive stream containing all the tracks since the last `clean` command and a makefile for automaticaly mix all those tracks with the right timing. Connection must be from an admin.
 - `clean` : Will clear all tracks in the room. Connection must be from an admin.
 - `burn` : "forget" all the keys and "clean" the room so it can be "claimed" again.
