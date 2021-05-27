#  Khmer Chhankitek Calendar
Transform `GREGORIAN` calendar's date into `KHMER CHHANKITEK` calendar's date.
# Build from source
Pre-requires
* Java 8+
* Maven as its build tool.

How to build
* Simply by clone source code from our repository and using maven command `mvn clean install` to build this lib.

# Usages
1. Adding dependency into maven project

       <dependency>
            <groupId>io.methea</groupId>
            <artifactId>khmer-chhankitek-calendar</artifactId>
            <version>1.0.0</version>
        </dependency>

2. Import our compiled jar
   Download `khmer-chhankitek-calendar.jar` from `bin` directory then import it into your java project.
# Support
If you encounter any issues regarding this project, please create a Github Issue.

# Authors and acknowledgment
This library might not exist without hardwork of these people:
1. Base on algorithm of `Mr.Phylypo Tum` from [Cam-CC](https://www.cam-cc.org/calendar/)
2. Porting from [momentkh](https://github.com/ThyrithSor/momentkh) by `ThyrithSor` into `Java`
3. [Khmer New Year Time Calculation](http://www.dahlina.com/education/khmer_new_year_time.html)