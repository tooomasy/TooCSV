![TooCsv Logo](./assert/logo.png)
### **TooCSV** is a robust, fast, dependency-free, and RFC 4180 compliant CSV utility. It requires less memory usage and supports reading large CSV real-world data.



# Benchmark

The following benchmark is tested with this *[dataset](https://www3.stats.govt.nz/2018census/Age-sex-by-ethnic-group-grouped-total-responses-census-usually-resident-population-counts-2006-2013-2018-Censuses-RC-TA-SA2-DHB.zip)

|CSV Utility|Execution Time (ms)|Records Per Second|
|:----|:----|:----|
|FastCSV 2.2.0|5134|6809|
|OpenCSV 5.7.0|8875|3939|
|TooCSV 0.1.0|11036|3168|
|Commons CSV 1.9.0|24662|1418|

\* source: https://www.stats.govt.nz/large-datasets/csv-files-for-download/ (Age and sex by ethnic group (grouped total responses), for census usually resident population counts, 2006, 2013, and 2018 Censuses (RC, TA, SA2, DHB), CSV zipped file, 103 MB)

# Features
- Simple API
- Easy to use
- Robust
- RFC 4180 compliant
- Simple structure

# Requirements
- Minimum version: Java 8

# Examples
## CsvReader
```java
try (CsvReader reader = new CsvReader(new FileReader("./sample.csv"))) {
  String[] row;
  while ((row = reader.readRow()) != null) {
    // ...
  }

} catch (IOException e) {
  e.printStackTrace();
}
```