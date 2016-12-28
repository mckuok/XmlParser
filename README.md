# XmlParser
A light weight XML File parser that parses XML content into a tree-like  structure. 
This parser is optimized to handle large file, with the maximum size of 2GB. 
Attributes are accepted in the XML file, but are ignored for simplicity. 
XML File should follow the standard set by w3 and can be found here "https://www.w3.org/TR/xml/". 
Any invalid XML file might lead to ParseException. This parser runs with O(n) for space and time, 
where n is the number of characters in the xml file / string.

