# Performance tests with JMH

Micro-benchmarking is all about monitoring and measuring performance of pieces of code. Micro-benchmarking should be applied to code that is in a performance-critical path of your application, code that is called over and over again. When code is called over and over again, it pays to optimize the algorithm or certain approaches.

JMH stands for Java Micro-benchmarking Harness. It's a tool that's developed as part of the OpenJDK project and a basic suite of microbenchmarks was added to the JDK 12 source code to make it easy for developers to run existing microbenchmarks for the JDK itself and to create new ones.

Note: all benchmarks are under the `test` folder

#### Package `stepbystep`

The benchmark samples in the package `stepbystep` are for training session, they cover the basics topics of JMH and they introduce new feature step by step.

#### Package `jdkapi`

The benchmarks in the package `jdkapi` are extremely simple to understand, they highlight some not obvious results using operation with very common JDK api (LocalDateTime, parallel streams and unordered stream). It is suggested to start look at them after the `stepbystep` samples.

#### Package `forex`

The `forex` package contains a sample service that sums several amounts in different currencies into a target currency using a simple collaborator defined by the interface `PriceServiceClient`. The related benchmark calculates the performance of this piece of code using an input of different size and it also compares the code with a different algorithm.

The results are not very useful because they may vary in different machines, so the purpose of the JUnit 5 tests is to validate the results. There are three tests:
1) the first test just check if the average time of execution is below a specific threshold
2) the second test check the time complexity in O(n); in details it validates that time(10*n) <= 10*time(n) using an error of 0.2
3) the last test compare the results of the code with the other implementation

