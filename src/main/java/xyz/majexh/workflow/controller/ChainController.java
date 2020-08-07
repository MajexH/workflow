package xyz.majexh.workflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.majexh.workflow.service.ControllerService;

import java.util.HashMap;

@RestController
@RequestMapping("/workflow")
public class ChainController {
    private ControllerService controllerService;

    @Autowired
    public void setControllerService(ControllerService controllerService) {
        this.controllerService = controllerService;
    }

    @GetMapping("/allChain")
    public ResponseEntity<HashMap<String, Object>> getAllChain() throws Exception {
        return ResEntity.okDefault(this.controllerService.getAllChain());
    }

    @GetMapping("/chain")
    public ResponseEntity<HashMap<String, Object>> getChain(String chainId) throws Exception {
        return ResEntity.okDefault(this.controllerService.getChain(chainId));
    }
}
