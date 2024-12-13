#!/bin/sh

# setup and validate args
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

# check number of args
if [ "$#" -ne 5 ]; then
  echo "Illegal number of parameters"
  exit 1
fi

# save machine stats in MB and hostname
vmstat_mb=$(vmstat --unit M)
hostname=$(hostname -f)

# retrieve hardware specs
memory_free=$(echo "$vmstat_mb" | awk '{print $4}' | tail -n1 | xargs)
cpu_idle=$(echo "$vmstat_mb" | tail -1 | awk -v col="15" '{print $col}')
cpu_kernel=$(echo "$vmstat_mb" | tail -1 | awk -v col="14" '{print $col}')
disk_io=$(vmstat -d | tail -1 | awk '{print $10}')
disk_available=$(df -BM / | tail -1 | awk -v col="4" '{print $col}' | sed 's/[A-Z]//')

# timestamp in 2019-11-26 14:40:19 UTC format
timestamp=$(vmstat -t | tail -1 | awk -v date_col="18" -v time_col="19" '{print $date_col " " $time_col}')

# find matching id in host_info table
host_id="(SELECT id FROM host_info WHERE hostname='$hostname')";

# insert usage data into table
insert_stmt="INSERT INTO host_usage(timestamp, host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available)
  VALUES('$timestamp', $host_id, '$memory_free', '$cpu_idle', '$cpu_kernel', '$disk_io', '$disk_available')";

# set up env var for psql cmd
export PGPASSWORD=$psql_password
# insert data into db
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?
