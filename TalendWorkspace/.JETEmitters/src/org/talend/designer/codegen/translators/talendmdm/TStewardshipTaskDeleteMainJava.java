package org.talend.designer.codegen.translators.talendmdm;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import java.util.List;

public class TStewardshipTaskDeleteMainJava
{
  protected static String nl;
  public static synchronized TStewardshipTaskDeleteMainJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TStewardshipTaskDeleteMainJava result = new TStewardshipTaskDeleteMainJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\t\t\t\tlog.debug(\"";
  protected final String TEXT_2 = " - Deleting the task: \" + (deleted_task_";
  protected final String TEXT_3 = "+1) + \".\");" + NL + "\t\t\t";
  protected final String TEXT_4 = NL + "\t\t\t\tTDSCWS_";
  protected final String TEXT_5 = ".deleteTask(";
  protected final String TEXT_6 = ");" + NL + "\t\t\t\tdeleted_task_";
  protected final String TEXT_7 = "++;" + NL + "\t\t\t";
  protected final String TEXT_8 = NL + "\t\t\t\t\t\t\tTDSCWS_";
  protected final String TEXT_9 = ".deleteTask(";
  protected final String TEXT_10 = ".";
  protected final String TEXT_11 = ");" + NL + "\t\t\t\t\t\t\tdeleted_task_";
  protected final String TEXT_12 = "++;" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_13 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
     
    CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
    INode node = (INode)codeGenArgument.getArgument();
    String cid = node.getUniqueName();	

	boolean isDeleteById = ("true").equals(ElementParameterParser.getValue(node, "__DELETE_BY_ID__"));
	boolean isSpecifyTaskID = ("true").equals(ElementParameterParser.getValue(node, "__SPECIFY_TASK_ID__"));
	String taskID = ElementParameterParser.getValue(node,"__TASK_ID__");
	List<IMetadataTable> metadatas = node.getMetadataList();
	final boolean isLog4jEnabled = ("true").equals(ElementParameterParser.getValue(node.getProcess(), "__LOG4J_ACTIVATE__"));
	if(isDeleteById){
		if (isSpecifyTaskID || (metadatas!=null)&&(metadatas.size()>0)) {//1
        	if(isLog4jEnabled){
			
    stringBuffer.append(TEXT_1);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_3);
    
			}
			if(isSpecifyTaskID){
		    
    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_5);
    stringBuffer.append(taskID);
    stringBuffer.append(TEXT_6);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_7);
    
		    }else{
		    	IMetadataTable metadata = metadatas.get(0);
		    	if (metadata!=null) {//2
			    	List< ? extends IConnection> conns = node.getIncomingConnections();
			    	String idColumn = ElementParameterParser.getValue(node,"__ID_FIELD__");
			    	for (IConnection conn : conns) {//3
			    		if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {//4
						
    stringBuffer.append(TEXT_8);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_9);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_10);
    stringBuffer.append(idColumn);
    stringBuffer.append(TEXT_11);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_12);
    
						}
					}
				}
			}
		}
	}

    stringBuffer.append(TEXT_13);
    return stringBuffer.toString();
  }
}
