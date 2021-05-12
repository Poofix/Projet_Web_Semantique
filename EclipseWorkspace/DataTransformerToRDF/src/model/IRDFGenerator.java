package model;

import java.util.List;

public interface IRDFGenerator {

	public abstract List<Triplet> generateRDFTriplet();
	public abstract String getKey();
}
