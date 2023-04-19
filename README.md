# ARXML Sorter

> **ARXML**: Autosar XML File  
> **Autosar**: AUTomotive Open Source ARchitecture

## How to use:
- Include the `arxml` relative file path in the command line arguments
- Output filename will be the input filename concatenated with "_mod"

> |Input|Output|
> |:-:|:-:|
> `filename.arxml`|`filename_mod.arxml`
- The output file will be sorted alphabetically by the SHORT-NAME text field.
---
### Input ARXML Example:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<AUTOSAR>
 <CONTAINER UUID="198ae269-8478-44bd-92b5-14982c4ff68a">
 <SHORT-NAME>ContainerB</SHORT-NAME>
 <LONG-NAME>AA</LONG-NAME>
 </CONTAINER>
 <CONTAINER UUID="198ae269-8478-44bd-92b5-14982c4ff68b">
 <SHORT-NAME>ContainerA</SHORT-NAME>
 <LONG-NAME>BB</LONG-NAME>
 </CONTAINER>
 <CONTAINER UUID="198ae269-8478-44bd-92b5-14982c4ff68c">
 <SHORT-NAME>ContainerC</SHORT-NAME>
 <LONG-NAME>CC</LONG-NAME>
 </CONTAINER>
</AUTOSAR>
```

### Output:
```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<AUTOSAR>
    <CONTAINER UUID="198ae269-8478-44bd-92b5-14982c4ff68b">
         
        <SHORT-NAME>ContainerA</SHORT-NAME>
         
        <LONG-NAME>BB</LONG-NAME>
         
    </CONTAINER>
    <CONTAINER UUID="198ae269-8478-44bd-92b5-14982c4ff68a">
         
        <SHORT-NAME>ContainerB</SHORT-NAME>
         
        <LONG-NAME>AA</LONG-NAME>
         
    </CONTAINER>
    <CONTAINER UUID="198ae269-8478-44bd-92b5-14982c4ff68c">
         
        <SHORT-NAME>ContainerC</SHORT-NAME>
         
        <LONG-NAME>CC</LONG-NAME>
         
    </CONTAINER>
</AUTOSAR>
```

---
## `test.bat`
Running this file will test three cases

1. `valid_autosar.arxml`: This file will be sorted successfully
2. `invalid_autosar.xml`: this file will trigger `NotValidAutosarFileException` because of the incorrect file extension
3. `empty_file.arxml`: This one is self explanatory, it will trigger `EmptyAutosarFileException`.