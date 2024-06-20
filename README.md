# Operating Systems Capstone Project

## Overview

This repository contains the source code for the Capstone Project for the Operating Systems course. The project demonstrates memory management techniques, including first fit, best fit, and worst fit memory allocation strategies. The program allows users to request memory, release memory, compact memory, and view the status of memory blocks.

## Project Files

- `CapstoneProject.java`: Handles user input and contains the main method.
- `MemoryObject.java`: Represents a block of memory.
- `MemoryProcess.java`: Manages memory allocation and deallocation processes.

## Features

- Memory Request: Allocate memory using first fit, best fit, or worst fit strategies.
- Memory Release: Release previously allocated memory.
- Memory Compaction: Compact memory to combine free spaces.
- Memory Status: Display the status of memory blocks.

## Usage

When you run the program, you will be prompted to enter the size of the memory. Then, you will see a menu with the following options:

- RQ: Request memory.
- RL: Release memory.
- C: Compact memory.
- STAT: Status report.
- X: Exit the program.

## Example

```plaintext
Enter the size (in bytes) of the memory:
1024
********************************************************
Pick from the following menu:
RQ: request memory
RL: release memory
C: compact memory
STAT: status report
X: exit
********************************************************

Enter information in this order: RQ ProcessName Size Strategy
Enter here: RQ P1 100 F
Memory allocated successfully.

Enter information in this order: RQ ProcessName Size Strategy
Enter here: STAT
Addresses [0:99] Process P1
Addresses [100:1023] Unused

