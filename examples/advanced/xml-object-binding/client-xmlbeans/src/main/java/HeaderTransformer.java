public class HeaderTransformer {

  /**
   * @TODO : Need to think of better way to add the header tags.
   */
  public static String addRequestHeader(String operationName, String xml) {

    StringBuffer buffer = new StringBuffer();

    int headerStart = xml.indexOf("<?");
    int headerEnd = xml.indexOf("?>");
    if (headerStart >= 0 && headerEnd >= 0 && headerEnd + 3 <= xml.length()) {
        String header = xml.substring(headerStart, headerEnd + 2);
        buffer.append(header);

        xml = xml.substring(headerEnd + 3);
    }

    buffer.append("<" + operationName + ">");
    buffer.append("<app-context><userName>makoto</userName></app-context>");
    buffer.append("<app-data>");
    buffer.append(xml);
    buffer.append("</app-data>");
    buffer.append("</" + operationName + ">");

    return buffer.toString();
  }

  /**
   * @TODO : Need to think of better way to remove the header tags.
   */
  public static String removeResponseHeader(String operationName, String xml) {

    String startText = "<" + operationName;
    String endText = "</" + operationName + ">";

    int start = xml.indexOf(startText);
    int end = xml.indexOf(endText);

    if (start >= 0 && end > start) {
        return xml.substring(start, end + endText.length());
    }

    return xml;
  }

}
