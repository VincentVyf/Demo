package com.jude.service;

import com.jude.entity.RoleMenu;

/**
 * 角色权限关联Service接口
 * @author jude
 *
 */
public interface RoleMenuService {

	/**
	 * 根据角色id删除所有关联信息
	 * @param id
	 */
    void deleteByRoleId(Integer roleId);
	
	/**
	 * 保存
	 * @param roleMenu
	 */
    void save(RoleMenu roleMenu);
}
