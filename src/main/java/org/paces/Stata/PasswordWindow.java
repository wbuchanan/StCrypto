package org.paces.Stata;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

/**
 * A Swing program that demonstrates usages of JPasswordField component.
 * @author www.codejava.net
 *
 */
public class PasswordWindow  {

	private JLabel labelPassword = new JLabel("Enter password : ");
	private JLabel labelConfirmPassword = new JLabel("Confirm password : ");
	private JFrame gui = new JFrame("Stata Cryptography Password Verification");
	private JPasswordField passwordField1 = new JPasswordField(20);
	private JPasswordField passwordField2 = new JPasswordField(20);

	private JButton submit = new JButton("Submit");
	private JButton cancel = new JButton("Cancel");
	private PWContainer passwordContainer = new PWContainer();
	private Boolean pw1valid = false;
	private Boolean pw2valid = false;
	private JTextArea pwqual = new JTextArea("Password requires a minimum of " +
			"1 lower case, 1 upper case, 1 numeric, and 1 special character");

	public void makeGui() {

		this.gui.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);

		constraints.gridx = 0;
		constraints.gridy = 0;
		this.gui.add(pwqual, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		this.gui.add(labelPassword, constraints);

		constraints.gridx = 1;
		this.gui.add(passwordField1, constraints);

		passwordField1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent event) {
				validatePassword(event, 1);
			}
		});

		passwordField2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent event) {
				validatePassword(event, 2);
			}
		});



		constraints.gridx = 0;
		constraints.gridy = 2;
		gui.add(labelConfirmPassword, constraints);

		constraints.gridx = 1;
		gui.add(passwordField2, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		this.gui.add(submit, constraints);
		constraints.gridx = 1;
		this.gui.add(cancel, constraints);

		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				submitButton(event);
			}
		});

		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				passwordField1.setText("");
				passwordField2.setText("");
				gui.dispose();
			}
		});

		this.gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.gui.pack();
		this.gui.setLocationRelativeTo(null);
	}

	public void validatePassword(KeyEvent event, Integer field) {
		JPasswordField pwfield = (JPasswordField) event.getSource();
		char[] password = pwfield.getPassword();
		if (field == 1) pw1valid = PasswordValidation.validPassword(password);
		else pw2valid = PasswordValidation.validPassword(password);
	}

	private void submitButton(ActionEvent event) {
		char[] password1 = passwordField1.getPassword();
		char[] password2 = passwordField2.getPassword();
		if (!Arrays.equals(password1, password2)) {
			JOptionPane.showMessageDialog(this.gui, "Passwords do not match!");
			passwordField1.setText("");
			passwordField2.setText("");
		} else {
			passwordContainer.setValidPassword(password2);
			passwordField1.setText("");
			passwordField2.setText("");
			gui.dispose();
		}
	}

	public PWContainer getPasswordContainer() {
		return this.passwordContainer;
	}

	public PasswordWindow(){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				makeGui();
				gui.setVisible(true);
			}
		});
	}

}