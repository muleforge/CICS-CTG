package org.mule.providers.cics.tools.xsd2xls;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.*;

import org.apache.xmlbeans.*;
import java.io.*;
import java.util.*;
import javax.xml.namespace.QName;
import java.math.BigInteger;

/**
 * This program converts an Xsd file to a XLS file, which can be opened in
 * Excel and used to prepare the test data.     <BR>
 *                                              <BR>
 * The XLS file contains 3 columns where:       <BR>
 * Column1: name of the Cobol field (with prefix to indicate nesting level)<BR>
 * Column2: dummy test data                     <BR>
 * Column3: PIC clause e.g X(8), 9(8), etc.     <BR>
 * Copyright: OGIS-RI Co. Ltd (2006) All rights reserved. 
 */
public class Xsd2Xls {

  /**
   * Usage: java Xsd2Xls <xsdFile>
   */
  public static void main(String [] args) throws Exception {
    
     if (args.length < 1) {
       System.out.println("Usage: java Xsd2Xls <xsdFile>");
       System.exit(1);
     } 

	 File xsdFile = new File(args[0]);
	 String xlsFileName = getXlsFileName(xsdFile.getName());
     new Xsd2Xls().process(xsdFile, new File(xlsFileName));
  }

  private static String getXlsFileName(String xsdFile) {
	 int dot = xsdFile.indexOf('.');
	 if (dot >= 0)
	   return xsdFile.substring(0, dot) + ".xls";
	 else 
	   return xsdFile + ".xls";
  }

  private HSSFSheet xlsSheet;
  private HSSFWorkbook xlsWorkbook;
  private int currentRowNo = 0;
  private HSSFCellStyle boldStyle;

  /** Constructor */
  public Xsd2Xls() {
      this.xlsWorkbook = new HSSFWorkbook();
      this.xlsSheet = xlsWorkbook.createSheet("Sheet 1");
      setColumnWidth(0, 20);
      setColumnWidth(1, 40);
      setColumnWidth(2, 8);
      HSSFFont boldFont = this.xlsWorkbook.createFont();
      boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
      this.boldStyle = this.xlsWorkbook.createCellStyle();
      this.boldStyle.setFont(boldFont);  
  }

  private void setColumnWidth(int col, int widthInChars) {
    this.xlsSheet.setColumnWidth((short) col, (short) (widthInChars * 256));
  }

  /**
   * Reads the Xsd file and generates corresponding Xls file.
   * @param xsdFile  XSD File (Input file)
   * @param xlsFile  Excel file (Output file)
   */
  public void process(File xsdFile, File xlsFile) throws Exception {

     SchemaTypeSystem sts = XmlBeans.compileXsd(new XmlObject[]
                            { XmlObject.Factory.parse(xsdFile) },
                            XmlBeans.getBuiltinTypeSystem(), null);

     List allSeenTypes = new ArrayList();
     allSeenTypes.addAll(Arrays.asList(sts.documentTypes()));
     allSeenTypes.addAll(Arrays.asList(sts.globalTypes()));
     for (int i = 0; i < allSeenTypes.size(); i++)
     {
         SchemaType sType = (SchemaType) allSeenTypes.get(i); 
         processSchemaType("", sType);
     }

     SchemaType rootType = (SchemaType) sts.documentTypes()[0]; 
	 String namespace = getNamespaceOfSchemaType(rootType);
	 writeNamespaceInFirstRow(namespace);
     saveXlsFile(xlsFile);
  }

  /**
   * This method recursively processes each schema type in the XSD file.
   */  
  private void processSchemaType(String padding, SchemaType sType) {
     int maxOccurs = getMaxOccurs(sType);
     if (maxOccurs > 1) {
       processArraySchemaType(padding, sType);
       return;
     }

     String name = getNameOfSchemaType(sType);
     createExcelRow(padding, name, sType);
     SchemaType[] anonTypes = sType.getAnonymousTypes();
     if (!name.equals("")) padding = padding + "|  ";
     for (int i=0; i<anonTypes.length; i++) {
          // recursively prints nested types.
          processSchemaType(padding, anonTypes[i]);
     }
  }

  /**
   * Creates a new row in the Excel worksheet.
   * @param   the padding to add to the first column.
   * @param 
   */
  private void createExcelRow(String padding, String name, SchemaType sType) {
     if (name.equals("")) return;

     HSSFRow row = this.xlsSheet.createRow(this.currentRowNo);
     this.currentRowNo++;

     String type = getDefaultValueOfAttribute(sType, "type");
     String length = getDefaultValueOfAttribute(sType, "length");
     if (!type.equals("")) {
         String pic = "PIC " + type + "(" + length + ")";
         String data = getSampleTestData(type, length);
         createExcelCell(row, 0, padding + " - " + name); 
         createExcelCell(row, 1, data);
         createExcelCell(row, 2, pic);
     } else {
         HSSFCell cell = createExcelCell(row, 0, padding + "+- " + name + "/");
         cell.setCellStyle(this.boldStyle);
     }
  }

  private HSSFCell createExcelCell(HSSFRow row, int cellNo, String cellValue) {
     HSSFCell cell = row.createCell((short) cellNo);
     cell.setEncoding(HSSFCell.ENCODING_UTF_16);
     cell.setCellValue(cellValue);
     return cell; 
  }

  private void processArraySchemaType(String padding, SchemaType sType) {
     int arraySize = getMaxOccurs(sType);
     String name = getNameOfSchemaType(sType);

     String childPadding = padding + "|  ";
     for (int index=0; index<arraySize; index++) {
         String nameWithIndex = name + "[" + index + "]";
         createExcelRow(padding, nameWithIndex, sType);
         SchemaType[] anonTypes = sType.getAnonymousTypes();
         for(int i=0; i<anonTypes.length; i++) {
           // recursively prints nested types.
           processSchemaType(childPadding, anonTypes[i]);
         }
     }
  }

  private String getNameOfSchemaType(SchemaType sType) {
    SchemaField schemaField = sType.getContainerField();
    if (schemaField != null) {
        QName name = schemaField.getName();
        if (name != null) return name.getLocalPart(); 
    }
    return "";
  }

  private String getNamespaceOfSchemaType(SchemaType sType) {
  
	if (sType.isDocumentType()) {
      QName name = sType.getDocumentElementName();
	  if (name != null) {
			return name.getNamespaceURI(); 
	  }
	}
	  
    return "";
  }

  private int getMaxOccurs(SchemaType sType) {
    SchemaField schemaField = sType.getContainerField();
    if (schemaField != null) {
        BigInteger maxOccurs = schemaField.getMaxOccurs();
        if (maxOccurs != null) return maxOccurs.intValue();
    }
    return 1;
  }

  private String getDefaultValueOfAttribute(SchemaType sType, String sAttributeName) {
    SchemaProperty[] attrs = sType.getAttributeProperties();
    for (int i=0; i< attrs.length; i++) {
      SchemaProperty attr = attrs[i];
      QName attrName = attr.getName();
      if (attrName != null && attrName.getLocalPart().equals(sAttributeName)) {
        return attr.getDefaultText();
      }
    }

    return "";
  }

  /**
   * @param  type   'X' for character data, 'N' for numeric data.
   * @param  length  the length of the data.
   */
  private String getSampleTestData(String type, String length) {

    try {
      char letter = 'x';
      if (type.equalsIgnoreCase("X")) {
        letter = 'A';
      } else if (type.equalsIgnoreCase("9")) {
        letter = '1';
      } else if (type.equalsIgnoreCase("G")) {
        letter = 'あ';
      }
  
      int len = Integer.parseInt(length);
      char [] buf = new char[len]; 
      for (int i=0; i<len; i++) 
          buf[i] = letter;
      return new String(buf);
    } catch (Exception e) {
      e.printStackTrace();
    } 

    return "";
  }

  private void writeNamespaceInFirstRow(String namespace) {
     HSSFRow row = this.xlsSheet.createRow(0);
     createExcelCell(row, 1, namespace);
  }

  private void saveXlsFile(File xlsFile) throws IOException {
      FileOutputStream fileOut = new FileOutputStream(xlsFile);
      xlsWorkbook.write(fileOut);
      fileOut.close();
  }
}
