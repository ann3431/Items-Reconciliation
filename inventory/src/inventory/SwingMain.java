package inventory;

import java.awt.EventQueue;

import org.apache.commons.io.FilenameUtils;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.xmlbeans.impl.piccolo.io.FileFormatException;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Color;
import javax.swing.JCheckBox;
import javax.swing.AbstractAction;



public class SwingMain extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private static JFileChooser fileChooser = new JFileChooser();
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	protected JTextArea textArea;
	private int csvName;
	private int csvNum;
	private int xlsxSheet;
	private int xlsxName;
	private int xlsxNum;
	private int ERPName;
	private int ERPNum;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField_9;
	private boolean addStar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SwingMain frame = new SwingMain();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SwingMain() {
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 569, 476);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JPanel panel = new JPanel();
		contentPane.add(panel);

		JLabel lblNewLabel = new JLabel("Daily Inventory file (.xlsx)          ");
		panel.add(lblNewLabel);
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);

		textField = new JTextField();
		panel.add(textField);
		textField.setColumns(18);

		JButton btnNewButton = new JButton("Browse");
		panel.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetForeground();
				String fileName;
				FileNameExtensionFilter filter = new FileNameExtensionFilter("xlsx", "xlsx");
				fileChooser.setFileFilter(filter);
				int result = fileChooser.showOpenDialog(fileChooser);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					new TextAction("Selected file: " + selectedFile.getAbsolutePath() + "\n");
					fileName = selectedFile.getPath();
				} else
					fileName = "/Users/annie/eclipse-workspace/inventory/Daily Inventory 05-08-18.xlsx";
				textField.setText(fileName);

			}
		});

		JLabel label = new JLabel("");
		contentPane.add(label);

		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1);

		JLabel lblNewLabel_4 = new JLabel("Sheet number");
		panel_1.add(lblNewLabel_4);

		textField_4 = new JTextField();
		panel_1.add(textField_4);
		textField_4.setHorizontalAlignment(SwingConstants.CENTER);
		textField_4.setText("3");
		textField_4.setColumns(2);

		JLabel lblNewLabel_1 = new JLabel("Column of item name");
		panel_1.add(lblNewLabel_1);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);

		textField_2 = new JTextField();
		panel_1.add(textField_2);
		textField_2.setHorizontalAlignment(SwingConstants.CENTER);
		textField_2.setText("1");
		textField_2.setColumns(2);

		JLabel lblNewLabel_3 = new JLabel("        Column of quantity");
		panel_1.add(lblNewLabel_3);

		textField_3 = new JTextField();
		panel_1.add(textField_3);
		textField_3.setHorizontalAlignment(SwingConstants.CENTER);
		textField_3.setText("3");
		textField_3.setColumns(2);

		JLabel label_1 = new JLabel("");
		contentPane.add(label_1);

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		contentPane.add(panel_2);

		JLabel lblNewLabel_2 = new JLabel("Item Reconcilation file (.csv)      ");
		panel_2.add(lblNewLabel_2);
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.RIGHT);

		textField_1 = new JTextField();
		panel_2.add(textField_1);
		textField_1.setColumns(18);

		JButton button = new JButton("Browse");
		panel_2.add(button);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					resetForeground();
					String fileName;
					FileNameExtensionFilter filter = new FileNameExtensionFilter("csv", "csv");
					fileChooser.setFileFilter(filter);
					int result = fileChooser.showOpenDialog(fileChooser);
					if (result == JFileChooser.APPROVE_OPTION) {
						File selectedFile = fileChooser.getSelectedFile();

						if (selectedFile.isFile()) {
							new TextAction("Selected file: " + selectedFile.getAbsolutePath() + "\n");
							fileName = selectedFile.getPath();
						} else
							throw new FileFormatException("CSV file not found\n");
					} else
						fileName = "/Users/annie/eclipse-workspace/inventory/Items_reconciliation.csv";
					textField_1.setText(fileName);
				} catch (FileFormatException ffe) {
					new TextAction(ffe.getMessage());
				}
			}
		});

		JLabel label_2 = new JLabel("");
		contentPane.add(label_2);

		JPanel panel_3 = new JPanel();
		contentPane.add(panel_3);
		panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblNewLabel_9 = new JLabel("     ");
		panel_3.add(lblNewLabel_9);

		JLabel lblNewLabel_5 = new JLabel("                          Column of item name");
		panel_3.add(lblNewLabel_5);

		textField_5 = new JTextField();
		panel_3.add(textField_5);
		textField_5.setHorizontalAlignment(SwingConstants.CENTER);
		textField_5.setText("1");
		textField_5.setColumns(2);

		JLabel lblNewLabel_6 = new JLabel("        Column of quantity");
		panel_3.add(lblNewLabel_6);

		textField_6 = new JTextField();
		panel_3.add(textField_6);
		textField_6.setHorizontalAlignment(SwingConstants.CENTER);
		textField_6.setText("4");
		textField_6.setColumns(2);

		JLabel label_3 = new JLabel("");
		contentPane.add(label_3);

		JPanel panel_4 = new JPanel();
		contentPane.add(panel_4);

		JLabel lblNewLabel_7 = new JLabel("ERP Number Reference File( .csv)");
		panel_4.add(lblNewLabel_7);

		textField_7 = new JTextField();
		panel_4.add(textField_7);
		textField_7.setColumns(18);

		JButton button_1 = new JButton("Browse");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					resetForeground();
					String fileName;
					FileNameExtensionFilter filter = new FileNameExtensionFilter("csv", "csv");
					fileChooser.setFileFilter(filter);
					int result = fileChooser.showOpenDialog(fileChooser);
					if (result == JFileChooser.APPROVE_OPTION) {
						File selectedFile = fileChooser.getSelectedFile();

						if (selectedFile.isFile()) {
							new TextAction("Selected file: " + selectedFile.getAbsolutePath() + "\n");
							fileName = selectedFile.getPath();
						} else
							throw new FileFormatException("CSV file not found\n");
					} else
						fileName = "/Users/annie/eclipse-workspace/inventory/Items_reconciliation.csv";
					textField_7.setText(fileName);
				} catch (FileFormatException ffe) {
					new TextAction(ffe.getMessage());
				}
			}
		});
		panel_4.add(button_1);

		JLabel label_4 = new JLabel("");
		contentPane.add(label_4);

		JPanel panel_5 = new JPanel();
		contentPane.add(panel_5);

		JLabel label_6 = new JLabel("     ");
		panel_5.add(label_6);

		JLabel lblColumnOfItem = new JLabel("                         Column of item name");
		panel_5.add(lblColumnOfItem);

		textField_8 = new JTextField();
		textField_8.setForeground(Color.BLACK);
		panel_5.add(textField_8);
		textField_8.setText("2");
		textField_8.setHorizontalAlignment(SwingConstants.CENTER);
		textField_8.setColumns(2);

		JLabel lblColumnOfErp = new JLabel("  Column of ERP Number");
		panel_5.add(lblColumnOfErp);

		textField_9 = new JTextField();
		panel_5.add(textField_9);
		textField_9.setText("1");
		textField_9.setHorizontalAlignment(SwingConstants.CENTER);
		textField_9.setColumns(2);

		JPanel panel_6 = new JPanel();
		contentPane.add(panel_6);

		JCheckBox chckbxNewCheckBox = new JCheckBox("Add \"*\" to updated quantities");
		panel_6.add(chckbxNewCheckBox);
		
				JButton btnNewButton_1 = new JButton("Run");
				panel_6.add(btnNewButton_1);
				
						btnNewButton_1.addActionListener(new ActionListener() {
							/**
							 * Once the run button is hit, start reading files.
							 */
							public void actionPerformed(ActionEvent e) {
								resetForeground();
								textArea.setText("");
								// Get the info for data to compare and update
								File csv = new File(textField_1.getText());
								File xlsx = new File(textField.getText());
								File erp = new File(textField_7.getText());
								String csvNameCol = textField_5.getText();
								String csvNumCol = textField_6.getText();
								String xlsxSheetNum = textField_4.getText();
								String xlsxNameCol = textField_2.getText();
								String xlsxNumCol = textField_3.getText();
								String refNumCol = textField_9.getText();
								String refNameCol = textField_8.getText();
								// A value that tells the program whether it is ready to run the comparing, it
								// would be changed to false if any exception is thrown
								Boolean runnable = true;
				
								// Parse numbers one at a time in order to distinguish errors when occurred
								// Check the column number of item name in csv file
								try {
									// Check file paths
									if (!csv.exists()) {
										textField_1.setForeground(Color.red);
										throw new NullPointerException("Please enter valid csv file path\n");
									}
				
									if (!xlsx.exists()) {
										textField.setForeground(Color.red);
										throw new NullPointerException("Please enter valid xlsx file path\n");
									}
									if (!erp.exists()) {
										textField_7.setForeground(Color.red);
										throw new NullPointerException("Please enter valid xlsx file path\n");
									}
									// Check whether the number is a positive number
									csvName = Integer.parseInt(csvNameCol) - 1;
									if (csvName < 0) {
										textField_5.setForeground(Color.red);
										throw new NumberFormatException("Invalid column number of item name in csv file\n");
									}
				
									// Check the column number of quantity in csv file
									// Subtract by 1 to meet the way of counting in computer science (also the
									// comparer method.)
									csvNum = Integer.parseInt(csvNumCol) - 1;
									if (csvNum < 0) {
										textField_6.setForeground(Color.red);
										throw new NumberFormatException("Invalid column number of quantity in csv file\n");
									}
				
									// Check the sheet number of xlsx file
									xlsxSheet = Integer.parseInt(xlsxSheetNum) - 1;
									if (xlsxSheet < 0) {
										textField_4.setForeground(Color.red);
										throw new NumberFormatException("Invalid number of xlsx sheet number\n");
									}
				
									// Check the column number of item name in xlsx file
									xlsxName = Integer.parseInt(xlsxNameCol) - 1;
									if (xlsxName < 0) {
										textField_2.setForeground(Color.red);
										throw new NumberFormatException("Invalid column number of item name in xlsx file\n");
									}
				
									// Check the column number of quantity in xlsx file
									xlsxNum = Integer.parseInt(xlsxNumCol) - 1;
									if (xlsxNum < 0) {
										textField_3.setForeground(Color.red);
										throw new NumberFormatException("Invalid column number of quantity in xlsx file\n");
									}
				
									ERPNum = Integer.parseInt(refNumCol) - 1;
									if (ERPNum < 0) {
										textField_9.setForeground(Color.red);
										throw new NumberFormatException("Invalid column number of ERP reference number in csv file\n");
									}
				
									ERPName = Integer.parseInt(refNameCol) - 1;
									if (ERPName < 0) {
										textField_8.setForeground(Color.red);
										throw new NumberFormatException("Invalid column number of item name in ERP csv file\n");
									}
				
								} catch (NumberFormatException nfe) {
									new TextAction("Please check number format " + nfe.getMessage());
									runnable = false;
									return;
								} catch (NullPointerException npe) {
									new TextAction(npe.getMessage() + "\n");
									runnable = false;
									return;
								} catch (SecurityException se) {
									new TextAction(se.getMessage() + "\n");
									runnable = false;
									return;
								}
								if (runnable) {
									// Construct the file comparer
				
									int result = fileChooser.showSaveDialog(fileChooser);
									if (result == JFileChooser.APPROVE_OPTION) {
										File selectedFile = fileChooser.getSelectedFile();
										if (FilenameUtils.getExtension(selectedFile.getPath()) != ".csv") {
											// selectedFile = new File(selectedFile.toString() + ".csv"); // append .xml if
											// "foo.jpg.xml"
											// // is OK
											selectedFile = new File(selectedFile.getParentFile(),
													FilenameUtils.getBaseName(selectedFile.getName()) + ".csv"); // ALTERNATIVELY:
																													// remove the
																													// extension (if
																													// any) and replace
																													// it with ".xml"
										}
										Data_compare comparer = new Data_compare(csv, xlsx, selectedFile, addStar);
										new TextAction(comparer.msg);
										textArea.update(textArea.getGraphics());
										// Finally, run the comparing
										comparer.addRef(erp);
										comparer.runWithRef(csvName, csvNum, xlsxSheet, xlsxName, xlsxNum, ERPName, ERPNum);
										textArea.update(textArea.getGraphics());
										new TextAction(
												"File " + selectedFile.getName() + " saved at : " + selectedFile.getPath() + "\n");
										new TextAction("Items not updated:\n");
										new TextAction(comparer.listNotFound());
									} else {
										new TextAction("Please select valid destination\n");
										return;
									}
								} else
									return;
							}
						});
		chckbxNewCheckBox.addActionListener(checkBox);
		textArea = new JTextArea();
		textArea.setColumns(40);
		textArea.setRows(7);
		JScrollPane scroll = new JScrollPane(textArea);
		contentPane.add(scroll);
	}

	private void resetForeground() {
		textField.setForeground(Color.BLACK);
		textField_1.setForeground(Color.BLACK);
		textField_2.setForeground(Color.BLACK);
		textField_3.setForeground(Color.BLACK);
		textField_4.setForeground(Color.BLACK);
		textField_5.setForeground(Color.BLACK);
		textField_6.setForeground(Color.BLACK);
		textField_7.setForeground(Color.BLACK);
		textField_8.setForeground(Color.BLACK);
		textField_9.setForeground(Color.BLACK);
	}

	ActionListener checkBox = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JCheckBox cb = (JCheckBox) e.getSource();
			if (cb.isSelected()) {
				new TextAction("* will be marked in each changed quantity.\n");
				addStar = true;
			}

			else {
				new TextAction("No * will be marked in each changed quantity.\n");
				addStar = false;
			}
		}
	};


	class TextAction {
		String text;

		public TextAction(String text) {
			this.text = text;
			textArea.append(text);
		}
		public TextAction (Enumeration<String> enu) {
			while(enu.hasMoreElements()) {
				textArea.append(enu.nextElement()+'\n');
			}
			
		}
		public TextAction (ArrayList<String> list) {
			for(int i=0;i<list.size();i++) {
				textArea.append(list.get(i)+'\n');
			}
		}

		public String getText() {
			return text;
		}

		protected void generate(String text) {
			textArea.append(text);
		}
	}

	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
}