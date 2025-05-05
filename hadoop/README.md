# Hadoop Project

Table of contents
* [Introduction](#introduction)
* [Hadoop Cluster](#hadoop-cluster)
* [Hive Project](#hive-project)
* [Improvements](#improvements)

# Introduction
This project serves to help the data analytics team to analyze the World Development Indicators dataset.

Technologies:
 - Hadoop
 - GCP
 - HDFS
 - Hive / Beeline
 - Zeppelin

# Hadoop Cluster
![Architecture](.\assets\architecture.png)
- MapReduce is the main execution used in the project. Code written in HQL were translated by Hadoop into MapReduce tasks which can be executed in a distributed fashion over 2 workers nodes. 
- YARN helps manage resources on our worker nodes. It monitors the resources available on each worker and schedules jobs for execution.
- HDFS is the distributed file system used by Hadoop. Files are stored as chunks of 64 Mb on one or more machines, and replicated in case of machine failures.
- Zeppelin is the interactive notebook used to execute code on the master node. It provides interpreters for various languages, including shell, HQL, markdown.
- Hardware specifications:
  - Master node: n4-standard-2 (2 vCPUs, 8 GB Memory), 100 GB Disk
  - Worker nodes: n4-standard-2 (2 vCPUs, 8 GB Memory), 100 GB Disk 

# Hive Project
To improve the performance of the queries, we first tried partitioning the data by year, which improved the execution 
time from 19.457s to 1.046s. We also noticed an improvement of 6.581s (37.83% speedup) by storing the data files in 
parquet format instead of text files. Finally, we compared the runtime of the Hive query with the same query executed 
with Spark. Hive took 34 seconds while Spark only took 4 seconds.

![notebook screenshot](.\assets\notebook.png)

# Improvements
- Execute each query multiple times and take the average runtime
- Execute all queries in the master node shell to avoid any Zeppelin overhead
- Explore the effect of partitioning the data over different columns