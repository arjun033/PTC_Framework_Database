package Demo;


import PTCFramework.ConsumerIterator;
import PTCFramework.PTCFramework;
import PTCFramework.ProducerIterator;

public class RelationToRelationPTC2 extends PTCFramework<byte [], byte []> {

	public RelationToRelationPTC2(ProducerIterator<byte []> pIterator,ConsumerIterator<byte []> cIterator) {
		super(pIterator, cIterator);
	}
	
	public void run(){
		try{
			this.producerIterator.open();
			this.consumerIterator.open();
			while(this.producerIterator.hasNext()){
				byte [] bytes= new byte[31];
				byte [] producerElement= producerIterator.next();
				
				//Extracting the salary from each tuple
				int salary = toInt(producerElement,27);
				//Filtering out those tuples which have salary less than 50000
				if(salary < 50000)
					continue;
				//The first 4 and last 8 bytes of each tuple are extracted through the below loops
				for(int i=0; i<23; i++){
					bytes[i]=producerElement[i];
				}
				for(int i=24; i<27; i++){
					bytes[i]=producerElement[i+4];
				}
				//Send the transformed tuple to the Consumer Iterator
				consumerIterator.next(bytes);
				
			}
			this.producerIterator.close();
			this.consumerIterator.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	//Method to convert to int
	private static int toInt(byte[] bytes, int offset) {
		  int ret = 0;
		  for (int i=0; i<4; i++) {
		    ret <<= 8;
		    ret |= (int)bytes[offset+i] & 0xFF;
		  }
		  return ret;
		}
	
}