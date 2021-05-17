package model.model.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import model.IRDFGenerator;
import model.Triplet;
import services.Utils;

public class LieuBuilder {
	public int    id			= -1;
	public String adresse		= "<NONE>";
	public String codePostal	= "<NONE>";
	
	public LieuBuilder(int id, String adr, String c) {
		this.id 		= id;
		this.adresse 	= adr;
		this.codePostal = c;
	}
}
