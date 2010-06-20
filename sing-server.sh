#!/bin/sh
export ROOT=`dirname $0`
java                                 \
     -Djava.library.path="$ROOT/lib" \
     -Dscala.usejavacp=true          \
     -cp "$ROOT/bin":`find "$ROOT" -name "*.jar" | xargs | sed "s/ /:/g"`:bin \
     -d64 -Xmx2g \
     com.km.sing.Server "$@"