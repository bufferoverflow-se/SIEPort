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
    <version>0.0.7</version>
</dependency>
```

### Using Gradle

Add the following to your `build.gradle`:

```gradle
implementation 'se.bufferoverflow:sieport:0.0.7'
```

## Usage

### Reading SIE4 files

`SIE4.parse()` returns an immutable `SIE4Document`. Use the typed getters to access specific item
types, or `getItems(Class<T>)` / `getItem(Class<T>)` for generic type-safe access.

```java
SIE4Document doc = SIE4.parse(Path.of("/path/to/file.se"));

// Typed getter
doc.getVer().forEach(System.out::println);

// Generic access
List<SIE4Item.Konto> accounts = doc.getItems(SIE4Item.Konto.class);
Optional<SIE4Item.Fnamn> name = doc.getItem(SIE4Item.Fnamn.class);

// Flat item list (e.g. for writing back out)
List<SIE4Item> all = doc.getItems();
```

### Writing SIE4 files

Use `SIE4Document.newDocument()` to start building a new export document. It pre-populates the
standard defaults: `FLAGGA 0`, `FORMAT PC8`, `GEN` set to today, and `SIETYP 4`.

```java
SIE4Document doc = SIE4Document.newDocument()
    .program(new SIE4Item.Program("Redovisning", "0.0.1"))
    .fnamn("Acme AB")
    .orgnr("556000-0000")
    .ver(List.of(...))
    .build();

// Write a SIE 4E export file (default, validates mandatory items)
SIE4.write(Path.of("/path/to/file.se"), doc);

// Write a SIE 4I transaction file
SIE4.write(Path.of("/path/to/file.si"), doc, SIE4.WriteOptions.SIE4I);
```

`SIE4Document.builder()` is also available for full control without any pre-set defaults.

### Validation

`SIE4.write()` validates the document before writing and throws `SIE4Exception` on failure.
Pass `SIE4.WriteOptions.SKIP_VALIDATION` to bypass this. The validator checks:

- Mandatory items are present (`FLAGGA`, `PROGRAM`, `FORMAT`, `GEN`, `SIETYP`, `FNAMN`, and more for 4E)
- Forbidden items are absent (e.g. balance items in 4I files)
- `IB`, `UB`, and `RES` items for the current year are present (4E only)
- `FLAGGA` is `0` (a file ready for import must not already be marked as imported)

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
