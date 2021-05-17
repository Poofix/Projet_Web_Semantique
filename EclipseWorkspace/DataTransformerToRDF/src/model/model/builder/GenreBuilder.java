package model.model.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import model.IRDFGenerator;
import model.Triplet;
import services.Utils;


public class GenreBuilder {
	
	//private static final AtomicInteger count = new AtomicInteger(0); 
	//private String key ;
	
	public int 		id 		= -1;
	public String 	label	= "";
	
	public GenreBuilder(int id, String token) {
		this.id 	= id;
		this.label 	= token;
	}
}
