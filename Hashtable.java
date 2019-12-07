import java.util.*;

public class Hashtable
{
    private double LOAD_THRESHHOLD = 0.5;
    private ArrayList<HashNode>bucket;  
    private int entries;
    private int numOfBuckets;

    public Hashtable() //Constructor
    {
        bucket = new ArrayList<HashNode>();
        numOfBuckets = 2029; //prime number of buckets
        entries = 0;
        for (int i=0; i < numOfBuckets; i++)
        {
            bucket.add(null); //initialize buckets to null
        }
    }

    public boolean containsKey(String key)
    {
        HashNode head = bucket.get(getHash(key)); //set the head of the bucket to try and traverse
        while (head != null) //if the bucket is not empty
        {
            if (head.key.equals(key)) // if it is there return it
            {
                return true;
            }
            head = head.next; //traverse list
        }
        return false;
    }

    public String get(String key)
    {
        HashNode head = bucket.get(getHash(key)); //head of the bucket
        while(head!=null) //if the bucket isn't empty
        {
            if((head.key).equals(key)) //if it equals the key
            {
                return head.value; //return value
            }
            head = head.next; //traverse linked list
        }
        return null; //if cannot find return null
    }

    public void put(String key, String value)
    {
        HashNode head = bucket.get(getHash(key));
        while (head != null)    
        {
            if (head.key.equals(key))
            {
                head.value = value; //if it already exists then return.
                return;
            }
            head = head.next; //traverse list
        }
        head = bucket.get(getHash(key)); //set the head back to the front
        HashNode node = new HashNode(key, value); //make new node and set that as the head with the next being the current head
        node.next = head;
        bucket.set(getHash(key), node);
        ++entries; //increase number of entries

        if (((entries*1.0)/ bucket.size()) >= LOAD_THRESHHOLD) //if the lamda is greater than the threshhold
        {
            ArrayList<HashNode> tempBucket = bucket;//temp bucket to store data
            numOfBuckets = numOfBuckets * 2 + 1; //grow the bucket
            bucket = new ArrayList<HashNode>(numOfBuckets); //set the new bucket to that new size
            for (int i = 0; i < numOfBuckets; i++)
            {
                bucket.add(null);//initialize bucket
            }
            for (HashNode headToReplace : tempBucket) //rehash and place back all the new items
            {
                while (headToReplace != null)
                { 
                    put(headToReplace.key, headToReplace.value); 
                    headToReplace = headToReplace.next; 
                } 
            } 
        }
    }

    public String remove(String key) throws Exception
    { 
        HashNode head = bucket.get(getHash(key)); //set the head of the linked list
        HashNode prev = null;
        while (head != null && head.key.equals(key)==false) //traverse through the list until the key is found
        {
            prev = head;
            head = head.next;
        }
        if (head == null) //if not found then throw an exception
        {
            throw new Exception();
        }
        entries--;
        if (prev != null) //if the prev is not null then set it equal to the next item
        {
            prev.next = head.next;
        }
        else // if it is at the head of the list then set the head equal to the next
        {
            bucket.set(getHash(key), head.next);
        }
        return head.value; //return head
    }

    class HashNode //node class
    {
        String key;
        String value;
        HashNode next;
        public HashNode(String key, String value) 
        {
            this.key = key;
            this.value = value;
            next = null;
        }
    }

    private int getHash(String key) //helper function
    {
        return (Math.abs(key.hashCode()) % numOfBuckets);
    }

}