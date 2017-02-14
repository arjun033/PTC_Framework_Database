# PTC_Framework_Database

Project details

Storage
------------
The code for the storage is available in the folder named StorageManager. It consists of the function "CreateStorage" to create the storage. The storage is implemented as "Storage" class. After the storage has been created it can to be loaded for use by using Load Storage command. In the code provided to you, the storage is created by the following Java code:

Storage s1 = new Storage();
s1.CreateStorage("myDisk", 1024, 1024*100);

Note the parameter values "myDisk", 1024, and 1024*1000.

The storage will be created in the myDisk binary file which resides in the same folder from where it is created. A path relative to this folder or an absolute folder can be used. When you are completely done with the storage, you may delete the file or the folder where the storage resides. Otherwise this part of the disk will become a memory leak.
The constant 1024 specifies the size of a page in bytes. All pages have the same number of bytes. This is also a user choice: a different size can be specified.
Next, 1024 * 100 (= 102400) is the size of the storage to be available to a user. One could specified the size as102400 - in the context the intention is to make it clear that 100 pages of 1024 bytes are desired for use by the user. This can be tweaked if one desires. To this some book keeping information is added. This will be explained shortly.
The Storage class consists of pages with addresses. The following page level operations are allowed. (You will find ample of examples of code using these functions in the Demo folder.)

ReadPage (n,b) to read page number n in buffer b. Buffer b is a java variable consisting of an array of bytes - of same size as a page. This function copies the contents of Page n on the disk into the buffer b.
WritePage (n,b) copies the contents of buffer b into Page n.
AllocatePage (). On execution, this function finds a page and returns its address to the user. The meaning of this is that this page now belongs to the user. The user must use the page - otherwise it will lead to memory leak.
DeallocatePage (n) will return page n to the storage for future use of the users.
The first 4 bytes hold the page size (1024 in our example); the next 4 bytes hold the number of pages, which is 100 in our example. The next 8 bytes are currently unused. These 16 bytes are followed by an array of bits, 1 bit per page (100 bits in our example). These bits constitute the bit map. The purpose of the bitmap is to keep track of which pages are being used and the ones that are available. On creation of storage all bits are set to 0. On execution of AllocatePage, the storage system finds the first bit that is 0, sets it to 1, and returns the bit number to the user. Deallocate (n) simply sets the bit number n to 0 indicating that the corresponding page is now available for use.

In addition, to the above, a function PrintStats () is available. This prints the counts of numbers of pages, read, written, allocated, and deallocated. The counts are initialized to 0 when the storage loaded. The counts can be printed at any point in a program to gage at concerns like memory leak (lost pages) and performance.

Storing a relation
--------------------
As shown in lecture slides, a relation is stored as a sequence of pages. The NextPage pointer in the last page of the sequence is -1 that stands for null. In the absence of a directory facility, for all practical purposes the first page in the sequence is synonym with a relation itself.

The PTC framework
-----------------------
Imagine two persons P and C (called producer and consumer, respectively). Initially, P has a bin full of balls, and that the balls need to be transferred to C who puts then into its own bin. We can realize P and C as iterators in the following way: P (the producer) gets started by arranging a thrower, a mechanical device, sees the bin and realizes that there are ball(s) that need to be taken care of, and loads the first ball into the thrower. (This is analogous to open function) It throws the ball to the consumer who catches it and puts it in its own bin. The producer than loads the next ball on the thrower (an pauses). This process is repeated. When P throws the last ball, it known that there are no more balls and does not attempt to throw another ball after this. This is analogous to the next() function of an iterator.
After all balls are finished, P returns the thrower device back to its owner and quits. This is analogous to the close() function of an iterator. Thus we see that the behavior of P is like that of an iterator. Similarly, C can be seen as an iterator as well. Thus we have a producer and consumer part who can work in tandem to finish the transfer of balls. To this we can add another person T (called a transformer). Now P throws the ball not directly to C but to transformer T. On receiving the ball, the transformer verifies if the ball is of acceptable quality. if not then T destroys the ball; otherwise paints it with a some color and makes it available to C.  Now we have the PTC framework. In the PTC framework, P and C are iterators and T is a transformer. The framework for PTC is setup in the code available in PTCFramwork folder. ProducerIterator.java and ConsumerIterator.Java are simply interfaces that extend Java iterators by adding Open() and Close() functions. Unlike Java, we insist on having the Open() and Close() functions as it is a good practice. These functions often  do some nontrivial work. The PTC framework presupposes supply of two iterators; the framework itself is simply a configuring mechanism that allows easy use and reuse of iterators. The code for transformation has to be written by the user. Note that the transformer does not always make an object available to the consumer. This means that the implementation of transformer may involve a conditional - an if statement.


Collaborator : Prasanna Desai (Iowa State University, Dept. of Computer Science)
Thanks to Prof. Shashi Gadia for providing us with the problem statement.
