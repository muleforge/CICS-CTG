package org.mule.providers.cics.tools.cb2xsd;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;

import java.io.*;

/**
 * This program is used to convert Cobol copybook files into XSD. (XML schema definition) <BR>
 * This class uses the Cb2XML program (sourceforget.net/projects/cb2xml)<BR>
 * The Cobol file is converted into XML by the Cb2XML program. <BR>
 * The XML is then converted into XSD using a XSL stylesheet.
 */
public class Cb2Xsd {

  /** The default stylesheet used to produce the XSD file */
  private static final String DEFAULT_XSLT_FILE = "/META-INF/cb2xsd.xsl";
  private static final String DEFAULT_NAMESPACE_PREFIX = "http://ogis-ri.co.jp/";

  /** The stylesheet used to produce the XSD file with annotation comments. */
  private static final String ANNOTATION_XSLT_FILE = "/META-INF/cb2xsd_annotation.xsl";
  Transformer xsltTransformer;

  /**
   * Main method (Entry point to the Cb2Xsd program)
   */
  public static void main(String [] args) throws Exception {

    if (args.length < 1) {
      printUsage();
      System.exit(1);
    }
	System.out.println("Test");
    CommandArgs commandArgs = null;
    try {
      commandArgs = new CommandArgs(args);
    } catch (IllegalArgumentException e) {
      System.out.println();
      System.out.println("Invalid command arguments: " + e.getMessage()); 
      printUsage();
      System.exit(1);
    }

    String xsltFile = (commandArgs.generateAnnotation()) ? ANNOTATION_XSLT_FILE : DEFAULT_XSLT_FILE;
    InputStream xsltStream = Cb2Xsd.class.getResourceAsStream(xsltFile); 
    Cb2Xsd cb2xsd = new Cb2Xsd(xsltStream, commandArgs.getNamespacePrefix());

    if (!commandArgs.processDirectory()) {
      File cobolFile = commandArgs.getInputFile(); 
      File xsdFile = new File(getXsdFileName(cobolFile));
      cb2xsd.generateXsd(cobolFile, xsdFile);
    } else {
      File inputDir = commandArgs.getInputDir();
      File outputDir = commandArgs.getOutputDir();
      File [] cobolFiles = inputDir.listFiles();
      for (int i=0; i<cobolFiles.length; i++) {
        File cobolFile = cobolFiles[i];
        if (cobolFile.isFile()) {
          File xsdFile = new File(outputDir, getXsdFileName(cobolFile));
          cb2xsd.generateXsd(cobolFiles[i], xsdFile);
        }
      }
    }
  }

  /**
   * Returns the name of Xsd file to create for the given Cobol copybook file.
   * The xsd file has the same file name with different extension (.xsd)
   */
  private static String getXsdFileName(File cobolFile) {
    String cobolName = cobolFile.getName();
    if (cobolName.endsWith("xsd")) {
      return cobolName + ".xsd";
    }
   
    int dot = cobolName.lastIndexOf(".");
    String nameWithoutExtension = cobolName.substring(0, dot);
    return nameWithoutExtension + ".xsd"; 
  }

  /** Prints the Usage for this program */
  public static void printUsage() {
      System.out.println();
      System.err.println("Usage: java " + Cb2Xsd.class.getName() + 
                         " [-options] [cobolFile]");
      System.out.println();
      System.out.println("where options include:");
      System.out.println("   -input_dir            directory containing Cobol files");
      System.out.println("   -output_dir           directory to generate output files");
      System.out.println("   -annotation           generate annotation (documention) in XSD");
      System.out.println("   -namespace_prefix     target namespace of the output XSD file");
  }


  /**
   * Constructor
   * xsltInputSteam   XSLT used to transform output of Cb2XML into XSD.
   * namespacePrefix  Namespace prefix to be used in output XSD file. 
   */
  public Cb2Xsd(InputStream xsltInputStream, String namespacePrefix) throws Exception {

    String xslt = readInputStreamIntoString(xsltInputStream); 
    if (namespacePrefix != null && !namespacePrefix.equals("")) {
        if (!namespacePrefix.endsWith("/")) {
            namespacePrefix = namespacePrefix + "/";
		}
		xslt = xslt.replaceAll(DEFAULT_NAMESPACE_PREFIX, namespacePrefix);
	}
    Source xsltSource = new StreamSource(new StringReader(xslt));
    TransformerFactory factory = TransformerFactory.newInstance();
    this.xsltTransformer = factory.newTransformer(xsltSource);

    this.xsltTransformer.setOutputProperty("encoding","Shift_JIS");

    //xsltTransformer.setOutputProperty(OutputKeys.ENCODING, "MS932"); 
    //xsltTransformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); 
    xsltTransformer.setOutputProperty(OutputKeys.METHOD, "xml"); 
    xsltTransformer.setOutputProperty(OutputKeys.INDENT, "yes");  
    xsltTransformer.setOutputProperty( "{http://xml.apache.org/xslt}indent-amount", "2" );
  }

  private String readInputStreamIntoString(InputStream is) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(is,"MS932"));
	StringBuffer buffer = new StringBuffer();
	int c;
	while ((c = reader.read()) != -1) {
        buffer.append((char) c);
	}
	reader.close();
	return buffer.toString();
  }

  /**
   * Generates an XSD file for the specified cobol copybook file.
   * @param cobolFile   the cobol copy book file.
   * @param xsdFile     the output XSD file to create.
   */ 
  public void generateXsd(File cobolFile, File xsdFile) throws Exception {

	String xml = net.sf.cb2xml.Cb2Xml.convertToXMLString(cobolFile, false);
    if (xml == null || xml.equals("")) {
      throw new Exception("Error parsing Cobol data file" + cobolFile.getName());
    }
 
    Source xmlSource = new StreamSource(new StringReader(xml));
    Result result = new StreamResult(xsdFile);
    this.xsltTransformer.transform(xmlSource, result);
  }

  /**
   * Inner class used to validate/initialize Cb2Xsd command arguments as per
   * arguments passed in the command line.
  */
  static class CommandArgs {
 
    private File inputDir;
    private File outputDir;
    private boolean bGenerateAnnotation = false;
    private boolean bProcessDirectory = false;
    private File inputFile;
	private String namespacePrefix = "";

    /**
     * Constructor
     * args  arguments passed to the main method.
     * @throws IllegalArgumentException if the arguments are invalid/incomplete.
     */
    public CommandArgs(String[] args) throws IllegalArgumentException {

      validate(args);
 
      for (int i=0; i<args.length; i++) {
        if (args[i].equals("-input_dir")) {
            inputDir = new File(args[i+1]); 
            bProcessDirectory = true;
            i++; 
        }
        else if (args[i].equals("-output_dir")) {
           outputDir = new File(args[i+1]); 
           i++; 
        }
        else if (args[i].equals("-namespace_prefix")) {
           namespacePrefix = args[i+1]; 
           i++; 
        }
        else if (args[i].equals("-annotation")) {
           bGenerateAnnotation = true;
        } else {
          inputFile = new File(args[i]);
          //if (!inputFile.isFile()) {
          //  throw new IllegalArgumentException("The file " + args[i] + " could not be found.");
          //}           
        }
      }

      if (inputDir != null && outputDir == null) {
        throw new IllegalArgumentException("The option -output_dir is also required.");
      }
      else if (outputDir != null && inputDir == null) {
        throw new IllegalArgumentException("The option -input_dir is also required.");
      }
    }
 
    /**
     * Validates the command arguments.
     * args  arguments passed to the main method.
     */
    private void validate(String [] args) throws IllegalArgumentException {

      for (int i=0; i<args.length; i++) {
        if (args[i].equals("-input_dir")) {
            if (i + 1 >= args.length) {
              throw new IllegalArgumentException("-input_dir requires directory specification.");
            }

            File inputDir = new File(args[i+1]);
            if (!inputDir.isDirectory()) {
              throw new IllegalArgumentException("The directory " + args[i+1] + " could not be found.");
            };
            i++;
        }
        else if (args[i].equals("-output_dir")) {
            if (i + 1 >= args.length) {
              throw new IllegalArgumentException("-output_dir requires directory specification."); 
            }

            File outputDir = new File(args[i+1]);
            if (!outputDir.isDirectory()) {
              throw new IllegalArgumentException("The directory " + args[i+1] + " could not be found.");
            }
            i++;
        }
        else if (args[i].equals("-namespace_prefix")) {
            if (i + 1 >= args.length) {
              throw new IllegalArgumentException("-namespace_prefix requires 'namespace prefix'specification."); 
            }
            i++;
        }
        else if (args[i].equals("-annotation")) {
        }
        else if (args[i].startsWith("-")) {
            throw new IllegalArgumentException("Invalid option " + args[i]);
        }
      }
    }

    public File getInputFile() {
        return this.inputFile;
    }

    public File getInputDir() {
        return this.inputDir;
    }

    public File getOutputDir() {
        return this.outputDir;
    }

	public String getNamespacePrefix() {
        return this.namespacePrefix;
    }

    public boolean generateAnnotation() {
        return this.bGenerateAnnotation;
    }

    public boolean processDirectory() {
        return this.bProcessDirectory;
    }
  }
}
