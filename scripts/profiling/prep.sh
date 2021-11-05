#!/bin/bash
# This script assumes the presence of a directory known as profiling_files containing the files needed to perform the profiling

username="$USER"
dest_dir=$(pwd)
data_files_dir="profiling_files"

if ! [[ -d ${data_files_dir} ]]
then
	echo "${data_files_dir} directory is not present"
	exit 1
fi

[[ -e ${data_files_dir}.tgz ]] && rm ${data_files_dir}.tgz
tar -czf ${data_files_dir}.tgz ${data_files_dir}

# NOTE: REPLACE THIS VARIABLE ACCORDINGLY
servers=(xcnc4{1..4})

for server in "${servers[@]}"
do
	ssh ${username}@${server} "mkdir -p ${dest_dir}"
	scp ${data_files_dir}.tgz ${username}@${server}:${dest_dir}
	ssh ${username}@${server} "cd ${dest_dir}; tar -xzf ${data_files_dir}.tgz"
done
