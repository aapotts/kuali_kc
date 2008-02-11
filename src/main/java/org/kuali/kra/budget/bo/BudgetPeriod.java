package org.kuali.kra.budget.bo;

import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.sql.Date;

import org.kuali.core.util.KualiDecimal;
import org.kuali.kra.bo.KraPersistableBusinessObjectBase;
import org.kuali.kra.budget.BudgetDecimal;

public class BudgetPeriod extends KraPersistableBusinessObjectBase {
	private Integer budgetPeriod;
	private String proposalNumber;
	private Integer budgetVersionNumber;
	private String comments;
	private BudgetDecimal costSharingAmount;
	private Date endDate;
	private Date startDate;
	private BudgetDecimal totalCost;
	private BudgetDecimal totalCostLimit;
	private BudgetDecimal totalDirectCost;
	private BudgetDecimal totalIndirectCost;
	private BudgetDecimal underrecoveryAmount;
	private List<BudgetLineItem> budgetLineItems;

	public Integer getBudgetPeriod() {
		return budgetPeriod;
	}

	public void setBudgetPeriod(Integer budgetPeriod) {
		this.budgetPeriod = budgetPeriod;
	}

	public String getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}

	public Integer getBudgetVersionNumber() {
		return budgetVersionNumber;
	}

	public void setBudgetVersionNumber(Integer budgetVersionNumber) {
		this.budgetVersionNumber = budgetVersionNumber;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public BudgetDecimal getCostSharingAmount() {
		return costSharingAmount;
	}

	public void setCostSharingAmount(BudgetDecimal costSharingAmount) {
		this.costSharingAmount = costSharingAmount;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public BudgetDecimal getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(BudgetDecimal totalCost) {
		this.totalCost = totalCost;
	}

	public BudgetDecimal getTotalCostLimit() {
		return totalCostLimit;
	}

	public void setTotalCostLimit(BudgetDecimal totalCostLimit) {
		this.totalCostLimit = totalCostLimit;
	}

	public BudgetDecimal getTotalDirectCost() {
		return totalDirectCost;
	}

	public void setTotalDirectCost(BudgetDecimal totalDirectCost) {
		this.totalDirectCost = totalDirectCost;
	}

	public BudgetDecimal getTotalIndirectCost() {
		return totalIndirectCost;
	}

	public void setTotalIndirectCost(BudgetDecimal totalIndirectCost) {
		this.totalIndirectCost = totalIndirectCost;
	}

	public BudgetDecimal getUnderrecoveryAmount() {
		return underrecoveryAmount;
	}

	public void setUnderrecoveryAmount(BudgetDecimal underrecoveryAmount) {
		this.underrecoveryAmount = underrecoveryAmount;
	}


	@Override 
	protected LinkedHashMap toStringMapper() {
		LinkedHashMap hashMap = new LinkedHashMap();
		hashMap.put("budgetPeriod", getBudgetPeriod());
		hashMap.put("proposalNumber", getProposalNumber());
		hashMap.put("budgetVersionNumber", getBudgetVersionNumber());
		hashMap.put("comments", getComments());
		hashMap.put("costSharingAmount", getCostSharingAmount());
		hashMap.put("endDate", getEndDate());
		hashMap.put("startDate", getStartDate());
		hashMap.put("totalCost", getTotalCost());
		hashMap.put("totalCostLimit", getTotalCostLimit());
		hashMap.put("totalDirectCost", getTotalDirectCost());
		hashMap.put("totalIndirectCost", getTotalIndirectCost());
		hashMap.put("underrecoveryAmount", getUnderrecoveryAmount());
		return hashMap;
	}


    /**
     * Gets the budgetLineItems attribute. 
     * @return Returns the budgetLineItems.
     */
    public List<BudgetLineItem> getBudgetLineItems() {
        return budgetLineItems;
    }

    /**
     * Sets the budgetLineItems attribute value.
     * @param budgetLineItems The budgetLineItems to set.
     */
    public void setBudgetLineItems(List<BudgetLineItem> budgetLineItems) {
        this.budgetLineItems = budgetLineItems;
    }
}
