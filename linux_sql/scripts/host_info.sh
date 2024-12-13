#!/bin/sh

# read args
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

# check number of args
if [ $# -ne 5 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

# parse host hardware
lscpu_out=`lscpu`
cpu_info=`cat /proc/cpuinfo`
vmstat_M=`vmstat --unit M`

hostname=$(hostname -f)
cpu_number=$(echo "$lscpu_out" | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out" | egrep "^Model name:" | sed 's/Model name://' | xargs)
cpu_mhz=$(echo "$cpu_info" | egrep "^cpu MHz" | head -1 | awk '{print $4}' | xargs)
l2_cache=$(echo "$lscpu_out" | egrep "^L2 cache:" | awk '{print $3}' | xargs)
total_mem=$(echo "$vmstat_M" | tail -1 | awk '{print $4}')
timestamp=$(date --utc +"%Y-%m-%d %H:%M:%S")

# construct insert stmt
insert_stmt="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, timestamp,
  total_mem) VALUES('$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', $cpu_mhz, $l2_cache, '$timestamp',
  $total_mem)"

# execute insert
export PGPASSWORD=$psql_password
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?