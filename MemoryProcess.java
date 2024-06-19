//********************************************************************
//
//  Author:        Grecia Bueno
//
//  Program #:     Capstone
//
//  File Name:     MemoryProcess.java
//
//  Course:        COSC 4302 Operating Systems
//
//  Instructor:    Prof. Fred Kumi
//
//  Java Version:  17.0.2
//
//  Chapter:      9
//
//  Description:   This program will manage all the memory allocations 
//                  for the user.  It receives a parameter for the max 
//                  capacity the our disk will have.  This capacity will
//                  the same, as well as adjusted during runtime.  Some
//                  of the functionalities that this program include are:
//                  Finding the best fit, the worst fit, the first fit, allocating
//                  memory according to one of the mentioned strategies.  The user
//                  also has the option of releasing, compacting, and printing
//                  the status of the disk.
//
//********************************************************************

import java.util.ArrayList;
import java.util.List;

public class MemoryProcess {
     List<MemoryObject> blocks;
     final int capacity;

    //***************************************************************
    //
    //  Method:       MemoryProcess
    // 
    //  Description:  The constructor will receive the size of the disk
    //                  through a parameter.  It will also instantiate a
    //                  list of objects of type MemoryObject. 
    //
    //  Parameters:     int capacity
    //
    //  Returns:        N/A
    //
    //**************************************************************
    public MemoryProcess(int capacity) {
        this.capacity = capacity;
        this.blocks = new ArrayList<>();
        //populates the first object in the list starting at 0, with the 
        //complete size of the disk and null as their process id
        this.blocks.add(new MemoryObject(0, capacity, null));
    }

    //***************************************************************
    //
    //  Method:       printSTAT
    // 
    //  Description:   This method will print all of the objects in the blocks list.
    //                  It will print them in the following order: Addresses [start : end],
    //                  process number, or unused if the block of memory is free.
    //
    //  Parameters:     N/A
    //
    //  Returns:        String
    //
    //**************************************************************
    public String printSTAT() {
        //string that will get returned
        String status = "";
        //iterate through list
        for (MemoryObject block : blocks) {
            //find the sttat of the address using accessor
            int startAddress = block.getStartAddress();
            //find the end of the address.  Subtract 1 because it is exclusive
            int endAddress = startAddress + block.getSize() - 1;
            //if the block is free, print unused, otherwise, print the process name
            String processId = block.isFree() ? "Unused" : "Process " + block.getProcessId();
            status += String.format("Addresses [%d:%d] %s\n", startAddress, endAddress, processId);
        }
        return status;
    }

    //***************************************************************
    //
    //  Method:       compactMemory
    // 
    //  Description:  This method creates a new ArrayList to keep track of the
    //                  block of memory that have already been allocated.  It then
    //                  calculates how much memory is left free and places it at the
    //                  end of the list.  Finally, it assigns this newly created list
    //                  to the original blocks list that all the other methods use.
    //
    //  Parameters:     N/A
    //
    //  Returns:        String
    //
    //**************************************************************
    public String compactMemory() {
        int memoryUsed = 0;
        List<MemoryObject> usedBlocks = new ArrayList<>();
        String message = "";

        //for loop to iterate over the original blocks list
        for (MemoryObject block : blocks) {
            //if block is not free, add it to the new list, usedBlocks
            if (!block.isFree()) {
                //memoryUsed is used as the starting address, you get the current size, and pass it the
                //process id
                usedBlocks.add(new MemoryObject(memoryUsed, block.getSize(), block.getProcessId()));
                //the memoryUsed is updated and increased with the getSize method, this allows
                //us to expand the current memory  and calculate how much free memory there is
                memoryUsed += block.getSize();
            }
        }

        //this calculates how much memory is left
        int totalFreeSize = capacity - memoryUsed;
        if (totalFreeSize > 0) {
            //adds the free memory  to the end of the usedBlocks list
            usedBlocks.add(new MemoryObject(memoryUsed, totalFreeSize, null));
        }

        //assigns it to original list, blocks
        blocks = usedBlocks;
        //message to be returned
        message = "\nMemory compacted successfully! The new free size is: " + totalFreeSize;
        return message;

    }

    //***************************************************************
    //
    //  Method:       releaseMemory
    // 
    //  Description:  This method releases memory in the disk using the 
    //                  process id as an identifier.  It compares the parameter
    //                  passed with the processIds in the list, if a match is found,
    //                  the processId becomes null.  Meaning the block is now
    //                  ready to be used by another process.
    //
    //  Parameters:     int processId
    //
    //  Returns:        String
    //
    //**************************************************************
    public String releaseMemory(String processId) {
        String result = "\nERROR: Memory was not found";

        for (MemoryObject block : blocks) {
            //compare parameter with current process id
            if (processId.equalsIgnoreCase(block.getProcessId())) {
                //if found, block's new process id is null
                //symbolizes the removal of the process from the disk
                block.setProcessId(null);
                result = "\nMemory released for process " + processId;
                break; 
            }
        }

        return result;
    }

    //***************************************************************
    //
    //  Method:       worstFit
    // 
    //  Description:  This method uses the worst fit memory allocation 
    //                  strategy.  It will find the biggest possible space
    //                  in the disk and allocate the process passed as a parameter
    //                  in that space.  If there isn't enough memory it will return that
    //                  message to its calling function.  If the allocation was successfull,
    //                  or if the process already exists in the disk, it will respectively 
    //                  return those message to its calling function.
    //
    //  Parameters:     int processId, int size
    //
    //  Returns:        String
    //
    //**************************************************************
    public String worstFit(String processId, int size) {
        String result = "\nERROR: Not sufficient memory";

        //isIdMemory is a helper function that checks if the id already exists in the disk
        if (!isIdInMemory(processId)) {
            //this object will be passed to a helper function
            MemoryObject worstMemory = null;
            int largestSize = 0;

            for (MemoryObject block : blocks) {
                //checks if the current block is free, checks if its size is enough for our request
                //checks if our request for size is larger than our previous largestSize
                if (block.isFree() && block.getSize() >= size && block.getSize() > largestSize) {
                    //if all of those are true, then this new block object will be assigned to worstMemory
                    worstMemory = block;
                    //largetSize will be updated, meaning we have found an even bigger chunk of memory
                    largestSize = block.getSize();
                }
            }

            //if we did find a worst block of memory
            if (worstMemory != null) {
                //we are calling this helper function
                allocateProcess(worstMemory, processId, size);
                result = "\nMemory allocated successfully.";
            }
        } else {
            result = "\nProcess with the same name already exists.";
        }

        return result;
    }

    //***************************************************************
    //
    //  Method:       firstFit
    // 
    //  Description:  This method uses the first fit memory allocation 
    //                  strategy.  It will find the first space
    //                  in the disk, regardless of how big or small it is
    //                  as long as it can allocate the requested space,
    //                  and allocate the process passed as a parameter
    //                  in that space.  If there isn't enough memory it will return that
    //                  message to its calling function.  If the allocation was successfull,
    //                  or if the process already exists in the disk, it will respectively 
    //                  return those message to its calling function.
    //
    //  Parameters:     String processId, int size
    //
    //  Returns:        String
    //
    //**************************************************************
    public String firstFit(String processId, int size) {
        String result = "\nERROR: Not sufficient memory";

        //calls the helper function to check if process id already exists
        //if it doesn't, we proceed
        if (!isIdInMemory(processId)) {
            for (MemoryObject block : blocks) {
                //checks if block of memory  is free, and if it is bigger or equal to what
                //we are requesting, size
                if (block.isFree() && block.getSize() >= size) {
                    //calls helper function to allocate this space
                    allocateProcess(block, processId, size);
                    result = "\nMemory allocated successfully.";
                    break;
                }
            }
        } else {
            //in case the memory is already found in the disk, we just return this string
            result = "\nProcess with the same name already exists.";
        }

        return result;
    }

    //***************************************************************
    //
    //  Method:       bestFit
    // 
    //  Description:  This method uses the best fit memory allocation 
    //                  strategy.  It will find the best space
    //                  in the disk, meaning the smallest one that can fit the request.
    //                  It will allocate the process passed as a parameter
    //                  in that space.  If there isn't enough memory it will return that
    //                  message to its calling function.  If the allocation was successfull,
    //                  or if the process already exists in the disk, it will respectively 
    //                  return those message to its calling function.
    //
    //  Parameters:     String processId, int size
    //
    //  Returns:        String
    //
    //**************************************************************
    public String bestFit(String processId, int size) {
        String result = "\nERROR: Not sufficient memory";

        //checks if id already exists in the disk
        if (!isIdInMemory(processId)) {
            //creates a new MemoryObject object where the best one will be stored
            MemoryObject bestMemory = null;
            //this variable is set to the largest number and it will be decreased
            //as we keep finding smaller spaces in the for loop
            int smallestSize = Integer.MAX_VALUE;

            for (MemoryObject block : blocks) {
                //checks if block of memory is free, if the size of the block is greater than or 
                //equal to the request, and if the size of the block is smaller than what we have
                //already found, smallestSize
                if (block.isFree() && block.getSize() >= size && block.getSize() < smallestSize) {
                    //if all of those are true, we assign the memory space to bestMemory
                    bestMemory = block;
                    //keep updating the smallestSize to the newly smallest found space
                    smallestSize = block.getSize();
                }
            }

            //if we did find a spot
            if (bestMemory != null) {
                //call the helper function to allocate memory
                allocateProcess(bestMemory, processId, size);
                result = "\nMemory allocated successfully.";
            }
        } else {
            //otherwise, the request already exists in the disk
            result = "\nProcess with the same name already exists.";
        }

        return result;
    }

    //***************************************************************
    //
    //  Method:       allocateProcess
    // 
    //  Description:  This method allocates memory space as long as the disk
    //                  has enough of it.  It will split the block of memory  in two
    //                  if the block is too large for the request.  Then it will adjust
    //                  the block's size to the requested size.  The newly  created empty block
    //                  will start at the end of the current's start address + size.
    //                  The empty block will be added one index over to the right from
    //                  the allocated chunk of space
    //
    //  Parameters:     MemoryObject block, String processId, int size
    //
    //  Returns:        N/A
    //
    //**************************************************************
    public void allocateProcess(MemoryObject block, String processId, int size) {
        //checks if the current chunk of space is bigger than needed
        if (block.getSize() > size) {
            //if so, a new memory block is added.  It starts from block's start address plus its size,
            //this empty block's size is the original block's size minus the requested size, its
            //process id is null
            MemoryObject newMemorySpace = new MemoryObject(block.getStartAddress() + size, block.getSize() - size, null);
            //the empty block is added to the original blocks list, one index over to the right
            blocks.add(blocks.indexOf(block) + 1, newMemorySpace);
            //the current's block's size is adjusted
            block.setSize(size);
        }
        //its new process id is set
        block.setProcessId(processId);
    }

    //***************************************************************
    //
    //  Method:       isIdInMemory
    // 
    //  Description:  This method checks if the process id passed as a
    //                  parameter, already exists in the disk.  If so, it 
    //                  returns true, if not, it returns false
    //
    //  Parameters:     String processId
    //
    //  Returns:        boolean
    //
    //**************************************************************
    public boolean isIdInMemory(String processId) {
        boolean flag = false;

        for (MemoryObject block : blocks) {
            //checks if parameter process id is equal to the current block in the
            //for loop's process id
            if (processId.equals(block.getProcessId())) {
                flag = true;
            }
        }
        return flag;
    }

}
