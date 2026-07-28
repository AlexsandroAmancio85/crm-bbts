package br.com.bbts.crm.dashboard.controller;

import br.com.bbts.crm.dashboard.dto.KpisDTO;
import br.com.bbts.crm.dashboard.dto.TrilhaDTO;
import br.com.bbts.crm.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/trilha")
    public TrilhaDTO trilha() {
        return dashboardService.resumoTrilha();
    }

    @GetMapping("/kpis")
    public KpisDTO kpis() {
        return dashboardService.kpis();
    }
}
