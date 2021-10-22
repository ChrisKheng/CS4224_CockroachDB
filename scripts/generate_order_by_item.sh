#!/bin/bash
archive_name="project_files_4"
awk -F "," '{OFS=","; print $5, $1, $2, $3}' ${archive_name}/data_files/order-line.csv | uniq > ${archive_name}/data_files/order-by-item.csv
