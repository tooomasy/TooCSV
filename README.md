![TooCsv Logo](./assert/logo.png)
### **TooCSV** is a robust, fast, dependency-free, and RFC 4180 compliant CSV utility. It requires less memory usage and supports reading large CSV real-world data.



# Benchmark

The following benchmark is tested with the [UK Property Price Paid](https://www.gov.uk/government/statistical-data-sets/price-paid-data-downloads) dataset (4.3GB)

- Takes 61.398 seconds to read the complete dataset
- Read 447091 rows/second

# Features
- Simple API
- Easy to use
- Robust
- RFC 4180 compliant
- Simple structure

# Requirements
- Minimum version: Java 7

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