/**
 * 
 */
package org.n52.owsSupervisor.checks;

import java.util.Date;

/**
 * @author Daniel Nüst
 *
 */
public class CheckResult implements ICheckResult {

	private Date timeOfCheck;
	
	private String checkIdentifier;
	
	private String result;
	
	private ResultType type;

	
	public CheckResult(String checkIdentifierP, String resultP, ResultType typeP) {
		this.checkIdentifier = checkIdentifierP;
		this.result = resultP;
		this.type = typeP;
		this.timeOfCheck = new Date();
	}

	/* (non-Javadoc)
	 * @see org.n52.owsSupervisor.checks.ICheckResult#getTimeOfCheck()
	 */
	@Override
	public Date getTimeOfCheck() {
		return this.timeOfCheck;
	}

	/* (non-Javadoc)
	 * @see org.n52.owsSupervisor.checks.ICheckResult#getCheckIdentifier()
	 */
	@Override
	public String getCheckIdentifier() {
		return this.checkIdentifier;
	}

	/* (non-Javadoc)
	 * @see org.n52.owsSupervisor.checks.ICheckResult#getResult()
	 */
	@Override
	public String getResult() {
		return this.result;
	}

	/* (non-Javadoc)
	 * @see org.n52.owsSupervisor.checks.ICheckResult#getType()
	 */
	@Override
	public ResultType getType() {
		return this.type;
	}

}
