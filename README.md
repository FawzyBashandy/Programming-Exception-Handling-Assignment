# ARXML File Sorter
This program sorts the containers in an ARXML file alphabetically by their SHORT-NAME attribute. It validates the input file, sorts the containers logically, updates the XML structure, and generates a modified output file.

## Usage
To run this program, you need to pass the input ARXML file path as an argument.
For example:
java ArxmlFileSorter input.arxml

This will create a new file called input_mod.arxml with the containers sorted.

## Input File Validation
The program first validates that the input file has a .arxml extension and is not empty.
It throws customized exceptions in case of invalid input.

NotVaildAutosarFileException: Thrown if the input file does not have .arxml extension. This ensures the file contains XML data for autosar software components.
EmptyAutosarFileException: Thrown if the input file is empty. This indicates the file does not contain any components.
## XML Parsing
The input ARXML file is parsed using the DOM (Document Object Model) XML parser.
The DocumentBuilderFactory creates a DocumentBuilder which then parses the file.
The root element is normalized for easy traversing.

## Fetching Container Elements
All container elements are fetched using getElementsByTagName() and stored in an ArrayList for sorting.

## Sorting Containers
The containers are sorted logically by comparing their SHORT-NAME attribute values.
A customized Comparator is used to compare two Element objects.
The List.sort() method sorts the ArrayList in-place.

## Updating XML Structure
The sorted containers are appended to the root element in the correct order.
This updates the XML structure as per the logical sorting.

## Output Generation
The transformed DOM document is converted into a new XML file using Transformer.
The DOMSource provides the document, and StreamResult outputs it to the file.

## Time Complexity
The sorting is done using List.sort() which has a time complexity of nlogn, where n is the number of container elements.

## Space Complexity
An ArrayList of size n is used, so the space complexity is O(n).
