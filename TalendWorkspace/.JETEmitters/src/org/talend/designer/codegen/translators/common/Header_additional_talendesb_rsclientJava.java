package org.talend.designer.codegen.translators.common;

import java.util.Vector;
import java.util.List;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.designer.codegen.config.CodeGeneratorArgument;

public class Header_additional_talendesb_rsclientJava
{
  protected static String nl;
  public static synchronized Header_additional_talendesb_rsclientJava create(String lineSeparator)
  {
    nl = lineSeparator;
    Header_additional_talendesb_rsclientJava result = new Header_additional_talendesb_rsclientJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "private java.util.Map<String, String> stsProperties;" + NL + "" + NL + "public void setStsProperties(java.util.Map<String, String> stsProperties) {" + NL + "\t\tthis.stsProperties = stsProperties;" + NL + "}";
  protected final String TEXT_2 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
Vector v = (Vector) codeGenArgument.getArgument();
IProcess process = (IProcess) v.get(0);

boolean useSAML = false;

List<? extends INode> restClients = process.getNodesOfType("tRESTClient");
for (INode restClient : restClients) {
	boolean useAuth = Boolean.valueOf(ElementParameterParser.getValue(restClient, "__NEED_AUTH__"));
	String authType = ElementParameterParser.getValue(restClient, "__AUTH_TYPE__");
	if (true) {
		useSAML = true;
		break;
	}
}

if (useSAML) { 
    stringBuffer.append(TEXT_1);
     } 
    stringBuffer.append(TEXT_2);
    return stringBuffer.toString();
  }
}
