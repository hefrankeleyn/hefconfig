package io.github.hefrankeleyn.deferredresultdemo.controller;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.Objects;

/**
 * @Date 2024/6/7
 * @Author lifei
 */
@RestController
@RequestMapping(value = "/helloController")
public class HelloController {

    private final MultiValueMap<String, DeferredResult<Long>> map = new LinkedMultiValueMap<>();

    @RequestMapping(value = "/poll/{ns}")
    public DeferredResult<Long> poll(@PathVariable("ns") String ns){
        DeferredResult<Long> deferredResult = new DeferredResult<>();
        deferredResult.onCompletion(()->{
            System.out.println("=====> poll completion.....");
            map.remove(ns);
        });
        deferredResult.onTimeout(()->{
            System.out.println("====> poll timeout......");
            map.remove(ns);
        });
        deferredResult.onError((t)->{
            System.out.println("====> poll error: "+t.getMessage());
        });
        map.add(ns, deferredResult);
        return deferredResult;
    }

    @RequestMapping(value = "/push/{ns}/{val}")
    public void push(@PathVariable("ns") String ns, @PathVariable("val") Long val) {
        List<DeferredResult<Long>> deferredResults = map.get(ns);
        if (Objects.nonNull(deferredResults) && !deferredResults.isEmpty()) {
            for (DeferredResult<Long> deferredResult : deferredResults) {
                deferredResult.setResult(val);
            }
        }
    }
}
