
import java.io.*;
import java.util.*;

//represents a block of memory
class Partition{

	String pID;
	int pSize;
	int start;
	int end;
	boolean isHole;
	Partition next;

	Partition(String pID, int pSize)
	{
		this.pID=pID;
		this.pSize=pSize;
		this.start=0;
		this.end=this.start+this.pSize-1;
		this.isHole=true;
		this.next=null;
	}
}



class Memory_Allocation{

	Partition block;
	int total_memory;

	Memory_Allocation(int total_memory)
	{
		this.total_memory=total_memory;
		block=new Partition("Initial",total_memory);
	}

	//allocates memory to a process
	public void myMalloc(String pID,int pSize)
	{
		Partition cur=this.block;
		Partition bestfit=new Partition("bestfit",this.total_memory);
							
		while(cur!=null)
		{
			
			if(cur.isHole==true)
				{
					int free_memory=(cur.end+1)-cur.start;
					cur.pSize=(cur.end+1)-cur.start;
					
					if(free_memory>=pSize && free_memory<=bestfit.pSize)
					{	//finds the bestfit
						bestfit=cur;
						bestfit.pSize=cur.pSize;
					}
				}
			cur=cur.next;
		}

		//if process size > space in memory
		if(bestfit.pID=="bestfit")
			System.out.println("There is no space in memory for the item "+pID+" of size "+pSize);
		else
		{	
			Partition new_process =new Partition(pID,pSize);
			new_process.next=bestfit.next;
			bestfit.next=new_process;
			new_process.end=bestfit.end;
			bestfit.end=bestfit.start+pSize-1;
			new_process.start=bestfit.end+1;
			bestfit.pID=pID;
			bestfit.isHole=false;

		}

	}

	//To free the memory of a process
	public void myFree(String pID){


		Partition cur=this.block;

		while(cur!=null && !cur.pID.equals(pID))
			cur=cur.next;

		if(cur==null)
			System.out.println("There is no such process. Please enter a valid process ID");
		else
		{
			cur.isHole=true;
			cur.pID= "Hole";
		}

		Partition current=this.block;

		//combines consecutive free memory spaces
		while(current.next!=null)
		{
			if(current.isHole==true && current.next.isHole==true)
			{
				current.end=current.next.end;
				current.next=current.next.next;
				continue;
			}
			else
				current=current.next;
		}

	}


	//prints the linked list of memory partitions visually
	public void printMemoryList(){

		System.out.print("\n\n * Current memory allocation is as follows:");
		Partition cur=this.block;
		System.out.println();
		while(cur!=null)
		{
			if(cur.isHole==true && cur.start<cur.end)
					System.out.println("Free space -> "+cur.start+" - "+cur.end+" : "+(cur.end-cur.start+1)+" bytes");
			else if (cur.start<cur.end)
				System.out.println("Process "+cur.pID+" -> "+cur.start+" - "+cur.end+" : "+(cur.end-cur.start+1)+" bytes");
			cur=cur.next;
		}

	}



}

class memory{

	public static void main(String args[])
	{
		Memory_Allocation memo=new Memory_Allocation(25000);
		Scanner sc=new Scanner(System.in);

		System.out.print("\nEnter ProcessID<space>number of bytes to allocate memory to a process OR ProcessID to terminate a process: ");
		String line=sc.nextLine();

		while(!line.equals("Q"))
		{
			//Gets the input
			String[] input=line.split(" ");
			if(input.length==2)
				memo.myMalloc(input[0],Integer.parseInt(input[1]));
			else
				memo.myFree(input[0]);
							
			memo.printMemoryList();
			System.out.print("\nEnter ProcessID<space>number of bytes to allocate memory to a process OR ProcessID to terminate a process: ");
			line=sc.nextLine();

		}
	}
}