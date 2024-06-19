//********************************************************************
//
//  Author:        Grecia Bueno
//
//  Program #:     Capstone
//
//  File Name:     MemoryObject.java
//
//  Course:        COSC 4302 Operating Systems
//
//  Instructor:    Prof. Fred Kumi
//
//  Java Version:  17.0.2
//
//  Chapter:      9
//
//  Description:   This program is where the memory lives in segments or in
//                  full if one request takes the space up completely or if the
//                  disk is empty.  Each memoryObject object has a starting address, 
//                  a size, and an id associated with each request.  The starting 
///                 address tells us where the memory for that space begins, the size
//                  tells us how much memory it takes, and the id, what process it
//                  represents.
//
//********************************************************************
public class MemoryObject {
    private int startAddress;
    private int size;
    private String processId; // null if the block is free

    //***************************************************************
    //
    //  Method:       MemoryObject
    // 
    //  Description:  The constructor will receive the starting address
    //                  of the space requested, the size, and the id
    //                  for that specific request.
    //
    //  Parameters:     int startAddress, int size, String processId
    //
    //  Returns:        N/A
    //
    //**************************************************************
    public MemoryObject(int startAddress, int size, String processId) {
        this.startAddress = startAddress;
        this.size = size;
        this.processId = processId;
    }

    //***************************************************************
    //
    //  Method:       getStartAddress
    // 
    //  Description:  This method will get the startAdress.
    //
    //  Parameters:     N/A
    //
    //  Returns:        int
    //
    //**************************************************************
    public int getStartAddress() {
        return startAddress;
    }

    //***************************************************************
    //
    //  Method:       setStartAddress
    // 
    //  Description:  This method will set the start address.
    //
    //  Parameters:     int startAddress
    //
    //  Returns:        N/A
    //
    //**************************************************************
    public void setStartAddress(int startAddress) {
        this.startAddress = startAddress;
    }

    //***************************************************************
    //
    //  Method:       getSize
    // 
    //  Description:  This method will get the size of the block of memory.
    //
    //  Parameters:     N/A
    //
    //  Returns:        int
    //
    //**************************************************************
    public int getSize() {
        return size;
    }

    //***************************************************************
    //
    //  Method:       setSize
    // 
    //  Description:  The method will set the size of the block of memory.
    //
    //  Parameters:     int size
    //
    //  Returns:        N/A
    //
    //**************************************************************
    public void setSize(int size) {
        this.size = size;
    }

    //***************************************************************
    //
    //  Method:       getProcessId
    // 
    //  Description:  Getter method to get the process id. 
    //
    //  Parameters:     N/A
    //
    //  Returns:        String
    //
    //**************************************************************
    public String getProcessId() {
        return processId;
    }

    //***************************************************************
    //
    //  Method:       setProcessId
    // 
    //  Description:  Setter method for process id.
    //
    //  Parameters:     int processId
    //
    //  Returns:        N/A
    //
    //**************************************************************
    public void setProcessId(String processId) {
        this.processId = processId;
    }

    //***************************************************************
    //
    //  Method:       isFree
    // 
    //  Description:  This method will check if an specific part of a memory
    //                  block is already taken by a process.  It will return true if 
    //                  that space in the disk is empty, it returns false if it 
    //                  is taken by a process.
    //
    //  Parameters:     N/A
    //
    //  Returns:        boolean
    //
    //**************************************************************
    public boolean isFree() {
        return processId == null;
    }
}
