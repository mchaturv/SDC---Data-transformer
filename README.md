# SDC---Data-transformer
Simple Software Development Concept - Data Manipulation and Transformation software using core Java


# Overview
--------

This program performs the following operation
  - Read the data from the file and load into object.
  - Adding the new column to the Data metrics stored in the object.
  - Calculating the Equation and assigning the value to column used for assinging in the equation.
  - Printing the data Records - All the records along with column name
  - Printing the the first 5 rows of data record along with the column name.
  - Clearing the Data object where the data are stored.
  - Writing the the data content from the object to the file.

The solution delivered is based on several important factors and information available in the input file.


# Files and external data
-----------------------

There are two main java files:
  - DataChangeUI.java  -- main for the program that prompts the user for input in form of command, in return calling the method from DataTransformer.
  - DataTransformer.java -- class that contains the functionality for all the different operation performed on file and data as mention above.


Data structures and their relations to each other
-------------------------------------------------

The program read the data available in file into different different main datastructure object that are

  - columnNameList - ArrayList of type string to store all the column Name.

  - dataMetric - Its an ArrayList of ArrayList<Object>. The internal arraylist is used to store the
	       value of all column of one particular record/row into one index as an entry.
		The External ArrayList is for containing all the rows i.e ArrayList<Object>.

# Assumptions
-----------

  - The input file will have at most 10 columns.
  - File names will contain the full path to the file, including any file name extensions.
  - No line in the file is more than 80 characters.
  - Column names will be a single alphabetic string.  They will not have any spaces or nonletter characters.
  - The user will not ask you to make a calculation that divides by 0.
  - String columns will only contain alphabetic characters.
  - Data in a column in a data file will always have the same data type (you won’t get a column where row 1 has an integer in the column and row 2 has an integer in the column).
  - There will always be at least one space between each component of an equation.
  - Every line in an input file will have the same number of columns of data. 
 

# Key algorithms and design elements
----------------------------------

Reading the file : * Filename being the first parameter of the read method.
	 	   * This method is to read the file and handle any exception being thrown. File would be read and first row i.e column name would be stored 
           	     in the ArrayList of type string.
         	   * Second row onwards all the record would be saved in different data structure "dataMetric" which would be arrayList<arrayList<Object>>


Printing the records : This Method would print all the Data Stored in the Object i.e Arraylist : columnNamesList and ArrayList : dataMetric
		       with each column seperated by tab.


Adding the New Column : This Method would add the new column passed in parameter if the column name is not already existing and return true and set the default value for
                        that particular column for all the row to zero.
                        If the Column name provided is not in correct format or already exist in the object it will return false.

Calculate the Equation : This method is for Calculating the Equation provided and perform below algorithm
			 * -Checks for the Equation is not null or Empty.
			 * Before equation is evaluated provided column Name should be available in objects.
	 		 * the Syntax of the Equation - Should have only one operator and allowed
	 		 * It check for operator are +,-,*,/.
	 		 * Should contain column name , only which exists in the object.
			 * After all the validation are passed it calls an user defined
			   Function Compute(ArrayList<object> , String) and perform the calculation and return the value calculated.

Clear the object : This Method is to clear the Object if Object is not empty and have some value.
		   Once object is clear True needs to be returned. If Object is already empty, false need to be returned.

Writing the Records in the file : This method is to write the data in the file and handle any exceptions being thrown along with below operation.
	 			  * First column name would be read from columnNamesList object and written into the file.
	 			  * Second row onwards all the data record would be read from object : dataMetric and written into the file.
	 			  * At the end file would be closed.


# Limitations
-----------

The current design is limited to a process on the data on Column level
and not on the record level.

the operation or the equation used can only consist of atmost one operator
along with two operand.
