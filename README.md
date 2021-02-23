#seat-code-mowers Application

- Current Version: 1.0-SNAPSHOT

#Main purpose: 

This application is created to navigate the SEAT Mowers through the green grass plateau in order to cut the grass and get a complete view of the surrounding terrain with their on-board cameras to send this information to the SEAT Maintenance Office.


#How to execute the application?

It can be easily executed through the command line in terminal.

Steps to execute:

- Create the input file following the necessary structure for this application. Can be found an example file in the project root directory with name input.txt
    - First line should contain a pair or integer numbers with the grid information size for X and Y axis, separate by space. Example: 5 5
    - Following lines should contain the mower information. First line with the mowers position, and second line with the movements for the mower. 
        - Example: 
            - line 1: 1 2 N
            - line 2: LMLMLMLMM

- To execute the application, open a terminal and go to the root directory. Then execute the following command:

        java -jar target\mower-control-1.0-SNAPSHOTa.jar [INPUT_FILE_PATH]
  
  - Where the [INPUT_FILE_PATH] should be the path to the input file with the mowers information. For example: 
    
        java -jar target/seat-code-mowers-1.0-SNAPSHOT-shaded.jar input.txt

#How to build the application?
- This application was build with Maven. 
- To rebuild the .jar file, it can be done through terminal, pointing to the root directory and executing the command:
        
        mvn clean install
  

#Assumptions:

- If a mower tries to move to a busy coordinate (there is a mower already placed in this coordinate), the movement won't be effectively done and it will be omitted. 
Nevertheless, the rest of the movements will be evaluated and applied if possible.
- The input file cannot contain two or more mowers in the same starting position. 
- Grid size cannot be bigger than 2.147.483.648 (Max. value for Integers in Java language).
- Assumed the bottom-left coordinates for grid are 0,0. So, coordinates cannot be a negative value.
- Valid values for movements are: L,R,M. 
- Valid values for cardinal points are: N,S,E,W. 
- Exception will be thrown if a mower tries to move out of the grid size.
- Exception will be thrown if the file is not following the correct structure. 

# Developer:

Vanessa Torres - Email: vanetowers@gmail.com
