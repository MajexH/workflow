package xyz.majexh.workflow.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workflow")
public class PostWorkFlowController {

    @PostMapping(path = "/chain")
    public void modifyChain() {

    }

    @PostMapping(path = "/topology")
    public void modifyTopology() {
        
    }
}
