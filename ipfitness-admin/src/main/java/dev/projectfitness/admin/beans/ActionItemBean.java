package dev.projectfitness.admin.beans;

import dev.projectfitness.admin.dtos.ActionItem;

public class ActionItemBean {
	private ActionItem action;

	public ActionItemBean() {
		super();
	}

	public ActionItemBean(ActionItem action) {
		super();
		this.action = action;
	}

	public ActionItem getAction() {
		return action;
	}

	public void setAction(ActionItem action) {
		this.action = action;
	}

}
