package com.ssm.core;
import com.ssm.model.User;
import com.ssm.service.IUserService;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashSet;
import java.util.Set;

public class MyShiroDbRealm extends AuthorizingRealm {
    private static Logger logger = Logger.getLogger(MyShiroDbRealm.class);
    @Autowired
    private IUserService userService;

    public MyShiroDbRealm() {
        super();
    }

    /**
     * 验证登陆
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        logger.info(token.getUsername());
        User user = userService.getUserByUsername(token.getUsername());
        if (user != null) {
            //账号被锁定
            if ("LOCKED".equals(user.getStatus())) {
                throw new LockedAccountException();
            }
            return new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), this.getName());
        } else {
            throw new UnknownAccountException();
        }
    }

    /**
     * 登陆成功之后，进行角色和权限验证
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        /*这里应该根据userName使用role和permission 的serive层来做判断，并将对应 的权限加进来，下面简化了这一步*/
        Set<String> roleNames = new HashSet<String>();
        Set<String> permissions = new HashSet<String>();
        roleNames.add("admin");//添加角色。对应到index.jsp
        roleNames.add("administrator");
        permissions.add("create");//添加权限,对应到index.jsp
        permissions.add("login.do?main");
        permissions.add("login.do?logout");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
        info.setStringPermissions(permissions);
        return info;
    }

    /**
     * 清除所有用户授权信息缓存.
     */
    public void clearCachedAuthorizationInfo(String principal) {
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
        clearCachedAuthorizationInfo(principals);
    }

    /**
     * 清除所有用户授权信息缓存.
     */
    public void clearAllCachedAuthorizationInfo() {
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        if (cache != null) {
            for (Object key : cache.keys()) {
                cache.remove(key);
            }
        }
    }
}