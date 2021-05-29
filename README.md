[![Maven Central](https://img.shields.io/maven-central/v/io.github.metheax/khmer-chhankitek-calendar.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.metheax%22%20AND%20a:%22khmer-chhankitek-calendar%22)
#  Khmer Chhankitek Calendar
Transform `GREGORIAN` calendar's date into `KHMER CHHANKITEK` calendar's date.
# Build from source
`If need modification on library, elseif skip this part.`
Pre-requires
* Java 8+
* Maven as its build tool.

How to build
* Simply by clone source code from our repository and using maven command `mvn clean install` to build this lib.

# Usages
### 1. Add dependency into project
1. Apache Maven Project
    ````
    <dependency>
        <groupId>io.github.metheax</groupId>
        <artifactId>khmer-chhankitek-calendar</artifactId>
        <version>1.0.0</version>
    </dependency>
    ````
2. Gradle Groovy DSL
    ````
    implementation 'io.github.metheax:khmer-chhankitek-calendar:1.0.0'
    ````
3. Gradle Kotlin DSL
    ````
    implementation("io.github.metheax:khmer-chhankitek-calendar:1.0.0")
    ````
4. Scala SBT
    ````
    libraryDependencies += "io.github.metheax" % "khmer-chhankitek-calendar" % "1.0.0"
    ````
5. Legacy Java Porject, Imported compiled jar
   Download `khmer-chhankitek-calendar-1.0.0.jar` from `bin` directory then import it into your java project.
### 2. Access Chhankitek
    ````
    KhmerLunarDate lunarDate = Chhankitek.toKhmerLunarDateFormat(LocalDateTime.of(2021, 5, 28, 0, 0, 0, 0));
    ````
Log `lunarDate`, it should display `ថ្ងៃសុក្រ ២ រោច ខែជេស្ឋ ឆ្នាំឆ្លូវ ត្រីស័ក ពុទ្ធសករាជ ២៥៦៥` in your console.
Available properties of class `KhmerLunarDate`
````
dayOfWeek: String // អាទិត្យ, ច័ន្ទ...
lunarDay: String // ១កើត, ២កើត...
lunarMonth: String // ចេត្រ...
lunarZodiac: String // ជូត, ឆ្លូវ...
lunarEra: String // ត្រីស័ក...
lunarYear: String // ២៥៦៥, ២៥៦៦...
````
# Support
If you encounter any issues regarding this project, please create a Github Issue.

# Authors and acknowledgment
This library might not exist without hardwork of these people:
1. Base on algorithm of `Mr.Phylypo Tum` from [Cam-CC](https://www.cam-cc.org/calendar/)
2. Porting from [momentkh](https://github.com/ThyrithSor/momentkh) by `ThyrithSor` into `Java`
3. [Khmer New Year Time Calculation](http://www.dahlina.com/education/khmer_new_year_time.html)
