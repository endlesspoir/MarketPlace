package com.marketplace.userservice.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RoleResponse {

    private String roleName;

    private String roleDescription;

    private String rolePermissions;

}
