import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


/*Class contains the method to
    - Read the data from the file.
    - Adding the new column to the Data metrics.
    - Calculating the Equation and assigning the value to column.
	- Printing the data Records - All the records.
    - Clearing the Data object where the data are stored.
    - Writing the the data content from the object to the file*/ 
public class DataTransformer {

	// ArrayList to store the column Name
	private ArrayList<String> columnNamesList = new ArrayList<String>();
	// ArrayList to store the data record of data metric
	private ArrayList<ArrayList<Object>> dataMetric = new ArrayList<ArrayList<Object>>();
	private BufferedReader reader;
	private FileWriter filewriter;
	private boolean divedeByZero;
	private boolean containString;

	/*
	 * Reading the file : Filename being the first parameter which contains filename to read.
	 * This method is to read the file and handle any exceptions
	 * being thrown File would be read and first row i.e column name would be stored
	 * in the ArrayList of type string Second row onwards all the record would be
	 * saved in different data structure which would be arrayList of
	 * arrayList<Object>
	 */
	public Integer read(String filename) {
		int count = 0;
		columnNamesList = new ArrayList<String>();
		dataMetric = new ArrayList<ArrayList<Object>>();
		try {

			// reading the file available at given path
			if (filename != null && !filename.isEmpty()) {
				reader = new BufferedReader(new FileReader(filename));
				String line = null;
				try {
					line = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}

				/*
				 * Reading the line one by one from the file to extract the data metrices and
				 * put the data in the data structure until reading of all the lines are
				 * completed
				 */
				while (line != null) {
					if (!line.isEmpty()) {
						if (count == 0) {
							if (!line.matches(".*\\d.*")) {
								String[] columnName = line.split("\\s+");
								// iterating each column name by splitting it with tab
								for (String columnKey : columnName) {
									// Adding columnNames in ArrayList : columnNamesList
									columnNamesList.add(columnKey.trim());
								}
							} else
								break;
						} else {
							String[] columnValue = line.split("\\t");
							ArrayList<Object> templist = new ArrayList<Object>();

							// iterating each record by splitting it with tab
							for (String data : columnValue) {
								// checking record value if its is integer or not
								if (data.trim().matches("[\\d]*")) {

									// Adding records in ArrayList : templist
									templist.add(Integer.parseInt(data));

									// checking record value if its is String or not
								} else if (data.trim().matches("[a-zA-Z\\w\\d\\s]*")) {

									templist.add(data);
								}
							}

							// Adding all records ArrayList in ArrayList : dataMetric
							dataMetric.add(templist);
						}
						count++;
					}
					line = reader.readLine();
				}
				// Closing the File reader
				reader.close();
			} else
				count = 0;
		} catch (FileNotFoundException e) {
			count = 0;
		} catch (IOException e) {
			count = 0;
		}
		return count;
	}
	
	
	

	/*
	 * Printing the records : This Method Would would print all the Data Stored in
	 * the Object i.e Arraylist : columnNamesList and ArrayList : dataMetric
	 */
	public void print() {

		// printing the column name first
		for (String s : columnNamesList) {
			System.out.print(s + "\t");
		}
		System.out.println();

		// printing the data records
		for (ArrayList<Object> records : dataMetric) {
			for (Object obj : records) {
				System.out.print(obj + "\t");
			}
			System.out.println();
		}
	}
	
	
	

	/*
	 * Adding the New Column : This Method would add the new column if the column
	 * name is not already existing and return true and set the default value for
	 * that particular column for all the row to zero. If the Column name provided
	 * is not in correct format or already exist in the object it will return false
	 */
	boolean newColumn(String columnName) {
		if (columnName != null && !columnName.isEmpty() && !columnName.matches(".*\\s.*")) {

			// check for column name to be empty or null
			if (columnNamesList != null && !columnNamesList.isEmpty()) {

				// check to validate if column Name already exist
				if (!containsIgnoreCase(columnNamesList, columnName)) {

					// Adding the column to the object
					columnNamesList.add(columnName.toLowerCase());
					for (ArrayList<Object> records : dataMetric) {

						// setting default value for all the rows to "0"
						records.add(0);
					}
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	
	

	/*
	 * Calculate the Equation : this method is for Calculating the Equation provided
	 * - Checks for the Equation is not null or Empty. - Before equation is
	 * evaluated provided column Name should be available in objects. - It check for
	 * the Syntax of the Equation - Should have only one operator and allowed
	 * operator are +,-,*,/. - Should contain column name , only which exists in the
	 * object. - After all the validation are passed it calls an user defined
	 * Function Compute(ArrayList<object> , String) and perform the calculation and
	 * return the value calculated
	 */
	public Integer calculate(String equation) {
		boolean isEqualExist;
		boolean isEquationValid = false;
		int count = 0;
		divedeByZero = false;
		containString = false;
		String equationLHS, equationRHS;

		// To checks for the Equation is not null or Empty. Before equation is evaluated
		// records should be available in objects.
		if (equation != null && !equation.isEmpty() && columnNamesList != null && !columnNamesList.isEmpty()) {
			isEqualExist = equation.contains("=");
			System.out.println(isEqualExist);
			if (isEqualExist) {
				String[] partsOfEquation = equation.split("=");
				equationLHS = partsOfEquation[0].trim();
				if (partsOfEquation.length == 2 && containsIgnoreCase(columnNamesList, equationLHS)) {
					int index = columnNamesList.indexOf(equationLHS);
					equationRHS = partsOfEquation[1].trim();
					if (!equationRHS.isEmpty()) {

						// To check for the Syntax of the Equation - Should have only one operator and
						// allowed operator are +,-,*,/.
						boolean result = equationRHS
								.matches("[a-zA-Z\\d\\s]*[\\+\\-\\*\\/]?[\\s]*[\\+\\-\\*\\/]?[a-zA-Z\\d\\.]*");
						if (result) {
							String[] operands = equationRHS.split("[\\+\\-\\*\\/]\\s");
							for (String operand : operands) {

								// To check provided column Name should be available in objects. or it should be
								// constant: integer or decimal.
								if (containsIgnoreCase(columnNamesList, operand.trim())
										|| operand.trim().matches("^([\\-]?[0-9]*[\\.]?[0-9]+)?$")) {
									isEquationValid = true;
								} else {
									isEquationValid = false;
									break;
								}
							}

							/*
							 * If all the validation are passed and it calls an user defined Function
							 * Compute(ArrayList<object> , String) and perform the calculation and return
							 * the value calculated.
							 */
							if (isEquationValid) {
								for (ArrayList<Object> record : dataMetric) {
									divedeByZero = false;

									// Compute method to perform calculation.
									int computedValue = compute(equationRHS, record);
									if (!divedeByZero && !containString) {
										record.set(index, computedValue);
										count++;
									}
								}
								return count;
							} else
								return 0;

						} else {
							return 0;
						}
					} else {
						return 0;
					}
				} else {
					return 0;
				}
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}
	
	
	
	

	/*
	 * Compute Method : To compute the Equation - Splits the operands and operator -
	 * Check if operand are column name then if it does exist in object or not -
	 * Operator should +,-,*,/. - Check if operand are not column name then it
	 * should be constant, either decimal or integer. - based on the operator
	 * perform the required operation.
	 */

	public Integer compute(String equation, ArrayList<Object> record) {
		double result = 0;
		double multipleResult = 0, additionResult = 0, subsResult = 0;
		int index = 0;

		// Perform operation if division is the Operator
		// - find the dividend and the divisor
		if (equation.trim().contains("/ ")) {
			double dividend;

			// Split the LHS equation based on "/" operator followed by space
			String[] divideOperandlist = equation.trim().split("\\/[\\s]+");
			String divideFirstOperand = divideOperandlist[0].trim().toLowerCase();

			// check if 1st operand is Existing Column Name find the dividend and fetch the
			// value
			if (containsIgnoreCase(columnNamesList, divideFirstOperand)) {
				index = columnNamesList.indexOf(divideFirstOperand);
				if (!record.get(index).toString().matches("[A-Za-z\\s*]+")) {
					dividend = (int) record.get(index);
				} else {
					containString = true;
					return 0;
				}

				// If 1st operand is not the existing Column name calculate the dividend
			} else {
				if (!divideFirstOperand.matches("[A-Za-z\\s*]+")) {
					dividend = Double.parseDouble(divideFirstOperand);
				} else {
					containString = true;
					return 0;
				}
			}

			String divideSecondOperand = divideOperandlist[1].trim().toLowerCase();

			// check if 2nd operand is Existing Column Name and fetch the value
			if (containsIgnoreCase(columnNamesList, divideSecondOperand)) {
				index = columnNamesList.indexOf(divideSecondOperand);
				if (!record.get(index).toString().matches("[A-Za-z\\s*]+")) {
					if (!((int) record.get(index) == 0)) {
						dividend /= (int) record.get(index);
					} else
						divedeByZero = true;
				} else {
					containString = true;
					return 0;
				}

				// check if 2nd operand is not Existing Column Name, calculate the division
				// result
			} else {
				if (!divideSecondOperand.matches("[A-Za-z\\s*]+")) {
					if (!divideSecondOperand.equals("0")) {
						dividend /= Double.parseDouble(divideSecondOperand);
					} else
						divedeByZero = true;
				} else {
					containString = true;
					return 0;
				}
			}
			result = dividend;
		}

		// Perform operation if Multiply is the Operator
		else if (equation.trim().contains("* ")) {
			multipleResult = 1;

			// Split the LHS equation based on "*" operator followed by space
			String[] multipleOperandlist = equation.trim().split("\\*[\\s]+");
			for (String multipleOperand : multipleOperandlist) {
				multipleOperand = multipleOperand.trim().toLowerCase();

				// check if the operand is Existing Column Name and fetch the value
				if (containsIgnoreCase(columnNamesList, multipleOperand)) {
					index = columnNamesList.indexOf(multipleOperand);
					if (!record.get(index).toString().matches("[A-Za-z\\s*]+")) {
						multipleResult *= (int) record.get(index);
					} else {
						containString = true;
						break;
					}
					/*
					 * Check if operand is not Existing Column Name, it should be constant and not
					 * any string. Calculate the multiplication result
					 */
				} else {
					if (!multipleOperand.matches("[A-Za-z\\s*]+")) {
						multipleResult *= Double.parseDouble(multipleOperand);
					} else {
						containString = true;
						break;
					}
				}
			}
			result = multipleResult;
		}

		// Perform calculation operation if Addition is the Operator
		else if (equation.trim().contains("+ ")) {
			additionResult = 0;

			// Split the LHS equation based on "+" operator followed by space
			String[] additionOperandlist = equation.trim().split("\\+[\\s]+");
			for (String additionOperand : additionOperandlist) {
				additionOperand = additionOperand.trim().toLowerCase();

				// check if the operand is Existing Column Name and fetch the value
				if (containsIgnoreCase(columnNamesList, additionOperand)) {
					index = columnNamesList.indexOf(additionOperand);
					if (!record.get(index).toString().matches("[A-Za-z\\s*]+")) {
						additionResult += (int) record.get(index);
					} else {
						containString = true;
						break;
					}

					/*
					 * Check if operand is not Existing Column Name, it should be constant and not
					 * any string. Calculate the Addition result
					 */
				} else {
					if (!additionOperand.matches("[A-Za-z\\s*]+")) {
						additionResult += Double.parseDouble(additionOperand);
					} else {
						containString = true;
						break;
					}

				}
			}
			result = additionResult;
		}

		// Perform calculation operation if Subtraction is the Operator
		else if (equation.trim().contains("- ")) {
			subsResult = 0;
			int count = 0;

			// Split the LHS equation based on "-" operator followed by space
			String[] subsOperandlist = equation.trim().split("\\-[\\s]+");
			for (String subsOperand : subsOperandlist) {
				subsOperand = subsOperand.trim().toLowerCase();

				// check if the operand is Existing Column Name and fetch the value
				if (containsIgnoreCase(columnNamesList, subsOperand)) {
					index = columnNamesList.indexOf(subsOperand);
					if (!record.get(index).toString().matches("[A-Za-z\\s*]+")) {

						if (count == 0) {
							subsResult = (int) record.get(index);
							count++;
						} else {
							subsResult -= (int) record.get(index);
							count++;
						}
					} else {
						containString = true;
						break;
					}

					/*
					 * Check if operand is not Existing Column Name, it should be constant and not
					 * any string. Calculate the Subtraction result
					 */
				} else {
					if (!subsOperand.matches("[A-Za-z\\s*]+")) {
						if (count == 0) {
							subsResult = Double.parseDouble(subsOperand);
							count++;
						} else {
							subsResult -= Double.parseDouble(subsOperand);
							count++;
						}
					} else {
						containString = true;
						break;
					}
				}
			}
			result = subsResult;
		}

		// If "+,-,*,/" operator are not there , and equation is just the equation to
		// assign the value.
		else {
			double eqResult = 0;
			String operand = equation.trim().toLowerCase();
			if (containsIgnoreCase(columnNamesList, operand)) {
				index = columnNamesList.indexOf(operand);
				eqResult = (int) record.get(index);
			} else {
				eqResult = Double.parseDouble(operand);
			}
			result = eqResult;
		}

		// return the calculated value to calculate function
		return (int) Math.round(result);
	}
	
	
	
	

	/*
	 * Clear the object : This Method is to clear the Object if Object is not empty
	 * and have some value. Once object is clear True needs to be returned. If
	 * Object is already empty, false need to be returned.
	 */
	public boolean clear() {
		boolean result = false;

		// Check if object wher data is stored is not empty
		if (!columnNamesList.isEmpty()) {
			// clearing the object
			columnNamesList.clear();
			if (!dataMetric.isEmpty()) {
				// clearing the object
				dataMetric.clear();
			}
			result = true;
		} else {
			result = false;
		}
		return result;
	}
	
	
	
	
	/*
	 * Printing the first 5 records : This Method Would would print the first 5 record from the Data Stored in
	 * the Object i.e Arraylist : columnNamesList and ArrayList : dataMetric
	 */
	public void top() {
		int count = 0;
		
		// printing the column name
		for (String s : columnNamesList) {
			System.out.print(s + "\t");
		}
		System.out.println();
		for (ArrayList<Object> records : dataMetric) {

			// counting the records printed
			if (count < 5) {
				for (Object obj : records) {

					// printing the data records
					System.out.print(obj + "\t");
				}
				count++;
			} else
				{break;}
			System.out.println();
		}
	}

	
	
	/*
	 * Writing the Records in the file : This method is to write the data in the file and handle any exceptions
	 * being thrown.
	 * First column name would be read from columnNamesList object and written into the file.
	 * Second row onwards all the data record would be read from object : dataMetric and written into the file.
	 * At the end file would be closed.
	 */
	public Integer write(String filename) {
		int result = 0, count = 0;
		
		// check for file name is not null and empty
		if (filename != null && !filename.isEmpty()) {
			try {
				//creating instance and opening the file
				filewriter = new FileWriter(filename);
				
				// reading the object containing the column names
				if (!columnNamesList.isEmpty()) {
					String columnLine = "";
					for (int i = 0; i < columnNamesList.size(); i++) {
						if (i != columnNamesList.size() - 1) {
							columnLine = columnLine.concat(columnNamesList.get(i)) + "\t";
						} else {
							columnLine = columnLine.concat(columnNamesList.get(i));
						}
					}
					
					// writing the column name into the file
					filewriter.write(columnLine + "\n");
					count++;
					
					// reading the data record from the "dataMetric" object
					if (!dataMetric.isEmpty()) {
						for (ArrayList<Object> record : dataMetric) {
							String recordLine = "";
							for (int i = 0; i < record.size(); i++) {
								if (i != record.size() - 1) {
									recordLine = recordLine.concat(record.get(i).toString()) + "\t";
									System.out.println(recordLine + "ghjk");
								} else {
									recordLine = recordLine.concat(record.get(i).toString());
									System.out.println(recordLine + "ghfghghyg");
								}
							}
							try {
								if (count < dataMetric.size()) {
									//writing the data records into the file
									filewriter.write(recordLine + "\n");
								} else {
									//writing the last data records into the file
									filewriter.write(recordLine);
								}

							} catch (IOException e) {
								// on exception setting the count to "0"
								count = 0;
								break;
							}
							count++;
						}
					}
					result = count;
					try {
						// closing the file 
						filewriter.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						result = 0;
					}
				} else {
					result = 0;
				}
			} catch (IOException e2) {
				// on exception setting the count to "0"
				result = 0;
			}
		} else {
			result = 0;
		}
		return result;
	}

	
	
	/*
	 * containsIgnoreCase : Takes the first parameter as ArrayList where search is
	 * to be made and second parameter as keyword need to searched.
	 * This method is to search the keyword being case insensitive into ArrayList
	 */
	public boolean containsIgnoreCase(ArrayList<String> listToBeSearched, String keyWord) {
		boolean result = false;
		listToBeSearched = columnNamesList;
		for (String s : listToBeSearched) {
			if (s.equalsIgnoreCase(keyWord)) {
				result = true;
				break;
			}
		}
		return result;
	}
}
