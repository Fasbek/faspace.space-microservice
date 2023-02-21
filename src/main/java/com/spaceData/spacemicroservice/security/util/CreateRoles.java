package com.spaceData.spacemicroservice.security.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.spaceData.spacemicroservice.security.entity.Rol;
import com.spaceData.spacemicroservice.security.enums.RolNombre;
import com.spaceData.spacemicroservice.security.service.RolService;

//@Component
public class CreateRoles implements CommandLineRunner{

	@Autowired
	RolService rolService;
	
	@Override
	public void run(String... args) throws Exception {
//		Rol rolAdmin = new Rol(RolNombre.ROLE_ADMIN);
//		Rol rolUser = new Rol(RolNombre.ROLE_USER);
//		Rol rolInv = new Rol(RolNombre.ROLE_INV);
//		rolService.save(rolAdmin);
//		rolService.save(rolUser);
//		rolService.save(rolInv);
	}

}
