package com.jude.realm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.jude.entity.Menu;
import com.jude.entity.Role;
import com.jude.entity.User;
import com.jude.repository.MenuRepository;
import com.jude.repository.RoleRepository;
import com.jude.repository.UserRepository;

/**
 * 自定义Realm
 *
 * @author jude
 */
public class MyRealm extends AuthorizingRealm {

    @Resource
    private UserRepository userRepository;

    @Resource
    private RoleRepository roleRepository;

    @Resource
    private MenuRepository menuRepository;

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User principal = (User) principals.getPrimaryPrincipal();
        //String userName = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userRepository.findByUserName(principal.getUserName());

        List<Role> roleList = roleRepository.findByUserId(user.getId());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> roles = new HashSet<String>();
        for (Role role : roleList) {
            roles.add(role.getName());
            List<Menu> menuList = menuRepository.findByRoleId(role.getId());
            for (Menu menu : menuList) {
                info.addStringPermission(menu.getName()); // 添加权限
            }
        }
        info.setRoles(roles);
        return info;
    }

    /**
     * 登录
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String userName = ((UsernamePasswordToken) token).getUsername();
        User user = userRepository.findByUserName(userName);
        if (user != null) {
            AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(user.getUserName(), "!!!!!", getName());
            return authcInfo;
        } else {
            return null;
        }
    }
}
