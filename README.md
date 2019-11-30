# mcprol-log4j12-ext
Some useful extensions to log4 v1.2.x.


## JSON layout
(some ideas from: [thmshmm/log4j-json-layout](https://github.com/thmshmm/log4j-json-layout) and [michaeltandy/log4j-json](https://github.com/michaeltandy/log4j-json))

A new layout to produce events in JSON format.
These are formatted with one logging event per line.

### how to configure log4j properties file?
```
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=mcprol.log4j.JsonLayout
log4j.appender.stdout.layout.ConversionPattern=%X{MDC-key} %t %p %m
```

### What does the output look like?
```
logger.info("sample", new Exception());

{"MDC-key":"99999","Thread":"main","Level":"INFO","Message":"sample","Stacktrace":"java.lang.Exception\tat mcprol.log4j.Log4jTestMain.main(Log4jTestMain.java:34)"}
```


## Java System Properties pattern converter
Now, you can log any java system properties (the default ones, or any '-D' in your jvm command line options), setting the log4j ConversionPattern.

### how to configure log4j properties file?
```
log4j.appender.console.layout=mcprol.log4j.PatternLayout
log4j.appender.console.layout.ConversionPattern=%D{java.version} %D{exist} %t %p %m%n
```

### What does the output look like?
```
logger.info("sample");

1.8.0_202 yes main INFO sample
```
