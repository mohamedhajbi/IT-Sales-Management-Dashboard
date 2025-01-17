# wait-for-it.sh
#!/bin/bash
host=$1
port=$2
shift 2
while ! nc -z $host $port; do
  sleep 0.1
done
exec "$@"
