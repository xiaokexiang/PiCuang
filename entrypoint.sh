#!/bin/sh
if [ -n "$PASSWORD" ]; then
  sed -i "s/^password=.*$/password=${PASSWORD}/" /opt/config.ini
fi
if [ -n "$IPS" ]; then
  sed -i "s/^ips=.*$/ips=${IPS}/" /opt/config.ini
fi
if [ -n "$PREFIX" ]; then
  sed -i "s|^prefix=.*$|prefix=${PREFIX}|" /opt/config.ini
fi
if [ -n "$REFERER" ]; then
  sed -i "s|^referer=.*$|referer=${REFERER}|" /opt/config.ini
fi
exec java -jar /opt/picuang.jar