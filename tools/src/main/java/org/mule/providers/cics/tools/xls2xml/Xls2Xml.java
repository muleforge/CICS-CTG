package org.mule.providers.cics.tools.xls2xml;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.*;
import java.io.*;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * This program converts an Excel file (.xls) into XML.<BR>
 * 
 * The Excel sheet is assumed to contain 3 columns :       <BR>
 * Column1: name of the Cobol field (with prefix to indicate nesting level)<BR>
 * Column2: dummy test data                     <BR>
 * Column3: PIC clause e.g X(8), 9(8), etc.     <BR>
 * Copyright: OGIS-RI Co. Ltd (2006) All rights reserved. 
 */
public class Xls2Xml {

  /**
   * Usage: java Xls2Xml <xlsFile>
   */
  public static void main(String [] args) throws Exception {
    if (args.length < 1) {
      System.out.println("Usage: java Xls2Xml <xlsFile>");
      System.exit(1);
    } 
    File xlsFile = new File(args[0]);
    new Xls2Xml().process(xlsFile);
  }

  private static String getNameWithoutExtension(String filename) {
    int dot = filename.indexOf('.');
    if (dot >= 0) return filename.substring(0, dot); 
    return filename;
  }

  private Stack elementStack;
  private StringBuffer xmlBuffer;
  
  /**
   * Reads the Excel worksheet and generates XML file for each sheet.
   * The XML files are generated in the current directory.
   * @param xlsFile   The excel worksheet file (Input file)
   */
  public void process(File xlsFile) throws IOException {
 
    String filename = getNameWithoutExtension(xlsFile.getName());
    POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(xlsFile));
    HSSFWorkbook wb = new HSSFWorkbook(fs);
    for (int i=0; i<wb.getNumberOfSheets(); i++) {
        HSSFSheet sheet = wb.getSheetAt(i);
        File xmlFile = new File(filename + "_" + (i+1) + ".xml");
        if (wb.getNumberOfSheets() == 1) 
          xmlFile = new File(filename + ".xml");
        processSheet(sheet, xmlFile);
    }
  }

  /**
   * Reads a Excel sheet and generates corresponding XML file.
   * @param sheet   The excel sheet (Apache POI object)
   * @param xmlFile   The XML file. (Output file)
   */
  private void processSheet(HSSFSheet sheet, File xmlFile) throws IOException {

    this.elementStack = new Stack();
    //this.xmlBuffer = new StringBuffer("<?xml version='1.0' encoding='Shift_JIS'?>");
	this.xmlBuffer = new StringBuffer("<?xml version='1.0' encoding='UTF-8'?>");

    processFirstRow(sheet);
    for (int i=sheet.getFirstRowNum() + 1; i<=sheet.getLastRowNum(); i++) {
        HSSFRow row = sheet.getRow(i);

        String col1 = getCellValue(row, 0);
        int depth = getElementDepth(col1); // the depth of the element in XML
        String elementName = getElementName(col1); // the name of XML element.
        String elementValue = getCellValue(row, 1); // the value of XML element.
        String pic = getCellValue(row, 2);
        boolean newLine = false;
        while (depth < elementStack.size()) {
          String parentElem = (String) elementStack.pop();
          endElement(parentElem, newLine);
          newLine = true;
        }
        startElement(elementName, elementValue, pic);
        elementStack.push(elementName);
    }

    boolean newLine = false;
    while (elementStack.size() > 0) {
        String parentElem = (String) elementStack.pop();
        endElement(parentElem, newLine);
        newLine = true;
    }

    saveXmlFile(xmlFile);
  }

  private void processFirstRow(HSSFSheet sheet) {
	HSSFRow firstRow = sheet.getRow(sheet.getFirstRowNum());
	String col1 = getCellValue(firstRow, 0);
	String rootElementName = getElementName(col1);
    String namespace = getCellValue(firstRow, 1);
    startRootElement(rootElementName, namespace);
	this.elementStack.push(rootElementName);
  }

  /**
   * Adds the start of an XML element into the internal xml buffer.
   * @param name  name of the xml element.
   * @param value value of the xml element.
   * @param pic   PIC clause (used to add element attributes)
   */
  private void startElement(String name, String value, String pic) {
    // print white-space for pretty XML.
    this.xmlBuffer.append("\n");
    for (int i=0; i<elementStack.size(); i++) {
      this.xmlBuffer.append("  ");
    }

    if (pic.equals("")) {
      this.xmlBuffer.append("<" + name + ">");
    } else {
      String type = getTypeFromPicClause(pic);
      String length = getLengthFromPicClause(pic);
      String errorMsg = validateElementValue(type, length, value);
      if (!errorMsg.equals("")) {
        System.out.println("åxçê: " + name + " " + pic + " : " + errorMsg);
      }
      this.xmlBuffer.append("<" + name);
      //this.xmlBuffer.append(" type='" + type + "'");
      //this.xmlBuffer.append(" length='" + length + "'");
      this.xmlBuffer.append(">" + value);
    }
  }

  private void startRootElement(String name, String namespace) {
    this.xmlBuffer.append("\n<" + name + " xmlns=\"" + namespace + "\">");
  }

  /**
   * Adds the end of an XML element into the internal xml buffer.
   * @param name  name of the xml element.
   * @param newLine if newline should be added to the xml buffer.
   */
  private void endElement(String name, boolean newLine) {
    if (newLine) {
      this.xmlBuffer.append("\n");
      for (int i=0; i<elementStack.size(); i++) {
        this.xmlBuffer.append("  ");
      }
    }
    this.xmlBuffer.append("</" + name + ">");
  }

  private String getCellValue(HSSFRow row, int cellNo) {
    HSSFCell cell = row.getCell((short) cellNo);
    if (cell != null) {
        try {
	      return cell.getStringCellValue();
        } catch (Exception e) { /* ignore */ }
    }
    return "";
  }

  /**
   * Returns the depth of the element in XML tree, based on the cell value. 
   * (Depth is equal to number of times '|  ' occurs in the cell value)
   */
  private int getElementDepth(String cellValue) {
    int depth = 0;
    String prefix = "|  "; 
    int offset = 0;
    while (cellValue.regionMatches(offset, prefix, 0, 3)) {
      depth = depth + 1;
      offset = offset + 3;
    }
    return depth;
  }

  // Pattern to split using "|  ", " - ", "+- " or "/" 
  // Note: Double-backslash is used as escape character in the regular
  // expression because Java eats one backslash.
  Pattern pattern = Pattern.compile("\\|  | - |\\+- |\\/");

  /**
   * Returns the name of the XML element from the Excel cell value. <BR>
   * This method removes "|  ", " - ", "+- " and "/" from the cell value.<BR>
   */
  private String getElementName(String cellValue) {
    String [] values = pattern.split(cellValue);
    if (values != null) {
       for (int i=0; i<values.length; i++) {
         if (values[i].equals("")) continue;
         String name = values[i];
         int arrayIndex = name.indexOf('[');
         if (arrayIndex >= 0) name = name.substring(0, arrayIndex);
         return name;
       }
     }
    return "";
  }

  private void saveXmlFile(File xmlFile) throws IOException {
    FileOutputStream fos = new FileOutputStream(xmlFile);

	//fos.write(xmlBuffer.toString().getBytes("MS932"));
	
	String str = xmlBuffer.toString();
	String utf8String = new String(str.getBytes("UTF-8"),"UTF-8");
	fos.write(utf8String.getBytes("UTF-8"));
    
    fos.flush();
    fos.close();
  }

  // Returns  the type in the PIC clause i.e 'X', '9', 'G' etc.
  private String getTypeFromPicClause(String pic) {
      int bracketStart = pic.indexOf("(");
      if (bracketStart > 0) {
        return pic.substring(bracketStart - 1, bracketStart);
      }
      return "";
  }

  private String getLengthFromPicClause(String pic) {
      int bracketStart = pic.indexOf("(");
      int bracketEnd = pic.indexOf(")");
      if (bracketStart > 0 && bracketEnd > 0 && bracketEnd > bracketStart) {
        return pic.substring(bracketStart + 1, bracketEnd);
      }
      return "";
  }

  /**
   * Validates the element value as per the type and length in PIC clause.
   * @param type   'N', 'X' or 'G'.
   * @param length  the length of element as per PIC clause.
   * @param value   the value.
   * @return error message or blank string if validation succeeds.
   */ 
  private String validateElementValue(String type, String length, String value) {
    try {
      if (value.length() != Integer.parseInt(length)) {
        return "Incorrect length: " + value.length();
      }
    } catch (NumberFormatException e) {
      return "Error reading length in PIC clause"; 
    }

    if (type.equals("9")) {
      for (int i=0; i<value.length(); i++) {
        if (!Character.isDigit(value.charAt(i))) {
          return "Value should contain only digits.";
        }
      }
    } 
    return "";
  }
}
