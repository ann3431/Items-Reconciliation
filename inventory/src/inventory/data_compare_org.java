package inventory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

/**
 * this program updates the values of an item reconciliation csv file by
 * matching the product name from another inventory xlsx file
 * 
 * @author annie
 *
 */
public class data_compare_org {
	private static final char DEFAULT_SEPARATOR = ',';
	private static ArrayList<String> itemName; // Stores the item names from csv file
	private static ArrayList<String> quantity; // Stores the quantities from csv file
	private static ArrayList<String[]> lines; // Stores the rows from csv file
	private static ArrayList<String[]> ERPNumber;// Stores the ERPNumber of each product
	private static int numLines;
	private static String csv;
	private static String xlsx;
	private static String desName;

	/**
	 * 
	 * @param csvPath
	 * @param xlsxPath
	 */
	public data_compare_org(String csvPath, String xlsxPath) {
		itemName = new ArrayList<String>();
		quantity = new ArrayList<String>();
		lines = new ArrayList<String[]>();
		ERPNumber = new ArrayList<String[]>();
		numLines = 0;
		csv = csvPath;
		xlsx = xlsxPath;
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		data_compare_org comparer = new data_compare_org(
				"/Users/annie/eclipse-workspace/inventory/Items_reconciliation.csv",
				"/Users/annie/eclipse-workspace/inventory/Daily Inventory 05-15-18.xlsx");
		comparer.run(0, 3, 2, 1, 2);
		// String csvFile =
		// "/Users/annie/eclipse-workspace/inventory/Items_reconciliation.csv";
		// CSVReader reader = null;
		// int count = 0; // Counts the total lines in csv file
		// try {
		// reader = new CSVReader(new FileReader(csvFile));
		// String[] line;
		// // Read through the file and store data
		// while ((line = reader.readNext()) != null) {
		// lines.add(line);
		// itemName.add(line[0]);
		// quantity.add(line[3]);
		// count++;
		// }
		//
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		//
		// File myFile = new File("/Users/annie/eclipse-workspace/inventory/Daily
		// Inventory 05-08-18.xlsx");
		// FileInputStream fis = new FileInputStream(myFile);
		// // Finds the workbook instance for XLSX file
		// XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
		// // Return first sheet from the XLSX workbook
		// XSSFSheet mySheet = myWorkBook.getSheetAt(2);// "Daily inventory" is the 3rd
		// sheet
		// // Get iterator to all the rows in current sheet
		// Iterator<Row> rowIterator = mySheet.iterator();
		// int num = 0;
		// String content = null;
		// // Traversing over each row of XLSX file
		// while (rowIterator.hasNext()) {
		// Row row = rowIterator.next();
		//
		// // Preventing null exceptions
		// if (row.getCell(1) != null) {
		// // Check the second column for item name
		// if (row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING)
		// content = row.getCell(1).getStringCellValue().trim();
		// // Check the quantity column, which has type of formula
		// if (row.getCell(2).getCellType() == Cell.CELL_TYPE_FORMULA) {
		//
		// // Check whether the formula generates an actual number or error
		// switch (row.getCell(2).getCachedFormulaResultType()) {
		// case Cell.CELL_TYPE_ERROR:
		// break;
		// case Cell.CELL_TYPE_NUMERIC:
		// num = (int) row.getCell(2).getNumericCellValue();
		// break;
		// default:
		// }
		//
		// // Compare the item name with all the elements in list itemName
		// for (int i = 0; i < count; i++) {
		//
		// // See if matches
		// if (content.equals(itemName.get(i))) {
		// // Update quantity
		// quantity.set(i, Integer.toString(num));
		// // Modify the data to generate csv file
		// String[] temp = lines.get(i);
		// temp[3] = "*" + Integer.toString(num); // Added the '*' to mark updated
		// quantities
		// lines.set(i, temp);
		// }
		// }
		// }
		// }
		// }
		//
		// for (int j = 0; j < count; j++)
		// System.out.println("Item: " + itemName.get(j) + " quantity:" +
		// quantity.get(j));
		//
		// // Writing compared quantities to new csv file
		// CSVWriter writer = new CSVWriter(new FileWriter("test.csv"),
		// DEFAULT_SEPARATOR);
		// // feed in array
		// for (int k = 0; k < count; k++) {
		// writer.writeNext(lines.get(k));
		// }
		// writer.close();

	}

	/**
	 * 
	 * @param csvCompCol
	 *            the column to compare in csv file
	 * @param csvNumCol
	 *            the column to update in csv file
	 * @param xlsxSheetNum
	 *            the sheet number to read in xlsx file
	 * @param xlsxCompCol
	 *            the column to compare in xlsx file
	 * @param xlsxNumCol
	 *            the column that has the source number data in xlsx file
	 * @return 1 if successful, 0 if an exception is thrown
	 */

	public int run(int csvCompCol, int csvNumCol, int xlsxSheetNum, int xlsxCompCol, int xlsxNumCol) {
		String csvFile = this.csv;
		CSVReader reader = null;

		try {
			reader = new CSVReader(new FileReader(csvFile));
		} catch (FileNotFoundException e) {
			System.out.println("Invalid csv file path");
			return -1;
		}

		try {
			String[] line;
			// Read through the file and store data
			while ((line = reader.readNext()) != null) {
				lines.add(line);
				if (line.length > csvCompCol) { // TODO Code quality could be better here
					itemName.add(line[csvCompCol].trim());
					quantity.add(line[csvNumCol]);
				} else {
					itemName.add("");
					quantity.add("");
				}
				String[] blank = { "" };
				ERPNumber.add(blank);
				numLines++;
			}
			reader.close();
			System.out.println("csv read complete");
			try {
			File myFile = new File(this.xlsx);
			FileInputStream fis = new FileInputStream(myFile);
			// Finds the workbook instance for XLSX file
			XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
			// Return first sheet from the XLSX workbook
			XSSFSheet mySheet = myWorkBook.getSheetAt(xlsxSheetNum);// "Daily inventory" is the 3rd sheet
			// Get iterator to all the rows in current sheet
			Iterator<Row> rowIterator = mySheet.iterator();
			int num = 0;
			String content = null;
			System.out.println("Start reading xlsx");
			// Traversing over each row of XLSX file
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();

				// Preventing null exceptions
				if (row.getCell(xlsxCompCol) != null) {
					
					// Check the name column for item name
					if (row.getCell(xlsxCompCol).getCellType() == Cell.CELL_TYPE_STRING)
						content = row.getCell(xlsxCompCol).getStringCellValue().trim();
					
					// Check the quantity column, which has type of formula
					if (row.getCell(xlsxNumCol).getCellType() == Cell.CELL_TYPE_FORMULA) {
						
						// Check whether the formula generates an actual number or error
						switch (row.getCell(xlsxNumCol).getCachedFormulaResultType()) {
						case Cell.CELL_TYPE_ERROR:
							break;
						case Cell.CELL_TYPE_NUMERIC:
							num = (int) row.getCell(xlsxNumCol).getNumericCellValue();
							break;
						default:
						}

						// Compare the item name with all the elements in list itemName
						for (int i = 0; i < numLines; i++) {
							// See if matches
							if (content.equals(itemName.get(i))) {	
								// Update quantity
								quantity.set(i, Integer.toString(num));
								
								// Modify the data to generate csv file
								String[] temp = lines.get(i);
								temp[csvNumCol] = "*" + Integer.toString(num); // Added the '*' to mark updated
																				// quantities
//								temp[0] = row.getCell(0).toString();
								lines.set(i, temp);
								String[] erp = { row.getCell(0).toString() };
								ERPNumber.set(i, erp);
								break;
							}
							
						}
					}
				}
			}

			}catch(FileNotFoundException fnfe)
			{
				System.out.println("Invalid xlsx file path");
				return -1;
			}
			System.out.println("xlsx read complete");
			// Writing compared quantities to new csv file
			CSVWriter writer = new CSVWriter(new FileWriter("test_ERP.csv"), DEFAULT_SEPARATOR);
			System.out.println("Start writing new csv file");
			// feed in array
			for (int k = 0; k < numLines; k++) {
				writer.writeNext(lines.get(k));
			}
			writer.close();
			System.out.println("csv file generated");
			return 1;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}

}