package inventory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
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
public class Data_compare {
	private static final char DEFAULT_SEPARATOR = ',';
	private static ArrayList<String> itemName; // Stores the item names from csv file
	private static ArrayList<String> quantity; // Stores the quantities from csv file
	private static ArrayList<String[]> lines; // Stores the rows from csv file
	private static ArrayList<String[]> ERPNumber;// Stores the ERPNumber of each product
	private static int numLines;
	private static int csvNumCol;
	private static File csv;
	private static File xlsx;
	private static File desName;
	private static Hashtable<String, Integer> hashName;
	private static Hashtable<String, Integer> hashERP;
	private XSSFWorkbook myWorkBook;
	private CSVReader reader;
	private static boolean runnable;
	protected String msg;
	private File ref;
	private boolean withStar;

	/**
	 * 
	 * @param csvPath
	 * @param xlsxPath
	 * @param des
	 * @throws InterruptedException
	 */

	public Data_compare(File csvPath, File xlsxPath, File des, boolean withStar) {
		itemName = new ArrayList<String>();
		quantity = new ArrayList<String>();
		lines = new ArrayList<String[]>();
		ERPNumber = new ArrayList<String[]>();
		numLines = 0;
		csv = csvPath;
		xlsx = xlsxPath;
		hashName = new Hashtable<String, Integer>();
		hashERP = new Hashtable<String, Integer>();
		desName = des;
		this.withStar = withStar;
		try {
			reader = new CSVReader(new FileReader(csv));
		} catch (FileNotFoundException e) {
			msg = "Invalid csv file path";
			System.out.println(msg);
			runnable = false;
		}

		try {
			FileInputStream fis = new FileInputStream(xlsx);
			myWorkBook = new XSSFWorkbook(fis);
		} catch (FileNotFoundException fnfe) {
			runnable = false;
			msg = "Invalid xlsx file path";
			System.out.println(msg);

		} catch (IOException e) {

			runnable = false;
			e.printStackTrace();
			msg = e.getMessage();
		} catch (NullPointerException npe) {
			msg = "Please enter xlsx file path";
			runnable = false;
		}
		msg = "";
		runnable = true;
	}

	public static void main(String[] args) throws Exception {
		Data_compare comparer = new Data_compare(
				new File("/Users/annie/downloads/Items_reconciliation.csv"),
				new File("/Users/annie/downloads/Daily Inventory 06-05-18.xlsx"),
				new File("/Users/annie/desktop/new.csv"), true);
		comparer.addRef(new File("/Users/annie/downloads/ERP_Ref.csv"));

		comparer.runWithRef(0, 3, 2, 0, 2, 1, 0);
		 ArrayList<String> list =comparer.listNotFound();
		 for(int i =0;i<list.size();i++)
			 System.out.println(list.get(i));
//		comparer.notFound();
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

	@SuppressWarnings("deprecation")
	public String run(int csvCompCol, int csvNumCol, int xlsxSheetNum, int xlsxCompCol, int xlsxNumCol) {

		if (!runnable)
			return "Please change file path to run";
		try {

			Data_compare.csvNumCol = csvNumCol;
			// Return first sheet from the XLSX workbook
			XSSFSheet mySheet = myWorkBook.getSheetAt(xlsxSheetNum);
			String[] line;

			line = reader.readNext();

			// Check whether the given column numbers of csv are valid
			if (line.length < csvNumCol)
				throw new IndexOutOfBoundsException("Invalid column number of quantity in csv file");
			if (line.length < csvCompCol)
				throw new IndexOutOfBoundsException("Invalid column number of item name in csv file");

			// Read through the file and store data
			while (line != null) {
				lines.add(line);
				if (line.length > csvCompCol) { // TODO Code quality could be better here
					String name = line[csvCompCol].trim();
					itemName.add(name);
					quantity.add(line[csvNumCol]);
					hashName.put(name, numLines);
				} else {
					itemName.add("");
					quantity.add("");
				}
				String[] blank = { "" };
				ERPNumber.add(blank);
				numLines++;
				line = reader.readNext();
				

			}
			reader.close();
			System.out.println("csv read complete");

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
					Cell current = row.getCell(xlsxNumCol);
					if (current != null) {
						int type = current.getCellType();
						if (type == Cell.CELL_TYPE_FORMULA) {
							// Check whether the formula generates an actual number or error
							if (current.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC) {
								num = (int) row.getCell(xlsxNumCol).getNumericCellValue();
								matchName(content, num);
							}
						} else if (type == Cell.CELL_TYPE_NUMERIC) {
							num = (int) row.getCell(xlsxNumCol).getNumericCellValue();
							matchName(content, num);
						}
					}
				}

			}

			System.out.println("xlsx read complete");
			// Writing compared quantities to new csv file
			CSVWriter writer = new CSVWriter(new FileWriter(Data_compare.desName), DEFAULT_SEPARATOR);
			System.out.println("Start writing new csv file");
			// feed in array
			for (int k = 0; k < numLines; k++) {
				writer.writeNext(lines.get(k));
			}
			writer.close();
			System.out.println("csv file generated");
			return "csv file generated\n";
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IndexOutOfBoundsException e) {
			return e.getMessage();
		} catch (IllegalArgumentException e) {
			return "There is only " + this.myWorkBook.getNumberOfSheets() + " number of sheets in xlsx file\n";
		} catch (NullPointerException npe) {
			return "Please check file path\n";
		}
	}

	private boolean matchName(String content, int num) {
		if (content == null)
			return false;
		// Compare the item name with all the elements in list itemName

		// See if matches using hashtable
		if (hashName.containsKey(content)) {
			// Index of the matching item in csv file
			int index = hashName.get(content);
			// Update quantity
			quantity.set(index, Integer.toString(num));

			// Modify the data to generate csv file
			String[] temp = lines.get(index);
			temp[csvNumCol] = "*" + Integer.toString(num); // Added the '*' to mark updated
															// quantities

			lines.set(index, temp);
			return true;
		} else
			return false;
	}

	public void readCSV(int csvCompCol, int csvNumCol) {

		try {
			String[] line;
			line = reader.readNext();

			// Check whether the given column numbers of csv are valid
			if (line.length < csvNumCol)
				throw new IndexOutOfBoundsException("Invalid column number of quantity in csv file");
			if (line.length < csvCompCol)
				throw new IndexOutOfBoundsException("Invalid column number of item name in csv file");

			// Read through the file and store data
			while (line != null) {
				lines.add(line);
				if (line.length > csvCompCol&&numLines>6) { // TODO Code quality could be better here
					String name = line[csvCompCol].trim();
					hashName.put(name, numLines);
					itemName.add(name);
				}
				else 
					itemName.add("");
				numLines++;
				line = reader.readNext();
			}
			reader.close();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	public void readRef(int numCol, int nameCol) {
		try {
			CSVReader erp = new CSVReader(new FileReader(ref));
			String[] line = erp.readNext();

			// Check whether the given column numbers of csv are valid
			if (line.length < numCol) {
				erp.close();
				throw new IndexOutOfBoundsException("Invalid column number of ERP Nummber in reference csv file");
			}
			if (line.length < nameCol) {
				erp.close();
				throw new IndexOutOfBoundsException("Invalid column number of item name in reference csv file");
			}

			// Read through the file, check whether item exists in hash table and store data
			while (line != null) {
				String name = line[nameCol].trim();
				if (hashName.containsKey(name)) {
					int lineNum = hashName.get(name);
					hashERP.put(line[numCol], lineNum);

				}
				line = erp.readNext();
			}
			erp.close();
		} catch (IndexOutOfBoundsException e) {
			System.out.println(e.getMessage());
		} catch (IOException ie) {
			ie.printStackTrace();
		}

	}

	private boolean matchERP(String erp, int num) {
		if (erp == null)
			return false;
		// Compare the item name with all the elements in list itemName

		// See if matches using hashtable
		if (hashERP.containsKey(erp)) {

			// Index of the matching item in csv file
			int index = hashERP.get(erp);
			hashName.remove(itemName.get(index));
			// Modify the data to generate csv file
			String[] temp = lines.get(index);
			if (withStar)
				temp[csvNumCol] = "*" + Integer.toString(num); // Added the '*' to mark updated// quantities
			else
				temp[csvNumCol] = Integer.toString(num);
			lines.set(index, temp);
			return true;
		} else
			return false;
	}

	@SuppressWarnings("deprecation")
	public void readxlsx(int xlsxSheetNum, int xlsxCompCol, int xlsxNumCol) {
		XSSFSheet mySheet = myWorkBook.getSheetAt(xlsxSheetNum);
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
				Cell current = row.getCell(xlsxNumCol);
				if (current != null) {
					int type = current.getCellType();
					if (type == Cell.CELL_TYPE_FORMULA) {
						// Check whether the formula generates an actual number or error
						if (current.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC) {
							num = (int) row.getCell(xlsxNumCol).getNumericCellValue();
							matchERP(content, num);
						}
					} else if (type == Cell.CELL_TYPE_NUMERIC) {
						num = (int) row.getCell(xlsxNumCol).getNumericCellValue();
						matchERP(content, num);
					}
				}
			}

		}

	}

	@SuppressWarnings("deprecation")
	public void writeCSV() {
		try {
			CSVWriter writer = new CSVWriter(new FileWriter(Data_compare.desName), DEFAULT_SEPARATOR);
			System.out.println("Start reading csv file.");
			// feed in array
			for (int k = 0; k < numLines; k++) {
				writer.writeNext(lines.get(k));
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void runWithRef(int csvCompCol, int csvNumCol, int xlsxSheetNum, int xlsxCompCol, int xlsxNumCol,
			int erpName, int erpNum) {
		Data_compare.csvNumCol = csvNumCol;
		if (runnable) {
			readCSV(csvCompCol, csvCompCol);
			readRef(erpNum, erpName);
			readxlsx(xlsxSheetNum, xlsxCompCol, xlsxNumCol);
			writeCSV();
		}
	}

	public void addRef(File reference) {
		if (reference.isFile())
			this.ref = reference;
	}

	public ArrayList<String> listNotFound() {
		ArrayList<String> sorted= new ArrayList<String>();
		Enumeration<String> list =hashName.keys();
		while (list.hasMoreElements())
		{
			sorted.add(list.nextElement());
		}
		Collections.sort(sorted);
		return sorted;
	}

	public ArrayList<String>notFound() {

		return itemName;

	}
}
