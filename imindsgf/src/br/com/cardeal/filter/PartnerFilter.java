package br.com.cardeal.filter;

import br.com.cardeal.enums.PartnerStyle;

public class PartnerFilter {
	private String text;
	
	private PartnerStyle partnerStyle;
	private boolean enabledToShowBlocked;
	private boolean isAllPartners = false;
	private boolean onlyCodExternal = false;
	
	public boolean isAllPartners() {
		return isAllPartners;
	}

	public void setAllPartners(boolean isAllPartners) {
		this.isAllPartners = isAllPartners;
	}

	public boolean isEnabledToShowBlocked() {
		return enabledToShowBlocked;
	}
	
	public void setEnabledToShowBlocked(boolean enabledToShowBlocked) {
		this.enabledToShowBlocked = enabledToShowBlocked;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public PartnerStyle getPartnerStyle() {
		return partnerStyle;
	}
	public void setPartnerStyle(PartnerStyle partnerStyle) {
		this.partnerStyle = partnerStyle;
	}

	public boolean isOnlyCodExternal() {
		return onlyCodExternal;
	}

	public void setOnlyCodExternal(boolean onlyCodExternal) {
		this.onlyCodExternal = onlyCodExternal;
	}
}	
