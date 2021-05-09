package org.talend.designer.codegen.translators.talendmdm;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import java.util.List;

public class TStewardshipTaskDeleteBeginJava
{
  protected static String nl;
  public static synchronized TStewardshipTaskDeleteBeginJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TStewardshipTaskDeleteBeginJava result = new TStewardshipTaskDeleteBeginJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\tint deleted_task_";
  protected final String TEXT_2 = " = 0;\t" + NL + "        java.net.Authenticator.setDefault(new java.net.Authenticator() {" + NL + "            protected java.net.PasswordAuthentication getPasswordAuthentication() {" + NL + "            ";
  protected final String TEXT_3 = NL + "                ";
  protected final String TEXT_4 = " " + NL + "\tfinal String decryptedPassword_";
  protected final String TEXT_5 = " = routines.system.PasswordEncryptUtil.decryptPassword(";
  protected final String TEXT_6 = ");";
  protected final String TEXT_7 = NL + "\tfinal String decryptedPassword_";
  protected final String TEXT_8 = " = ";
  protected final String TEXT_9 = "; ";
  protected final String TEXT_10 = NL + "                        " + NL + "            \treturn new java.net.PasswordAuthentication(";
  protected final String TEXT_11 = ", decryptedPassword_";
  protected final String TEXT_12 = ".toCharArray());" + NL + "        }});" + NL + "\t\t" + NL + "\t\tjava.net.URL wsdlUrl_";
  protected final String TEXT_13 = " = new java.net.URL(";
  protected final String TEXT_14 = ");" + NL + "\t\t" + NL + "\t\t";
  protected final String TEXT_15 = "\t" + NL + "\t\t\tlog.info(\"";
  protected final String TEXT_16 = " - Attempt to get a task delete service from: \" + wsdlUrl_";
  protected final String TEXT_17 = " + \".\");" + NL + "\t\t";
  protected final String TEXT_18 = NL + "\t\torg.talend.datastewardship.server.ws.TDSCWS_Service service_";
  protected final String TEXT_19 = " = new org.talend.datastewardship.server.ws.TDSCWS_Service(wsdlUrl_";
  protected final String TEXT_20 = ");" + NL + "\t\torg.talend.datastewardship.server.ws.TDSCWS TDSCWS_";
  protected final String TEXT_21 = " = service_";
  protected final String TEXT_22 = ".getTDSCWSImplPort();" + NL + "\t\t";
  protected final String TEXT_23 = "\t" + NL + "\t\t\tlog.info(\"";
  protected final String TEXT_24 = " - Got successfully.\");" + NL + "\t\t\tlog.info(\"";
  protected final String TEXT_25 = " - Start to delete tasks from server.\");" + NL + "\t\t";
  protected final String TEXT_26 = NL + "\t\t\tString startDateString_";
  protected final String TEXT_27 = " = ";
  protected final String TEXT_28 = ";" + NL + "\t\t\tString endDateString_";
  protected final String TEXT_29 = " = ";
  protected final String TEXT_30 = ";" + NL + "\t\t\tjavax.xml.datatype.DatatypeFactory factory_";
  protected final String TEXT_31 = " = javax.xml.datatype.DatatypeFactory.newInstance();" + NL + "\t    \tjavax.xml.datatype.XMLGregorianCalendar startXMLGregorianCalendar_";
  protected final String TEXT_32 = " = null;" + NL + "\t    \tjavax.xml.datatype.XMLGregorianCalendar endXMLGregorianCalendar_";
  protected final String TEXT_33 = " = null;" + NL + "\t    \tif(startDateString_";
  protected final String TEXT_34 = "!=null && !startDateString_";
  protected final String TEXT_35 = ".equals(\"\")){" + NL + "\t    \t\tjava.text.SimpleDateFormat startdf_";
  protected final String TEXT_36 = " = new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\");" + NL + "\t\t\t\tstartdf_";
  protected final String TEXT_37 = ".parse(startDateString_";
  protected final String TEXT_38 = ");" + NL + "\t\t\t\tjava.util.Calendar startCal_";
  protected final String TEXT_39 = " = startdf_";
  protected final String TEXT_40 = ".getCalendar();" + NL + "\t\t\t\tjava.util.GregorianCalendar startDate_";
  protected final String TEXT_41 = " = (java.util.GregorianCalendar) startCal_";
  protected final String TEXT_42 = ";" + NL + "\t\t\t\tstartXMLGregorianCalendar_";
  protected final String TEXT_43 = " = factory_";
  protected final String TEXT_44 = ".newXMLGregorianCalendar(startDate_";
  protected final String TEXT_45 = ");" + NL + "\t\t\t}" + NL + "\t    \tif(endDateString_";
  protected final String TEXT_46 = "!=null && !endDateString_";
  protected final String TEXT_47 = ".equals(\"\")){" + NL + "\t        \tjava.text.SimpleDateFormat enddf_";
  protected final String TEXT_48 = " = new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\");" + NL + "\t            enddf_";
  protected final String TEXT_49 = ".parse(endDateString_";
  protected final String TEXT_50 = ");" + NL + "\t            java.util.Calendar endCal_";
  protected final String TEXT_51 = " = enddf_";
  protected final String TEXT_52 = ".getCalendar();" + NL + "\t            java.util.GregorianCalendar endDate_";
  protected final String TEXT_53 = " = (java.util.GregorianCalendar) endCal_";
  protected final String TEXT_54 = ";" + NL + "\t\t\t\tendXMLGregorianCalendar_";
  protected final String TEXT_55 = " = factory_";
  protected final String TEXT_56 = ".newXMLGregorianCalendar(endDate_";
  protected final String TEXT_57 = ");" + NL + "\t\t\t}" + NL + "\t\t\tdeleted_task_";
  protected final String TEXT_58 = " = TDSCWS_";
  protected final String TEXT_59 = ".deleteTasks(";
  protected final String TEXT_60 = ",";
  protected final String TEXT_61 = ",";
  protected final String TEXT_62 = ",startXMLGregorianCalendar_";
  protected final String TEXT_63 = ",endXMLGregorianCalendar_";
  protected final String TEXT_64 = ",";
  protected final String TEXT_65 = ",";
  protected final String TEXT_66 = ",";
  protected final String TEXT_67 = ");" + NL + "\t\t";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
     
    CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
    INode node = (INode)codeGenArgument.getArgument();
    String cid = node.getUniqueName();	

	String url = ElementParameterParser.getValue(node, "__URL__");
	String taskType = ElementParameterParser.getValue(node, "__TASKTYPE__");
	String owner = ElementParameterParser.getValue(node,"__OWNER__");
	String tag = ElementParameterParser.getValue(node, "__TAG__");
	String startDate = ElementParameterParser.getValue(node, "__START_DATETIME__");
	String endDate = ElementParameterParser.getValue(node, "__END_DATETIME__");
	String starRanking = ElementParameterParser.getValue(node, "__STAR_RANKING__");
	String limit = ElementParameterParser.getValue(node,"__LIMIT__");
	String status = ElementParameterParser.getValue(node,"__STATUS__");
	String username = ElementParameterParser.getValue(node, "__USERNAME__");
	String password = ElementParameterParser.getValue(node, "__PASSWORD__");
	boolean targetRecordOnly = ("true").equals(ElementParameterParser.getValue(node,"__TARGET_RECORD_ONLY__"));
	boolean isDeleteById = ("true").equals(ElementParameterParser.getValue(node, "__DELETE_BY_ID__"));
	boolean isLog4jEnabled = ("true").equals(ElementParameterParser.getValue(node.getProcess(), "__LOG4J_ACTIVATE__"));

    stringBuffer.append(TEXT_1);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_2);
    
                String passwordFieldName = "__PASSWORD__";
                
    stringBuffer.append(TEXT_3);
    if (ElementParameterParser.canEncrypt(node, passwordFieldName)) {
    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_5);
    stringBuffer.append(ElementParameterParser.getEncryptedValue(node, passwordFieldName));
    stringBuffer.append(TEXT_6);
    } else {
    stringBuffer.append(TEXT_7);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_8);
    stringBuffer.append( ElementParameterParser.getValue(node, passwordFieldName));
    stringBuffer.append(TEXT_9);
    }
    stringBuffer.append(TEXT_10);
    stringBuffer.append(username);
    stringBuffer.append(TEXT_11);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_12);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_13);
    stringBuffer.append(url );
    stringBuffer.append(TEXT_14);
    
		if(isLog4jEnabled){
		
    stringBuffer.append(TEXT_15);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_16);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_17);
    
		}
		
    stringBuffer.append(TEXT_18);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_19);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_20);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_21);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_22);
    
		if(isLog4jEnabled){
		
    stringBuffer.append(TEXT_23);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_25);
    
		}
		if(!isDeleteById){
		
    stringBuffer.append(TEXT_26);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_27);
    stringBuffer.append(startDate.equals("")?null:startDate);
    stringBuffer.append(TEXT_28);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_29);
    stringBuffer.append(endDate.equals("")?null:endDate);
    stringBuffer.append(TEXT_30);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_31);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_32);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_33);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_34);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_35);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_36);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_39);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_40);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_41);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_42);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_43);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_44);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_45);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_46);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_47);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_48);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_49);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_50);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_51);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_52);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_53);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_54);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_55);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_56);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_57);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_58);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_59);
    stringBuffer.append(taskType);
    stringBuffer.append(TEXT_60);
    stringBuffer.append(owner.equals("")?null:owner);
    stringBuffer.append(TEXT_61);
    stringBuffer.append(tag.equals("")?null:tag);
    stringBuffer.append(TEXT_62);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_63);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_64);
    stringBuffer.append(starRanking);
    stringBuffer.append(TEXT_65);
    stringBuffer.append(limit);
    stringBuffer.append(TEXT_66);
    stringBuffer.append(status.equals("null")?null:"\""+status+"\"");
    stringBuffer.append(TEXT_67);
    
		}
		
    return stringBuffer.toString();
  }
}
