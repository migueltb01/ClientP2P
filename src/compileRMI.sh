#! /bin/bash

cd ../out/production/ClientP2P

rmic -classpath . -v1.1 p2p.RemoteClientImplementation

cd ../../../../ServerP2P/out/production/ServerP2P
rmic -classpath . -v1.1 p2p.RemoteServerImplementation

cp ../../../../ClientP2P/out/production/ClientP2P/p2p/RemoteClientImplementation_Stub.class p2p
cp p2p/RemoteServerImplementation_Stub.class ../../../../ClientP2P/out/production/ClientP2P/p2p

rm p2p/RemoteServerImplementation_Stub.class