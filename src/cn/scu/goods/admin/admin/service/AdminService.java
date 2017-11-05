package cn.scu.goods.admin.admin.service;

import java.sql.SQLException;

import cn.scu.goods.admin.admin.dao.AdminDao;
import cn.scu.goods.admin.admin.domain.Admin;

/**
*@author longchao
*@E-mail:353158647@qq.com
*@time:2016年7月25日 下午2:57:16
*/
public class AdminService {
	private AdminDao adminDao=new AdminDao();
	
	/**
	 * 登录功能
	 * @param admin
	 * @return
	 */
	public Admin login(Admin admin) {
		try {
			return adminDao.find(admin.getAdminname(), admin.getAdminpwd());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
