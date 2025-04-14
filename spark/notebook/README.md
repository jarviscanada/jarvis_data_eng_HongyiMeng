# Introduction
There are two parts to this project. First, we analyzed the World Development Indicators dataset from the World Bank 
DataBank using Spark and produced graphs to visualize the data. Then, we performed RFM analysis on the customers of a
retail store to identify key customer groups.

Technologies:
- Zeppelin
- Google Cloud Platform
- Databricks
- Azure
- Spark
- Matplotlib
- Seaborn

# [Databricks and Spark Implementation](./Retail%20Data%20Analytics%20with%20PySpark.ipynb)

The dataset used for this task is a list of invoice entries from a retail store. An invoice may have multiple entries.
There are also cancelled invoices. Each row of the dataset specify the invoice number, the item code, a description, the
quantity purchased, the invoice date, the unit price of the item, the customer id, and the country of purchase.

We used Azure Databricks and Spark to perform the analytic. The data was originally stored in an SQL file, which we read to a 
PostgreSQL database and dumped as a csv file. The file was then uploaded to DBFS and saved as a table. 

# [Zeppelin and Spark Implementation](Spark%20Dataframe%20-%20WDI%20Data%20Analytics.zpln)

The dataset used for this task is the World Development Indicators dataset from the World Bank DataBank.
The data we worked with includes the country, the year, the indicator type (eg: GDP), and the indicator value.

The ETL for this task was performed with GCP, a Zeppelin notebook, and PySpark. 

# Future Improvement
- Improve readability
- Create nicer graphs with Matplotlib