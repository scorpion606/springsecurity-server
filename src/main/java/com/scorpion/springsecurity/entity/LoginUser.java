package com.scorpion.springsecurity.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author scorpion
 * @date 2023/10/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"authorities","username","password","authorities","password","userName","accountNonExpired","accountNonLocked","credentialsNonExpired","enabled"})
public class LoginUser implements UserDetails {
    private User user;
    private String accessToken;
    private Set<String> permissionSet;

    private List<SimpleGrantedAuthority> authorities;

    public LoginUser(User user, String accessToken, Set<String> permissionSet) {
        this.user = user;
        this.accessToken = accessToken;
        this.permissionSet = permissionSet;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 减少每次请求的循环权限赋值
        if(!CollectionUtils.isEmpty(authorities)){
            return authorities;
        }
        // 把permissionSet中的权限信息包装成GrantedAuthority
       authorities = permissionSet.stream().
                map(SimpleGrantedAuthority::new).
                collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
