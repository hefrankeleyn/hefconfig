package io.github.hefrankeleyn.hefconfigserver.controller;

import io.github.hefrankeleyn.hefconfigserver.beans.Configs;
import io.github.hefrankeleyn.hefconfigserver.conf.DistributedLock;
import io.github.hefrankeleyn.hefconfigserver.service.HefConfigService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Date 2024/5/26
 * @Author lifei
 */
@RestController
@RequestMapping(value = "/hefConfigController")
public class HefConfigController {

    @Resource
    private DistributedLock distributedLock;

    @Resource
    private HefConfigService hefConfigService;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Configs> list(@RequestParam("capp") String capp,
                              @RequestParam("cenv") String cenv,
                              @RequestParam("cnamespace") String cnamespace) {
        return hefConfigService.findConfigsList(capp, cenv, cnamespace);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public List<Configs> update(@RequestParam("capp") String capp,
                         @RequestParam("cenv") String cenv,
                         @RequestParam("cnamespace") String cnamespace,
                         @RequestBody Map<String, String> ckeyvalueMap) {
        return hefConfigService.updateOrInsert(capp, cenv, cnamespace, ckeyvalueMap);
    }

    @RequestMapping(value = "/version", method = RequestMethod.GET)
    public Long version(@RequestParam("capp") String capp,
                 @RequestParam("cenv") String cenv,
                 @RequestParam("cnamespace") String cnamespace) {
        return hefConfigService.version(capp, cenv, cnamespace);
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public boolean status() {
        return distributedLock.getLocked();
    }
}
