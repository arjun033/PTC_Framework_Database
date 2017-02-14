package Demo;

import Demo.GetTupleFromRelationIterator2;

public class PrintRelation3{
	public static void main(String args[]) throws Exception{
		System.out.println("The tuples after loading file to Relation are: ");
		GetTupleFromRelationIterator2 getTupleFromRelationIterator= new GetTupleFromRelationIterator2("myDisk1", 27, 21);
		getTupleFromRelationIterator.open();

		while(getTupleFromRelationIterator.hasNext()){
			byte [] tuple = getTupleFromRelationIterator.next();
			System.out.println(new String(tuple).substring(0, 23)+", "+ toInt(tuple, 23));
		}
	}
	
	
	private static int toInt(byte[] byteArray, int offset) {
	  int num = 0;
	  for (int i=0; i<4; i++) {
	    num <<= 8;
	    num |= (int)byteArray[offset+i] & 0xFF;
	  }
	  return num;
	}
}
