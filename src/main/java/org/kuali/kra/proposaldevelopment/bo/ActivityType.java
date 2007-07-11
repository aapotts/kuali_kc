package org.kuali.kra.proposaldevelopment.bo;

import java.util.LinkedHashMap;

import org.kuali.kra.bo.KraPersistableBusinessObjectBase;

public class ActivityType extends KraPersistableBusinessObjectBase {
	
	private Integer activityTypeCode;
	private String description;
	
	public Integer getActivityTypeCode() {
		return activityTypeCode;
	}
	public void setActivityTypeCode(Integer activityTypeCode) {
		this.activityTypeCode = activityTypeCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	protected LinkedHashMap toStringMapper() {
		LinkedHashMap propMap = new LinkedHashMap();
		propMap.put("scienceCode", this.getActivityTypeCode());
		propMap.put("description", this.getDescription());
		propMap.put("updateTimestamp", this.getUpdateTimestamp());
		propMap.put("updateUser", this.getUpdateUser());
		return propMap;
	}
}
