package org.paces.Stata.Cryptography;

import org.apache.commons.io.FilenameUtils;

import javax.crypto.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;

/**
 * @author Billy Buchanan
 * @version 0.0.0
 */
public class PasswordWindow extends JPanel implements ActionListener {

	private static final JButton openButton = new JButton("Open");
	private static final JButton encrypt = new JButton("Encrypt");
	private static final JButton decrypt = new JButton("Decrypt to file");
	private static final JButton decrypt2 = new JButton("Decrypt in memory");
	private static final JButton cancel = new JButton("Cancel");
	private static final JPasswordField pw1 = new JPasswordField(20);
	private static final JPasswordField pw2 = new JPasswordField(20);
	private static final JTextField filename = new JTextField(20);
	private static final JFileChooser fc = new JFileChooser();
	private static final JLabel fnameLab = new JLabel("Select a file: ");
	private static final JLabel labelPassword1 = new JLabel("Enter Password:");
	private static final JLabel labelPassword2 = new JLabel("Confirm Password:");
	private static String theFile;
	private static final JPanel filePanel = new JPanel();
	private static final JPanel pwPanel = new JPanel();
	private static final JPanel options = new JPanel();
	private static final GridBagLayout layout = new GridBagLayout();
	private static final GridBagConstraints constraints = new GridBagConstraints();
	private static final JFrame f = new JFrame("Stata Cryptography");

	private static File cryptFile;
	private static String fileStub;
	private static String fileExt;
	private static String filePath;

	public PasswordWindow(String[] args) {

		if (args != null && args.length >= 1) {
			filename.setText(args[0]);
			setInFile();
			setFileNameParams();
		}

		//Create the log first, because the action listeners
		//need to refer to it.
		filename.setEditable(true);
		filename.setScrollOffset(15);

		//Create the open button.  We use the image from the JLF
		//Graphics Repository (but we extracted it from the jar).
		openButton.addActionListener(this);
		encrypt.addActionListener(this);
		decrypt.addActionListener(this);
		decrypt2.addActionListener(this);
		cancel.addActionListener(this);

		f.setLayout(layout);
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);

		constraints.gridx = 0;
		constraints.gridy = 0;
		f.add(fnameLab, constraints);
		constraints.gridx = 1;
		f.add(filename, constraints);
		constraints.gridx = 2;
		f.add(openButton, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		f.add(labelPassword1, constraints);
		constraints.gridx = 1;
		f.add(pw1, constraints);
		constraints.gridx = 2;
		f.add(cancel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		f.add(labelPassword2, constraints);
		constraints.gridx = 1;
		f.add(pw2, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		f.add(encrypt, constraints);

		constraints.gridx = 1;
		f.add(decrypt, constraints);

//		constraints.gridx = 2;
//		f.add(decrypt2, constraints);

	}

	public void actionPerformed(ActionEvent e) {

		//Handle open button action.
		if (e.getSource() == openButton) {
			//Create a file chooser
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setMultiSelectionEnabled(false);
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				setInFile(fc);
				setFileNameParams();
				//This is where a real application would open the file.
			}
		//Handle save button action.
		} else if (e.getSource() == encrypt) {
			setInFile();
			setFileNameParams();
			if(checkPW()) {
				if (isSame()) {
					CryptoIO cf = new CryptoIO(filePath);
					try {
						cf.read(fileExt);
						makePopUp(cf.encrypt(256, pw2.getPassword()));
					} catch (IOException | NoSuchPaddingException |
							NoSuchAlgorithmException | InvalidKeyException |
							InvalidParameterSpecException e1) {
						e1.printStackTrace();
					}
					encrypt.setSelected(false);
				}
			}
		} else if (e.getSource() == decrypt) {
			setInFile();
			setFileNameParams();
			if (checkPW() && isSame()) {
				CryptoIO cf = new CryptoIO(filePath);
				try {
					makePopUp(cf.decrypt(pw2.getPassword()));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				decrypt.setSelected(false);
			}
//		} else if (e.getSource() == decrypt2) {
//			setFileNameParams();

		} else if (e.getSource() == cancel) {
			pw1.setText("");
			pw2.setText("");
			f.dispose();
		}

	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = PasswordWindow.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	private void setInFile(JFileChooser fc) {
		cryptFile = fc.getSelectedFile().getAbsoluteFile();
		filePath = cryptFile.getAbsolutePath();
	}

	private void setInFile() {
		cryptFile = new File(filename.getText()).getAbsoluteFile();
		filePath = cryptFile.getAbsolutePath();
	}

	private void setFileNameParams() {
		fileStub = FilenameUtils.getBaseName(cryptFile.getAbsolutePath());
		fileExt = FilenameUtils.getExtension(cryptFile.getAbsolutePath());
		filename.setText(cryptFile.getAbsolutePath());
	}

	private static Boolean checkPW() {
		if (!org.paces.Stata.Cryptography.PasswordValidation.validPassword(pw1.getPassword())) {
			String msg = org.paces.Stata.Cryptography.PasswordValidation.invalidMessage(pw1.getPassword());
			pw1.setText("");
			pw2.setText("");
			makePopUp(msg);
			return false;
		} else {
			return true;
		}
	}

	private static void makePopUp(String message) {
		JOptionPane.showMessageDialog(f, message);
	}

	private static Boolean isSame() {
		if (!Arrays.equals(pw1.getPassword(), pw2.getPassword())) {
			String msg = "Confirmation Password Does not Match First Password";
			pw1.setText("");
			pw2.setText("");
			makePopUp(msg);
			return false;
		} else {
			return true;
		}

	}

	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event dispatch thread.
	 */
	private static void createAndShowGUI(String[] args) {
		//Create and set up the window.
		f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		//Add content to the window.
		new PasswordWindow(args);

		//Display the window.
		f.pack();
		f.setVisible(true);
	}

	public static void main(String[] args) {
		//Schedule a job for the event dispatch thread:
		//creating and showing this application's GUI.
		SwingUtilities.invokeLater(() -> {
			//Turn off metal's use of bold fonts
			UIManager.put("swing.boldMetal", Boolean.FALSE);
			createAndShowGUI(args);
		});
	}

	public static Boolean launch(String[] args) {
		SwingUtilities.invokeLater(() -> {
			//Turn off metal's use of bold fonts
			UIManager.put("swing.boldMetal", Boolean.FALSE);
			createAndShowGUI(args);
		});
		return true;
	}
}