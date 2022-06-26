package ru.shakurov.diploma.yandex_module.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.shakurov.diploma.yandex_module.entity.DiskType;
import ru.shakurov.diploma.yandex_module.service.TokenFacade;

@Controller
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenFacade tokenFacade;

    @GetMapping("/{disk}")
    public String processYandexOAuth(@PathVariable String disk,
                                     @RequestParam String code,
                                     @RequestParam(name = "state") String tgUserId) {
        tokenFacade.handleAuth(DiskType.valueOf(disk.toUpperCase()), tgUserId, code);
        return "redirect:https://t.me/CloudDriveAggregator_Bot";
    }
}
