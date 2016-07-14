package br.com.cardeal.filter;

import br.com.cardeal.model.Company;
import br.com.cardeal.model.Terminal;

public class ControlAccessFilter {

	private Company company;
	
	private Terminal terminal;

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Terminal getTerminal() {
		return terminal;
	}

	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}
	
}
