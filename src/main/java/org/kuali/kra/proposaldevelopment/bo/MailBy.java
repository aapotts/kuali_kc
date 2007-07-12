package org.kuali.kra.proposaldevelopment.bo;

import java.util.LinkedHashMap;

import org.kuali.kra.bo.KraPersistableBusinessObjectBase;

public class MailBy extends KraPersistableBusinessObjectBase {
	
	private Integer mailByCode;
	private String description;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getMailByCode() {
		return mailByCode;
	}
	public void setMailByCode(Integer mailByCode) {
		this.mailByCode = mailByCode;
	}

	@Override
	protected LinkedHashMap toStringMapper() {
		LinkedHashMap propMap = new LinkedHashMap();
		propMap.put("mailByCode", this.getMailByCode());
		propMap.put("description", this.getDescription());
		propMap.put("updateTimestamp", this.getUpdateTimestamp());
		propMap.put("updateUser", this.getUpdateUser());
		return propMap;
	}
}
