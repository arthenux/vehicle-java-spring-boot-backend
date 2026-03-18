package com.alan.vehicle_java_spring_boot_backend.auth;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class InventoryUserPrincipal implements UserDetails {

    private final InventoryUser inventoryUser;

    public InventoryUserPrincipal(InventoryUser inventoryUser) {
        this.inventoryUser = inventoryUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(inventoryUser.getRole()));
    }

    @Override
    public String getPassword() {
        return inventoryUser.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return inventoryUser.getUsername();
    }
}
