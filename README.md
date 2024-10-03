
# Prettifier: Itinerary Processor

The **Prettifier** is a Java program that processes itinerary files by converting airport codes to names, formatting dates and times, and outputting the cleaned and formatted data either to a file or to the console. The program can also handle different types of input including city names associated with airport codes.

## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Class Descriptions](#class-descriptions)
  - [Prettifier](#prettifier-class)
  - [AirportLookup](#airportlookup-class)
  - [DateAndTimeFormatter](#dateandtimeformatter-class)
  - [ItineraryProcessor](#itineraryprocessor-class)
  - [Utils](#utils-class)
- [How to Download and Run](#how-to-download-and-run)
- [Usage](#usage)
  - [Command Line Options](#command-line-options)
- [CSV File Format](#csv-file-format)
- [Error Handling](#error-handling)

## Introduction

The Prettifier program reads an itinerary input file, processes airport codes (IATA, ICAO), formats date/time entries, and outputs the results to either an output file or the console. The airport codes are looked up from a provided CSV file, and any recognized codes are replaced by their corresponding airport names or cities.

## Features

- Replaces **IATA** and **ICAO** airport codes with full airport names.
- Supports city-based lookup for both IATA and ICAO codes.
- Formats dates and times found in specific patterns (12-hour and 24-hour formats).
- Outputs processed data to a file or directly to the console.
- Handles errors such as malformed CSVs or missing files gracefully.

## Class Descriptions

### Prettifier Class

The `Prettifier` class is the main entry point for the program. It handles the following:

- Parsing command-line arguments to determine file paths and output preferences.
- Managing the creation of the necessary helper objects (`AirportLookup`, `ItineraryProcessor`).
- Handling exceptions during file operations and invalid data inputs.
- Printing success or error messages in colored text using ANSI codes.

### AirportLookup Class

The `AirportLookup` class is responsible for loading and managing airport data from a CSV file. It performs the following:

- Reads the airport lookup data from the CSV file.
- Maps IATA and ICAO codes to their corresponding airport names.
- Maps IATA and ICAO codes to city names for city-based lookups.
- Replaces airport codes in the input lines with their names or cities, depending on the format.

#### Example Codes Conversion:
- `##CODE`: Replaces ICAO code.
- `#CODE`: Replaces IATA code.
- `*##CODE`: Replaces ICAO code with city.
- `*#CODE`: Replaces IATA code with city.

### DateAndTimeFormatter Class

The `DateAndTimeFormatter` class processes and reformats date and time strings found in the itinerary file. It performs the following:

- Identifies date/time patterns using regular expressions.
- Converts time zones to the format `(+HH:MM)` or `(-HH:MM)`.
- Supports multiple formats:
  - **D**: Converts to "dd MMM yyyy" (e.g., 05 Oct 2024).
  - **T12**: Converts to 12-hour time format with timezone (e.g., 02:30PM (+00:00)).
  - **T24**: Converts to 24-hour time format with timezone (e.g., 14:30 (+00:00)).

### ItineraryProcessor Class

The `ItineraryProcessor` class processes each line of the input itinerary file by calling the airport lookup and date/time formatter. It:

- Reads the input file.
- Processes each line, applying airport code replacement and date/time formatting.
- Writes the processed data to the output file.
- Cleans up extra spaces and empty lines in the final output.

### Utils Class

The `Utils` class provides helper methods for common operations such as file reading and writing, cleaning up whitespace, and printing lines to the console. It includes:

- Methods to read from and write to text files.
- CSV file reading utility.
- Console printing method for formatted output.
- Whitespace cleanup method to remove unnecessary empty lines.

## How to Download and Run

### Step 1: Clone the Repository

To download the program, clone the Git repository to your local machine using the following command:

```bash
git clone https://github.com/your-username/prettifier.git
cd prettifier
```

### Step 2: Compile the Program

Make sure you have **Java JDK** installed on your system. Once you are in the project directory, compile the program with the following command:

```bash
javac *.java
```

This will compile all Java files and produce the necessary class files.

### Step 3: Run the Program

After compiling, you can run the program with the following command:

```bash
java Prettifier [options] <inputFile> <outputFile> <airportsFile>
```

Refer to the [Usage](#usage) section for details on command-line options and examples.


## Usage

To run the program, use the following command line structure:

```bash
java Prettifier [options] <inputFile> <outputFile> <airportsFile>
```

### Command Line Options

- `-h`: Displays help message for program usage.
- `-o`: Prints the processed data to the console in addition to saving it to the output file.

#### Examples:
1. **Basic usage**:
   ```bash
   java Prettifier ./input.txt ./output.txt ./airport-lookup.csv
   ```
   This reads `input.txt`, processes the data, and saves the output to `output.txt` using the airport data from `airport-lookup.csv`.

2. **Console output**:
   ```bash
   java Prettifier -o ./input.txt ./output.txt ./airport-lookup.csv
   ```
   This additionally prints the processed data to the console.

3. **Help**:
   ```bash
   java Prettifier -h
   ```
   This displays usage information.

## CSV File Format

The CSV file used for airport lookup should have the following columns:

- **name**: The airport name.
- **iata_code**: The IATA code (3-letter).
- **icao_code**: The ICAO code (4-letter).
- **municipality**: The city where the airport is located.

Sample CSV data:
```csv
name,iata_code,icao_code,municipality
Los Angeles International,LAX,KLAX,Los Angeles
John F Kennedy International,JFK,KJFK,New York
```

## Error Handling

The program handles the following errors gracefully:

- **File not found**: Displays an error message if any input file is missing.
- **Malformed CSV file**: Displays an error if the CSV file is missing required columns or contains incomplete rows.
- **Invalid airport codes**: If an airport code is not found, the original code is retained in the output.

