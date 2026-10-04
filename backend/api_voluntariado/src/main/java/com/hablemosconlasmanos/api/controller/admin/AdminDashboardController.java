package com.hablemosconlasmanos.api.controller.admin;

import com.hablemosconlasmanos.api.dto.DashboardDTO;
import com.hablemosconlasmanos.api.service.PortadaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    private final PortadaService portadaService;

    @GetMapping
    public DashboardDTO dashboard() {
        return portadaService.dashboard();
    }
}
