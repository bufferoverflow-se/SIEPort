# SIEPort

SIEPort is a Java library for reading (im*port*) and writing (ex*port*) SIE4 files used in Swedish financial accounting software.

The aim of this project is to fulfill my needs, so it may not support everything in the standard, or validate everything before e.g. writing files.
However, open an issue or, even better, a pull request with your suggestions for improvement and I'll look into it!

## Features

Read and write SIE4 files, both 4E (.se) and 4I (.si)

## Installation

### Using Maven

Add the following dependency to your `pom.xml`:

```xml

<dependency>
    <groupId>se.bufferoverflow</groupId>
    <artifactId>sieport</artifactId>
    <version>0.0.8</version>
</dependency>
```

### Using Gradle

Add the following to your `build.gradle`:

```gradle
implementation 'se.bufferoverflow:sieport:0.0.8'
```

## Usage TODO

### Reading SIE4 files

```java
public class Reader {
    public static void main(String[] args) {
        Path file = Path.of("/path/to/sie4.se");
        // Get the file's items with rudimentary access methods 
        SIE4Items items = SIE4.parse(file);
        // Wrap in SIE4Document for more convenience
        SIE4Document document = new SIE4Document(items);

        // Print all transaction data
        document.getVer().forEach(System.out::println);
    }
}
```

### Writing SIE4 files

```java
public class Writer {
    public static void main(String[] args) {
        Path dest = Path.of("/path/to/sie4.si");
        
        SIE4Document document = SIE4Document
            .builder()
            .program(new SIE4Item.Program("Redovisning", "0.0.1"))
            .fnamn(companyName)
            .orgnr(orgNumber)
            .ver(List.of(...))
            .build();
        
        // Write a 4I file with only transaction data 
        SIE4.write(dest, document.getItems(), SIE4.WriteOptions.SIE4I);
    }
}
```

## Contributing

Contributions are welcome!
If you don't want to write code, add an issue.
Please first check that a similar issue has not already been created.

1. Fork the repository
2. Create a feature branch (`git checkout -b feature-name`)
3. Make your changes, but be consistent with existing code style and structure
4. Commit changes (`git commit -am 'Some awesome changes'`)
5. Push your branch (`git push origin feature-name`)
6. Open a pull request

## License

This project is licensed under the [Apache 2.0 license](LICENSE).
