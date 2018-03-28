import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class HashingApp {

    public static void main(String[] args)throws FileNotFoundException{

	HashTable table = new HashTable();

	table.getDataSetSize(args[0]);
	
	table.fillTable(); 
    }
}

class HashTable{

    private int size;	
    private HashTableEntry[] arr;
    private ArrayList<HashTableEntry> tempArr;

    public void getDataSetSize(String fileName)throws FileNotFoundException {
	Scanner sc = new Scanner(new File(fileName));
	tempArr = new ArrayList<HashTableEntry>();
	
	while(sc.hasNextLine()){
	    String[] str = sc.nextLine().split("[ \t]+");
	    if (str.length == 2){
		tempArr.add(new HashTableEntry(str[0], Integer.parseInt(str[1])));
	    }
	    else{
		tempArr.add(new HashTableEntry(str[0]));
	    }
	}

	size = tempArr.size() * 3;
       // System.out.println(size);
    }

    public void fillTable(){
	arr = new HashTableEntry[size];
	
	for(HashTableEntry i: tempArr){
	    if(i.getInsert())
		insertEntry(i);
	    else
		findEntry(i.getName());
	}
    }
 
    public void insertEntry (HashTableEntry entry){
	int insertPos = acquireHashCode(entry.getName());
	int colls = 0;	
	while(true){
	    if (arr[insertPos] == null){
		arr[insertPos] = entry;
		if (colls>0)
		    System.out.print("Resolved " + colls + " collision(s). ");
		System.out.print("Stored " + entry.getName()+ " " + entry.getValue() + " at position " + insertPos + ".\n");
		break;
	    }
	    else if (entry.getName().equals(arr[insertPos].getName())){
		System.out.print("ERROR " + entry.getName() + " already exists at position " + insertPos + ".\n");
		break;
	    }
	    else{
		colls++;
		insertPos = resolveCollision(insertPos, colls); 
	    }
	}
    }

    public void findEntry (String str){
        int insertPos = acquireHashCode(str);
	int colls = 0;
	while(true)
	    if (arr[insertPos] == null){
		System.out.print("ERROR " + str + " not found.\n");
		break;
	    }
	    else if (arr[insertPos].getName().equals(str)){
		System.out.print(str + " found at position " + insertPos  + " with value " + arr[insertPos].getValue() +  ".\n");
		break;
	    }
    	    else{
		colls++;
		insertPos = resolveCollision(insertPos, colls);
    	    }
    }

    public int acquireHashCode(String str){
        int g = 31;
	int hash = 0;
	for (int i = 0; i<str.length(); i++){
            hash = (g * hash + str.charAt(i)) % size;
        }
        return hash;
    }

    public int resolveCollision(int pos, int colls){
	return (pos + colls * colls) % size;
    }



}

class HashTableEntry {
    private String name;
    private int value;
    private boolean insert;	    

    public HashTableEntry(){

    }

    public HashTableEntry(String name, int value){
        this.name = name;
        this.value = value;
	this.insert = true;
	
    }

    public HashTableEntry(String name){
        this.name = name;
	this.insert = false;
    }

    public String getName(){return name;}

    public void setName(String name){this.name = name;}

    public int getValue(){return value;}

    public void setValue(int value){this.value = value;}

    public boolean getInsert(){return insert;}
}
