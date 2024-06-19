//********************************************************************
//
//  Author:        Grecia Bueno
//
//  Program #:     Capstone
//
//  File Name:     CapstoneProject.java
//
//  Course:        COSC 4302 Operating Systems
//
//  Instructor:    Prof. Fred Kumi
//
//  Java Version:  17.0.2
//
//  Chapter:      9
//
//  Description:   This program handles the user input and contains the main method.
//                  In the main method, we declarre an instance of the class and obtain 
//                  the size of the disk, we then pass it to MemoryProcess.  This program
//                  offers the user the option of requesting memory, releasing memory, compacting
//                  memory, and showing the status of the disk.  When allocating memory, the
//                  user has the option of choosing from 3 options, first fit, best fit, and
//                  worst fit.  The for loop in main will keep running until the user enter
//                  X to exit the program.
//
//********************************************************************

import java.util.Scanner;

public class CapstoneProject {
    private static final Scanner scanner = new Scanner(System.in);

    //***************************************************************
    //
    //  Method:       main
    // 
    //  Description:  The main method of the program
    //
    //  Parameters:   String array
    //
    //  Returns:      N/A 
    //
    //**************************************************************
    public static void main(String[] args) {
        
        CapstoneProject main = new CapstoneProject();
        main.developerInfo();
        int memorySize = main.getMemorySize(scanner);
        MemoryProcess manager = new MemoryProcess(memorySize);

        boolean running = true;
        while (running) {
            running = main.getMenuInput(manager);
        }
    }

    
    //***************************************************************
    //
    //  Method:       getMemorySize
    // 
    //  Description:  This method will prompt the user for the size of the disk.
    //                  It will keep running in a for loop until the user enter
    //                  the correct number.  No negatives, and only integers.
    //                  It iwll then return the size to the main method.
    //
    //  Parameters:     Scanner scanner
    //
    //  Returns:        int
    //
    //**************************************************************
    public int getMemorySize(Scanner scanner) {
        int size = 0;
        boolean flag = true;

        while (flag) {
            System.out.println("Enter the size (in bytes) of the memory:");
            try {
                //tries to convert the string into an int
                size = Integer.parseInt(scanner.nextLine());
                //cheks if the size is greater than 0. No negative numbers.
                if (size > 0) {
                    //The loop exits here once the correct number has been entered.
                    break; 
                }
                System.out.println("Please enter a positive integer");
                //catch exception will alert the user to enter the correct character.
            } catch (NumberFormatException e) {
                System.out.println("Input is not valid.  Please enter an integer");
            }
        }
        //return size of disk 
        return size;
    }

    //***************************************************************
    //
    //  Method:       getMenuInput
    // 
    //  Description:  This method will show the user a menu to pick an option from.
    //                  The user must enter the information in the followin manner:
    //                  Action (from menu), process name, size, and strategy.
    //                  If x is entered, the program retunrs false and the while loop
    //                  in main, stops running.  Program ends.
    //
    //  Parameters:     MemoryProcess manager
    //
    //  Returns:        boolean
    //
    //**************************************************************
    public boolean getMenuInput(MemoryProcess manager) {
        System.out.println("\n********************************************************");
        System.out.println("Pick from the following menu:");
        System.out.println("RQ: request memory");
        System.out.println("RL: release memory");
        System.out.println("C: compact memory");
        System.out.println("STAT: status report");
        System.out.println("X: exit");   
        System.out.println("********************************************************\n");
        System.out.println("Enter information in this order: RQ ProcessName Size Strategy");
        System.out.print("Enter here: ");
    
        String input = scanner.nextLine();
        boolean keepRunning = true;

        //checks if user entered X
        if (!"X".equalsIgnoreCase(input)) {
            //if not, processInput is called.  This is where all the processing happens
            processInput(manager, input);
        } else {
            //otherwhise, it returns false
            keepRunning = false;
        }

        return keepRunning;
    }

    //***************************************************************
    //
    //  Method:       processInput
    // 
    //  Description:  The method will check the value of the user's input.
    //                 It splits the string passed as Strin input, into parts.
    //                 Index 0 holds the request keyword.  A series of if statements
    //                  check if it matches with RQ, RL, C, or STAT.  If so, they will call
    //                  the respective method through the manager object.
    //
    //  Parameters:     MemoryProcess manager, String input
    //
    //  Returns:        N/A
    //
    //**************************************************************
    public void processInput(MemoryProcess manager, String input) {
        //boolean keepRunning = true;
        //splits the input string into separate indexes
        String[] splitString = input.split(" ");

        //if statements check each option in the menu and compare them to the keywords.
        //If the keyword (splitString[0]) doesn't match, then the else statement prints
        //out an error and the while loop continues to ask the user for the correct input
        
        //checks for RQ keyword and whether the command recieves the 4 paramenters needed
        //in the command line
        if ("RQ".equalsIgnoreCase(splitString[0]) && splitString.length == 4) {
            //calls allocateMemory
            allocateMemory(manager, splitString);
            //checks the RL keyword and checks if the length is 2, for the keyword
            //and the process name
        } else if ("RL".equalsIgnoreCase(splitString[0]) && splitString.length == 2) {
            //releaseMemory method returns a string which contains a message
            String releaseResult = manager.releaseMemory(splitString[1]);
            System.out.println(releaseResult);
        } else if ("C".equalsIgnoreCase(splitString[0])) {
            String compactResult = manager.compactMemory();
            System.out.println(compactResult);
        } else if ("STAT".equalsIgnoreCase(splitString[0])) {
                String statResult = manager.printSTAT();
                System.out.println(statResult);
        } else {
            //if none of them match to the keywords then a message appears on the user's screen
            //and they must enter the correct option to proceed
            System.err.println("\nERROR: Invalid request format or unknown command.");
            System.out.println("Valid commands are RQ, RL, C, STAT, and X.");
            System.out.println("Please try again.");
        }
        //return keepRunning;
    }

    //***************************************************************
    //
    //  Method:       allocateMemory
    // 
    //  Description:  This method passes the manager object and the user's 
    //                  input already split into parts.  It will assign variables 
    //                  to each of the indexes.  It will also check for the strategy
    //                  chosen, F, B, or W.  If none of them match, then a message is printed
    //                  and the user must enter the correct one.
    //
    //  Parameters:     MemoryProcess manager, String[] parts
    //
    //  Returns:        N/A
    //
    //**************************************************************
    private void allocateMemory(MemoryProcess manager, String[] parts) {
        String processName = parts[1];
        //boolean keepRunning = true;

        try {
            //converts index 2 from string into an integer
            int processSize = Integer.parseInt(parts[2]);
            String strategy = parts[3];
            //string will be printed with message to screen
            String allocationResult = "";

            //if statements call first fit, best fits, or worst fit, respectively
            if ("F".equalsIgnoreCase(strategy)) {
                allocationResult = manager.firstFit(processName, processSize);
            } else if ("B".equalsIgnoreCase(strategy)) {
                allocationResult = manager.bestFit(processName, processSize);
            } else if("W".equalsIgnoreCase(strategy)) {
                allocationResult = manager.worstFit(processName, processSize);
            } else {
                //if match isn't found, then error message is printed
                System.err.println("\nERROR: This strategy is not valid");
                System.out.println("Pick from (F) First-fit, (B) Best-fit, (W) Worst-fit");
            }

            //if there was something returned by the methods, it is printed here
            if (!allocationResult.isEmpty()) {
                System.out.println(allocationResult);
            }
        } catch (NumberFormatException e) {
            System.out.println("Size must be a positive integer");
        }
        //return keepRunning;
    }

    //***************************************************************
    //
    //  Method:       developerInfo
    // 
    //  Description:  The developer information method of the program
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void developerInfo()
    {
       System.out.println("Name:    Grecia Bueno");
       System.out.println("Course:  COSC 4302 Operating Systems");
       System.out.println("Program: Capstone\n");

    } // End of the developerInfo method
}
