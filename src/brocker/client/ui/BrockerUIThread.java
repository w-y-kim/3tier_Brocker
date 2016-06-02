package brocker.client.ui;

import javax.swing.DefaultListModel;

import brocker.vo.Customer;

public class BrockerUIThread implements Runnable {

	DefaultListModel<Customer> dlm2;
	Customer cus;

	public BrockerUIThread(DefaultListModel<Customer> dlm2, Customer cus) {
		this.dlm2 = dlm2;
		this.cus = cus;
	}

	@Override
	public void run() {
		boolean exit = false;

		try {
			while (!exit)
			dlm2.removeAllElements();
			dlm2.addElement(cus);

		} catch (Exception e) {
			exit = true;
		}
	}

}
