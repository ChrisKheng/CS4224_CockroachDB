#!/bin/bash
if [[ $# -ne 2 ]]
then
	echo "Usage: launch <database_name> <workload_type>"
	echo "database_name: Name of database for the workload"
	echo "workload_type: A or B"
	exit 1
fi

username="$USER"
database_name="$1"
workload_type="$2"
dest_dir=$(pwd)
data_files_dir="profiling_files"

# NOTES: REPLACE THIS ACCORDINGLY BASED ON THE SERVERS TO RUN ON
servers=(xcnc4{1..4})

current_id=1
for server in "${servers[@]}"
do
	ssh ${username}@${server} "cd ${dest_dir}/${data_files_dir}; ./run-clients.sh ${current_id} ${database_name} ${workload_type}"
	(( current_id += 1 ))
done

cd ${data_files_dir}
./run-clients.sh 0 "$database_name" "$workload_type"
