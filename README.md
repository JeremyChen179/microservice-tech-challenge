
## setup guide

a. install Maven
```bash
brew install maven

# verification
mvn -version

# sample output
Apache Maven 3.10.0 (c43a36b8d67be7e0805a411bc0898af1a51f5472)
Maven home: /opt/homebrew/Cellar/maven/3.10.0/libexec
Java version: 27, vendor: Homebrew, runtime: /opt/homebrew/Cellar/openjdk/27/libexec/openjdk.jdk/Contents/Home
Default locale: en_US, platform encoding: UTF-8, time zone: Asia/Singapore
OS name: "mac os x", version: "26.7.1", arch: "aarch64", family: "mac"
```

b. install VSCode IDE plugins
1. Language support for Java ™ for Visual Studio Code (RedHat)
2. Spring Boot Extension Pack (VMWare)

## folder structure (suggested or sampled)

```bash
my-app/
├── pom.xml                        # Maven configuration file (Project Object Model)
├── README.md                      # Project documentation
├── data/                          # External/Local project data (not bundled into the JAR)
│   └── raw/                       # Example: CSVs, raw datasets, database dumps
├── src/
│   ├── main/                      # All production-ready code and resources
│   │   ├── java/                  # Java source code
│   │   │   └── com/
│   │   │       └── company/       # Your package hierarchy
│   │   │           └── App.java
│   │   └── resources/             # Configuration, property files, SQL scripts
│   │       └── application.properties
│   └── test/                      # All test-related code and resources
│       ├── java/                  # Test source code (JUnit, TestNG, etc.)
│       │   └── com/
│       │       └── company/       # Mirror package structure of main/java
│       │           └── AppTest.java
│       └── resources/             # Test-specific mock data, configs, or files
│           └── test-data.json
└── target/                        # Auto-generated build output directory
```