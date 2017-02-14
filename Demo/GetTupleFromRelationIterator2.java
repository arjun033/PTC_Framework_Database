package Demo;

import PTCFramework.ProducerIterator;
import StorageManager.Storage;

public class GetTupleFromRelationIterator2
implements ProducerIterator<byte[]> {
    int currPage = 0;
    int nxtPage = -1;
    int tupleLen;
    int pageSize;
    int maxTupleCount;
    int readTupleCount = 0;
    String fileName;
    Storage storage;
    byte[] buffer;

    public GetTupleFromRelationIterator2(String fileName, int tupleLen, int currPage) throws Exception {
        this.fileName = fileName;
        this.tupleLen = tupleLen;
        this.currPage = currPage;
        
        this.storage = new Storage();
        this.storage.LoadStorage(fileName);
        this.pageSize = this.storage.pageSize;
    }
    
    //Overriden from ProducerIterator
    public void open() throws Exception {
        this.buffer = new byte[1024];
        this.storage.ReadPage((long)this.currPage, this.buffer);
        this.nxtPage = this.toInt(this.buffer, 4);
        this.maxTupleCount = this.toInt(this.buffer, 0);
        this.readTupleCount = 0;
    }
    
    //Overriden from Iterator
    public boolean hasNext() {
        if (this.readTupleCount == this.maxTupleCount && this.nxtPage == -1) {
            return false;
        }
        return true;
    }
    
    //Overriden from Iterator
    public byte[] next() {
    	byte[] byteArray = null;
        if (this.maxTupleCount == this.readTupleCount) {
            if (this.nxtPage == 0) {
                return null;
            }
            this.buffer = new byte[this.pageSize];
            try {
                this.storage.ReadPage((long)this.nxtPage, this.buffer);
                this.currPage = this.nxtPage;
                this.nxtPage = this.toInt(this.buffer, 4);
                this.maxTupleCount = this.toInt(this.buffer, 0);
                this.readTupleCount = 0;
                
                byteArray = new byte[this.tupleLen];
                for (int i=0; i<this.tupleLen; ++i) {
                    byteArray[i] = this.buffer[this.tupleLen*this.readTupleCount+8+i];
                }
                ++this.readTupleCount;
                return byteArray;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            byteArray = new byte[this.tupleLen];
            for (int i=0; i<this.tupleLen; ++i) {
                byteArray[i] = this.buffer[this.tupleLen*this.readTupleCount+8+i];
            }
            ++this.readTupleCount;
            return byteArray;
        }
        return null;
    }
    
    //Overriden from Iterator
    public void remove() {
    }
    
    //Overriden from ProducerIterator
    public void close() throws Exception {
    }
    
    //Converts byte to int
    private int toInt(byte[] byteArray, int n) {
        int num = 0;
        for (int i=0; i<4; ++i) {
            num <<= 8;
            num |= byteArray[n+i] & 255;
        }
        return num;
    }
}
