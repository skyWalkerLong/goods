package cn.scu.goods.user.doamin;
/**
*@author longchao
*@E-mail:353158647@qq.com
*@time:2016年6月7日 上午10:12:57
*/
/*　User类作为实体类需要与数据库表对应，即t_user表对象。而且User类还要用来封装表单数据，所以User类还要与表单对应。
 * 
 * */
public class User {
	//对应数据库表
	private String uid;
	private String loginname;
	private String loginpass;
	private String email;
	private boolean status;
	private String activationCode;
	//对应注册表单
	private String reloginpass;
	private String verifyCode;
	//修改密码表单
	private String newpass;
	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
	/**
	 * @return the loginname
	 */
	public String getLoginname() {
		return loginname;
	}
	/**
	 * @param loginname the loginname to set
	 */
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	/**
	 * @return the loginpass
	 */
	public String getLoginpass() {
		return loginpass;
	}
	/**
	 * @param loginpass the loginpass to set
	 */
	public void setLoginpass(String loginpass) {
		this.loginpass = loginpass;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the status
	 */
	public boolean isStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}
	/**
	 * @return the activationCode
	 */
	public String getActivationCode() {
		return activationCode;
	}
	/**
	 * @param activationCode the activationCode to set
	 */
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	/**
	 * @return the reloginpass
	 */
	public String getReloginpass() {
		return reloginpass;
	}
	/**
	 * @param reloginpass the reloginpass to set
	 */
	public void setReloginpass(String reloginpass) {
		this.reloginpass = reloginpass;
	}
	/**
	 * @return the verifyCode
	 */
	public String getVerifyCode() {
		return verifyCode;
	}
	/**
	 * @param verifyCode the verifyCode to set
	 */
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	/**
	 * @return the newpass
	 */
	public String getNewpass() {
		return newpass;
	}
	/**
	 * @param newpass the newpass to set
	 */
	public void setNewpass(String newpass) {
		this.newpass = newpass;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [uid=" + uid + ", loginname=" + loginname + ", loginpass=" + loginpass + ", email=" + email
				+ ", status=" + status + ", activationCode=" + activationCode + ", reloginpass=" + reloginpass
				+ ", verifyCode=" + verifyCode + ", newpass=" + newpass + "]";
	}
	
	
	
}
