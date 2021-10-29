# CS4224 Wholesale Project (CockroachDB Implementation)
## Setting up the database
1) The following instructions assume that:
* a CockroachDB secure cluster has been set up.
* the client certificates to access the CockroachDB cluster is placed in a `certs` directory.

2) Log into one of the CockroachDB cluster node.

3) Download the provided project files into the CockroachDB cluster node.
```
wget http://www.comp.nus.edu.sg/~cs4224/project_files_4.zip
```

4) For each of the provided data files in the `project_files` directory, run the following command
to upload the provided project data files to the local CockroachDB cluster node.
```
cockroach nodelocal upload <source file> <destination file> --certs-dir=certs

# e.g.
cockroach nodelocal upload cockroachdb_data_setup/project_files_4/data_files/warehouse.csv warehouse.csv --certs-dir=certs
```
* The output should show the id of the CockroachDB node where the file is uploaded to. The id should be the id of the
current CockroachDB cluster node. Jot down the id.

5) Upload the `.sql` files under the `scripts` directory of the project root directory to the cluster node.

6) Run the command below to create the wholesale database. Enter the password of the CockroachDB user when prompted.
```
cockroach sql --user=<CockroachDB username> --certs-dir=certs --file create_table.sql
```

7) Substitute the id of the node in `load_data.sql` accordingly with the id of the node where the data files were
uploaded to, i.e.
```
# Replace 4 with the id of the node where the project files were uploaded to
... CSV DATA ('nodelocal://4/warehouse.csv') ...
```

8) Run the command below to load the data into the wholesale database.
```
cockroach sql --user=<CockroachDB username> --certs-dir=certs --file load_data.sql
```

9) Run the command below to create indices in the wholesale database.
```
cockroach sql --user=<CockroachDB username> --certs-dir=certs --file create_index.sql
```
* Note that this command should only be run after the data has been loaded into the database because CockroachDB currently
does not support `IMPORT` statement on tables on which secondary indices have been created.


## Compilation of Client Program
The following compilation has been tested on macOS.
1) Install the following software on your local machine:
* Gradle (version 7.2)
* Java (version 11.0.12)
  * Make sure that `JAVA_HOME` variable is pointing to the installed Java 11 directory.

2) To compile, run the following command in the project root directory.
```
gradle shadowJar
```
* The compiled jar file can be found in the `build/libs` directory.


## Execution
The following instructions assumes that:
* a database has been created following the instructions in the [set up database](#setting-up-the-database) section.
* a user has been created in the CockroachDB cluster with a password.

### Usage of jar file
```
usage: CS4224_CDB-1.0-SNAPSHOT-all.jar
 -d,--Database <arg>      Database name
 -f,--fileName <arg>      Name of transaction query file
 -i,--ip <arg>            IP address of CockroachDB cluster
 -l,--logFileName <arg>   Name of log file
 -p,--port <arg>          Port of CockroachDB cluster
 -pw,--password <arg>     Password of CockroachDB user provided 
 -t,--task <arg>          Type of task: transaction or dbstate
 -u,--user <arg>          Username of CockroachDB user
```
* Required arguments for all type of tasks: `-t`
* Required arguments for processing input transaction file: `-d, -f, -u, -pw`
* Required arguments for computing final state of database: `-d, -u, -pw`
* Other arguments are optional.
* Default value of optional argument:
  * `-l`: `out.log`
  * `-i`: `localhost`
  * `-p`: `26257`


### How to run the jar file for processing input transaction file
1) Example 1: Runs the jar file on the cluster node that runs the CockroachDB instance:
```
java -jar CS4224_CDB-1.0-SNAPSHOT-all.jar -t transaction -f xact_files_B/0.txt -d wholesaledb -u [username] -pw [password] -l 0-out.log 1> out/workload_B/0.out 2> out/workload_B/0.err
```

2) Example 2: Runs the jar file on a remote machine (i.e. not on the cluster node that runs the CockroachDB instance)
```
java -jar CS4224_CDB-1.0-SNAPSHOT-all.jar -t transaction -f xact_files_B/0.txt -d wholesaledb -u [username] -pw [password] -l 0-out.log -ip [CockroachDB node IP address] 1> out/workload_B/0.out 2> out/workload_B/0.err
```


### How to run the jar file for computing the final state of the database
The final state of the database is saved to a file called `dbstate.csv`.
1) Example 1: Runs the jar file on the cluster node that runs the CockroachDB instance:
```
java -jar CS4224_CDB-1.0-SNAPSHOT-all.jar -t dbstate -d wholesaledb -u [username] -pw [password]
```

2) Example 2: Runs the jar file on a remote machine.
```
java -jar CS4224_CDB-1.0-SNAPSHOT-all.jar -t dbstate -d wholesaledb -u [username] -pw [password] -ip [CockroachDB node IP address]
```

## Running 40 clients simultaneously
A few Bash scripts have been created for running 40 clients simultaneously. The scripts are `prep.sh`, `launch.sh`, and
`run-clients.sh`. They can be found under `scripts/profiling` of the project root directory.

The scripts assume that:
* there are 5 CockroachDB cluster nodes.
* `tmux` is installed on those nodes.

### Steps
1) Upload the scripts in `scripts/profiling` to one of the CockroachDB cluster node. 
2) Create a directory in the `/temp` directory of the cluster node, e.g. `mkdir -p /temp/cs4224o/profiling/cockroachdb`
3) In the created directory, create a directory called `profiling_files`.
4) Upload the compiled jar file to the `profiling_files` directory.
5) Copy the provided transaction files directories (`xact_files_A` and `xact_files_B`) into the `profiling_files` directory.
6) Copy `run-clients.sh`into the `profiling_files` directory.
7) `cd` to the parent directory of the `profiling_files` directory.
8) Place `prep.sh`, `launch.sh`, and `gather_outputs.sh` in the current directory.
9) In `prep.sh`, substitute the `servers` variable with the list of hostnames of other nodes to run the clients on.
10) Run `prep.sh` to send the `profiling_files` archive to the group of CockroachDB cluster nodes.
11) In `launch.sh`, substitute the `servers` variable with the list of hostnames of other nodes to run the clients on.
12) Run `launch.sh` to launch 40 clients simultaneously.
```
Usage: launch <database_name> <workload_type>
database_name: Name of database for the workload
workload_type: A or B

# e.g.
./launch.sh wholesaledb A
```
13) Run `tmux ls` to check the status of the running clients on the current node.
14) Add the following to your `~/.bash_profile` on the cluster node to check the status of running clients on the other
nodes. Run `source ~/.bash_profile` to reload the Bash profile.
```
# Replace the list of servers (xcnc4{1..4}) accordingly.
alias checkstatus='for server in xcnc4{1..4}; do ssh $server "tmux ls"; done'
```
15) Run `checkstatus` to check the status of the running clients on other nodes.
16) Once the running clients finish, you can run `gather_outputs.sh` to gather all the output files back to current
node.
* Replace the list of nodes in `gather_outputs.sh` before running the script.