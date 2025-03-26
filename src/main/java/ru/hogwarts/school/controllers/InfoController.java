package ru.hogwarts.school.controllers;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.services.InfoService;

@RestController
@RequestMapping("/info")
public class InfoController {

    private final InfoService infoService;

    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }

    @GetMapping("/port")
    public String getPort() {
        return infoService.getPort();
    }

    @GetMapping("/sum-of-1m/{method}")
    public Long getSumOf1M_original(@PathVariable String method,
                                          @RequestParam(defaultValue = "1", required = false) long start,
                                          @RequestParam(defaultValue = "1000000", required = false) long end) {
        return infoService.getSumOf1M(method, start, end);
    }
}
