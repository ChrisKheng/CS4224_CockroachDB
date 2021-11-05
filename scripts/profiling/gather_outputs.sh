#!/bin/bash
if [[ $# -ne 1 ]]
then
	echo "Usage: gather_outputs.sh <workload type>"
	echo "Workload type: A or B"
	exit 1
fi

# NOTE: Replace the value of outputs_location accordingly.
workload_type="$1"
archive="archive_$(date --iso-8601=hour)"
out_archive="workload-${workload_type}_out_${archive}"
err_archive="workload-${workload_type}_err_${archive}"
outputs_location="/temp/cs4224o/profiling/cockroachdb/profiling_files/out/workload_${workload_type}"

# NOTE: Replace the list of other servers accordingly
# Copy .out files from other nodes
mkdir $out_archive
for server in xcnc4{1..4}
do
	mkdir ${out_archive}/${server}
	cd ${out_archive}/${server}
	scp ${server}:${outputs_location}/*.out .
	cd ../..
done

# NOTE: Replace the list of other servers accordingly
# Copy err files from other nodes 
mkdir $err_archive
for server in xcnc4{1..4}
do
	mkdir ${err_archive}/${server}
	cd ${err_archive}/${server}
	scp ${server}:${outputs_location}/*.err .
	cd ../..
done

# NOTE: Replace the name of the current node accordingly
# Copy .out files from local node
mkdir ${out_archive}/xcnc40
cd ${out_archive}/xcnc40
cp ${outputs_location}/*.out .
cd ../..

# NOTE: Replace the name of the current node accordingly
# Copy .err files from local node
mkdir ${err_archive}/xcnc40
cd ${err_archive}/xcnc40
cp ${outputs_location}/*.err .
cd ../..

# Create zips
zip -r ${out_archive}.zip ${out_archive}
zip -r ${err_archive}.zip ${err_archive}

