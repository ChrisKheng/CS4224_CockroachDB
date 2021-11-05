#!/bin/bash

usage() {
	echo "Usage: run-clients.sh <start id> <database name> <workload type>"
	echo "startid: The id of the first client (0 - 32 inclusive)"
	echo "database name: Name of database"
	echo "workload type: A or B"
}

if [[ $# -lt 3 ]]
then
	usage
	exit 1
fi

start_id="$1"
database="$2"
workload_type="$3"
output_dir="out/workload_$workload_type"

if [[ "$start_id" -lt 0 ]] || [[ "$start_id" -gt 32 ]]
then
	echo "start_id must be in the range mentioned in usage"
	usage
	exit 1
fi

if [[ "$workload_type" != "A"  ]] && [[ "$workload_type" != "B"  ]]
then
	echo "workload type must be A or B"
	usage
	exit 1
fi

mkdir -p $output_dir
rm cmd*.sh

currentId="$start_id"
for i in {0..7}
do
	echo "java -jar CS4224_CDB-1.0-SNAPSHOT-all.jar -t transaction -f xact_files_${workload_type}/${currentId}.txt -d ${database} -u cs4224o -pw j8uFlYB7StQV -l ${currentId}-out.log 1> ${output_dir}/${currentId}.out 2> ${output_dir}/${currentId}.err" > cmd${currentId}.sh
	chmod u+x cmd${currentId}.sh
	tmux new-session -d -s "client${currentId}" ./cmd${currentId}.sh
	(( currentId += 5 ))
done


